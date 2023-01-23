package db.migration;

import org.springframework.jdbc.core.JdbcTemplate;

public class V2_03__Remove_Score_0_From_Health_Indicators   {

    
    public void doMigrate(JdbcTemplate jdbcTemplate) {
        jdbcTemplate.update("delete from master.scores where score=0");

        jdbcTemplate.update("delete from master.health_indicator_scores where score=0");

        jdbcTemplate.update("update country_health_data.health_indicators set indicator_score=null where indicator_score=0");
    }
}
