CREATE TABLE IF NOT EXISTS i18n.region(
   region_id VARCHAR,
   language_id VARCHAR,
   region_name VARCHAR,
   PRIMARY KEY (region_id, language_id),
   FOREIGN KEY(language_id) REFERENCES i18n.language (id)
);