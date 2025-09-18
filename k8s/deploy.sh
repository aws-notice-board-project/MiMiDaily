#!/bin/bash
set -euo pipefail

# =========================
# 0) 공통 변수 & kubeconfig
# =========================
export REGION=ap-northeast-2
export CLUSTER=PRJ-PRD-EKS
export ACCOUNT_ID
ACCOUNT_ID=$(aws sts get-caller-identity --query Account --output text)
export OIDC_URL
OIDC_URL=$(aws eks describe-cluster --name "$CLUSTER" --region "$REGION" \
  --query "cluster.identity.oidc.issuer" --output text | sed 's#https://##')

echo "[INFO] REGION=$REGION CLUSTER=$CLUSTER ACCOUNT_ID=$ACCOUNT_ID"
aws eks update-kubeconfig --region "$REGION" --name "$CLUSTER"

# 필수 도구 체크(있으면 넘어감)
command -v helm >/dev/null || { echo "[ERR] helm not found"; exit 1; }
command -v kubectl >/dev/null || { echo "[ERR] kubectl not found"; exit 1; }

# =========================
# 1) (선택) Secrets Manager 비밀
# =========================
SECRET_NAME="prj/prod/db"
if ! aws secretsmanager describe-secret --secret-id "$SECRET_NAME" >/dev/null 2>&1; then
  echo "[INFO] Creating secret: $SECRET_NAME"
  aws secretsmanager create-secret \
    --name "$SECRET_NAME" \
    --description "MiMiDaily DB creds" \
    --secret-string '{
      "DB_HOST":"prj-db.cps6wc00of5n.ap-northeast-2.rds.amazonaws.com",
      "DB_NAME":"mimidaily",
      "DB_USER":"admin",
      "DB_PASS":"<실패스워드로교체>"
    }'
else
  echo "[INFO] Secret exists: $SECRET_NAME (skip create)"
fi

# =======================================
# 2) ESO IRSA (컨트롤러가 SM/KMS 읽도록)
# =======================================
echo "[INFO] Creating IRSA role for ESO (controller)"
cat > eso-trust.json <<EOF
{
  "Version":"2012-10-17",
  "Statement":[{
    "Effect":"Allow",
    "Principal":{"Federated":"arn:aws:iam::${ACCOUNT_ID}:oidc-provider/${OIDC_URL}"},
    "Action":"sts:AssumeRoleWithWebIdentity",
    "Condition":{"StringEquals":{"${OIDC_URL}:sub":"system:serviceaccount:external-secrets:external-secrets"}}
  }]
}
EOF

aws iam create-role \
  --role-name PRJ-PRD-ESO-IRSA \
  --assume-role-policy-document file://eso-trust.json || true

cat > eso-policy.json <<'EOF'
{
  "Version":"2012-10-17",
  "Statement":[
    {
      "Effect":"Allow",
      "Action":[ "secretsmanager:GetSecretValue","secretsmanager:DescribeSecret","secretsmanager:ListSecretVersionIds" ],
      "Resource":"arn:aws:secretsmanager:ap-northeast-2:*:secret:prj/prod/db*"
    },
    {
      "Effect":"Allow",
      "Action":[ "kms:Decrypt" ],
      "Resource":"*"
    }
  ]
}
EOF
aws iam create-policy --policy-name PRJ-PRD-ESO-Policy --policy-document file://eso-policy.json || true
aws iam attach-role-policy \
  --role-name PRJ-PRD-ESO-IRSA \
  --policy-arn arn:aws:iam::${ACCOUNT_ID}:policy/PRJ-PRD-ESO-Policy || true

# =====================================================
# 3) External Secrets Operator 설치 (CRD+컨트롤러+웹훅+cert)
#    * taint 제거 환경: tolerations 불필요
# =====================================================
echo "[INFO] Installing External Secrets"
helm repo add external-secrets https://charts.external-secrets.io >/dev/null 2>&1 || true
helm repo update >/dev/null

kubectl create ns external-secrets 2>/dev/null || true

helm upgrade --install external-secrets external-secrets/external-secrets \
  -n external-secrets \
  --set installCRDs=true \
  --set serviceAccount.create=true \
  --set serviceAccount.name=external-secrets \
  --set serviceAccount.annotations."eks\.amazonaws\.com/role-arn"="arn:aws:iam::${ACCOUNT_ID}:role/PRJ-PRD-ESO-IRSA"

echo "[INFO] Wait for ESO ready…"
kubectl -n external-secrets rollout status deploy/external-secrets
kubectl -n external-secrets rollout status deploy/external-secrets-webhook
kubectl -n external-secrets rollout status deploy/external-secrets-cert-controller

# ============================
# 4) K8s 비민감 설정 적용
# ============================
echo "[INFO] Apply namespace/configmap"
kubectl apply -f app-bootstrap.yaml

# =========================================
# 5) ClusterSecretStore / ExternalSecret (v1)
# =========================================
echo "[INFO] Apply ClusterSecretStore & ExternalSecret"
kubectl apply -f clustersecretstore.yaml
kubectl -n app apply -f db-externalsecret.yaml

echo "[INFO] Wait for db-secret creation"
for i in {1..20}; do
  if kubectl -n app get secret db-secret >/dev/null 2>&1; then
    echo "[OK] db-secret created"; break
  fi
  sleep 3
done

# ========================================
# 6) ALB Controller (Role/Policy/Helm)
# ========================================
echo "[INFO] Installing AWS Load Balancer Controller (create Role/Policy)"
cat > albc-trust.json <<EOF
{
  "Version": "2012-10-17",
  "Statement": [{
    "Effect": "Allow",
    "Principal": {
      "Federated": "arn:aws:iam::${ACCOUNT_ID}:oidc-provider/${OIDC_URL}"
    },
    "Action": "sts:AssumeRoleWithWebIdentity",
    "Condition": {
      "StringEquals": {
        "${OIDC_URL}:sub": "system:serviceaccount:kube-system:aws-load-balancer-controller"
      }
    }
  }]
}
EOF

aws iam create-role \
  --role-name PRJ-PRD-ALBC-IRSA \
  --assume-role-policy-document file://albc-trust.json || true

curl -sL https://raw.githubusercontent.com/kubernetes-sigs/aws-load-balancer-controller/main/docs/install/iam_policy.json \
  -o albc-policy.json
aws iam create-policy --policy-name PRJ-PRD-ALBC-Policy --policy-document file://albc-policy.json || true
aws iam attach-role-policy \
  --role-name PRJ-PRD-ALBC-IRSA \
  --policy-arn arn:aws:iam::${ACCOUNT_ID}:policy/PRJ-PRD-ALBC-Policy || true

echo "[INFO] Installing ALB Controller (Helm)"
helm repo add eks https://aws.github.io/eks-charts >/dev/null 2>&1 || true
helm repo update >/dev/null

kubectl -n kube-system create serviceaccount aws-load-balancer-controller 2>/dev/null || true
kubectl -n kube-system annotate sa aws-load-balancer-controller \
  eks.amazonaws.com/role-arn=arn:aws:iam::${ACCOUNT_ID}:role/PRJ-PRD-ALBC-IRSA --overwrite

VPC_ID=$(aws eks describe-cluster --name "$CLUSTER" --region "$REGION" \
  --query 'cluster.resourcesVpcConfig.vpcId' --output text)

helm upgrade -i aws-load-balancer-controller eks/aws-load-balancer-controller \
  -n kube-system \
  --set clusterName="$CLUSTER" \
  --set region="$REGION" \
  --set vpcId="$VPC_ID" \
  --set serviceAccount.create=false \
  --set serviceAccount.name=aws-load-balancer-controller

kubectl -n kube-system rollout status deploy aws-load-balancer-controller

# ==================================================
# 7) 앱 배포 (Tomcat → Nginx → Ingress)
# ==================================================
echo "[INFO] Deploy backend/frontend"
kubectl -n app apply -f tomcat.yaml
kubectl -n app apply -f nginx.yaml
kubectl -n app rollout status deploy/tomcat-backend
kubectl -n app rollout status deploy/nginx-frontend

echo "[INFO] Apply Ingress"
kubectl -n app apply -f ingress.yaml

# =====================
# 8) 최종 점검
# =====================
echo "[INFO] Check ALB address"
ALB=$(kubectl -n app get ing web -o jsonpath='{.status.loadBalancer.ingress[0].hostname}')
echo "[ALB] $ALB"

echo "[INFO] SVC/EP"
kubectl -n app get svc
kubectl -n app get ep

echo "[INFO] curl healthz"
curl -I "http://${ALB}/healthz" || true

echo "[INFO] Root path (도메인 쓰면 Host 헤더 맞춰 테스트)"
curl -I -H "Host: app.example.com" "http://${ALB}/" || true

echo "[DONE] All set."