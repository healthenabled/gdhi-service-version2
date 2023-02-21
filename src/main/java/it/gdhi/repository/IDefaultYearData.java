package it.gdhi.repository;

import it.gdhi.model.CountryPhase;
import it.gdhi.model.DefaultYearData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IDefaultYearData extends JpaRepository<DefaultYearData, String> {
    DefaultYearData findFirstByOrderByCreatedAtDesc();
}
