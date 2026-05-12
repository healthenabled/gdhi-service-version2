package it.gdhi.repository;

import java.util.List;

import it.gdhi.model.CountryHealthIndicator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ICountryHealthIndicatorRepository extends JpaRepository<CountryHealthIndicator, Long>,
        JpaSpecificationExecutor<CountryHealthIndicator> {

    CountryHealthIndicator save(CountryHealthIndicator countryHealthIndicatorSetupData);

    @Modifying
    void deleteByCountryHealthIndicatorIdCountryIdAndCountryHealthIndicatorIdYearAndStatus(String countryId, String year, String currentStatus);

    List<CountryHealthIndicator> findByCountryHealthIndicatorIdYearAndStatus(String year,
                                                                             String currentStatus);

    List<CountryHealthIndicator> findByCountryHealthIndicatorIdCategoryIdAndCountryHealthIndicatorIdYearAndStatus(Integer categoryId, String year, String currentStatus);

    List<CountryHealthIndicator> findByCountryHealthIndicatorIdCountryIdAndCountryHealthIndicatorIdYearAndStatus(String countryId, String year, String currentStatus);

    List<CountryHealthIndicator> findByCountryHealthIndicatorIdCountryIdInAndCountryHealthIndicatorIdYearAndStatus(List<String> countryId, String year, String status);

    List<CountryHealthIndicator> findByCountryHealthIndicatorIdCountryIdInAndCountryHealthIndicatorIdYearInAndStatus(List<String> countryId, List<String> year, String status);

    List<CountryHealthIndicator> findByStatus(String status);

    @Query(value = """
            SELECT hi.*
            FROM country_health_data.health_indicators hi
            JOIN country_health_data.country_phase cp
              ON cp.country_id = hi.country_id
             AND cp.year = hi.year
            WHERE hi.status = :status
              AND cp.latest = true
              AND hi.country_id = COALESCE(UPPER(CAST(:countryId AS text)), hi.country_id)
              AND hi.category_id = COALESCE(:categoryId, hi.category_id)
            """, nativeQuery = true)
    List<CountryHealthIndicator> findLatestByCountryAndCategoryAndStatus(
            @Param("countryId") String countryId,
            @Param("categoryId") Integer categoryId,
            @Param("status") String status);

}
