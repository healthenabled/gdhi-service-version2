CREATE TABLE master.health_indicator_scores(indicator_id INTEGER,
    score INTEGER, definition VARCHAR,
    FOREIGN KEY(indicator_id) REFERENCES master.health_indicators (indicator_id)
);