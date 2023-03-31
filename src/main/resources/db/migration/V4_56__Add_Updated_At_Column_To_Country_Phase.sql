ALTER TABLE country_health_data.country_phase ADD COLUMN IF NOT EXISTS updated_at TIMESTAMP;

UPDATE country_health_data.country_phase t SET updated_at = s.updated_at FROM  country_health_data.country_summary s WHERE s.country_id = t.country_id AND s.year = t.year ;