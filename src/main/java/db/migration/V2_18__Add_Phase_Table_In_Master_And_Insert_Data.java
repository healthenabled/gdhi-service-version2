package db.migration;

import org.springframework.jdbc.core.JdbcTemplate;

public class V2_18__Add_Phase_Table_In_Master_And_Insert_Data  {

    
    public void doMigrate(JdbcTemplate jdbcTemplate) {
        jdbcTemplate.execute("CREATE TABLE master.phases(phase_id INTEGER PRIMARY KEY, " +
                "phase_name VARCHAR," +
                "phase_value INTEGER)" );

        jdbcTemplate.execute("ALTER TABLE master.phases ADD CONSTRAINT phase_value_limit_check " +
                "CHECK ((phase_value >=1 AND phase_value <=5));");

        jdbcTemplate.update("insert into master.phases(phase_id, phase_name, phase_value) values(1, 'Phase 1', 1);");
        jdbcTemplate.update("insert into master.phases(phase_id, phase_name, phase_value) values(2, 'Phase 2', 2);");
        jdbcTemplate.update("insert into master.phases(phase_id, phase_name, phase_value) values(3, 'Phase 3', 3);");
        jdbcTemplate.update("insert into master.phases(phase_id, phase_name, phase_value) values(4, 'Phase 4', 4);");
        jdbcTemplate.update("insert into master.phases(phase_id, phase_name, phase_value) values(5, 'Phase 5', 5);");
    }
}
