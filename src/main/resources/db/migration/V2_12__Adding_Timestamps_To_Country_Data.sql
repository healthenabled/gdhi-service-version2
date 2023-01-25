ALTER TABLE country_health_data.country_summary ADD COLUMN created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT now();
ALTER TABLE country_health_data.country_summary ADD COLUMN updated_at TIMESTAMP WITHOUT TIME ZONE;

ALTER TABLE country_health_data.country_resource_links ADD COLUMN created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT now();
ALTER TABLE country_health_data.country_resource_links ADD COLUMN updated_at TIMESTAMP WITHOUT TIME ZONE;

ALTER TABLE country_health_data.health_indicators ADD COLUMN created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT now();
ALTER TABLE country_health_data.health_indicators ADD COLUMN updated_at TIMESTAMP WITHOUT TIME ZONE;