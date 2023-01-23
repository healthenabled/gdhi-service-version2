package db.migration;

import org.springframework.jdbc.core.JdbcTemplate;

public class V2_06__Add_Status_Column_And_Create_Composite_Key   {

    
    public void doMigrate(JdbcTemplate jdbcTemplate) {
        jdbcTemplate.execute("ALTER TABLE country_health_data.country_summary ADD COLUMN status VARCHAR");
        jdbcTemplate.execute("ALTER TABLE country_health_data.country_resource_links ADD COLUMN status VARCHAR");
        jdbcTemplate.execute("ALTER TABLE country_health_data.health_indicators ADD COLUMN status VARCHAR");

        jdbcTemplate.execute("UPDATE country_health_data.country_summary SET status = 'PUBLISHED';");
        jdbcTemplate.execute("UPDATE country_health_data.country_resource_links SET status = 'PUBLISHED';");
        jdbcTemplate.execute("UPDATE country_health_data.health_indicators SET status = 'PUBLISHED';");

        jdbcTemplate.execute("ALTER TABLE country_health_data.country_summary  DROP CONSTRAINT countries_summary_pkey;");
        jdbcTemplate.execute("ALTER TABLE country_health_data.country_resource_links  DROP CONSTRAINT country_resource_links_country_id_fkey;");
        jdbcTemplate.execute("ALTER TABLE country_health_data.health_indicators  DROP CONSTRAINT health_indicators_pkey;");

        jdbcTemplate.execute("ALTER TABLE country_health_data.health_indicators  DROP CONSTRAINT health_indicators_country_id_fkey;");

        jdbcTemplate.execute("ALTER TABLE country_health_data.country_summary ADD CONSTRAINT country_summary_pkey PRIMARY KEY (country_id, status)");
        jdbcTemplate.execute("ALTER TABLE country_health_data.country_resource_links\n" +
                "        ADD CONSTRAINT country_resource_links_fkey FOREIGN KEY (country_id, status)\n" +
                "        REFERENCES country_health_data.country_summary (country_id, status)");

        jdbcTemplate.execute("ALTER TABLE country_health_data.health_indicators ADD CONSTRAINT health_indicators_pkey PRIMARY KEY (country_id, category_id, indicator_id, status)");

        jdbcTemplate.execute("ALTER TABLE country_health_data.health_indicators\n" +
                "        ADD CONSTRAINT health_indicators_country_id_status_fkey FOREIGN KEY (country_id, status)\n" +
                "        REFERENCES country_health_data.country_summary (country_id, status)");



    }
}
