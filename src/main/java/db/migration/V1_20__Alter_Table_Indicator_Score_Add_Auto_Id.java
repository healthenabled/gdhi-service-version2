package db.migration;

import org.springframework.jdbc.core.JdbcTemplate;

public class V1_20__Alter_Table_Indicator_Score_Add_Auto_Id   {
    
    public void doMigrate(JdbcTemplate jdbcTemplate) {
        jdbcTemplate.execute("ALTER TABLE master.health_indicator_scores ADD COLUMN id BIGSERIAL PRIMARY KEY");
    }
}



