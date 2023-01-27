ALTER TABLE validated_config.health_indicators DROP CONSTRAINT health_indicator_pkey;
ALTER TABLE validated_config.health_indicators DROP COLUMN last_survey_date;
ALTER TABLE validated_config.health_indicators ADD CONSTRAINT health_indicators_pkey PRIMARY KEY (country_id, category_id, indicator_id);
