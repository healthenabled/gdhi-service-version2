package it.gdhi.repository;

import it.gdhi.model.CountryPhase;
import it.gdhi.model.id.CountryPhaseId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ICountryPhaseRepository extends JpaRepository<CountryPhase, CountryPhaseId> {

    CountryPhase findByCountryPhaseIdCountryIdAndCountryPhaseIdYear(String countryId, String year);

    @Query(value = "SELECT year from country_health_data.country_phase c where" +
            " c.country_id = UPPER(?1) " +
            " order by c.updated_at DESC LIMIT ?2", nativeQuery = true)
    List<String> findByCountryPhaseIdOrderByYearDesc(String countryId , Integer limit);
}
