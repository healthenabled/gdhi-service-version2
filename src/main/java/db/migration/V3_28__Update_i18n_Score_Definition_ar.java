package db.migration;

import org.springframework.jdbc.core.JdbcTemplate;

public class V3_28__Update_i18n_Score_Definition_ar   {
    
    public void doMigrate(JdbcTemplate jdbcTemplate) {
        jdbcTemplate.update("UPDATE i18n.score_definition SET " +
                "definition='7.0 - 5.4<' " +
                "WHERE indicator_id=15 AND score=5 AND language_id='ar'");
    }
}

