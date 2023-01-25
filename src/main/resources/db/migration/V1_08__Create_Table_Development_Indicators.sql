CREATE TABLE validated_config.development_indicators (
    country_id VARCHAR PRIMARY KEY,
    gni_per_capita NUMERIC,
    total_population BIGINT,
    life_expectancy NUMERIC,
    health_expenditure NUMERIC,
    ncd_deaths_per_capita_total NUMERIC,
    under_5_mortality NUMERIC,
    doing_business_index NUMERIC,
    adult_literacy NUMERIC,
    FOREIGN KEY (country_id) REFERENCES master.countries (id)
);