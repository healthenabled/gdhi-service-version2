#!/bin/bash
set -xe
pwd
source /opt/export_QA_env.sh
nohup ./gradlew -stop &
