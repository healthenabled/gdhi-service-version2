package db.migration;

import org.springframework.jdbc.core.JdbcTemplate;

public class V2_08__Update_Code_For_Master_Health_Indicator   {

    
    public void doMigrate(JdbcTemplate jdbcTemplate) {
        //Adding code for existing indicators
        jdbcTemplate.update("UPDATE master.health_indicators SET code=indicator_id");

        //Adding not null constraint for the code column
        jdbcTemplate.update("ALTER TABLE master.health_indicators ALTER COLUMN code SET not null");
    }
}
