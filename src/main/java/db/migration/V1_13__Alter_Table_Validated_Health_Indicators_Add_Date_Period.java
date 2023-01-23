package db.migration;

import org.springframework.jdbc.core.JdbcTemplate;

public class V1_13__Alter_Table_Validated_Health_Indicators_Add_Date_Period   {
    
    public void doMigrate(JdbcTemplate jdbcTemplate) {
        jdbcTemplate.execute("ALTER TABLE validated_config.health_indicators ADD COLUMN last_survey_date DATE ;");
    }
}



