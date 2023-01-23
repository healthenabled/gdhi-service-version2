package db.migration;

import org.springframework.jdbc.core.JdbcTemplate;

public class V2_15___Updating_Indicator_Information  {

    
    public void doMigrate(JdbcTemplate jdbcTemplate) {
        jdbcTemplate.update("UPDATE master.health_indicators " +
                "SET definition= 'Public sector priorities (eg. 14 domains included in ISO TR 14639) are supported by nationally-scaled digital health systems. (Use separate worksheet to determine the country''s specified priority areas, whether digital systems are in place, and whether those systems are national-scale.)  [eg. Country X chooses 4 priority areas, uses digital systems to address 2 of the 4, with only 1 being at national scale, receives a score of 25%%.]'" +
                "where indicator_id=17");
        //indicator 2
        jdbcTemplate.update("UPDATE master.health_indicator_scores set definition='Digital health is being implemented as part of national health or other relevant national strategies and/or plans.'" +
                "WHERE indicator_id = 2 and score = 4");

        jdbcTemplate.update("UPDATE master.health_indicator_scores set definition='Digital health is implemented and periodically evaluated and optimized in national health or other relevant national strategies and/or plans.'" +
                "WHERE indicator_id = 2 and score = 5");

        //indicator 3

        jdbcTemplate.update("UPDATE master.health_indicator_scores set definition='National digital health strategy or framework approved.'" +
                "WHERE indicator_id = 3 and score = 2");

        jdbcTemplate.update("UPDATE master.health_indicator_scores set definition='National digital health costed plan developed and approved.'" +
                "WHERE indicator_id = 3 and score = 3");

        jdbcTemplate.update("UPDATE master.health_indicator_scores set definition='National digital health strategy and costed plan partially implemented with resources to ensure full implementation.'" +
                "WHERE indicator_id = 3 and score = 4");

        jdbcTemplate.update("UPDATE master.health_indicator_scores set definition='National digital health strategy and costed plan fully implemented with planning underway for the next 3-5 year cycle.'" +
                "WHERE indicator_id = 3 and score = 5");
    }
}
