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
    void deleteByCountryHealthIndicatorIdCountryIdAndCountryHealthIndicatorIdStatusAndCountryHealthIndicatorIdYear(String countryId, String currentStatus, String year);

    List<CountryHealthIndicator> findByCountryHealthIndicatorIdStatusAndCountryHealthIndicatorIdYear(String currentStatus, String year);

    List<CountryHealthIndicator> findByCountryHealthIndicatorIdCategoryIdAndCountryHealthIndicatorIdYearAndCountryHealthIndicatorIdStatus(Integer categoryId, String year, String currentStatus);

    List<CountryHealthIndicator> findByCountryHealthIndicatorIdCountryIdAndCountryHealthIndicatorIdYearAndCountryHealthIndicatorIdStatus(String countryId, String year, String currentStatus);
}

