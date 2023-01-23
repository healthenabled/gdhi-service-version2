package db.migration;

import org.springframework.jdbc.core.JdbcTemplate;

public class V2_14__Modifying_Score_For_Not_Applicable_To_Minus_One   {

    
    public void doMigrate(JdbcTemplate jdbcTemplate) {

        jdbcTemplate.execute("ALTER TABLE country_health_data.health_indicators DROP CONSTRAINT score_limit_check");

        jdbcTemplate.execute("ALTER TABLE country_health_data.health_indicators ADD CONSTRAINT score_limit_check " +
                "CHECK ((indicator_score >=-1 AND indicator_score <=5) OR indicator_score IS NULL);");

        jdbcTemplate.update("UPDATE master.health_indicator_scores SET score = -1 " +
                "WHERE " +
                "definition like 'Not Available or Not Applicable'");

        jdbcTemplate.update("UPDATE country_health_data.health_indicators SET indicator_score = -1 " +
                "WHERE indicator_score IS NULL");
    }
}
