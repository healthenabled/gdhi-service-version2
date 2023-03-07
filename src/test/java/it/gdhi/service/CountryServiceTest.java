package it.gdhi.service;

import java.time.Year;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.google.common.collect.ImmutableList;
import it.gdhi.dto.CountrySummaryDto;
import it.gdhi.dto.GdhiQuestionnaire;
import it.gdhi.dto.HealthIndicatorDto;
import it.gdhi.internationalization.service.CountryNameTranslator;
import it.gdhi.model.Country;
import it.gdhi.model.CountryHealthIndicator;
import it.gdhi.model.CountryPhase;
import it.gdhi.model.CountryResourceLink;
import it.gdhi.model.CountrySummary;
import it.gdhi.model.Indicator;
import it.gdhi.model.id.CountryHealthIndicatorId;
import it.gdhi.model.id.CountryPhaseId;
import it.gdhi.model.id.CountryResourceLinkId;
import it.gdhi.model.id.CountrySummaryId;
import it.gdhi.repository.ICountryHealthIndicatorRepository;
import it.gdhi.repository.ICountryPhaseRepository;
import it.gdhi.repository.ICountryRepository;
import it.gdhi.repository.ICountrySummaryRepository;
import it.gdhi.utils.LanguageCode;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import static it.gdhi.utils.FormStatus.PUBLISHED;
import static it.gdhi.utils.LanguageCode.ar;
import static it.gdhi.utils.LanguageCode.en;
import static java.util.Arrays.asList;
import static java.util.UUID.randomUUID;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class CountryServiceTest {

    @InjectMocks
    private CountryService countryService;
    @Mock
    private ICountryRepository countryDetailRepository;
    @Mock
    private ICountrySummaryRepository iCountrySummaryRepository;
    @Mock
    private ICountryHealthIndicatorRepository iCountryHealthIndicatorRepository;
    @Mock
    private CountryNameTranslator translator;
    @Mock
    private ICountryPhaseRepository iCountryPhaseRepository;

    @Test
    public void shouldInsertTestData() {
        when(countryDetailRepository.findAll()).thenReturn(ImmutableList.of());
        countryService.fetchCountries(en);

        verify(translator).translate(any(), any());
    }

    @Test
    public void shouldInvoke() {
        countryService.fetchCountries(en);

        verify(countryDetailRepository).findAll();
        verify(countryDetailRepository).findAll();
    }

    @Test
    public void shouldReturnCountrySummaryDetailsForAGivenYear() {
        String countryId = "ARG";
        String summary = "Summary";
        String contactName = "Contact Name";
        String contactDesignation = "Contact Designation";
        String contactOrganization = "Contact Organization";
        String link1 = "Link 1";
        String link2 = "Link 2";
        String status = "PUBLISHED";
        String year = "Version1";
        CountryResourceLink countryResourceLink1 = new CountryResourceLink(new CountryResourceLinkId("ARG", link1,
                status, year), new Date(), null);
        CountryResourceLink countryResourceLink2 = new CountryResourceLink(new CountryResourceLinkId("ARG", link2,
                status, year), new Date(), null);
        List<CountryResourceLink> countryResourceLinks = asList(countryResourceLink1, countryResourceLink2);
        CountrySummary countrySummary = CountrySummary.builder()
                .countrySummaryId(new CountrySummaryId(countryId, status, year))
                .summary(summary)
                .country(new Country(countryId, "Argentina", randomUUID(), "AR"))
                .contactName(contactName)
                .contactDesignation(contactDesignation)
                .contactOrganization(contactOrganization)
                .contactEmail("email")
                .dataFeederName("feeder name")
                .dataFeederRole("feeder role")
                .dataFeederEmail("email")
                .dataApproverName("coll name")
                .dataApproverRole("coll role")
                .dataApproverEmail("coll email")
                .collectedDate(new Date())
                .countryResourceLinks(countryResourceLinks)
                .build();
        when(iCountrySummaryRepository.findByCountrySummaryIdCountryIdAndCountrySummaryIdYearAndCountrySummaryIdStatus(countryId, year, status)).thenReturn(countrySummary);

        CountrySummaryDto countrySummaryDto = countryService.fetchCountrySummary(countryId, year);

        assertThat(countrySummaryDto.getCountryId(), is(countryId));
        assertThat(countrySummaryDto.getContactName(), is(contactName));
        assertThat(countrySummaryDto.getContactDesignation(), is(contactDesignation));
        assertThat(countrySummaryDto.getContactOrganization(), is(contactOrganization));
        assertThat(countrySummaryDto.getContactEmail(), is(countrySummary.getContactEmail()));
        assertThat(countrySummaryDto.getDataFeederName(), is(countrySummary.getDataFeederName()));
        assertThat(countrySummaryDto.getDataFeederRole(), is(countrySummary.getDataFeederRole()));
        assertThat(countrySummaryDto.getDataFeederEmail(), is(countrySummary.getDataFeederEmail()));
        assertThat(countrySummaryDto.getDataApproverName(), is(countrySummary.getDataApproverName()));
        assertThat(countrySummaryDto.getDataApproverRole(), is(countrySummary.getDataApproverRole()));
        assertThat(countrySummaryDto.getDataApproverEmail(), is(countrySummary.getDataApproverEmail()));
        assertThat(countrySummaryDto.getCollectedDate(), is(countrySummary.getCollectedDate()));
        assertThat(countrySummaryDto.getSummary(), is(summary));
        assertThat(countrySummaryDto.getResources(), Matchers.containsInAnyOrder(link1, link2));
    }

    @Test
    public void shouldReturnEmptyCountrySummaryObjectWhenNoCountrySummaryPresentForAGivenYear() {
        String countryId = "ARG";
        String year = "Version1";
        when(iCountrySummaryRepository.findByCountrySummaryIdCountryIdAndCountrySummaryIdYearAndCountrySummaryIdStatus(countryId, year, PUBLISHED.name())).thenReturn(null);
        CountrySummaryDto countrySummaryDto = countryService.fetchCountrySummary(countryId, year);
        assertNull(countrySummaryDto.getCountryId());
    }

    @Test
    public void shouldGetGlobalHealthScoreDtoForCurrentYear() throws Exception {
        String countryId = "IND";
        String statusValue = "PUBLISHED";
        UUID countryUUID = randomUUID();
        String year = getCurrentYear();
        Country country = new Country(countryId, "India", countryUUID, "IN");
        CountrySummary countrySummary = CountrySummary.builder()
                .countrySummaryId(new CountrySummaryId(countryId, statusValue, year))
                .country(country)
                .summary("summary")
                .contactName("contactName")
                .contactDesignation("contact designation")
                .contactOrganization("contact org")
                .contactEmail("contact email")
                .dataFeederName("feeder name")
                .dataFeederRole("feeder role")
                .dataFeederEmail("feeder email")
                .dataApproverName("collector name")
                .dataApproverRole("collector role")
                .dataApproverRole("collector email")
                .collectedDate(new Date())
                .countryResourceLinks(asList(new CountryResourceLink(new CountryResourceLinkId(countryId, "link",
                        statusValue, year), new Date(), null)))
                .build();

        when(iCountrySummaryRepository.findByCountrySummaryIdCountryIdAndCountrySummaryIdStatusNotAndCountrySummaryIdYear(countryId, statusValue, year)).thenReturn(countrySummary);
        when(countryDetailRepository.findByUniqueId(countryUUID)).thenReturn(country);
        CountryHealthIndicator indicator1 = CountryHealthIndicator.builder()
                .countryHealthIndicatorId(new CountryHealthIndicatorId(countryId, 1, 2, statusValue, year))
                .indicator(new Indicator(2, "Some indicator", "some code", 1, null, new ArrayList<>(), "some def"))
                .score(5)
                .build();
        CountryHealthIndicator indicator2 = CountryHealthIndicator.builder()
                .countryHealthIndicatorId(new CountryHealthIndicatorId(countryId, 2, 3, statusValue, year))
                .indicator(new Indicator(3, "Some indicator", "some code", 2, null, new ArrayList<>(), "some def"))
                .score(4)
                .build();
        List<CountryHealthIndicator> countryHealthIndicators = asList(indicator1, indicator2);
        when(iCountryHealthIndicatorRepository.findByCountryHealthIndicatorIdCountryIdAndCountryHealthIndicatorIdYearAndCountryHealthIndicatorIdStatus(countryId, year, statusValue)).thenReturn(countryHealthIndicators);

        GdhiQuestionnaire details = countryService.getDetails(countryUUID, LanguageCode.en, false, year);

        assertSummary(countrySummary, details.getCountrySummary());
        assertIndicators(countryHealthIndicators, details.getHealthIndicators());
    }

    @Test
    public void shouldGetGlobalHealthScoreDtoForNotPublishedForCurrentYear() throws Exception {
        String countryIdInd = "IND";
        String statusValueInd = "REVIEW_PENDING";
        UUID countryUUIDInd = randomUUID();
        String year = getCurrentYear();
        Country countryInd = new Country(countryIdInd, "India", countryUUIDInd, "IN");
        CountrySummary countrySummaryInd = CountrySummary.builder()
                .countrySummaryId(new CountrySummaryId(countryIdInd, statusValueInd, year))
                .country(countryInd)
                .summary("summary")
                .contactName("contactName")
                .contactDesignation("contact designation")
                .contactOrganization("contact org")
                .contactEmail("contact email")
                .dataFeederName("feeder name")
                .dataFeederRole("feeder role")
                .dataFeederEmail("feeder email")
                .dataApproverName("collector name")
                .dataApproverRole("collector role")
                .dataApproverRole("collector email")
                .collectedDate(new Date())
                .countryResourceLinks(asList(new CountryResourceLink(new CountryResourceLinkId(countryIdInd, "link",
                        statusValueInd, year), new Date(), null)))
                .build();
        String countryIdArg = "ARG";
        String statusValueArg = "REVIEW_PENDING";
        UUID countryUUIDArg = randomUUID();
        Country countryArg = new Country(countryIdArg, "Argentina", countryUUIDArg, "Arg");
        CountrySummary countrySummaryArg = CountrySummary.builder()
                .countrySummaryId(new CountrySummaryId(countryIdArg, statusValueArg, year))
                .country(countryArg)
                .summary("summary")
                .contactName("contactName")
                .contactDesignation("contact designation")
                .contactOrganization("contact org")
                .contactEmail("contact email")
                .dataFeederName("feeder name")
                .dataFeederRole("feeder role")
                .dataFeederEmail("feeder email")
                .dataApproverName("collector name")
                .dataApproverRole("collector role")
                .dataApproverRole("collector email")
                .collectedDate(new Date())
                .countryResourceLinks(asList(new CountryResourceLink(new CountryResourceLinkId(countryIdInd, "link",
                        statusValueInd, year), new Date(), null)))
                .build();

        when(iCountrySummaryRepository.findByCountrySummaryIdCountryIdAndCountrySummaryIdStatusNotAndCountrySummaryIdYear(countryIdInd, "PUBLISHED", year)).thenReturn(countrySummaryInd);
        when(iCountrySummaryRepository.findByCountrySummaryIdCountryIdAndCountrySummaryIdYearAndCountrySummaryIdStatus(countryIdArg, year, statusValueInd)).thenReturn(countrySummaryArg);
        when(countryDetailRepository.findByUniqueId(countryUUIDInd)).thenReturn(countryInd);
        CountryHealthIndicator indicator1 = CountryHealthIndicator.builder()
                .countryHealthIndicatorId(new CountryHealthIndicatorId(countryIdInd, 1, 2, statusValueInd, year))
                .indicator(new Indicator(2, "Some indicator", "some code", 1, null, new ArrayList<>(), "some def"))
                .score(5)
                .build();
        CountryHealthIndicator indicator2 = CountryHealthIndicator.builder()
                .countryHealthIndicatorId(new CountryHealthIndicatorId(countryIdInd, 2, 3, statusValueInd, year))
                .indicator(new Indicator(3, "Some indicator", "some code", 2, null, new ArrayList<>(), "some def"))
                .score(4)
                .build();
        List<CountryHealthIndicator> countryHealthIndicators = asList(indicator1, indicator2);
        when(iCountryHealthIndicatorRepository.findByCountryHealthIndicatorIdCountryIdAndCountryHealthIndicatorIdYearAndCountryHealthIndicatorIdStatus(countryIdInd, year, statusValueInd)).thenReturn(countryHealthIndicators);

        GdhiQuestionnaire details = countryService.getDetails(countryUUIDInd, LanguageCode.en, false, year);

        assertSummary(countrySummaryInd, details.getCountrySummary());
        assertIndicators(countryHealthIndicators, details.getHealthIndicators());
    }

    @Test
    public void shouldGetGlobalHealthScoreDtoForArabic() throws Exception {
        String countryId = "IND";
        String indiaInArabic = "الهند";
        String statusValue = "PUBLISHED";
        UUID countryUUID = randomUUID();
        String indiaAlpha2Code = "IN";
        Country country = new Country(countryId, "India", countryUUID, indiaAlpha2Code);
        Date collectedDate = new Date();
        String year = getCurrentYear();
        CountrySummary countrySummary = CountrySummary.builder()
                .countrySummaryId(new CountrySummaryId(countryId, statusValue, year))
                .country(country)
                .summary("summary")
                .contactName("contactName")
                .contactDesignation("contact designation")
                .contactOrganization("contact org")
                .contactEmail("contact email")
                .dataFeederName("feeder name")
                .dataFeederRole("feeder role")
                .dataFeederEmail("feeder email")
                .dataApproverName("collector name")
                .dataApproverRole("collector role")
                .dataApproverRole("collector email")
                .collectedDate(collectedDate)
                .countryResourceLinks(asList(new CountryResourceLink(new CountryResourceLinkId(countryId, "link",
                        statusValue, year), collectedDate, null)))
                .build();

        when(iCountrySummaryRepository.findByCountrySummaryIdCountryIdAndCountrySummaryIdStatusNotAndCountrySummaryIdYear(countryId, statusValue, year)).thenReturn(countrySummary);
        when(translator.getCountryTranslationForLanguage(ar, countryId)).thenReturn(indiaInArabic);
        when(countryDetailRepository.findByUniqueId(countryUUID)).thenReturn(country);
        CountryHealthIndicator indicator1 = CountryHealthIndicator.builder()
                .countryHealthIndicatorId(new CountryHealthIndicatorId(countryId, 1, 2, statusValue, year))
                .indicator(new Indicator(2, "Some indicator", "some code", 1, null, new ArrayList<>(), "some def"))
                .score(5)
                .build();
        CountryHealthIndicator indicator2 = CountryHealthIndicator.builder()
                .countryHealthIndicatorId(new CountryHealthIndicatorId(countryId, 2, 3, statusValue, year))
                .indicator(new Indicator(3, "Some indicator", "some code", 2, null, new ArrayList<>(), "some def"))
                .score(4)
                .build();
        List<CountryHealthIndicator> countryHealthIndicators = asList(indicator1, indicator2);
        when(iCountryHealthIndicatorRepository.findByCountryHealthIndicatorIdCountryIdAndCountryHealthIndicatorIdYearAndCountryHealthIndicatorIdStatus(countryId, year, statusValue)).thenReturn(countryHealthIndicators);

        GdhiQuestionnaire details = countryService.getDetails(countryUUID, LanguageCode.ar, false, year);

        CountrySummary expectedCountrySummary = CountrySummary.builder()
                .countrySummaryId(new CountrySummaryId(countryId, statusValue, year))
                .country(new Country(countryId, indiaInArabic, countryUUID, indiaAlpha2Code))
                .summary("summary")
                .contactName("contactName")
                .contactDesignation("contact designation")
                .contactOrganization("contact org")
                .contactEmail("contact email")
                .dataFeederName("feeder name")
                .dataFeederRole("feeder role")
                .dataFeederEmail("feeder email")
                .dataApproverName("collector name")
                .dataApproverRole("collector role")
                .dataApproverRole("collector email")
                .collectedDate(collectedDate)
                .countryResourceLinks(asList(new CountryResourceLink(new CountryResourceLinkId(countryId, "link",
                        statusValue, year), collectedDate, null)))
                .build();
        assertSummary(expectedCountrySummary, details.getCountrySummary());
        assertIndicators(countryHealthIndicators, details.getHealthIndicators());
    }

    @Test
    public void shouldHandleCountriesNotAvailableForCurrentYear() throws Exception {
        String countryId = "IND";
        UUID countryUUID = randomUUID();
        String currentYear = getCurrentYear();
        Country country = new Country(countryId, "India", countryUUID, "IN");

        when(iCountrySummaryRepository.findByCountrySummaryIdCountryIdAndCountrySummaryIdStatusNotAndCountrySummaryIdYear(countryId, "PUBLISHED", currentYear)).thenReturn(null);

        when(iCountryHealthIndicatorRepository.findByCountryHealthIndicatorIdCountryIdAndCountryHealthIndicatorIdYearAndCountryHealthIndicatorIdStatus(countryId, currentYear, null)).thenReturn(Collections.emptyList());

        when(countryDetailRepository.findByUniqueId(countryUUID)).thenReturn(country);
        GdhiQuestionnaire details = countryService.getDetails(countryUUID, LanguageCode.en, false, currentYear);

        assertNull(details);
    }

    @Test
    public void shouldGetPublishedCountriesDistinctYears() {
        CountryPhaseId countryPhaseId1 = new CountryPhaseId("IND", "Version1");
        CountryPhaseId countryPhaseId2 = new CountryPhaseId("AUS", "2023");
        CountryPhaseId countryPhaseId3 = new CountryPhaseId("PAK", "2023");
        CountryPhase countryPhaseInd = CountryPhase.builder().countryPhaseId(countryPhaseId1).countryOverallPhase(1).build();
        CountryPhase countryPhaseAus = CountryPhase.builder().countryPhaseId(countryPhaseId2).countryOverallPhase(2).build();
        CountryPhase countryPhasePak = CountryPhase.builder().countryPhaseId(countryPhaseId3).countryOverallPhase(3).build();

        when(iCountryPhaseRepository.findAll()).thenReturn(asList(countryPhasePak, countryPhaseAus, countryPhaseInd));
        List<String> expectedDistinctYears = asList("Version1", "2023");
        List<String> actualDistinctYears = countryService.fetchPublishCountriesDistinctYears();

        assertEquals(expectedDistinctYears, actualDistinctYears);
    }

    private void assertIndicators(List<CountryHealthIndicator> expectedCountryHealthIndicators, List<HealthIndicatorDto> actualHealthIndicators) {
        assertEquals(expectedCountryHealthIndicators.size(), actualHealthIndicators.size());
        expectedCountryHealthIndicators.forEach(expected -> {
            HealthIndicatorDto actualIndicator = actualHealthIndicators.stream()
                    .filter(actual -> actual.getCategoryId().equals(expected.getCountryHealthIndicatorId().getCategoryId())
                            && actual.getIndicatorId().equals(expected.getCountryHealthIndicatorId().getIndicatorId()))
                    .findFirst()
                    .get();
            assertEquals(expected.getScore(), actualIndicator.getScore());
            assertEquals(expected.getSupportingText(), actualIndicator.getSupportingText());
        });
    }

    private void assertSummary(CountrySummary expectedCountrySummary, CountrySummaryDto actualCountrySummary) {
        assertEquals(expectedCountrySummary.getContactName(), actualCountrySummary.getContactName());
        assertEquals(expectedCountrySummary.getCountry().getName(), actualCountrySummary.getCountryName());
        assertEquals(expectedCountrySummary.getSummary(), actualCountrySummary.getSummary());
        assertEquals(expectedCountrySummary.getContactDesignation(), actualCountrySummary.getContactDesignation());
        assertEquals(expectedCountrySummary.getContactOrganization(), actualCountrySummary.getContactOrganization());
        assertEquals(expectedCountrySummary.getContactEmail(), actualCountrySummary.getContactEmail());
        assertEquals(expectedCountrySummary.getDataFeederName(), actualCountrySummary.getDataFeederName());
        assertEquals(expectedCountrySummary.getDataFeederRole(), actualCountrySummary.getDataFeederRole());
        assertEquals(expectedCountrySummary.getDataFeederEmail(), actualCountrySummary.getDataFeederEmail());
        assertEquals(expectedCountrySummary.getDataApproverName(), actualCountrySummary.getDataApproverName());
        assertEquals(expectedCountrySummary.getDataApproverEmail(), actualCountrySummary.getDataApproverEmail());
        assertEquals(expectedCountrySummary.getCollectedDate(), actualCountrySummary.getCollectedDate());
        assertEquals(expectedCountrySummary.getCountryResourceLinks().stream().map(CountryResourceLink::getLink).collect(Collectors.toList()),
                actualCountrySummary.getResources());
    }

    public String getCurrentYear() {
        int currentYear = Year.now().getValue();
        String year = new String(String.valueOf(currentYear));
        return year;
    }
}
