package db.migration;

import org.springframework.jdbc.core.JdbcTemplate;

public class V2_12__Adding_Timestamps_To_Country_Data   {

    
    public void doMigrate(JdbcTemplate jdbcTemplate) {
        jdbcTemplate.update("ALTER TABLE country_health_data.country_summary ADD COLUMN created_at TIMESTAMP WITHOUT" +
                " TIME ZONE DEFAULT now()");
        jdbcTemplate.update("ALTER TABLE country_health_data.country_summary ADD COLUMN updated_at TIMESTAMP WITHOUT" +
                " TIME ZONE");

        jdbcTemplate.update("ALTER TABLE country_health_data.country_resource_links ADD COLUMN created_at TIMESTAMP WITHOUT" +
                " TIME ZONE DEFAULT now()");
        jdbcTemplate.update("ALTER TABLE country_health_data.country_resource_links ADD COLUMN updated_at TIMESTAMP WITHOUT" +
                " TIME ZONE");

        jdbcTemplate.update("ALTER TABLE country_health_data.health_indicators ADD COLUMN created_at TIMESTAMP WITHOUT" +
                " TIME ZONE DEFAULT now()");
        jdbcTemplate.update("ALTER TABLE country_health_data.health_indicators ADD COLUMN updated_at TIMESTAMP WITHOUT" +
                " TIME ZONE");
    }
}
