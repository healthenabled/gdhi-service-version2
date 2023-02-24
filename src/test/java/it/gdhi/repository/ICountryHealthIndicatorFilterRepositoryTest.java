package it.gdhi.repository;

import it.gdhi.model.Country;
import it.gdhi.model.CountryHealthIndicator;
import it.gdhi.model.CountrySummary;
import it.gdhi.model.id.CountryHealthIndicatorId;
import it.gdhi.model.id.CountrySummaryId;
import org.junit.jupiter.api.BeforeEach;
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

import static it.gdhi.utils.FormStatus.PUBLISHED;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@Transactional
@ActiveProfiles("test")
public class ICountryHealthIndicatorFilterRepositoryTest {

    private Integer categoryId1 = 1;

    @Autowired
    private ICountrySummaryRepository countrySummaryRepository;

    @Autowired
    private ICountryHealthIndicatorRepository iCountryHealthIndicatorRepository;

    @Autowired
    private EntityManager entityManager;

    @BeforeEach
    public void setUp() throws Exception {
        String status = "PUBLISHED";

        String countryId1 = "IND";
        String year = "Version1";
        CountrySummary countrySummaryIndia = CountrySummary.builder()

                .countrySummaryId(new CountrySummaryId(countryId1, status, year))
                .summary("summary")
                .country(new Country(countryId1, "India", UUID.randomUUID(), "IN"))
                .contactName("contactName")
                .contactDesignation("contactDesignation")
                .contactOrganization("contactOrganization")
                .contactEmail("email")
                .dataFeederName("feeder name")
                .dataFeederRole("feeder role")
                .dataFeederEmail("email")
                .dataApproverName("coll name")
                .dataApproverRole("coll role")
                .dataApproverRole("coll email")
                .countryResourceLinks(new ArrayList<>())
                .build();
        countrySummaryRepository.save(countrySummaryIndia);

        String countryId2 = "ARG";
        CountrySummary countrySummaryARG = CountrySummary.builder()
                .countrySummaryId(new CountrySummaryId(countryId2, status, year))
                .summary("summary")
                .country(new Country(countryId2, "Argentina", UUID.randomUUID(), "AR"))
                .contactName("contactName")
                .contactDesignation("contactDesignation")
                .contactOrganization("contactOrganization")
                .contactEmail("email")
                .dataFeederName("feeder name")
                .dataFeederRole("feeder role")
                .dataFeederEmail("email")
                .dataApproverName("coll name")
                .build();
        countrySummaryRepository.save(countrySummaryARG);

        Integer indicatorId1 = 1;
        CountryHealthIndicatorId countryHealthIndicatorId1 = new CountryHealthIndicatorId(countryId1, categoryId1, indicatorId1, status, year);
        Integer indicatorScore1 = 1;
        CountryHealthIndicator countryHealthIndicatorSetupData1 = new CountryHealthIndicator(countryHealthIndicatorId1, indicatorScore1);

        CountryHealthIndicatorId countryHealthIndicatorId4 = new CountryHealthIndicatorId(countryId1, 2, 4, status, year);
        Integer indicatorScore2 = 3;
        CountryHealthIndicator countryHealthIndicatorSetupData4 = new CountryHealthIndicator(countryHealthIndicatorId4, indicatorScore2);

        Integer indicatorId2 = 2;
        CountryHealthIndicatorId countryHealthIndicatorId2 = new CountryHealthIndicatorId(countryId2, categoryId1, indicatorId2, status, year);
        CountryHealthIndicator countryHealthIndicatorSetupData2 = new CountryHealthIndicator(countryHealthIndicatorId2, indicatorScore1);

        Integer indicatorId3 = 3;
        CountryHealthIndicatorId countryHealthIndicatorId3 = new CountryHealthIndicatorId(countryId2, categoryId1, indicatorId3, status, year);
        Integer indicatorScore3 = 4;
        CountryHealthIndicator countryHealthIndicatorSetupData3 = new CountryHealthIndicator(countryHealthIndicatorId3, indicatorScore3);

        entityManager.persist(countryHealthIndicatorSetupData1);
        entityManager.persist(countryHealthIndicatorSetupData2);
        entityManager.persist(countryHealthIndicatorSetupData3);
        entityManager.persist(countryHealthIndicatorSetupData4);
        entityManager.flush();
        entityManager.clear();

    }

    @Test
    public void shouldFilterHealthRepositoryBasedOnCategoryAndPhaseAndByStatus() throws Exception {
        List<CountryHealthIndicator> countryHealthIndicators = iCountryHealthIndicatorRepository
                .findByCategoryAndStatus(categoryId1, PUBLISHED.name());
        assertEquals(3, countryHealthIndicators.size());
    }

    @Test
    public void shouldGetAllCountriesWhenNoCategoryAndPhaseIsGiven() throws Exception {
        List<CountryHealthIndicator> countryHealthIndicators = iCountryHealthIndicatorRepository
                .findByCategoryAndStatus(null, PUBLISHED.name());
        assertEquals(4, countryHealthIndicators.size());
    }

    @Test
    public void shouldReturnEmptyListWhenGivenCategoryHasNoHealthIndicatorPresent() throws Exception {
        List<CountryHealthIndicator> countryHealthIndicators = iCountryHealthIndicatorRepository
                .findByCategoryAndStatus(7, PUBLISHED.name());
        assertEquals(0, countryHealthIndicators.size());
    }
}