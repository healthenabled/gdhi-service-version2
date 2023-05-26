package it.gdhi.repository;

import it.gdhi.model.CountrySummary;
import it.gdhi.model.id.CountrySummaryId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ICountrySummaryRepository extends JpaRepository<CountrySummary, CountrySummaryId> {

    CountrySummary save(CountrySummary countrySummary);

    List<CountrySummary> findByCountrySummaryIdCountryIdAndCountrySummaryIdYear(String countryId, String currentYear);

    List<CountrySummary> findByCountrySummaryIdYearAndStatus(String year, String status);

    List<CountrySummary> findByCountrySummaryIdYearOrderByUpdatedAtDesc(String currentYear);

    List<CountrySummary> findAllByOrderByUpdatedAtDesc();

    @Modifying
    void deleteByCountrySummaryIdCountryIdAndCountrySummaryIdYearAndStatus(String countryId, String currentYear, String currentStatus);

    CountrySummary findByCountrySummaryIdCountryIdAndCountrySummaryIdYearAndStatusNot(String countryId, String year, String currentStatus);

    CountrySummary findByCountrySummaryIdCountryIdAndCountrySummaryIdYearAndStatus(String countryId, String year, String currentStatus);

    @Query(value = "SELECT year from country_health_data.country_summary c where" +
            " c.country_id = UPPER(?1) and " +
            " c.status <> 'NEW' order by updated_at DESC LIMIT 1", nativeQuery = true)
    String findFirstByCountryIdAndStatusNotNEWOrderByDesc(String countryId);

}
