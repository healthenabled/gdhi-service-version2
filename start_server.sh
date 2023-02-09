#!/bin/bash
set -xe
pwd
source /opt/export_QA_env.sh
nohup ./gradlew bootRun --args='--spring.profiles.active=qa' &

