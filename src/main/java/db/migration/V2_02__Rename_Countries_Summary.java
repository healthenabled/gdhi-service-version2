package db.migration;

import org.springframework.jdbc.core.JdbcTemplate;

public class V2_02__Rename_Countries_Summary   {

    
    public void doMigrate(JdbcTemplate jdbcTemplate) {
        jdbcTemplate.execute("ALTER TABLE country_health_data.countries_summary " +
                "RENAME TO country_summary");
    }
}
