package db.migration;

import org.springframework.jdbc.core.JdbcTemplate;

public class V2_07__Add_Code_Column_To_Health_Indicators   {

    
    public void doMigrate(JdbcTemplate jdbcTemplate) {
        jdbcTemplate.execute("ALTER TABLE master.health_indicators ADD COLUMN code VARCHAR(5)");
    }
}
