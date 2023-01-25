CREATE TABLE i18n.category(
    category_id INTEGER,
    language_id VARCHAR,
    name VARCHAR,
    PRIMARY KEY (category_id, language_id),
    FOREIGN KEY(language_id) REFERENCES i18n.language (id)
);