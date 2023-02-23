#!/bin/bash
set -xe
pwd
source /opt/scripts/export_aws_vars.sh
echo -n $"Shutting down :"$APPLICATION_NAME
pkill -f 'java.*$APPLICATION_NAME'