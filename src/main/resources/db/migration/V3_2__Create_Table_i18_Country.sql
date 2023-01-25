CREATE TABLE i18n.country(
   country_id VARCHAR,
   language_id VARCHAR,
   name VARCHAR,
   PRIMARY KEY (country_id, language_id),
   FOREIGN KEY(language_id) REFERENCES i18n.language (id)
);