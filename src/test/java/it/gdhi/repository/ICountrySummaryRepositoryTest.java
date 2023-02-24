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

import static it.gdhi.utils.FormStatus.NEW;
import static it.gdhi.utils.FormStatus.PUBLISHED;
import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@Transactional
@ActiveProfiles("test")
public class ICountrySummaryRepositoryTest {

    @Autowired
    private ICountrySummaryRepository iCountrySummaryRepository;

    @Autowired
    private EntityManager entityManager;

    private void addCountrySummary(String countryId, String countryName, String alpha2code, String summary,
                                   List<CountryResourceLink> countryResourceLinkList, String status) {
        String year = "Version1";
        CountrySummary countrySummary = CountrySummary.builder()
                .countrySummaryId(new CountrySummaryId(countryId, status, year))
                .summary(summary)
                .country(new Country(countryId, countryName, UUID.randomUUID(), alpha2code))
                .contactName("Contact Name")
                .contactDesignation("Contact Designation")
                .contactOrganization("contactOrganization")
                .contactEmail("email")
                .dataFeederName("feeder name")
                .dataFeederRole("feeder role")
                .dataFeederEmail("email")
                .dataApproverName("coll name")
                .dataApproverRole("coll role")
                .dataFeederRole("coll role")
                .dataApproverEmail("coll email")
                .countryResourceLinks(countryResourceLinkList)
                .build();
        iCountrySummaryRepository.save(countrySummary);
    }

    @Test
    public void shouldFetchPopulationGivenCountryCode() {
        addCountrySummary("NZL", "New Zealand", "NZ", "NZL summary",
                new ArrayList<>(), PUBLISHED.toString());
        CountrySummary actual = iCountrySummaryRepository.findOne("NZL");
        assertThat(actual.getCountrySummaryId().getCountryId(), is("NZL"));
        assertThat(actual.getSummary(), is("NZL summary"));
        assertThat(actual.getContactName(), is("Contact Name"));
        assertThat(actual.getContactDesignation(), is("Contact Designation"));
        assertThat(actual.getCountry().getName(), is("New Zealand"));
    }

    @Test
    public void shouldFetchCountryCodeCaseInsensitive() {
        addCountrySummary("NZL", "New Zealand", "NZ", "NZL summary",
                new ArrayList<>(), PUBLISHED.toString());
        CountrySummary actual = iCountrySummaryRepository.findOne("nzl");
        assertThat(actual.getCountrySummaryId().getCountryId(), is("NZL"));
        assertThat(actual.getSummary(), is("NZL summary"));
        assertThat(actual.getContactName(), is("Contact Name"));
        assertThat(actual.getContactDesignation(), is("Contact Designation"));
        assertThat(actual.getCountry().getName(), is("New Zealand"));
    }

    @Test
    public void shouldSaveCountrySummaryAlongWithResourceLinks() {
        String countryId = "NZL";
        String status = PUBLISHED.toString();
        String summary = "NZL summary 1";
        String countryName = "New Zealand";
        String alpha2code = "NZ";
        String year = "Version1";
        CountryResourceLink countryResourceLinkList = new CountryResourceLink(new CountryResourceLinkId(countryId, "www.google.com",
                PUBLISHED.toString(), year), new Date(), null);

        CountrySummary countrySummary = CountrySummary.builder()
                .countrySummaryId(new CountrySummaryId(countryId, status, year))
                .summary(summary)
                .country(new Country(countryId, countryName, UUID.randomUUID(), alpha2code))
                .contactName("Contact Name")
                .contactDesignation("Contact Designation")
                .contactOrganization("contactOrganization")
                .contactEmail("email")
                .dataFeederName("feeder name")
                .dataFeederRole("feeder role")
                .dataFeederEmail("email")
                .dataApproverName("coll name")
                .dataApproverRole("coll role")
                .dataFeederRole("coll role")
                .dataApproverEmail("coll email")
                .countryResourceLinks(asList(countryResourceLinkList))
                .build();

        iCountrySummaryRepository.save(countrySummary);

        CountrySummary nzl1 = iCountrySummaryRepository.findOne(countryId);
        assertThat(nzl1.getCountrySummaryId().getCountryId(), is(countryId));
        assertThat(nzl1.getSummary(), is("NZL summary 1"));
        assertThat(nzl1.getContactName(), is("Contact Name"));
        assertThat(nzl1.getCountryResourceLinks().get(0).getCountryResourceLinkId().getCountryId(), is(countryId));
        assertThat(nzl1.getCountryResourceLinks().get(0).getCountryResourceLinkId().getLink(), is("www.google.com"));

        CountrySummary countrySummary2 = CountrySummary.builder()
                .countrySummaryId(new CountrySummaryId(countryId, status, year))
                .summary("NZL summary 2")
                .country(new Country(countryId, countryName, UUID.randomUUID(), alpha2code))
                .contactName("Contact Name")
                .contactDesignation("Contact Designation")
                .contactOrganization("contactOrganization")
                .contactEmail("email")
                .dataFeederName("feeder name")
                .dataFeederRole("feeder role")
                .dataFeederEmail("email")
                .dataApproverName("coll name")
                .dataApproverRole("coll role")
                .dataFeederRole("coll role")
                .dataApproverEmail("coll email")
                .countryResourceLinks(asList(countryResourceLinkList))
                .build();
        iCountrySummaryRepository.save(countrySummary2);

        CountrySummary nzl2 = iCountrySummaryRepository.findOne(countryId);
        assertThat(nzl2.getCountrySummaryId().getCountryId(), is(countryId));
        assertThat(nzl2.getSummary(), is("NZL summary 2"));
    }

    @Test
    public void shouldNotBreakWhenNoCountrySummaryIsPresent() {
        String countryId = "IND";

        List<String> allStatus = iCountrySummaryRepository.getAllStatus(countryId);
        assertNotNull(allStatus);
        assert (allStatus.isEmpty());
    }

    @Test
    public void shouldGetAllTheCountrySummaryStatusesForExistingData() {
        String countryId = "NZL";
        addCountrySummary(countryId, "New Zealand",
                "NZ", "NZL summary", new ArrayList<>(), PUBLISHED.toString());
        addCountrySummary(countryId, "New Zealand",
                "NZ", "NZL summary", new ArrayList<>(), NEW.toString());
        List<String> allStatus = iCountrySummaryRepository.getAllStatus(countryId);
        assert (allStatus.contains(PUBLISHED.toString()));
        assert (allStatus.contains(NEW.toString()));
    }

    @Test
    public void shouldGetAllTheCountrySummaryData() {
        String countryId = "IND";
        addCountrySummary(countryId, "INDIA",
                "IN", "IND summary", new ArrayList<>(), PUBLISHED.toString());
        addCountrySummary(countryId, "INDIA",
                "IN", "IND summary", new ArrayList<>(), NEW.toString());
        List<CountrySummary> countrySummaries = iCountrySummaryRepository.findAllByOrderByUpdatedAtDesc();
        assertEquals(countrySummaries.size(), 2);
    }
}