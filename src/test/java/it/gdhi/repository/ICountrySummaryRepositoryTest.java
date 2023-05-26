package it.gdhi.repository;

import it.gdhi.model.Country;
import it.gdhi.model.CountryResourceLink;
import it.gdhi.model.CountrySummary;
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
import java.util.List;
import java.util.UUID;

import static it.gdhi.utils.FormStatus.NEW;
import static it.gdhi.utils.FormStatus.PUBLISHED;
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
                                   List<CountryResourceLink> countryResourceLinkList, String status, String year) {
        // String year = "Version1";
        CountrySummary countrySummary = CountrySummary.builder()
                .countrySummaryId(new CountrySummaryId(countryId, year))
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
                .status(status)
                .build();
        iCountrySummaryRepository.save(countrySummary);
    }

    @Test
    public void shouldGetAllTheCountrySummaryData() {
        String countryId = "IND";
        addCountrySummary(countryId, "INDIA",
                "IN", "IND summary", new ArrayList<>(), PUBLISHED.toString(), "Version1");
        addCountrySummary(countryId, "INDIA",
                "IN", "IND summary", new ArrayList<>(), NEW.toString(), "Version1");
        List<CountrySummary> countrySummaries = iCountrySummaryRepository.findAllByOrderByUpdatedAtDesc();
        assertEquals(countrySummaries.size(), 1);
    }

    @Test
    public void shouldGetTheLatestYearForPrefillingData() {
        String countryId = "IND";
        addCountrySummary(countryId, "INDIA",
                "IN", "IND summary", new ArrayList<>(), PUBLISHED.toString(), "2022");
        addCountrySummary(countryId, "INDIA",
                "IN", "IND summary", new ArrayList<>(), NEW.toString(), "2018");
        String year = iCountrySummaryRepository.findFirstByCountryIdAndStatusNotNEWOrderByDesc(countryId);
        assertEquals("2022", year);
    }

    @Test
    public void shouldReturnNullWhenNoPreviousDataIsAvailable() {
        String countryId = "IND";
        String year = iCountrySummaryRepository.findFirstByCountryIdAndStatusNotNEWOrderByDesc(countryId);
        assertEquals(null, year);
    }
}