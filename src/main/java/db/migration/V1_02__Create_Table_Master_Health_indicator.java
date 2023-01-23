package db.migration;

import org.springframework.jdbc.core.JdbcTemplate;

public class V1_02__Create_Table_Master_Health_indicator   {

    
    public void doMigrate(JdbcTemplate jdbcTemplate) {
        jdbcTemplate.execute("CREATE TABLE master.health_indicators(indicator_id INTEGER PRIMARY KEY , name VARCHAR, " +
                                 "definition VARCHAR);");
    }

}
