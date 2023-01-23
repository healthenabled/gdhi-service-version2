package db.migration;

import org.springframework.jdbc.core.JdbcTemplate;

public class V3_5__Create_Table_i18_Category   {
    
    public void doMigrate(JdbcTemplate jdbcTemplate) {
        jdbcTemplate.execute("CREATE TABLE i18n.category(" +
                                "category_id INTEGER, " +
                                "language_id VARCHAR, " +
                                "name VARCHAR, " +
                                "PRIMARY KEY (category_id, language_id), " +
                                "FOREIGN KEY(language_id) REFERENCES i18n.language (id)" +
                                 ");"
        );
    }
}



