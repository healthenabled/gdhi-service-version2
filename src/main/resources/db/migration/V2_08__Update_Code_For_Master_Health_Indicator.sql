
UPDATE master.health_indicators SET code=indicator_id;
-- Adding not null constraint for the code column
ALTER TABLE master.health_indicators ALTER COLUMN code SET not null;