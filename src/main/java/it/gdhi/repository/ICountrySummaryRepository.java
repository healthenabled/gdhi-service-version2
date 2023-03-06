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

    List<CountrySummary> findByCountrySummaryIdStatus(String status);

    List<CountrySummary> findByCountrySummaryIdCountryIdAndCountrySummaryIdYear(String countryId , String currentYear);

    List<CountrySummary> findByCountrySummaryIdYearOrderByUpdatedAtDesc(String currentYear);

    List<CountrySummary> findAllByOrderByUpdatedAtDesc();

    @Modifying
    void deleteByCountrySummaryIdCountryIdAndCountrySummaryIdStatusAndCountrySummaryIdYear(String countryId, String currentStatus, String currentYear);

    CountrySummary findByCountrySummaryIdCountryIdAndCountrySummaryIdStatusNotAndCountrySummaryIdYear(String countryId, String currentStatus, String year);

    CountrySummary findByCountrySummaryIdCountryIdAndCountrySummaryIdStatusAndCountrySummaryIdYear(String countryId, String currentStatus, String year);

}
