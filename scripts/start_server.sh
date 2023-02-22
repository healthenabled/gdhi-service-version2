#!/bin/bash
set -xe
pwd
source /opt/scripts/export_aws_vars.sh
# Remove existing jars
rm -rf *.jar
FILE=`/usr/local/bin/aws s3 ls $AWS_S3_BUCKET_NAME/$AWS_S3_FOLDER_BACKEND --recursive | sort | tail -n 1 | awk '{print $4}'`
/usr/local/bin/aws s3 cp "s3://$AWS_S3_BUCKET_NAME/$FILE"  /opt/codedeploy-agent/
chmod +x /opt/codedeploy-agent/gdhi*.jar
if [ "$DEPLOYMENT_GROUP_NAME" == "GDHI-Backend-v2-QA" ]
then
    source /opt/scripts/export_QA_env.sh
    nohup java -jar gdhi-*.jar --spring.config.location=${SPRING_CONFIG_LOCATION} > /opt/logs/gdhi_stdout.txt 2> /opt/logs/gdhi_stderr.txt < /dev/null &
fi
if [ "$DEPLOYMENT_GROUP_NAME" == "GDHI-Backend-v2-Showcase" ]
then
    source /opt/scripts/export_Showcase_env.sh
    nohup java -jar gdhi-*.jar --spring.config.location=${SPRING_CONFIG_LOCATION} > /opt/logs/gdhi_stdout.txt 2> /opt/logs/gdhi_stderr.txt < /dev/null &
fi