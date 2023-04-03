CREATE TABLE IF NOT EXISTS regions.regions_countries(region_id VARCHAR,
     country_id VARCHAR, PRIMARY KEY (region_id, country_id),
     FOREIGN KEY(region_id) REFERENCES regions.region (id),
     FOREIGN KEY(country_id) REFERENCES master.countries (id)
);