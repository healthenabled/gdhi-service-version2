package db.migration;

import org.springframework.jdbc.core.JdbcTemplate;

public class V2_01__Rename_Validated_Config_schema   {

    
    public void doMigrate(JdbcTemplate jdbcTemplate) {
        jdbcTemplate.execute("ALTER SCHEMA validated_config RENAME TO country_health_data");
    }
}
