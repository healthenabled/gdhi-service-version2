package db.migration;

import org.springframework.jdbc.core.JdbcTemplate;

public class V1_12__Create_Table_Validated_Health_Indicators   {
    
    public void doMigrate(JdbcTemplate jdbcTemplate) {

        jdbcTemplate.execute("CREATE TABLE validated_config.health_indicators(country_id VARCHAR, " +
                "category_id INTEGER, indicator_id INTEGER, indicator_score INTEGER, " +
                "FOREIGN KEY(country_id) REFERENCES master.countries (id),"  +
                "FOREIGN KEY(category_id) REFERENCES master.categories (id),"  +
                "FOREIGN KEY(indicator_id) REFERENCES master.health_indicators (indicator_id));");
    }
}



