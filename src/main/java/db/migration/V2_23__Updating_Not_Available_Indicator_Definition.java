package db.migration;

import org.springframework.jdbc.core.JdbcTemplate;

public class V2_23__Updating_Not_Available_Indicator_Definition  {

    
    public void doMigrate(JdbcTemplate jdbcTemplate) {
        jdbcTemplate.update("UPDATE master.health_indicator_scores SET definition= 'Not Available' WHERE score = -1;");
    }
}
