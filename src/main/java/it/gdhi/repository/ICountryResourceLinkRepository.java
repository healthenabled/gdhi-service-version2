package it.gdhi.repository;

import it.gdhi.model.CountryResourceLink;
import it.gdhi.model.id.CountryResourceLinkId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

@Repository
public interface ICountryResourceLinkRepository extends JpaRepository<CountryResourceLink, CountryResourceLinkId> {

    @Modifying
    void deleteByCountryResourceLinkIdCountryIdAndCountryResourceLinkIdYearAndCountryResourceLinkIdStatus(String countryId, String currentYear, String currentStatus);
}
