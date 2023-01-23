package db.migration;

import org.springframework.jdbc.core.JdbcTemplate;

public class V1_21__Alter_Table_Health_Indicators_Add_Supporting_Text   {
    
    public void doMigrate(JdbcTemplate jdbcTemplate) {
        jdbcTemplate.execute("ALTER TABLE validated_config.health_indicators ADD COLUMN supporting_text VARCHAR ");
    }
}



