# GDHI-Backend

[![GDHI-Backend](https://github.com/healthenabled/gdhi-service-version2/actions/workflows/build.yml/badge.svg?branch=main)](https://github.com/healthenabled/gdhi-service-version2/actions/workflows/build.yml)

This is a Spring Boot application built using Gradle. Works well with Java 17 or newer.

## Table of Contents

1. **[Pre-requisites](#pre-requisites)**
2. **[Setup DB](#setup-db)** 
3. **[Pre-Commit Hooks](#pre-commit-hooks)**
4. **[Local Setup](#local-setup)**
5. **[Gradle Tasks](#gradle-tasks)**
6. **[Setup Test Environment](#setup-test-environment)**
7. **[Project Overview](#project-overview)**
8. **[Code Style](#code-style)**
9. **[Deployment](#Deployment)**
10. **[Troubleshooting](#Troubleshooting)**

## Pre-requisites
- Install Java
- Setup DB

## Setup DB

1. Pull docker image for postgres
```
    docker pull postgres
```
2.  Check if the postgres image is pulled in
```
    docker image ls 
```
3. Run the postgres container 
```
    docker run -dit -e POSTGRES_USER=gdhi -e POSTGRES_PASSWORD=password -p 5432:5432 --name gdhi postgres
```
4. Create user and database using Postgres GUI client (for e.g., pgAdmin4, dbeaver)
```
    CREATE USER gdhi WITH PASSWORD 'password';
    CREATE DATABASE gdhi OWNER gdhi;
```
5. Install uuid extension
```
    create extension if not exists "uuid-ossp";
```
## Pre-Commit Hooks

- Execute `sh ./utils/set-up-git-hooks.sh` from base folder to validate commit message format.

## Local Setup 
- Upon the server start, hit the following endpoint to populate the country overall phase (Only for Published Countries.)

```
   localhost:8080/api/admin/countries/calculate_phase
```
## Gradle Tasks

1. Run by executing
`./gradlew clean bootRun`
2. To run test 
`./gradlew clean build`
3. To build jar file
`./gradlew clean bootjar`

## Setup Test Environment
1. Create Test user and database
```
    CREATE USER gdhi_test WITH PASSWORD 'testpassword';
    CREATE DATABASE gdhi_test OWNER gdhi_test;
```
2. Install uuid extension

connect to DB gdhi_test
```
    create extension if not exists "uuid-ossp";
```
## Project overview
- This is the backend repo for [GDHM](https://monitor.digitalhealthmonitor.org/). 
- The project provides a platform to upload digital health metrices of various Countries globally and a way to visualize them. 
- We have **yearwise** segregation of data to allow comparisons across years.
- We also have **Regions** which are composed of Countries
- We rely on backend as our source of truth and use the UI for only minor data changes before rendering it on the UI

## Code Style

To ensure your Intellij code style matches the checkstyle in the gradle build.
1. Download the code style from the [Contracts Repository](https://github.com/healthenabled/contracts.git)
2. Go to **preferences** -> **code style** -> **java**
3. Click on the settings icon, next to the project
4. Click on the Import Schema -> Intellij IDEA style XML
5. Select the downloaded file
6. The style is updated, use the Reformat the code using (⌘ ⌥ L).

## Deployment

- We have 3 `environments` of Deployment:
  - [QA](https://github.com/healthenabled/gdhi-service-version2/deployments/activity_log?environment=QA)
  - [Showcase](https://github.com/healthenabled/gdhi-service-version2/deployments/activity_log?environment=SHOWCASE) 
  - [Production](https://github.com/healthenabled/gdhi-service-version2/deployments/activity_log?environment=PROD)
- We use `github actions` to configure our CI. The code for the same can be found in [`.github/workflows`](https://github.com/healthenabled/gdhi-service-version2/tree/main/.github/workflows) directory. 
- Below is a sequence diagram for CI/CD of the application:
```mermaid
  sequenceDiagram;
      participant local
      participant CI
      participant AWS/S3
      participant QA
      participant ShowCase
      participant Production
      local-->>local: Pre-push hooks
      local->>CI: Code push
      CI-->>CI: Unit tests
      CI-->>CI: Integration tests
      CI-->>CI: Build the Service and get buildNumber
      CI->>AWS/S3: Upload artifacts
      AWS/S3->>QA: Codedeploy QA
      QA-->>QA: Automation tests on QA
      QA->>ShowCase: Set a buildNumber and trigger deployment(manual)
      ShowCase->>Production: Promote from Showcase to Production(manual)
```

## Troubleshooting

1. Bootrun not working
- Check if local db is setup
- Check if docker container is running 
- Check if there is a migration mismatch in the schema_version table in the public schema
- Check if java version used is compatible with the backend
2. All Integration tests and repository tests failing 
- Check if test db is setup properly
- Check if there is a migration mismatch in the schema_version table in the public schema
3. Port already in use 
- Check if something else is using the port designated to the db
4. Migration script checksum mismatch error
- Delete the record and all record below it in schema_version table
- Rerun bootrun or build


