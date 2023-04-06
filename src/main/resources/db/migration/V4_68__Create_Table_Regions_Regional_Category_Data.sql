CREATE TABLE IF NOT EXISTS regions.regional_category_data (region_id varchar , category_id  integer , year varchar , score integer ,
PRIMARY KEY(region_id , category_id , year) ,
FOREIGN KEY(region_id) REFERENCES regions.region (region_id),
FOREIGN KEY(category_id) REFERENCES master.categories(id));