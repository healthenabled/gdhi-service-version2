package db.migration;

import org.springframework.jdbc.core.JdbcTemplate;

public class V2_10__Add_Rank_to_Master_Health_Indicators   {

    
    public void doMigrate(JdbcTemplate jdbcTemplate) {
        jdbcTemplate.update("alter table master.health_indicators add column rank INTEGER");

        jdbcTemplate.update("UPDATE master.health_indicators SET rank=1 where code='1'");
        jdbcTemplate.update("UPDATE master.health_indicators SET rank=2 where code='2'");
        jdbcTemplate.update("UPDATE master.health_indicators SET rank=3 where code='3'");
        jdbcTemplate.update("UPDATE master.health_indicators SET rank=4 where code='4'");
        jdbcTemplate.update("UPDATE master.health_indicators SET rank=5 where code='5'");
        jdbcTemplate.update("UPDATE master.health_indicators SET rank=6 where code='6'");
        jdbcTemplate.update("UPDATE master.health_indicators SET rank=7 where code='7'");
        jdbcTemplate.update("UPDATE master.health_indicators SET rank=8 where code='8'");
        jdbcTemplate.update("UPDATE master.health_indicators SET rank=9 where code='9'");
        jdbcTemplate.update("UPDATE master.health_indicators SET rank=10 where code='9a'");
        jdbcTemplate.update("UPDATE master.health_indicators SET rank=11 where code='9b'");
        jdbcTemplate.update("UPDATE master.health_indicators SET rank=12 where code='9c'");
        jdbcTemplate.update("UPDATE master.health_indicators SET rank=13 where code='10'");
        jdbcTemplate.update("UPDATE master.health_indicators SET rank=14 where code='10a'");
        jdbcTemplate.update("UPDATE master.health_indicators SET rank=15 where code='10b'");
        jdbcTemplate.update("UPDATE master.health_indicators SET rank=16 where code='10c'");
        jdbcTemplate.update("UPDATE master.health_indicators SET rank=17 where code='11'");
        jdbcTemplate.update("UPDATE master.health_indicators SET rank=18 where code='11a'");
        jdbcTemplate.update("UPDATE master.health_indicators SET rank=19 where code='12'");
        jdbcTemplate.update("UPDATE master.health_indicators SET rank=20 where code='13'");
        jdbcTemplate.update("UPDATE master.health_indicators SET rank=21 where code='14'");
        jdbcTemplate.update("UPDATE master.health_indicators SET rank=22 where code='15'");
        jdbcTemplate.update("UPDATE master.health_indicators SET rank=23 where code='16'");
        jdbcTemplate.update("UPDATE master.health_indicators SET rank=24 where code='17'");
        jdbcTemplate.update("UPDATE master.health_indicators SET rank=25 where code='18'");
        jdbcTemplate.update("UPDATE master.health_indicators SET rank=26 where code='19'");
        jdbcTemplate.update("UPDATE master.health_indicators SET rank=27 where code='19a'");
        jdbcTemplate.update("UPDATE master.health_indicators SET rank=28 where code='19b'");
        jdbcTemplate.update("UPDATE master.health_indicators SET rank=29 where code='19c'");
        jdbcTemplate.update("UPDATE master.health_indicators SET rank=30 where code='19d'");

    }
}
