CREATE TABLE master.categories_indicators(category_id INTEGER,
     indicator_id INTEGER, PRIMARY KEY (category_id, indicator_id),
     FOREIGN KEY(category_id) REFERENCES master.categories (id),
     FOREIGN KEY(indicator_id) REFERENCES master.health_indicators (indicator_id)
);