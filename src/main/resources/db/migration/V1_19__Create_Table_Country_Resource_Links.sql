CREATE TABLE validated_config.country_resource_links(
    country_id VARCHAR,
    link VARCHAR,
    FOREIGN KEY(country_id) REFERENCES master.countries (id)
);