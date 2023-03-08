UPDATE country_health_data.health_indicators SET year = 'Version1' WHERE status = 'PUBLISHED';
UPDATE country_health_data.country_summary SET year = 'Version1' WHERE status = 'PUBLISHED';
UPDATE country_health_data.country_resource_links SET year = 'Version1' WHERE status = 'PUBLISHED';

UPDATE country_health_data.health_indicators SET year = date_part('year', created_at) WHERE status  != 'PUBLISHED';
UPDATE country_health_data.country_summary SET year = date_part('year', created_at) WHERE status != 'PUBLISHED';
UPDATE country_health_data.country_resource_links SET year = date_part('year', created_at) WHERE status != 'PUBLISHED';