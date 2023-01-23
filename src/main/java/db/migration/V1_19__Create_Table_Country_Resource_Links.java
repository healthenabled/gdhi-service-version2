package db.migration;

import org.springframework.jdbc.core.JdbcTemplate;

public class V1_19__Create_Table_Country_Resource_Links   {
    
    public void doMigrate(JdbcTemplate jdbcTemplate) {
        jdbcTemplate.execute("CREATE TABLE validated_config.country_resource_links(country_id VARCHAR, "
                + "link VARCHAR, FOREIGN KEY(country_id) REFERENCES master.countries (id));");
    }
}



