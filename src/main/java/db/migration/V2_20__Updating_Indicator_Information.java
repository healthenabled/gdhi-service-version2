package db.migration;

import org.springframework.jdbc.core.JdbcTemplate;

public class V2_20__Updating_Indicator_Information  {

    
    public void doMigrate(JdbcTemplate jdbcTemplate) {
        jdbcTemplate.update("UPDATE master.health_indicator_scores SET definition = 'There is no digital health strategy or framework or draft digital health strategy or framework developed, but not officially reviewed.' WHERE indicator_id = 3 AND score = 1");
        jdbcTemplate.update("UPDATE master.health_indicator_scores SET definition = 'No budget line item for ICT or digital health available or a budget line item for ICT exists but proportion not available.' WHERE indicator_id = 4 AND score =1");
        jdbcTemplate.update("UPDATE master.health_indicator_scores SET definition = 'Digital health taught in relevant institutions with an estimated 26-75% health professionals receiving pre-service training.' WHERE indicator_id = 9 AND score = 4");
        jdbcTemplate.update("UPDATE master.health_indicator_scores SET definition = 'Digital health taught in relevant institutions with an estimated 26-75% of doctors/physicians receiving pre-service training.' WHERE indicator_id = 20 AND score = 4");
        jdbcTemplate.update("UPDATE master.health_indicator_scores SET definition = 'Digital health taught in relevant institutions with an estimated 26-75% of nurses receiving pre-service training.' WHERE indicator_id = 21 AND score = 4");
        jdbcTemplate.update("UPDATE master.health_indicator_scores SET definition = 'Digital health taught in relevant institutions with an estimated 26-75% of community health workers receiving pre-service training.' WHERE indicator_id = 22 AND score = 4");
        jdbcTemplate.update("UPDATE master.health_indicator_scores SET definition = 'Digital health curriculum is implemented as part of in-service (continuing education) training for 26-75% health professionals in the workforce.' WHERE indicator_id = 10 AND score = 4");
        jdbcTemplate.update("UPDATE master.health_indicator_scores SET definition = 'Digital health curriculum is implemented as part of in-service (continuing education) training for 26-75% of doctors/physicians in the workforce.' WHERE indicator_id = 23 AND score = 4");
        jdbcTemplate.update("UPDATE master.health_indicator_scores SET definition = 'Digital health curriculum is implemented as part of in-service (continuing education) training for 26-75% of nurses in the workforce.' WHERE indicator_id = 24 AND score = 4");
        jdbcTemplate.update("UPDATE master.health_indicator_scores SET definition = 'Digital health curriculum is implemented as part of in-service (continuing education) training for 26-75% of community health workers in the workforce.' WHERE indicator_id = 25 AND score = 4");
        jdbcTemplate.update("UPDATE master.health_indicator_scores SET definition = 'A plan for supporting digital health infrastructure (including equipment- computers/ tablets/ phones, supplies, software, devices, etc.) provision and maintenance has been implemented partially and consistently with estimated 26-75% of necessary digital health infrastructure needed in public healthcare service sector available and in use.' WHERE indicator_id = 16 AND score = 4");
        jdbcTemplate.update("UPDATE master.health_indicators SET definition = 'Public sector health priorities are supported by nationally-scaled digital health systems. (Use separate worksheet to list the country''s specified priority areas, whether digital systems are in place, and whether those systems are national-scale.) [eg. Country X chooses 4 priority areas, uses digital systems to address 2 of the 4, with only 1 being at national scale, receives a score of 25%.]' WHERE indicator_id = 17");
        jdbcTemplate.update("UPDATE master.health_indicators SET definition = 'Specifically, is there a secure digital birth registry of uniquely identifiable individuals available, accessible and current for use for health-related purposes?' WHERE indicator_id = 28");
        jdbcTemplate.update("UPDATE master.health_indicators SET definition = 'Extract the WEF Network Readiness Index score. This data can be sourced from the WEF Network Readiness Index (http://reports.weforum.org/global-information-technology-report-2016/networked-readiness-index/) or will be added by the GDHI team upon submission.' WHERE indicator_id = 15");
        jdbcTemplate.update("UPDATE master.health_indicator_scores SET definition = 'A secure registry exists, is available and inactive use and includes 26-75% of the relevant population.' WHERE indicator_id = 19 AND score = 4");
        jdbcTemplate.update("UPDATE master.health_indicator_scores SET definition = 'A master patient index exists, is available and inactive use and includes 26-75% of the relevant population.' WHERE indicator_id = 27 AND score = 4");
        jdbcTemplate.update("UPDATE master.health_indicator_scores SET definition = 'A secure digital birth registry exists, is available and inactive use and includes 26-75% of the relevant population.' WHERE indicator_id = 28 AND score = 4");
        jdbcTemplate.update("UPDATE master.health_indicator_scores SET definition = 'A secure death registry exists, is available and inactive use and includes 26-75% of the relevant population.' WHERE indicator_id = 29 AND score = 4");
        jdbcTemplate.update("UPDATE master.health_indicator_scores SET definition = 'A secure immunization registry exists, is available and inactive use and includes 26-75% of the relevant population.' WHERE indicator_id = 30 AND score = 4");
        jdbcTemplate.update("UPDATE master.health_indicator_scores SET definition = 'No secure digital birth registry exists.' WHERE indicator_id = 28 AND score = 1");
        jdbcTemplate.update("UPDATE master.health_indicator_scores SET definition = 'A secure digital birth registry exists, but is incomplete / partially available, used, and irregularly maintained.' WHERE indicator_id = 28 AND score = 2");
        jdbcTemplate.update("UPDATE master.health_indicator_scores SET definition = 'A secure digital birth registry exists, is available and inactive use and includes <25% of the relevant population.' WHERE indicator_id = 28 AND score = 3");
        jdbcTemplate.update("UPDATE master.health_indicator_scores SET definition = 'A secure digital birth registry exists, is available and inactive use and includes >75% of the relevant population. The data is available, used, and curated.' WHERE indicator_id = 28 AND score = 5");
    }
}
