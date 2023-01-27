CREATE TABLE master.phases(phase_id INTEGER PRIMARY KEY, phase_name VARCHAR, phase_value INTEGER);
ALTER TABLE master.phases ADD CONSTRAINT phase_value_limit_check CHECK ((phase_value >=1 AND phase_value <=5));
insert into master.phases(phase_id, phase_name, phase_value) values(1, 'Phase 1', 1);
insert into master.phases(phase_id, phase_name, phase_value) values(2, 'Phase 2', 2);
insert into master.phases(phase_id, phase_name, phase_value) values(3, 'Phase 3', 3);
insert into master.phases(phase_id, phase_name, phase_value) values(4, 'Phase 4', 4);
insert into master.phases(phase_id, phase_name, phase_value) values(5, 'Phase 5', 5);