package db.migration;

import org.springframework.jdbc.core.JdbcTemplate;

public class V1_22__Alter_Table_Health_Indicators_Add_Constraints   {
    
    public void doMigrate(JdbcTemplate jdbcTemplate) {
     jdbcTemplate.execute("ALTER TABLE validated_config.health_indicators ALTER COLUMN country_id SET NOT NULL;");
     jdbcTemplate.execute("ALTER TABLE validated_config.health_indicators ALTER COLUMN category_id SET NOT NULL;");
     jdbcTemplate.execute("ALTER TABLE validated_config.health_indicators ALTER COLUMN indicator_id SET NOT NULL;");
     jdbcTemplate.execute("ALTER TABLE validated_config.health_indicators ADD CONSTRAINT score_limit_check " +
                               "CHECK ((indicator_score >=0 AND indicator_score <=5) OR indicator_score IS NULL);");
    }
}



