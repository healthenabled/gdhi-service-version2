package db.migration;

import org.springframework.jdbc.core.JdbcTemplate;

public class V3_19__Update_Master_Health_Indicator_Scores_Data   {
    
    public void doMigrate(JdbcTemplate jdbcTemplate) {
        jdbcTemplate.update("UPDATE master.health_indicator_scores SET definition=" +
                "'There is no digital health strategy or framework. Draft digital health strategy or framework " +
                "developed, but not officially reviewed.' WHERE indicator_id=3 AND score=1");
        jdbcTemplate.update("UPDATE master.health_indicator_scores SET definition=" +
                "'No budget line item for digital health available. A budget line item for digital health exists but " +
                "proportion not available.' WHERE indicator_id=4 AND score=1");
        jdbcTemplate.update("UPDATE master.health_indicator_scores SET definition=" +
                "'There is a law on data security (storage, transmission, use) that is relevant to digital health that " +
                "has been implemented, but not consistently enforced.' WHERE indicator_id=5 AND score=4");
        jdbcTemplate.update("UPDATE master.health_indicator_scores SET definition=" +
                "'There is a law to protect individual privacy, governing ownership, access and sharing of individually" +
                " identifiable digital health data that has been implemented, but not consistently enforced.' " +
                "WHERE indicator_id=6 AND score=4");
        jdbcTemplate.update("UPDATE master.health_indicator_scores SET definition=" +
                "'Protocols, policies, frameworks or accepted processes governing the clinical and patient care use of " +
                "connected medical devices and digital health services (e.g. telemedicine, applications), particularly " +
                "in relation to safety, data integrity and quality of care have been implemented, but not consistently " +
                "enforced.' WHERE indicator_id=7 AND score=4");
        jdbcTemplate.update("UPDATE master.health_indicator_scores SET definition=" +
                "'Protocols, policies, frameworks or accepted processes for cross border data exchange and storage " +
                "have been proposed and are under review.' WHERE indicator_id=8 AND score=2");
        jdbcTemplate.update("UPDATE master.health_indicator_scores SET definition=" +
                "'Protocols, policies, frameworks or accepted processes for cross border data exchange and storage have " +
                "been passed, but are not fully implemented.' WHERE indicator_id=8 AND score=3");
        jdbcTemplate.update("UPDATE master.health_indicator_scores SET definition=" +
                "'Protocols, policies, frameworks or accepted processes for cross border data exchange and storage have " +
                "been implemented, but not consistently enforced.' WHERE indicator_id=8 AND score=4");
        jdbcTemplate.update("UPDATE master.health_indicator_scores SET definition=" +
                "'Protocols, policies, frameworks or accepted processes for cross border data exchange and storage have " +
                "been implemented and enforced consistently.' WHERE indicator_id=8 AND score=5");
        jdbcTemplate.update("UPDATE master.health_indicator_scores SET definition=" +
                "'Digital health taught in relevant institutions with an estimated 50-75% health professionals " +
                "receiving pre-service training.' WHERE indicator_id=9 AND score=4");
        jdbcTemplate.update("UPDATE master.health_indicator_scores SET definition=" +
                "'Digital health taught in relevant institutions with an estimated 50-75% of doctors/physicians " +
                "receiving pre-service training.' WHERE indicator_id=20 AND score=4");
        jdbcTemplate.update("UPDATE master.health_indicator_scores SET definition=" +
                "'Digital health taught in relevant institutions with >75% of doctors/physicians receiving pre-service " +
                "training.' WHERE indicator_id=20 AND score=5");
        jdbcTemplate.update("UPDATE master.health_indicator_scores SET definition=" +
                "'Digital health taught in relevant institutions with an estimated 50-75% of nurses receiving " +
                "pre-service training.' WHERE indicator_id=21 AND score=4");
        jdbcTemplate.update("UPDATE master.health_indicator_scores SET definition=" +
                "'Digital health taught in relevant institutions with an estimated 50-75% of community health workers " +
                "receiving pre-service training.' WHERE indicator_id=22 AND score=4");
        jdbcTemplate.update("UPDATE master.health_indicator_scores SET definition=" +
                "'Digital health curriculum proposed and under review as part of in-service (continuing education) " +
                "training for health professionals in the workforce.' WHERE indicator_id=10 AND score=2");
        jdbcTemplate.update("UPDATE master.health_indicator_scores SET definition=" +
                "'Digital health curriculum is implemented as part of in-service (continuing education) training for " +
                "0-25% health professionals in the workforce.' WHERE indicator_id=10 AND score=3");
        jdbcTemplate.update("UPDATE master.health_indicator_scores SET definition=" +
                "'Digital health curriculum is implemented as part of in-service (continuing education) training for " +
                "50-75% health professionals in the workforce.' WHERE indicator_id=10 AND score=4");
        jdbcTemplate.update("UPDATE master.health_indicator_scores SET definition=" +
                "'Digital health curriculum is implemented as part of in-service (continuing education) training for " +
                ">75% health professionals in the workforce.' WHERE indicator_id=10 AND score=5");
        jdbcTemplate.update("UPDATE master.health_indicator_scores SET definition=" +
                "'Digital health curriculum is implemented as part of in-service (continuing education) training for " +
                "50-75% of doctors/physicians in the workforce.' WHERE indicator_id=23 AND score=4");
        jdbcTemplate.update("UPDATE master.health_indicator_scores SET definition=" +
                "'Digital health curriculum is implemented as part of in-service (continuing education) training for " +
                "50-75% of nurses in the workforce.' WHERE indicator_id=24 AND score=4");
        jdbcTemplate.update("UPDATE master.health_indicator_scores SET definition=" +
                "'Digital health curriculum is implemented as part of in-service (continuing education) training for " +
                "50-75% of community health workers in the workforce.' WHERE indicator_id=25 AND score=4");
        jdbcTemplate.update("UPDATE master.health_indicator_scores SET definition=" +
                "'There is no training available for digital health workforce available in the country.' " +
                "WHERE indicator_id=11 AND score=1");
        jdbcTemplate.update("UPDATE master.health_indicator_scores SET definition=" +
                "'Digital health workforce needs assessed, gaps identified and training options under development.' " +
                "WHERE indicator_id=11 AND score=2");
        jdbcTemplate.update("UPDATE master.health_indicator_scores SET definition=" +
                "'No workforce strategy, policy, or guide that recognizes digital health is in place. Distribution of " +
                "digital health workforce is ad hoc.' WHERE indicator_id=12 AND score=1");
        jdbcTemplate.update("UPDATE master.health_indicator_scores SET definition=" +
                "'A national digital health architecture and/or health information exchange (HIE) has been proposed, " +
                "but not approved including semantic, syntactic, and organizational layers.' " +
                "WHERE indicator_id=13 AND score=2");
        jdbcTemplate.update("UPDATE master.health_indicator_scores SET definition=" +
                "'The national digital health architecture and/or health information exchange (HIE) is operable and " +
                "provides core functions, such as authentication, translation, storage and warehousing function, guide " +
                "to what data is available and how to access it, and data interpretation.' " +
                "WHERE indicator_id=13 AND score=3");
        jdbcTemplate.update("UPDATE master.health_indicator_scores SET definition=" +
                "'1.0 - 3.3' WHERE indicator_id=15 AND score=1");
        jdbcTemplate.update("UPDATE master.health_indicator_scores SET definition=" +
                "'>3.3 - 4.0' WHERE indicator_id=15 AND score=2");
        jdbcTemplate.update("UPDATE master.health_indicator_scores SET definition=" +
                "'>4.0 - 5.0' WHERE indicator_id=15 AND score=3");
        jdbcTemplate.update("UPDATE master.health_indicator_scores SET definition=" +
                "'>5.0 - 5.4' WHERE indicator_id=15 AND score=4");
        jdbcTemplate.update("UPDATE master.health_indicator_scores SET definition=" +
                "'>5.4 - 7.0' WHERE indicator_id=15 AND score=5");
        jdbcTemplate.update("UPDATE master.health_indicator_scores SET definition=" +
                "'A plan for supporting digital health infrastructure (including equipment- computers/ tablets/ phones, " +
                "supplies, software, devices, etc.) provision and maintenance has been implemented partially and " +
                "consistently with estimated 25-50% of necessary digital health infrastructure needed in public " +
                "healthcare service sector available and in use.' WHERE indicator_id=16 AND score=4");
        jdbcTemplate.update("UPDATE master.health_indicator_scores SET definition=" +
                "'National priority areas are not supported by digital health at any scale.' " +
                "WHERE indicator_id=17 AND score=1");
        jdbcTemplate.update("UPDATE master.health_indicator_scores SET definition=" +
                "'Few national priority areas are supported by digital health, and implementation initiated " +
                "(< 25% priority areas).' " +
                "WHERE indicator_id=17 AND score=2");
        jdbcTemplate.update("UPDATE master.health_indicator_scores SET definition=" +
                "'Some national priority areas supported by scaled digital health systems (25-50% of priority areas).' " +
                "WHERE indicator_id=17 AND score=3");
        jdbcTemplate.update("UPDATE master.health_indicator_scores SET definition=" +
                "'Some national priority areas supported by scaled digital health systems (25-50% of priority areas).' " +
                "WHERE indicator_id=17 AND score=3");
        jdbcTemplate.update("UPDATE master.health_indicator_scores SET definition=" +
                "'The majority, but not all national priority areas (50-75% of priority areas) supported by scaled " +
                "digital health systems.' WHERE indicator_id=17 AND score=4");
        jdbcTemplate.update("UPDATE master.health_indicator_scores SET definition=" +
                "'All nationally prioritized areas supported by national-scale digital health systems (>75%) with " +
                "monitoring and evaluation systems and results.' WHERE indicator_id=17 AND score=5");
        jdbcTemplate.update("UPDATE master.health_indicator_scores SET definition=" +
                "'A secure registry exists, is available and in active use and includes 25-50% of the relevant " +
                "population.' WHERE indicator_id=19 AND score=4");
        jdbcTemplate.update("UPDATE master.health_indicator_scores SET definition=" +
                "'A master patient index exists, is available and in active use and includes 25-50% of the relevant " +
                "population.' WHERE indicator_id=27 AND score=4");
        jdbcTemplate.update("UPDATE master.health_indicator_scores SET definition=" +
                "'No secure birth registry exists.' WHERE indicator_id=28 AND score=1");
        jdbcTemplate.update("UPDATE master.health_indicator_scores SET definition=" +
                "'A secure birth registry exists, but is incomplete / partially available, used, and irregularly maintained.' " +
                "WHERE indicator_id=28 AND score=2");
        jdbcTemplate.update("UPDATE master.health_indicator_scores SET definition=" +
                "'A secure birth registry exists, is available and in active use and includes <25% of the relevant " +
                "population.' WHERE indicator_id=28 AND score=3");
        jdbcTemplate.update("UPDATE master.health_indicator_scores SET definition=" +
                "'A secure birth registry exists, is available and in active use and includes 25-50% of the relevant " +
                "population.' WHERE indicator_id=28 AND score=4");
        jdbcTemplate.update("UPDATE master.health_indicator_scores SET definition=" +
                "'A secure birth registry exists, is available and in active use and includes >75% of the relevant " +
                "population. The data is available, used, and curated.' WHERE indicator_id=28 AND score=5");
        jdbcTemplate.update("UPDATE master.health_indicator_scores SET definition=" +
                "'A secure death registry exists, is available and in active use and includes 25-50% of the relevant " +
                "population.' WHERE indicator_id=29 AND score=4");
        jdbcTemplate.update("UPDATE master.health_indicator_scores SET definition=" +
                "'A secure death registry exists, is available and in active use and includes 25-50% of the relevant " +
                "population.' WHERE indicator_id=29 AND score=4");
        jdbcTemplate.update("UPDATE master.health_indicator_scores SET definition=" +
                "'A secure immunization registry exists, is available and in active use and includes 25-50% of the " +
                "relevant population.' WHERE indicator_id=30 AND score=4");
    }
}
