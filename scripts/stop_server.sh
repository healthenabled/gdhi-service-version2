#!/bin/bash
set -xe
pwd
source /opt/scripts/export_aws_vars.sh
echo -n $"Shutting down :"gdhi-*.jar
pkill -f java