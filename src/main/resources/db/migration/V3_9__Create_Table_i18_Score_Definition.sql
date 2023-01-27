CREATE TABLE i18n.score_definition(
    indicator_id INTEGER,
    score INTEGER,
    language_id VARCHAR,
    definition VARCHAR,
    PRIMARY KEY (indicator_id, score, language_id),
    FOREIGN KEY(language_id) REFERENCES i18n.language (id)
);