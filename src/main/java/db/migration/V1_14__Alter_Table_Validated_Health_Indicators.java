package db.migration;

import org.springframework.jdbc.core.JdbcTemplate;

public class V1_14__Alter_Table_Validated_Health_Indicators   {
    
    public void doMigrate(JdbcTemplate jdbcTemplate) {

        jdbcTemplate.execute("ALTER TABLE validated_config.health_indicators " +
                "ADD CONSTRAINT health_indicator_pkey PRIMARY KEY (country_id, last_survey_date, category_id, indicator_id);");
    }
}



