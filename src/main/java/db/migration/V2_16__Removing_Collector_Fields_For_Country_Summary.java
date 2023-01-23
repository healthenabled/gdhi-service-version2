package db.migration;

import org.springframework.jdbc.core.JdbcTemplate;

public class V2_16__Removing_Collector_Fields_For_Country_Summary  {

    
    public void doMigrate(JdbcTemplate jdbcTemplate) {
        jdbcTemplate.update("ALTER TABLE country_health_data.country_summary DROP COLUMN data_collector_name;");
        jdbcTemplate.update("ALTER TABLE country_health_data.country_summary DROP COLUMN data_collector_role;");
        jdbcTemplate.update("ALTER TABLE country_health_data.country_summary DROP COLUMN data_collector_email;");
    }
}
