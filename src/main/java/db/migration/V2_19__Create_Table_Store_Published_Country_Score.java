package db.migration;

import org.springframework.jdbc.core.JdbcTemplate;

public class V2_19__Create_Table_Store_Published_Country_Score  {

    
    public void doMigrate(JdbcTemplate jdbcTemplate) {
        jdbcTemplate.execute("CREATE TABLE country_health_data.country_phase(country_id VARCHAR PRIMARY KEY, " +
                "country_overall_phase INTEGER," +
                "FOREIGN KEY(country_id) REFERENCES master.countries (id))");
    }
}
