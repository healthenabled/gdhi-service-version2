ALTER TABLE country_health_data.country_summary ADD COLUMN IF NOT EXISTS govt_approved BOOLEAN DEFAULT TRUE;