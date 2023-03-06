ALTER TABLE country_health_data.country_resource_links  DROP CONSTRAINT IF EXISTS country_resource_links_fkey;
ALTER TABLE country_health_data.health_indicators  DROP CONSTRAINT IF EXISTS health_indicators_country_id_status_fkey;
ALTER TABLE country_health_data.health_indicators  DROP CONSTRAINT IF EXISTS health_indicators_pkey;
ALTER TABLE country_health_data.country_summary  DROP CONSTRAINT IF EXISTS country_summary_pkey;
ALTER TABLE country_health_data.country_phase DROP CONSTRAINT IF EXISTS country_phase_country_id_fkey;
ALTER TABLE country_health_data.country_phase DROP CONSTRAINT IF EXISTS country_phase_pkey;

ALTER TABLE country_health_data.country_summary ADD CONSTRAINT IF NOT EXISTS country_summary_pkey PRIMARY KEY (country_id, status , year);
ALTER TABLE country_health_data.country_resource_links ADD CONSTRAINT IF NOT EXISTS country_resource_links_fkey FOREIGN KEY (country_id, status , year) REFERENCES country_health_data.country_summary (country_id, status , year);
ALTER TABLE country_health_data.health_indicators ADD CONSTRAINT IF NOT EXISTS health_indicators_pkey PRIMARY KEY (country_id, category_id, indicator_id, status , year);
ALTER TABLE country_health_data.health_indicators ADD CONSTRAINT IF NOT EXISTS health_indicators_country_id_status_fkey FOREIGN KEY (country_id, status, year) REFERENCES country_health_data.country_summary (country_id, status , year);
ALTER TABLE country_health_data.country_phase ADD CONSTRAINT IF NOT EXISTS country_phase_country_id_fkey FOREIGN KEY (country_id) REFERENCES master.countries (id);
ALTER TABLE country_health_data.country_phase ADD CONSTRAINT IF NOT EXISTS country_phase_pkey PRIMARY KEY (country_id, year);