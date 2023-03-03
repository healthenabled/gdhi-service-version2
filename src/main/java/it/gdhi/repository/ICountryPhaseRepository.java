package it.gdhi.repository;

import it.gdhi.model.CountryPhase;
import it.gdhi.model.id.CountryPhaseId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ICountryPhaseRepository extends JpaRepository<CountryPhase, CountryPhaseId> {


    CountryPhase findByCountryPhaseIdCountryId(String countryId);

    CountryPhase findByCountryPhaseIdCountryIdAndCountryPhaseIdYear(String countryId, String year);
}
