package db.migration;

import org.springframework.jdbc.core.JdbcTemplate;

public class V1_01__Create_Table_Countries   {

    
    public void doMigrate(JdbcTemplate jdbcTemplate) {
        jdbcTemplate.execute("CREATE TABLE master.countries(id VARCHAR PRIMARY KEY, name VARCHAR);");
    }

}
