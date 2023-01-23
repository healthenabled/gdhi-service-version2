package db.migration;

import org.springframework.jdbc.core.JdbcTemplate;

public class V2_13__Adding_Approver_Columns_To_Country_Summary   {

    
    public void doMigrate(JdbcTemplate jdbcTemplate) {

        jdbcTemplate.update("ALTER TABLE country_health_data.country_summary ADD COLUMN data_approver_name VARCHAR");
        jdbcTemplate.update("ALTER TABLE country_health_data.country_summary ADD COLUMN data_approver_role VARCHAR");
        jdbcTemplate.update("ALTER TABLE country_health_data.country_summary ADD COLUMN data_approver_email VARCHAR");

    }
}
