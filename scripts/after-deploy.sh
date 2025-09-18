#!/bin/bash

# 경로 설정: appspec.yml destination 과 동일하게
cd /home/ec2-user/MiMiDaily || exit 1

# 만약 .jar 또는 .war 로 실행하는 방식이면
# 기존 프로세스 종료
if pgrep -f "MiMiDaily" ; then
  echo "Stopping existing application..."
  pkill -f "MiMiDaily"
fi

# 실행
echo "Starting application..."
# 예: 만약 Spring Boot jar 가 있다면
nohup java -jar target/MiMiDaily.jar > /home/ec2-user/logs/app.log 2>&1 &

# 만약 톰캣이나 JSP 컨테이너 사용한다면 그에 맞는 restart 스크립트
# 예: /opt/tomcat/bin/shutdown.sh && /opt/tomcat/bin/startup.sh

