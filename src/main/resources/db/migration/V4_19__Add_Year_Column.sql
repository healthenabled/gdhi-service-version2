ALTER TABLE country_health_data.health_indicators ADD COLUMN IF NOT EXISTS year VARCHAR;
ALTER TABLE country_health_data.country_summary ADD COLUMN IF NOT EXISTS year VARCHAR;
ALTER TABLE country_health_data.country_resource_links ADD COLUMN IF NOT EXISTS year VARCHAR;