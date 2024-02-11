#!/usr/bin/env bash

REPOSITORY=/home/ec2-user/DeployProject
cd $REPOSITORY

APP_NAME=DeployProject
JAR_NAME=$(ls $REPOSITORY/build/libs/ | grep 'SNAPSHOT.jar' | tail -n 1)
JAR_PATH=$REPOSITORY/build/libs/$JAR_NAME

CURRENT_PID=$(pgrep -f $APP_NAME)

if [ -z $CURRENT_PID ]
then
  echo "> 종료할것 없음."
else
  echo "> kill -9 $CURRENT_PID"
  kill -15 $CURRENT_PID
  sleep 5
fi

echo "> $JAR_PATH 배포"
nohup java -jar -Dspring.config.location=classpath:/application.yml,/home/ec2-user/app/application-oauth.yml,/home/ec2-user/app/application-real-db.yml,classpath:/application-real.yml -Dspring.profiles.active=real $JAR_PATH > /dev/null 2> /dev/null < /dev/null &
# nohup java -jar $JAR_PATH > /dev/null 2> /dev/null < /dev/null &