package db.migration;

import org.springframework.jdbc.core.JdbcTemplate;

public class V3_9__Create_Table_i18_Score_Definition   {
    
    public void doMigrate(JdbcTemplate jdbcTemplate) {
        jdbcTemplate.execute("CREATE TABLE i18n.score_definition(" +
                                "indicator_id INTEGER, " +
                                "score INTEGER, " +
                                "language_id VARCHAR, " +
                                "definition VARCHAR, " +
                                "PRIMARY KEY (indicator_id, score, language_id), " +
                                "FOREIGN KEY(language_id) REFERENCES i18n.language (id)" +
                                ");"
        );
    }
}



