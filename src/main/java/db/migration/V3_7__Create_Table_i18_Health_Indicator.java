package db.migration;

import org.springframework.jdbc.core.JdbcTemplate;

public class V3_7__Create_Table_i18_Health_Indicator   {
    
    public void doMigrate(JdbcTemplate jdbcTemplate) {
        jdbcTemplate.execute("CREATE TABLE i18n.health_indicator(" +
                                "indicator_id INTEGER, " +
                                "language_id VARCHAR, " +
                                "name VARCHAR, " +
                                "definition VARCHAR, " +
                                "PRIMARY KEY (indicator_id, language_id), " +
                                "FOREIGN KEY(language_id) REFERENCES i18n.language (id)" +
                                 ");"
        );
    }
}



