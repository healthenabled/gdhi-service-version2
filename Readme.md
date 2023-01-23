**Setup**

1. Create user and database
```
    CREATE USER gdhi WITH PASSWORD 'password';
    CREATE DATABASE gdhi OWNER gdhi;
```
2. Install uuid extension

connect to DB gdhi
```
    create extension if not exists "uuid-ossp";
```

3. Execute `sh ./utils/set-up-git-hooks.sh` from base folder to validate commit message format.

4. Upon the server start, hit the following endpoint to populate the country overall phase (Only for Published Countries.)

```
   localhost:8080/api/admin/countries/calculate_phase
```
**To Run**

1. Run by executing
`./gradlew clean bootRun`

**INTEGRATION TESTS**
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