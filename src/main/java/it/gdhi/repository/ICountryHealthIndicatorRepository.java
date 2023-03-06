package it.gdhi.repository;

import it.gdhi.model.CountryHealthIndicator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ICountryHealthIndicatorRepository extends JpaRepository<CountryHealthIndicator, Long> {

    CountryHealthIndicator save(CountryHealthIndicator countryHealthIndicatorSetupData);

    @Modifying
    void deleteByCountryHealthIndicatorIdCountryIdAndCountryHealthIndicatorIdStatusAndCountryHealthIndicatorIdYear(String countryId, String currentStatus, String year);

    List<CountryHealthIndicator> findByCountryHealthIndicatorIdStatusAndCountryHealthIndicatorIdYear(String currentStatus, String year);

    List<CountryHealthIndicator> findByCountryHealthIndicatorIdCategoryIdAndCountryHealthIndicatorIdStatusAndCountryHealthIndicatorIdYear(Integer categoryId, String currentStatus, String year);

    List<CountryHealthIndicator> findByCountryHealthIndicatorIdCountryIdAndCountryHealthIndicatorIdStatusAndCountryHealthIndicatorIdYear(String countryId, String currentStatus, String year);
}

