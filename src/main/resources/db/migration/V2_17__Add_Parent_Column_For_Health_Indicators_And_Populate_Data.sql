ALTER TABLE master.health_indicators ADD COLUMN parent_id INTEGER DEFAULT null;
UPDATE master.health_indicators SET parent_id = 9 WHERE code IN ('9a','9b','9c');
UPDATE master.health_indicators SET parent_id = 10 WHERE code IN ('10a','10b','10c');
UPDATE master.health_indicators SET parent_id = 11 WHERE code IN ('11a');
UPDATE master.health_indicators SET parent_id = 19 WHERE code IN ('19a','19b','19c','19d');
