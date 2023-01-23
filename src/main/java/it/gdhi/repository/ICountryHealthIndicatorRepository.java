package it.gdhi.repository;

import it.gdhi.model.CountryHealthIndicator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ICountryHealthIndicatorRepository extends JpaRepository<CountryHealthIndicator, Long> {

    List<CountryHealthIndicator> findAll();

    @Query("SELECT h FROM CountryHealthIndicator h WHERE " +
            "h.countryHealthIndicatorId.countryId = ?1 and h.countryHealthIndicatorId.status=?2")
    List<CountryHealthIndicator> findByCountryIdAndStatus(String countryId, String status);

    @Query("SELECT h FROM CountryHealthIndicator h, CountryPhase ph WHERE " +
            "h.countryHealthIndicatorId.countryId = ph.countryId and " +
            "h.countryHealthIndicatorId.status=?1 and " +
            "ph.countryOverallPhase = CASE WHEN (?2 = -1) THEN ph.countryOverallPhase ELSE ?2 END")
    List<CountryHealthIndicator> findByStatusAndPhase(String status, Integer countryPhase);

    @Query("SELECT distinct (countryHealthIndicatorId.countryId) FROM CountryHealthIndicator")
    List<String> findCountriesWithHealthScores();

    CountryHealthIndicator save(CountryHealthIndicator countryHealthIndicatorSetupData);

    @Query("SELECT h FROM CountryHealthIndicator h WHERE (?1 is null or h.category.id = ?1) "+
            "and h.countryHealthIndicatorId.status=?2")
    List<CountryHealthIndicator> findByCategoryAndStatus(Integer categoryId, String currentStatus);

    @Modifying
    @Query("DELETE FROM CountryHealthIndicator h WHERE " +
            "h.countryHealthIndicatorId.countryId = ?1 and h.countryHealthIndicatorId.status=?2")
    void removeHealthIndicatorsBy(String countryId, String currentStatus);
}

