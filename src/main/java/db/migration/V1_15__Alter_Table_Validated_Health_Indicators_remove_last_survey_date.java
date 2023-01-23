package db.migration;

import org.springframework.jdbc.core.JdbcTemplate;

public class V1_15__Alter_Table_Validated_Health_Indicators_remove_last_survey_date   {
    
    public void doMigrate(JdbcTemplate jdbcTemplate) {
        jdbcTemplate.execute("ALTER TABLE validated_config.health_indicators DROP CONSTRAINT " +
                                 "health_indicator_pkey");
        jdbcTemplate.execute("ALTER TABLE validated_config.health_indicators DROP COLUMN " +
                                 "last_survey_date");
        jdbcTemplate.execute("ALTER TABLE validated_config.health_indicators ADD CONSTRAINT health_indicators_pkey PRIMARY KEY " +
                                 "(country_id, category_id, indicator_id)");
    }
}



