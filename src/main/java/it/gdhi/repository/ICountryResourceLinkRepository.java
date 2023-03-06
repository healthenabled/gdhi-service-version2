package it.gdhi.repository;

import it.gdhi.model.CountryResourceLink;
import it.gdhi.model.id.CountryResourceLinkId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ICountryResourceLinkRepository extends JpaRepository<CountryResourceLink, CountryResourceLinkId> {

    @Modifying
    void deleteByCountryResourceLinkIdCountryIdAndCountryResourceLinkIdStatusAndCountryResourceLinkIdYear(String countryId, String currentStatus, String currentYear);
}
