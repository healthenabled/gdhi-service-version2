package it.gdhi.repository;

import it.gdhi.model.CountryPhase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ICountryPhaseRepository extends JpaRepository<CountryPhase, String> {
}
