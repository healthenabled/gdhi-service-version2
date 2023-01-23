package db.migration;

import org.springframework.jdbc.core.JdbcTemplate;

public class V3_1__Create_Table_i18_Language   {
    
    public void doMigrate(JdbcTemplate jdbcTemplate) {
        jdbcTemplate.execute("CREATE TABLE i18n.language(" +
                                 "id VARCHAR PRIMARY KEY, " +
                                 "language_name VARCHAR " +
                                 ");"
        );
    }
}



