package db.migration;

import org.springframework.jdbc.core.JdbcTemplate;

public class V1_03__Create_Table_Health_Indicator_Scores   {

    
    public void doMigrate(JdbcTemplate jdbcTemplate) {
        jdbcTemplate.execute("CREATE TABLE master.health_indicator_scores(indicator_id INTEGER, " +
                "score INTEGER, definition VARCHAR, " +
                "FOREIGN KEY(indicator_id) REFERENCES master.health_indicators (indicator_id));");
    }

}
