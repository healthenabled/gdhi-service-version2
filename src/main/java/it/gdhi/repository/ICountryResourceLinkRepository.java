package it.gdhi.repository;

import it.gdhi.model.CountryResourceLink;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface ICountryResourceLinkRepository extends JpaRepository<CountryResourceLink, String> {


    List<CountryResourceLink> findAllByCountryResourceLinkIdCountryId(String countryId);

    void deleteByCountryResourceLinkIdCountryIdAndCountryResourceLinkIdStatus(String countryId, String currentStatus);
}
