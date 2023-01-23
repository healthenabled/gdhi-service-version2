package db.migration;

import org.springframework.jdbc.core.JdbcTemplate;

public class V3_0__Create_Schema_i18n_Mapping   {

    
    public void doMigrate(JdbcTemplate jdbcTemplate) {
        jdbcTemplate.execute("CREATE SCHEMA i18n;");
    }

}
