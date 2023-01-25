insert into master.scores(score_type, score) values('Not Available', null);
insert into master.scores(score_type, score) values('No', 0);
insert into master.scores(score_type, score) values('Lowest Value', 1);
insert into master.scores(score_type, score) values('Low Value', 2);
insert into master.scores(score_type, score) values('Mid Value', 3);
insert into master.scores(score_type, score) values('Higher Value', 4);
insert into master.scores(score_type, score) values('Highest Value', 5);

insert into master.health_indicator_scores values
(1, null, 'Missing or Not Available'),
(1, 0, 'No coordinating body or systematic approach exists.'),
(1, 1, 'Nascent governance structure for digital health is constituted on a case-by-case basis'),
(1, 2, 'Governance structure is formally constituted though not fully-functional or meeting regularly.'),
(1, 3, 'Governance structure and any related working groups have a scope of work (SOW) and conduct regular meetings with stakeholder participation/ consultation.'),
(1, 4, 'Governance structure is fully-functional, government-led, consults with other ministries, and monitors implementation of digital health based on a work plan.'),
(1, 5,'The digital health governance structure is institutionalized, and relatively protected from interference or organizational changes. The governance structure and its TWGs emphasize gender balance in membership and are nationally recognized as the lead for digital health and consults with other ministries.');

insert into master.health_indicator_scores values(2, null, 'Missing or Not Available'),
(2, 0, 'Not included'),
(2, 1, 'Digital health is being implemented in an ad hoc fashion in health programs with some discussion of inclusion in national health or other relevant national strategies or plans'),
(2, 2, 'Proposed language for inclusion of digital health in national health or relevant national strategies and/or plans and under review'),
(2, 3, 'Digital health included in national health or relevant national strategies and/or plans'),
(2, 4, 'Digital health is being implemented as part of national health or other relevant national strategies and/or plans'),
(2, 5, 'Digital health is implemented and periodically evaluated and optimized in national health or other relevant national strategies and/or plans');

insert into master.health_indicator_scores values(3, null, 'Missing or Not Available'),
(3, 0 , 'No strategy or framework'),
(3, 1, 'Draft digital health strategy or framework developed, but not officially reviewed'),
(3, 2, 'National digital health strategy or framework approved'),
(3, 3, 'National digital health costed plan developed and approved'),
(3, 4, 'National digital health strategy and costed plan partially implemented with resources to ensure full implementation'),
(3, 5, 'National digital health strategy and costed plan fully implemented with planning underway for the next 3-5 year cycle');

insert into master.health_indicator_scores values
(4, null, 'Missing or Not Available'),
(4, 0 , 'No budget line item for ICT or digital health available'),
(4, 1, 'A budget line item for ICT exists but proportion not available'),
(4, 2, 'Less than 1%'),
(4, 3, '1-3%'),
(4, 4, '3-5%'),
(4, 5, 'Greater than 5%');

insert into master.health_indicator_scores values
(5, null, 'Missing or Not Available'),
(5, 0 , 'Non-existent'),
(5, 1, 'Proposed'),
(5, 2, 'Proposed and under review'),
(5, 3, 'Proposed and Passed'),
(5, 4, 'Implemented but not consistently enforced'),
(5, 5, 'Enforced consistently');

insert into master.health_indicator_scores values
(6, null, 'Missing or Not Available'),
(6, 0 , 'Non-existent'),
(6, 1, 'Proposed'),
(6, 2, 'Proposed and under review'),
(6, 3, 'Proposed and Passed'),
(6, 4, 'Implemented but not consistently enforced'),
(6, 5, 'Enforced consistently');

insert into master.health_indicator_scores values
(7, null, 'Missing or Not Available'),
(7, 0 , 'Non-existent'),
(7, 1, 'Proposed'),
(7, 2, 'Proposed and under review'),
(7, 3, 'Proposed and Passed'),
(7, 4, 'Implemented but not consistently enforced'),
(7, 5, 'Enforced consistently');

insert into master.health_indicator_scores values
(8, null, 'Missing or Not Available'),
(8, 0 , 'Non-existent'),
(8, 1, 'Proposed'),
(8, 2, 'Proposed protocols, policies, frameworks or accepted processes for cross boarder data exchange and storage under review'),
(8, 3, 'Proposed protocols, policies, frameworks or accepted processes for cross boarder data exchange proposed and passed'),
(8, 4, 'Proposed protocols, policies, frameworks or accepted processes for cross boarder data exchange implemented, but not consistently enforced'),
(8, 5, 'Proposed protocols, policies, frameworks or accepted processes for cross boarder data exchange implemented, enforced consistently');


insert into master.health_indicator_scores values
(9, null, 'Missing or Not Available'),
(9, 0 , 'Non-existent'),
(9, 1, 'Digital health curriculum proposed for health professionals as part of pre-service training requirements'),
(9, 2, 'Digital health curriculum proposed and under review with some programs providing some courses in the use of digital technology to <25% of health professionals in pre-service training'),
(9, 3, 'Digital health curriculum proposed and implementation underway covering an estimated 25-50% of health professionals in pre-service training'),
(9, 4, 'Taught in some relevant institutions and training curricula with 50-75% (estimated) health professionals trained in the use of digital technology'),
(9, 5, 'Taught in all relevant institutions and training curricula with 100% of health professionals receiving training in the use of digital technology');

insert into master.health_indicator_scores values
(10, null, 'Missing or Not Available'),
(10, 0 , 'Non-existent'),
(10, 1, 'Digital health curriculum proposed for health professionals as part of in-service (continuing education) training requirements for health professionals in the workforce'),
(10, 2, 'Digital health curriculum proposed and under review with programs for some cadres providing some in-service training in the use of digital technology to <25% of health professionals in the workforce'),
(10, 3, 'Digital health curriculum proposed and implementation underway for some cadres covering an estimated 25-50% of health professionals in the workforce'),
(10, 4, 'Digital health curriculum proposed and implementation underway for all cadres covering an estimated 50-75% of health professionals in the workforce'),
(10, 5, 'Digital health taught to all cadres of health professionals in the workforce with 100% receiving training in the use of digital technology');

insert into master.health_indicator_scores values
(11, null, 'Missing or Not Available'),
(11, 0 , 'Non-existent'),
(11, 1, 'Digital heath workforce needs assessed, gaps identified and training options under development'),
(11, 2, 'Training has begun but workforce not available and deployed'),
(11, 3, 'Trained digital health professionals available and deployed, but essential personnel gaps remain'),
(11, 4, 'Trained digital health professionals available and few essential personnel gaps remain'),
(11, 5, 'Sufficient numbers of trained digital health professionals available to support national digital health needs');

insert into master.health_indicator_scores values
(12, null, 'Missing or Not Available'),
(12, 0 , 'No work force strategy, policy, or guide that recognizes digital health is in place.'),
(12, 1, 'Distribution of digital health work force is ad hoc.'),
(12, 2, 'A national needs assessment shows the number and types of skills needed to support digital health with an explicit focus on training cadres of female health workers. Digital health staff roles and responsibilities are mapped to the government''s workforce and career schemes and <25% of needed public sector digital health workforce in place.'),
(12, 3, 'An HR policy and strategic plan exists that identifies skills and functions needed to support digital health with an explicit focus on training cadres of female health workers and an estimated 25-50% of public sector digital health workforce in place.'),
(12, 4, 'A long-term plan is in place to grow and sustain staff with the skills needed to sustain digital health at national and subnational levels with an explicit focus on training cadres of female health workers with an estimated 50-75% of positions needed filled.'),
(12, 5, 'Performance management systems are in place to ensure growth and sustainability of the digital health workforce with sufficient supply [estimated between 75-100%] with an explicit focus on training cadres of female health workers to meet digital health needs and little staff turnover.');

insert into master.health_indicator_scores values
(13, null, 'Missing or Not Available'),
(13, 0 , 'Does not exist.'),
(13, 1, 'Parts of digital health has been architected and/or there is some level of interoperability for some information exchange.'),
(13, 2, 'A national digital health architecture and/or health information exchange [HIE] is defined including semantic, syntactic, and organizational layers.'),
(13, 3, 'The HIE is operable and provides core functions, such as authentication, translation, storage and warehousing function, guide to what data is available and how to access it, and data interpretation.'),
(13, 4, 'The government leads, manages, and enforces implementation of the enterprise architecture, including the HIE. Enterprise architecture and HIE is fully implemented following industry standards.'),
(13, 5, 'The HIE provides core data exchange functions and is periodically reviewed and updated to meet the needs of the changing digital health architecture. There is continuous learning, innovation, and quality control. Data is actively used for national health strategic planning and budgeting.');

insert into master.health_indicator_scores values
(14, null, 'Missing or Not Available'),
(14, 0 , 'No defined health information standards exist for use in the country''s digital health architecture or HIE.'),
(14, 1, 'Standards are used, but inconsistently across programs or agencies'),
(14, 2, 'The country has adopted or developed some health information standards.'),
(14, 3, 'Health information standards for national data exchange have been published and disseminated in the country under the government’s leadership.'),
(14, 4, 'The use of industry-based technical standards in the majority of applications and systems to ensure the availability of high-quality data. Conformance testing is routinely carried out to certify implementers.'),
(14, 5, 'Data standards are routinely updated and data is actively used for monitoring and evaluating the health system and for national health strategic planning and budgeting.');

insert into master.health_indicator_scores values
(15, null, ''),
(15, 0 , ''),
(15, 1, ''),
(15, 2, ''),
(15, 3, ''),
(15, 4, ''),
(15, 5, '');

insert into master.health_indicator_scores values
(16, null, 'Missing or Not Available'),
(16, 0 , 'No plan'),
(16, 1, 'A digital health infrastructure plan is under development.'),
(16, 2, 'A plan exists but not implemented due to lack of resources with estimated <25% of necessary digital health infrastructure needed in the public healthcare service sector available.'),
(16, 3, 'Plan is being implemented partially, but not consistently with estimated 25-50% of necessary digital health infrastructure needed in public healthcare service sector available and in use.'),
(16, 4, 'Plan is being implemented partially and consistently with estimated 50-75% of necessary digital health infrastructure needed in public healthcare service sector available and in use.'),
(16, 5, 'Digital health infrastructure is 100% available, in use, and regularly upgraded and maintained.');

insert into master.health_indicator_scores values
(17, null, 'Missing or Not Available'),
(17, 0 , 'National priority areas are not supported by digital health at any scale'),
(17, 1, 'National priority areas are supported by digital health, and implementation initiated \\(< 25% priority areas\\)'),
(17, 2, 'Very few national priority areas supported by scaled digital health systems \\(25-50% of priority areas\\)'),
(17, 3, 'Few national priority areas supported by scaled digital health systems \\(50-75% of priority areas\\)'),
(17, 4, 'Some, but not all national priority areas \\(>75% of priority areas\\) supported by scaled digital health systems'),
(17, 5, 'All nationally prioritized areas supported by national-scale digital health systems with monitoring and evaluation systems and results');

insert into master.health_indicator_scores values
(18, null, 'Missing or Not Available'),
(18, 0 , 'Does not exist.'),
(18, 1, 'Health system registries are being planned but not yet implemented'),
(18, 2, 'Health system registries are being developed but are not available for use'),
(18, 3, 'Digital registers of providers, administrators, and facilities are incomplete, partially available, used sporadically, and irregularly maintained'),
(18, 4, 'Digital registers of providers, administrators, and facilities are available, used, and regularly updated and maintained'),
(18, 5, 'Digital registers of providers, administrators, and facilities are available, up-to-date, and used for health system and service strategic planning and budgeting');

insert into master.health_indicator_scores values
(19, null, 'Missing or Not Available'),
(19, 0 , 'Does not exist.'),
(19, 1, 'A registry and/or a master patient index is being planned but not yet implemented'),
(19, 2, 'A registry and/or a master patient index are being developed but are not available for use'),
(19, 3, 'A registry and/or a master patient index is incomplete / partially available, used, but irregularly maintained'),
(19, 4, 'A secure and privacy/confidentiality-enabled registry and/or a master patient index is  available, used, or irregularly curated'),
(19, 5, 'A secure and privacy/confidentiality-enabled registry and/or a master patient index is available, used, and curated.');