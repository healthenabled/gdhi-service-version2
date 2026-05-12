package it.gdhi.repository;

import it.gdhi.model.CountryPhase;
import it.gdhi.model.id.CountryPhaseId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ICountryPhaseRepository extends JpaRepository<CountryPhase, CountryPhaseId>,
        JpaSpecificationExecutor<CountryPhase> {

    CountryPhase findByCountryPhaseIdCountryIdAndCountryPhaseIdYear(String countryId, String year);

    @Query(value = "SELECT year from country_health_data.country_phase c where" +
            " c.country_id = UPPER(:countryId) " +
            " order by c.updated_at DESC, " +
            " CASE WHEN c.year ~ '^[0-9]{4}$' THEN CAST(c.year AS integer) ELSE 0 END DESC LIMIT :limit",
            nativeQuery = true)
    List<String> findByCountryPhaseIdOrderByYearDesc(@Param("countryId") String countryId, @Param("limit") Integer limit);

    @Query(value = "SELECT year FROM country_health_data.country_phase GROUP BY year" +
            " ORDER BY MAX(updated_at) DESC, CASE WHEN year ~ '^[0-9]{4}$' THEN CAST(year AS integer) ELSE 0 END DESC LIMIT :limit",
            nativeQuery = true)
    List<String> findAllDistinctYearsOrderByUpdatedAtDesc(@Param("limit") Integer limit);

    List<CountryPhase> findByCountryPhaseIdCountryIdInAndCountryPhaseIdYearIn(List<String> countryId,
                                                                              List<String> year);

    List<CountryPhase> findByCountryPhaseIdCountryIdIn(List<String> countryId);

    List<CountryPhase> findByCountryPhaseIdCountryIdInAndCountryPhaseIdYear(List<String> countryId, String year);

    List<CountryPhase> findByCountryPhaseIdYear(String year);

    List<CountryPhase> findByCountryPhaseIdYearAndCountryOverallPhase(String year, Integer countryOverallPhase);

    List<CountryPhase> findByLatestTrue();

    List<CountryPhase> findByLatestTrueAndCountryOverallPhase(Integer countryOverallPhase);

    List<CountryPhase> findByLatestTrueAndCountryPhaseIdCountryIdIn(List<String> countryIds);

    @Query(value = """
            SELECT *
            FROM country_health_data.country_phase
            WHERE country_id = UPPER(:countryId)
              AND latest = true
            """, nativeQuery = true)
    CountryPhase findLatestByCountryId(@Param("countryId") String countryId);

    @Modifying
    @Query(value = "UPDATE country_health_data.country_phase SET latest = false WHERE country_id = UPPER(:countryId)",
            nativeQuery = true)
    void clearLatestForCountry(@Param("countryId") String countryId);

    @Modifying
    @Query(value = """
            UPDATE country_health_data.country_phase cp
            SET latest = true
            WHERE cp.country_id = UPPER(:countryId)
              AND cp.year = (
                  SELECT ranked.year
                  FROM (
                      SELECT year
                      FROM country_health_data.country_phase
                      WHERE country_id = UPPER(:countryId)
                      ORDER BY
                          CASE WHEN year ~ '^[0-9]{4}$' THEN year::int ELSE 0 END DESC,
                          updated_at DESC
                      LIMIT 1
                  ) ranked
              )
            """, nativeQuery = true)
    void markLatestForCountry(@Param("countryId") String countryId);
}
