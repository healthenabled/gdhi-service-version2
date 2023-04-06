CREATE TABLE IF NOT EXISTS regions.regional_indicator_data (region_id varchar , indicator_id  integer , year varchar , score integer ,
PRIMARY KEY(region_id , indicator_id , year) ,
FOREIGN KEY(region_id) REFERENCES regions.region (region_id),
FOREIGN KEY(indicator_id) REFERENCES master.health_indicators (indicator_id));