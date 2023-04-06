CREATE TABLE IF NOT EXISTS regions.regional_overall_data (region_id varchar, year varchar , overall_score integer ,
PRIMARY KEY(region_id, year) ,
FOREIGN KEY(region_id) REFERENCES regions.region (region_id));