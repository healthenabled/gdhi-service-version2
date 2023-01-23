package db.migration;

import org.springframework.jdbc.core.JdbcTemplate;

public class V2_11__Change_Master_Health_Indicator_Data   {

    
    public void doMigrate(JdbcTemplate jdbcTemplate) {
        jdbcTemplate.update("UPDATE master.health_indicator_scores SET" +
                " definition='Not Available or Not Applicable' " +
                "where score is null");
    }
}
