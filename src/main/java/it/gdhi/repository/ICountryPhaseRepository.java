package it.gdhi.repository;

import it.gdhi.model.CountryPhase;
import it.gdhi.model.id.CountryPhaseId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ICountryPhaseRepository extends JpaRepository<CountryPhase, CountryPhaseId> {

    CountryPhase findByCountryPhaseIdCountryIdAndCountryPhaseIdYear(String countryId, String year);

    @Query(value = "SELECT year from country_health_data.country_phase c where" +
            " c.country_id = UPPER(:countryId) " +
            " order by c.updated_at DESC LIMIT :limit", nativeQuery = true)
    List<String> findByCountryPhaseIdOrderByYearDesc(@Param("countryId") String countryId, @Param("limit") Integer limit);

    @Query(value = "SELECT year FROM country_health_data.country_phase GROUP BY year" +
            " ORDER BY MAX(updated_at) DESC LIMIT :limit", nativeQuery = true)
    List<String> findAllDistinctYearsOrderByUpdatedAtDesc(@Param("limit") Integer limit);

    List<CountryPhase> findByCountryPhaseIdCountryIdInAndCountryPhaseIdYearIn(List<String> countryId,
                                                                              List<String> year);

    List<CountryPhase> findByCountryPhaseIdCountryIdInAndCountryPhaseIdYear(List<String> countryId, String year);
}
