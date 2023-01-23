package db.migration;

import org.springframework.jdbc.core.JdbcTemplate;

public class V1_24__Update_Master_Health_Indicator_data   {

    
    public void doMigrate(JdbcTemplate jdbcTemplate) {
        jdbcTemplate.update("UPDATE master.health_indicator_scores SET definition='National priority areas are supported by digital health, and implementation initiated (< 25% priority areas)' where id=115");
        jdbcTemplate.update("UPDATE master.health_indicator_scores SET definition='Very few national priority areas supported by scaled digital health systems (25-50% of priority areas)' where id=116");
        jdbcTemplate.update("UPDATE master.health_indicator_scores SET definition='Few national priority areas supported by scaled digital health systems (50-75% of priority areas)' where id=117");
        jdbcTemplate.update("UPDATE master.health_indicator_scores SET definition='Some, but not all national priority areas (>75% of priority areas) supported by scaled digital health systems' where id=118");
    }
}
