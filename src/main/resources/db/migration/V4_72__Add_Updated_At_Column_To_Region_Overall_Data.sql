ALTER TABLE regions.regional_overall_data ADD COLUMN IF NOT EXISTS updated_at TIMESTAMP;

UPDATE  regions.regional_overall_data t SET updated_at = s.updated_at FROM country_health_data.country_phase s
JOIN regions.regions_countries g ON g.country_id = s.country_id WHERE g.country_id = s.country_id AND t.year = s.year;