package db.migration;

import org.springframework.jdbc.core.JdbcTemplate;

public class V1_17__Insert_Category_Indicator_Mapping   {
    
    public void doMigrate(JdbcTemplate jdbcTemplate) {
        jdbcTemplate.execute("insert into master.categories_indicators(category_id, indicator_id) values \n" +
                "(1, 1),(1, 2),(2, 3),(2, 4),\n" +
                "(3, 5),(3, 6),(3, 7),(3, 8),\n" +
                "(4, 9),(4, 10),(4, 11),(4, 12),\n" +
                "(5, 13),(5, 14),(6, 15),(6, 16),\n" +
                "(7, 17),(7, 18),(7, 19);");
    }
}



