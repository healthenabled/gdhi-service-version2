package it.gdhi.repository;

import java.util.List;

import it.gdhi.model.CountryHealthIndicator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

@Repository
public interface ICountryHealthIndicatorRepository extends JpaRepository<CountryHealthIndicator, Long> {

    CountryHealthIndicator save(CountryHealthIndicator countryHealthIndicatorSetupData);

    @Modifying
    void deleteByCountryHealthIndicatorIdCountryIdAndCountryHealthIndicatorIdYearAndStatus(String countryId, String year, String currentStatus);

    List<CountryHealthIndicator> findByStatusAndCountryHealthIndicatorIdYear(String currentStatus, String year);

    List<CountryHealthIndicator> findByCountryHealthIndicatorIdCategoryIdAndCountryHealthIndicatorIdYearAndStatus(Integer categoryId, String year, String currentStatus);

    List<CountryHealthIndicator> findByCountryHealthIndicatorIdCountryIdAndCountryHealthIndicatorIdYearAndStatus(String countryId, String year, String currentStatus);

    List<CountryHealthIndicator> findByCountryHealthIndicatorIdCountryIdInAndCountryHealthIndicatorIdYearAndStatus(List<String> countryId, String year, String status);

    List<CountryHealthIndicator> findByCountryHealthIndicatorIdCountryIdInAndCountryHealthIndicatorIdYearInAndStatus(List<String> countryId, List<String> year, String status);

}

