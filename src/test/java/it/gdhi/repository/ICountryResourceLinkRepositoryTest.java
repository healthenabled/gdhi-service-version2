package it.gdhi.repository;

import it.gdhi.model.Country;
import it.gdhi.model.CountryResourceLink;
import it.gdhi.model.CountrySummary;
import it.gdhi.model.id.CountryResourceLinkId;
import it.gdhi.model.id.CountrySummaryId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static java.util.stream.Collectors.toList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.core.Is.is;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@Transactional
@ActiveProfiles("test")
public class ICountryResourceLinkRepositoryTest {

    @Autowired
    private ICountryResourceLinkRepository iCountryResourceLinkRepository;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private ICountrySummaryRepository countrySummaryRepository;

    private void addCountrySummary(String countryId, String countryName, String alpha2code) {
        String status = "PUBLISHED";
        CountrySummary countrySummary = CountrySummary.builder()
                .countrySummaryId(new CountrySummaryId(countryId, status))
                .summary("summary")
                .country(new Country(countryId, countryName, UUID.randomUUID(), alpha2code))
                .contactName("contactName")
                .contactDesignation("contactDesignation")
                .contactOrganization("contactOrganization")
                .contactEmail("email")
                .dataFeederName("feeder name")
                .dataFeederRole("feeder role")
                .dataFeederEmail("email")
                .dataApproverName("coll name")
                .dataApproverRole("coll role")
                .dataFeederRole("coll role")
                .dataApproverEmail("coll email")
                .countryResourceLinks(new ArrayList<>())
                .build();
        countrySummaryRepository.save(countrySummary);
    }

    @Test
    public void shouldFetchCountryResourceLinks() {
        addCountrySummary("NZL", "New Zealand", "NZ");
        addCountrySummary("AUS", "Australia", "AU");
        CountryResourceLink countryResourceLink1 = new CountryResourceLink(new CountryResourceLinkId("NZL", "Res " +
                "1","PUBLISHED"),new Date(), null);
        CountryResourceLink countryResourceLink2 = new CountryResourceLink(new CountryResourceLinkId("AUS", "Res " +
                "2","PUBLISHED"),new Date(), null);
        CountryResourceLink countryResourceLink3 = new CountryResourceLink(new CountryResourceLinkId("NZL", "Res " +
                "3","PUBLISHED"),new Date(), null);
        entityManager.persist(countryResourceLink1);
        entityManager.persist(countryResourceLink2);
        entityManager.persist(countryResourceLink3);
        entityManager.flush();
        entityManager.clear();
        List<CountryResourceLink> actual = iCountryResourceLinkRepository.findAllByCountryResourceLinkIdCountryId("NZL");
        assertThat(actual.size(), is(2));
        assertThat(actual.stream().map(CountryResourceLink::getLink).collect(toList()), containsInAnyOrder("Res 1", "Res 3"));
    }

    @Test
    public void shouldDeleteCountryResourceLinksForAGivenCountry() {
        addCountrySummary("NZL", "New Zealand", "NZ");
        addCountrySummary("AUS", "Australia", "AU");
        CountryResourceLink countryResourceLink1 = new CountryResourceLink(new CountryResourceLinkId("NZL", "Res " +
                "1","PUBLISHED"),new Date(), null);
        CountryResourceLink countryResourceLink2 = new CountryResourceLink(new CountryResourceLinkId("AUS", "Res " +
                "2","PUBLISHED"),new Date(), null);
        CountryResourceLink countryResourceLink3 = new CountryResourceLink(new CountryResourceLinkId("NZL", "Res " +
                "3","PUBLISHED"),new Date(), null);
        entityManager.persist(countryResourceLink1);
        entityManager.persist(countryResourceLink2);
        entityManager.persist(countryResourceLink3);
        entityManager.flush();
        entityManager.clear();
        iCountryResourceLinkRepository.deleteByCountryResourceLinkIdCountryIdAndCountryResourceLinkIdStatus("NZL", "PUBLISHED");
        List<CountryResourceLink> actual1 = iCountryResourceLinkRepository.findAllByCountryResourceLinkIdCountryId("NZL");
        List<CountryResourceLink> actual2 = iCountryResourceLinkRepository.findAllByCountryResourceLinkIdCountryId("AUS");
        assertThat(actual1.size(), is(0));
        assertThat(actual2.size(), is(1));
        assertThat(actual2.stream().map(CountryResourceLink::getLink).collect(toList()), containsInAnyOrder("Res 2"));
    }

}