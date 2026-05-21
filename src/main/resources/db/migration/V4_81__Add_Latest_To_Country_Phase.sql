ALTER TABLE country_health_data.country_phase
ADD COLUMN IF NOT EXISTS latest BOOLEAN DEFAULT FALSE;

UPDATE country_health_data.country_phase
SET latest = FALSE
WHERE latest IS NULL;

ALTER TABLE country_health_data.country_phase
ALTER COLUMN latest SET NOT NULL;

WITH ranked_country_phases AS (
    SELECT
        country_id,
        year,
        ROW_NUMBER() OVER (
            PARTITION BY country_id
            ORDER BY
                CASE WHEN year ~ '^[0-9]{4}$' THEN year::int ELSE 0 END DESC,
                updated_at DESC
        ) AS row_rank
    FROM country_health_data.country_phase
)
UPDATE country_health_data.country_phase cp
SET latest = ranked_country_phases.row_rank = 1
FROM ranked_country_phases
WHERE ranked_country_phases.country_id = cp.country_id
  AND ranked_country_phases.year = cp.year;

CREATE UNIQUE INDEX IF NOT EXISTS country_phase_one_latest_per_country
ON country_health_data.country_phase (country_id)
WHERE latest = true;
