#!/bin/bash
chmod +x ./gradlew
echo "Show the current directory for execution."
pwd
echo "Install dependencies for Spring Boot."
./gradlew clean build -x test