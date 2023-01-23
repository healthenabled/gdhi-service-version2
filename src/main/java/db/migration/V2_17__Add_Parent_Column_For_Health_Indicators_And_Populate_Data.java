package db.migration;

import org.springframework.jdbc.core.JdbcTemplate;

public class V2_17__Add_Parent_Column_For_Health_Indicators_And_Populate_Data  {

    
    public void doMigrate(JdbcTemplate jdbcTemplate) {
        jdbcTemplate.update("ALTER TABLE master.health_indicators ADD COLUMN parent_id INTEGER DEFAULT null;");
        jdbcTemplate.update("UPDATE master.health_indicators SET parent_id = 9 WHERE code IN ('9a','9b','9c')");
        jdbcTemplate.update("UPDATE master.health_indicators SET parent_id = 10 WHERE code IN ('10a','10b','10c')");
        jdbcTemplate.update("UPDATE master.health_indicators SET parent_id = 11 WHERE code IN ('11a')");
        jdbcTemplate.update("UPDATE master.health_indicators SET parent_id = 19 WHERE code IN ('19a','19b','19c','19d')");
        }
}
