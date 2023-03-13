#!/bin/bash
set -xe
pwd
source /opt/scripts/export_aws_vars.sh
echo -n "Remove existing jars"
rm -rf $CURRENT_DIR/*.jar
if [ "$DEPLOYMENT_GROUP_NAME" == "GDHI-Backend-v2-QA" ]
then
    source /opt/scripts/export_QA_env.sh
    FILE=`/usr/local/bin/aws s3 ls $AWS_S3_BUCKET_NAME/$AWS_S3_FOLDER_BACKEND --recursive | sort | tail -n 1 | awk '{print $4}'`
fi
if [ "$DEPLOYMENT_GROUP_NAME" == "GDHI-Backend-v2-Showcase" ]
then
    source /opt/scripts/export_Showcase_env.sh
    FILE=`/usr/local/bin/aws ssm get-parameters --name "buildNumberForService" --query "Parameters[0].Value" | tr -d '"'`
fi
if [ "$DEPLOYMENT_GROUP_NAME" == "GDHI-Backend-v2-Prod" ]
then
    source /opt/scripts/export_Prod_env.sh
    FILE=`/usr/local/bin/aws ssm get-parameters --name "buildNumberForService" --query "Parameters[0].Value" | tr -d '"'`
fi

/usr/local/bin/aws s3 cp "s3://$AWS_S3_BUCKET_NAME/$FILE" $CURRENT_DIR/
chmod +x $CURRENT_DIR/gdhi-*.jar;

echo -n $"Starting $FILE: "
nohup java -jar $CURRENT_DIR/gdhi-*.jar --spring.config.location=${SPRING_CONFIG_LOCATION} -Dlogback.configurationFile=${LOGBACK_FILE_LOCATION} > ${CURRENT_DIR}/logs/gdhi_stdout.txt 2> ${CURRENT_DIR}/logs/gdhi_stderr.txt < /dev/null &
PID="$!"
echo "Started, pid: $PID"

