ALTER TABLE master.countries ADD COLUMN unique_id UUID DEFAULT uuid_generate_v4() NOT NULL;
