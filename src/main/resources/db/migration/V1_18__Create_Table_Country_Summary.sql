CREATE TABLE validated_config.countries_summary(
    country_id VARCHAR PRIMARY KEY,
    summary VARCHAR,
    contact_name VARCHAR,
    contact_designation VARCHAR,
    contact_organization VARCHAR,
    contact_email VARCHAR,
    data_feeder_name VARCHAR,
    data_feeder_role VARCHAR,
    data_feeder_email VARCHAR,
    data_collector_name VARCHAR,
    data_collector_role VARCHAR,
    data_collector_email VARCHAR,
    collected_date DATE,
    FOREIGN KEY(country_id) REFERENCES master.countries (id)
);