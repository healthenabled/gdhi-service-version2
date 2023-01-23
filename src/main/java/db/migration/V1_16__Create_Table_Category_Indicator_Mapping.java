package db.migration;

import org.springframework.jdbc.core.JdbcTemplate;

public class V1_16__Create_Table_Category_Indicator_Mapping   {
    
    public void doMigrate(JdbcTemplate jdbcTemplate) {
        jdbcTemplate.execute("CREATE TABLE master.categories_indicators(category_id INTEGER, " +
                                 "indicator_id INTEGER, PRIMARY KEY (category_id, indicator_id)," +
                                 "FOREIGN KEY(category_id) REFERENCES master.categories (id),"  +
                                 "FOREIGN KEY(indicator_id) REFERENCES master.health_indicators (indicator_id));");
    }
}



