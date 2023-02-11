#!/bin/bash
set -xe
pwd
source /opt/export_QA_env.sh
nohup ./gradlew bootRun --args='--spring.profiles.active=qa' > /opt/logs/gdhi_stdout.txt 2> /opt/logs/gdhi_stderr.txt < /dev/null &

