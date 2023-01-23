package db.migration;

import org.springframework.jdbc.core.JdbcTemplate;

public class V1_23__Add_Score_Description_For_Network_Indicator   {
    
    public void doMigrate(JdbcTemplate jdbcTemplate) {
     jdbcTemplate.execute("UPDATE master.health_indicator_scores set definition='WEF score (<1.0)' WHERE indicator_id=15 AND score=0;");
     jdbcTemplate.execute("UPDATE master.health_indicator_scores set definition='WEF score (1.0 - 3.3)' WHERE indicator_id=15 AND score=1;");
     jdbcTemplate.execute("UPDATE master.health_indicator_scores set definition='WEF score (>3.3 - 4.0)' WHERE indicator_id=15 AND score=2;");
     jdbcTemplate.execute("UPDATE master.health_indicator_scores set definition='WEF score (>4.0 - 5.0)' WHERE indicator_id=15 AND score=3;");
     jdbcTemplate.execute("UPDATE master.health_indicator_scores set definition='WEF score (>5.0 - 5.4)' WHERE indicator_id=15 AND score=4;");
     jdbcTemplate.execute("UPDATE master.health_indicator_scores set definition='WEF score (>5.4 - 7.0)' WHERE indicator_id=15 AND score=5;");
     jdbcTemplate.execute("UPDATE master.health_indicator_scores set definition='Missing or Not Available' WHERE indicator_id=15 AND score is NULL ;");
     jdbcTemplate.execute("UPDATE master.health_indicators set definition='WEF Network Readiness Index score' WHERE indicator_id=15;");
    }
}



