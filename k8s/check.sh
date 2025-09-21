#!/usr/bin/env bash
set -euo pipefail

# pager 끄기 (멈춤 방지)
export AWS_PAGER=""
export KUBECTL_PAGER=""

# =====[ 설정 ]=====
DOMAIN="mimidaily.shop"
ALT_DOMAIN="www.mimidaily.shop"
NS="app"
ING="web"
FRONT_SVC="nginx-frontend"
BACK_SVC="tomcat-backend"
REGION="ap-northeast-2"

echo "=== [0] 환경/권한 확인 ==="
aws sts get-caller-identity --output text || { echo "[ERR] AWS 자격증명 확인 실패"; exit 1; }
kubectl version --client=true --output=yaml || true
echo

echo "=== [1] Ingress / ALB 확인 ==="
kubectl -n "$NS" get ing "$ING" -o wide
ING_YAML=$(kubectl -n "$NS" get ing "$ING" -o yaml)
ALB_DNS=$(echo "$ING_YAML" | yq '.status.loadBalancer.ingress[0].hostname' 2>/dev/null || true)
[ -z "${ALB_DNS:-}" ] && { echo "[ERR] Ingress에 ALB DNS가 아직 없음 (컨트롤러/서브넷 태그/권한 확인)"; exit 1; }
echo "[INFO] ALB DNS: $ALB_DNS"

echo
echo "=== [2] DNS 레코드 확인 (Route53 → ALB 매핑) ==="
HZ_ID=$(aws route53 list-hosted-zones-by-name --dns-name "$DOMAIN." \
  --query "HostedZones[?Name=='$DOMAIN.'].Id" --output text | sed 's#/hostedzone/##')
if [ -z "$HZ_ID" ]; then
  echo "[WARN] $DOMAIN Hosted Zone을 Route53에서 찾지 못함. (다른 DNS 사용중?)"
else
  echo "[INFO] HostedZoneId: $HZ_ID"
  echo "---- A/AAAA 레코드 ----"
  aws route53 list-resource-record-sets --hosted-zone-id "$HZ_ID" \
    --query "ResourceRecordSets[?Type=='A' || Type=='AAAA'].[Name,Type,AliasTarget.DNSName]" --output table
fi

echo
echo "=== [3] Ingress 규칙과 Service 연결 점검 ==="
echo "[INFO] Ingress rules:"
echo "$ING_YAML" | yq '.spec.rules'
echo
echo "[INFO] nginx-frontend Service:"
kubectl -n "$NS" get svc "$FRONT_SVC" -o wide
kubectl -n "$NS" get ep "$FRONT_SVC" -o wide

echo
echo "=== [4] nginx Pod 상태/로그 (최근 50줄) ==="
kubectl -n "$NS" get pods -l app="$FRONT_SVC" -o wide
NGINX_DEP=$(kubectl -n "$NS" get deploy -l app="$FRONT_SVC" -o jsonpath='{.items[*].metadata.name}')
if [ -n "$NGINX_DEP" ]; then
  kubectl -n "$NS" logs deploy/"$NGINX_DEP" --tail=50 || true
else
  echo "[WARN] nginx-frontend Deployment 없음 (labels: app=$FRONT_SVC). 스킵합니다."
fi

echo
echo "=== [5] tomcat Service/Pod 상태 ==="
kubectl -n "$NS" get svc "$BACK_SVC" -o wide || true
kubectl -n "$NS" get ep "$BACK_SVC" -o wide || true
if kubectl -n "$NS" get deploy "$BACK_SVC" >/dev/null 2>&1; then
  kubectl -n "$NS" logs deploy/"$BACK_SVC" --tail=50 || true
fi

echo
echo "=== [6] ALB → TargetGroup 헬스 확인 ==="
ALB_ARN=$(
  aws elbv2 describe-load-balancers --region "$REGION" \
    --query "LoadBalancers[?DNSName=='$ALB_DNS'].LoadBalancerArn" --output text
)
if [ -z "$ALB_ARN" ]; then
  echo "[ERR] ALB ARN을 DNS로 찾지 못했습니다."; exit 1
fi
echo "[INFO] ALB ARN: $ALB_ARN"

TG_ARNS=$(aws elbv2 describe-listeners --load-balancer-arn "$ALB_ARN" --region "$REGION" \
  --query "Listeners[].DefaultActions[].TargetGroupArn" --output text | tr '\t' '\n' | sort -u)
if [ -z "$TG_ARNS" ]; then
  echo "[WARN] 연결된 TargetGroup이 없습니다. Ingress가 Service를 정상 참조하는지 확인."
else
  for TG in $TG_ARNS; do
    echo "---- TargetGroup: $TG ----"
    aws elbv2 describe-target-health --target-group-arn "$TG" --region "$REGION" \
      --query "TargetHealthDescriptions[].{IP:Target.Id,Port:Target.Port,State:TargetHealth.State,Reason:TargetHealth.Reason,Descr:TargetHealth.Description}" \
      --output table || true
    echo
    aws elbv2 describe-target-groups --target-group-arns "$TG" --region "$REGION" \
      --query "TargetGroups[0].{Protocol:Protocol,Port:Port,HealthCheckPath:HealthCheckPath,HealthCheckPort:HealthCheckPort,SuccessCodes:Matcher.HttpCode}" \
      --output table || true
  done
fi

echo
echo "=== [7] 실제 HTTP 요청 테스트 (Host 헤더 유/무) ==="
set +e
curl -sS -o /dev/null -w "[TEST] ALB DNS no Host → HTTP %{http_code}\n" "http://${ALB_DNS}/"
curl -sS -o /dev/null -w "[TEST] Host=${DOMAIN} → HTTP %{http_code}\n" -H "Host: ${DOMAIN}" "http://${ALB_DNS}/"
curl -sS -o /dev/null -w "[TEST] Host=${ALT_DOMAIN} → HTTP %{http_code}\n" -H "Host: ${ALT_DOMAIN}" "http://${ALB_DNS}/"
curl -sS -o /dev/null -w "[TEST] HealthCheck / → HTTP %{http_code}\n" -H "Host: ${DOMAIN}" "http://${ALB_DNS}/"
set -e

echo
echo "=== [8] 빠른 원인 진단 힌트 ==="
echo "- Ingress host 규칙: ${DOMAIN}, ${ALT_DOMAIN} (Host 헤더 없으면 404 가능)"
echo "- nginx 로그의 499는 ELB HealthChecker가 응답 전 커넥션 종료(타임아웃/연결중단) → 헬스 패스/타임아웃 점검"
echo "- Service/Endpoints가 비어있으면 Selector 라벨 불일치"
echo "- TargetGroup Health가 unhealthy면: 포트/헬스패스/보안그룹(인바운드 80) 확인"