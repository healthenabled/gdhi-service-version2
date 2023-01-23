package db.migration;

import org.springframework.jdbc.core.JdbcTemplate;

public class V3_2__Create_Table_i18_Country   {
    
    public void doMigrate(JdbcTemplate jdbcTemplate) {
        jdbcTemplate.execute("CREATE TABLE i18n.country(" +
                                "country_id VARCHAR, " +
                                "language_id VARCHAR, " +
                                "name VARCHAR, " +
                                "PRIMARY KEY (country_id, language_id), " +
                                "FOREIGN KEY(language_id) REFERENCES i18n.language (id));"
                            );
    }

}




