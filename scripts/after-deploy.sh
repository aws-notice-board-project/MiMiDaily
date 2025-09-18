#!/bin/bash
# ==========================
# MiMiDaily after-deploy.sh
# ==========================
AWS_REGION="ap-northeast-2"            # ⚠️ 본인 리전
ACCOUNT_ID="${AWS_ACCOUNT_ID}"         # EC2 환경변수 또는 하드코딩
REPO_NGINX="ecr_nginx"                     # ⚠️ ECR nginx repository
REPO_TOMCAT="ecr_tomcat"                   # ⚠️ ECR tomcat repository
PORT_NGINX="80"                        # ⚠️ HostPort (nginx)
PORT_TOMCAT="8080"                     # ⚠️ HostPort (tomcat)

echo "=== MiMiDaily: after-deploy.sh started ==="

# Docker 로그인
aws ecr get-login-password --region $AWS_REGION | docker login --username AWS --password-stdin $ACCOUNT_ID.dkr.ecr.$AWS_REGION.amazonaws.com

# Function: deploy container
deploy_container() {
  local repo=$1
  local name=$2
  local port=$3 

  echo "--- Deploying $name from repo $repo ---"

  docker pull $ACCOUNT_ID.dkr.ecr.$AWS_REGION.amazonaws.com/$repo:latest

  if [ "$(docker ps -q -f name=$name)" ]; then
    echo "Stopping and removing existing container: $name"
    docker rm -f $name
  fi

  docker image prune -f

  docker run -d --name $name -p $port:$port \
    $ACCOUNT_ID.dkr.ecr.$AWS_REGION.amazonaws.com/$repo:latest
}

# 배포 nginx
deploy_container $REPO_NGINX mimidaily-nginx $PORT_NGINX

# 배포 tomcat
deploy_container $REPO_TOMCAT mimidaily-tomcat $PORT_TOMCAT

echo "=== MiMiDaily: after-deploy.sh completed successfully ==="
