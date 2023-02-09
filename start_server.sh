#!/bin/bash
source ~/.bash_profile
./gradlew bootRun --args='--spring.profiles.active=qa'

