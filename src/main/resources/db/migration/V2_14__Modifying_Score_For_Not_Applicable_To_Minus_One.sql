ALTER TABLE country_health_data.health_indicators DROP CONSTRAINT score_limit_check;

ALTER TABLE country_health_data.health_indicators ADD CONSTRAINT score_limit_check CHECK ((indicator_score >=-1 AND indicator_score <=5) OR indicator_score IS NULL);

UPDATE master.health_indicator_scores SET score = -1 WHERE definition like 'Not Available or Not Applicable';

UPDATE country_health_data.health_indicators SET indicator_score = -1 WHERE indicator_score IS NULL;