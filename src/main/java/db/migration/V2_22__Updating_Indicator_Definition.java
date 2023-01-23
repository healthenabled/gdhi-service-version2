package db.migration;

import org.springframework.jdbc.core.JdbcTemplate;

public class V2_22__Updating_Indicator_Definition  {

    
    public void doMigrate(JdbcTemplate jdbcTemplate) {
        jdbcTemplate.update("UPDATE master.health_indicator_scores SET definition = 'A secure digital birth registry exists, is available and in active use and includes <25% of the relevant population.' WHERE indicator_id = 28 AND score = 3");
        jdbcTemplate.update("UPDATE master.health_indicator_scores SET definition = 'A secure digital birth registry exists, is available and in active use and includes >75% of the relevant population. The data is available, used, and curated.' WHERE indicator_id = 28 AND score = 5");
    }
}
