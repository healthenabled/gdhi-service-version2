#!/bin/bash
set -xe
pwd
source /opt/scripts/export_QA_env.sh
nohup ./gradlew -stop &
