CREATE TABLE i18n.health_indicator(
    indicator_id INTEGER,
    language_id VARCHAR,
    name VARCHAR,
    definition VARCHAR,
    PRIMARY KEY (indicator_id, language_id),
    FOREIGN KEY(language_id) REFERENCES i18n.language (id)
);
