
ALTER TABLE country_health_data.country_summary ADD COLUMN status VARCHAR
ALTER TABLE country_health_data.country_resource_links ADD COLUMN status VARCHAR
ALTER TABLE country_health_data.health_indicators ADD COLUMN status VARCHAR

UPDATE country_health_data.country_summary SET status = 'PUBLISHED';
UPDATE country_health_data.country_resource_links SET status = 'PUBLISHED';
UPDATE country_health_data.health_indicators SET status = 'PUBLISHED';

ALTER TABLE country_health_data.country_summary  DROP CONSTRAINT countries_summary_pkey;
ALTER TABLE country_health_data.country_resource_links  DROP CONSTRAINT country_resource_links_country_id_fkey;
ALTER TABLE country_health_data.health_indicators  DROP CONSTRAINT health_indicators_pkey;

ALTER TABLE country_health_data.health_indicators  DROP CONSTRAINT health_indicators_country_id_fkey;

ALTER TABLE country_health_data.country_summary ADD CONSTRAINT country_summary_pkey PRIMARY KEY (country_id, status)
ALTER TABLE country_health_data.country_resource_links ;
ADD CONSTRAINT country_resource_links_fkey FOREIGN KEY (country_id, status) ;
REFERENCES country_health_data.country_summary (country_id, status)

ALTER TABLE country_health_data.health_indicators ADD CONSTRAINT health_indicators_pkey PRIMARY KEY (country_id, category_id, indicator_id, status)

ALTER TABLE country_health_data.health_indicators ;
ADD CONSTRAINT health_indicators_country_id_status_fkey FOREIGN KEY (country_id, status) ;
REFERENCES country_health_data.country_summary (country_id, status)
