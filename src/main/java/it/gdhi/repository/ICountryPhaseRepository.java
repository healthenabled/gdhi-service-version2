package it.gdhi.repository;

import it.gdhi.model.CountryPhase;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ICountryPhaseRepository extends CrudRepository<CountryPhase, String> {
}
