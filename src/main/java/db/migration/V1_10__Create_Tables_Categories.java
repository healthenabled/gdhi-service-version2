package db.migration;

import org.springframework.jdbc.core.JdbcTemplate;

public class V1_10__Create_Tables_Categories   {
    
    public void doMigrate(JdbcTemplate jdbcTemplate) {

        jdbcTemplate.execute("CREATE TABLE master.categories(id INTEGER PRIMARY KEY, name VARCHAR);");
    }
}



