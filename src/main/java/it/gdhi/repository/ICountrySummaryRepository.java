package it.gdhi.repository;

import it.gdhi.model.CountrySummary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ICountrySummaryRepository extends JpaRepository<CountrySummary, String> {

    @Query("SELECT c FROM CountrySummary c WHERE c.countrySummaryId.countryId = UPPER(?1)")
    CountrySummary findOne(String countryId);

    @Query("SELECT c.countrySummaryId.countryId  FROM CountrySummary c WHERE c.countrySummaryId.status= UPPER(?1)")
    List<String> findAllByStatus(String status);

    @Query("SELECT c FROM CountrySummary c WHERE c.countrySummaryId.countryId = UPPER(?1) and" +
            " c.countrySummaryId.status= UPPER(?2)")
    CountrySummary findByCountryAndStatus(String countryId, String status);

    @Query("SELECT c FROM CountrySummary c WHERE c.countrySummaryId.countryId = UPPER(?1)")
    List<CountrySummary> findAll(String countryId);

    CountrySummary save(CountrySummary countrySummary);

    @Query("SELECT c.countrySummaryId.status from CountrySummary c where" +
            " c.countrySummaryId.status <> 'PUBLISHED' and " +
            "c.countrySummaryId.countryId = UPPER(?1)")
    String getCountrySummaryStatus(String countryId);

    @Modifying
    @Query("DELETE FROM CountrySummary c where c.countrySummaryId.countryId = UPPER(?1) " +
            "and c.countrySummaryId.status = ?2")
    void removeCountrySummary(String countryId, String currentStatus);

    @Query("SELECT c.countrySummaryId.status from CountrySummary c where " +
            "c.countrySummaryId.countryId = UPPER(?1)")
    List<String> getAllStatus(String countryId);

    List<CountrySummary> findAllByOrderByUpdatedAtDesc();

    CountrySummary findByCountrySummaryIdCountryIdAndCountrySummaryIdStatusAndCountrySummaryIdYear(String countryId, String currentStatus, String year);
}
