package it.gdhi.service;

import java.util.ArrayList;
import java.util.Calendar;
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
import it.gdhi.model.CountryResourceLink;
import it.gdhi.model.CountrySummary;
import it.gdhi.model.Indicator;
import it.gdhi.model.id.CountryHealthIndicatorId;
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
import static it.gdhi.utils.Util.getCurrentYear;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
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
                year), new Date(), null, status);
        CountryResourceLink countryResourceLink2 = new CountryResourceLink(new CountryResourceLinkId("ARG", link2, year), new Date(), null, status);
        List<CountryResourceLink> countryResourceLinks = asList(countryResourceLink1, countryResourceLink2);
        CountrySummary countrySummary = CountrySummary.builder()
                .countrySummaryId(new CountrySummaryId(countryId, year))
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
                .countryResourceLinks(countryResourceLinks)
                .status(status)
                .build();
        when(iCountrySummaryRepository.findByCountrySummaryIdCountryIdAndCountrySummaryIdYearAndStatus(countryId, year, status)).thenReturn(countrySummary);

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
        assertThat(countrySummaryDto.getSummary(), is(summary));
        assertThat(countrySummaryDto.getResources(), Matchers.containsInAnyOrder(link1, link2));
    }

    @Test
    public void shouldReturnEmptyCountrySummaryObjectWhenNoCountrySummaryPresentForAGivenYear() {
        String countryId = "ARG";
        String year = "Version1";
        when(iCountrySummaryRepository.findByCountrySummaryIdCountryIdAndCountrySummaryIdYearAndStatus(countryId, year,
                PUBLISHED.name())).thenReturn(null);
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
                .countrySummaryId(new CountrySummaryId(countryId, year))
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
                .countryResourceLinks(asList(new CountryResourceLink(new CountryResourceLinkId(countryId, "link"
                        , year), new Date(), null, statusValue)))
                .status(statusValue)
                .build();

        when(iCountrySummaryRepository.findByCountrySummaryIdCountryIdAndCountrySummaryIdYear(countryId, year)).thenReturn(Collections.singletonList(countrySummary));
        when(countryDetailRepository.findByUniqueId(countryUUID)).thenReturn(country);
        CountryHealthIndicator indicator1 = CountryHealthIndicator.builder()
                .countryHealthIndicatorId(new CountryHealthIndicatorId(countryId, 1, 2, year))
                .indicator(new Indicator(2, "Some indicator", "some code", 1, null, new ArrayList<>(), "some def"))
                .score(5)
                .status(statusValue)
                .build();
        CountryHealthIndicator indicator2 = CountryHealthIndicator.builder()
                .countryHealthIndicatorId(new CountryHealthIndicatorId(countryId, 2, 3, year))
                .indicator(new Indicator(3, "Some indicator", "some code", 2, null, new ArrayList<>(), "some def"))
                .score(4)
                .status(statusValue)
                .build();
        List<CountryHealthIndicator> countryHealthIndicators = asList(indicator1, indicator2);
        when(iCountryHealthIndicatorRepository.findByCountryHealthIndicatorIdCountryIdAndCountryHealthIndicatorIdYearAndStatus(countryId, year, statusValue)).thenReturn(countryHealthIndicators);

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
                .countrySummaryId(new CountrySummaryId(countryIdInd, year))
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
                .countryResourceLinks(asList(new CountryResourceLink(new CountryResourceLinkId(countryIdInd, "link",
                        year), new Date(), null, statusValueInd)))
                .status(statusValueInd)
                .build();
        String countryIdArg = "ARG";
        String statusValueArg = "REVIEW_PENDING";
        UUID countryUUIDArg = randomUUID();
        Country countryArg = new Country(countryIdArg, "Argentina", countryUUIDArg, "Arg");
        CountrySummary countrySummaryArg = CountrySummary.builder()
                .countrySummaryId(new CountrySummaryId(countryIdArg, year))
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
                .countryResourceLinks(asList(new CountryResourceLink(new CountryResourceLinkId(countryIdInd, "link",
                        year), new Date(), null, statusValueInd)))
                .status(statusValueArg)
                .build();

        when(iCountrySummaryRepository.findByCountrySummaryIdCountryIdAndCountrySummaryIdYear(countryIdInd, year)).thenReturn(Collections.singletonList(countrySummaryInd));
        when(iCountrySummaryRepository.findByCountrySummaryIdCountryIdAndCountrySummaryIdYearAndStatus(countryIdArg, year,
                statusValueInd)).thenReturn(countrySummaryArg);
        when(countryDetailRepository.findByUniqueId(countryUUIDInd)).thenReturn(countryInd);
        CountryHealthIndicator indicator1 = CountryHealthIndicator.builder()
                .countryHealthIndicatorId(new CountryHealthIndicatorId(countryIdInd, 1, 2, year))
                .indicator(new Indicator(2, "Some indicator", "some code", 1, null, new ArrayList<>(), "some def"))
                .score(5)
                .status(statusValueInd)
                .build();
        CountryHealthIndicator indicator2 = CountryHealthIndicator.builder()
                .countryHealthIndicatorId(new CountryHealthIndicatorId(countryIdInd, 2, 3, year))
                .indicator(new Indicator(3, "Some indicator", "some code", 2, null, new ArrayList<>(), "some def"))
                .score(4)
                .status(statusValueInd)
                .build();
        List<CountryHealthIndicator> countryHealthIndicators = asList(indicator1, indicator2);
        when(iCountryHealthIndicatorRepository.findByCountryHealthIndicatorIdCountryIdAndCountryHealthIndicatorIdYearAndStatus(countryIdInd, year, statusValueInd)).thenReturn(countryHealthIndicators);

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
        Date createdDate = new Date();
        String year = getCurrentYear();
        CountrySummary countrySummary = CountrySummary.builder()
                .countrySummaryId(new CountrySummaryId(countryId, year))
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
                .countryResourceLinks(asList(new CountryResourceLink(new CountryResourceLinkId(countryId, "link"
                        , year), createdDate, null, statusValue)))
                .status(statusValue)
                .build();

        when(iCountrySummaryRepository.findByCountrySummaryIdCountryIdAndCountrySummaryIdYear(countryId, year)).thenReturn(Collections.singletonList(countrySummary));
        when(translator.getCountryTranslationForLanguage(ar, countryId)).thenReturn(indiaInArabic);
        when(countryDetailRepository.findByUniqueId(countryUUID)).thenReturn(country);
        CountryHealthIndicator indicator1 = CountryHealthIndicator.builder()
                .countryHealthIndicatorId(new CountryHealthIndicatorId(countryId, 1, 2, year))
                .indicator(new Indicator(2, "Some indicator", "some code", 1, null, new ArrayList<>(), "some def"))
                .score(5)
                .status(statusValue)
                .build();
        CountryHealthIndicator indicator2 = CountryHealthIndicator.builder()
                .countryHealthIndicatorId(new CountryHealthIndicatorId(countryId, 2, 3, year))
                .indicator(new Indicator(3, "Some indicator", "some code", 2, null, new ArrayList<>(), "some def"))
                .score(4)
                .status(statusValue)
                .build();
        List<CountryHealthIndicator> countryHealthIndicators = asList(indicator1, indicator2);
        when(iCountryHealthIndicatorRepository.findByCountryHealthIndicatorIdCountryIdAndCountryHealthIndicatorIdYearAndStatus(countryId, year, statusValue)).thenReturn(countryHealthIndicators);

        GdhiQuestionnaire details = countryService.getDetails(countryUUID, LanguageCode.ar, false, year);

        CountrySummary expectedCountrySummary = CountrySummary.builder()
                .countrySummaryId(new CountrySummaryId(countryId, year))
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
                .countryResourceLinks(asList(new CountryResourceLink(new CountryResourceLinkId(countryId, "link",
                        year), createdDate, null, statusValue)))
                .status(statusValue)
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

        when(iCountrySummaryRepository.findByCountrySummaryIdCountryIdAndCountrySummaryIdYear(countryId, currentYear)).thenReturn(null);

        when(iCountryHealthIndicatorRepository.findByCountryHealthIndicatorIdCountryIdAndCountryHealthIndicatorIdYearAndStatus(countryId, currentYear, null)).thenReturn(emptyList());

        when(countryDetailRepository.findByUniqueId(countryUUID)).thenReturn(country);
        GdhiQuestionnaire details = countryService.getDetails(countryUUID, LanguageCode.en, false, currentYear);

        assertNull(details);
    }

    @Test
    public void shouldGetPublishedCountriesDistinctYears() {
        when(iCountryPhaseRepository.findAllDistinctYearsOrderByUpdatedAtDesc(2)).thenReturn(asList("2023", "Version1"
        ));
        List<String> expectedDistinctYears = asList("2023", "Version1");
        List<String> actualDistinctYears = countryService.fetchPublishCountriesDistinctYears(2);

        assertEquals(expectedDistinctYears, actualDistinctYears);
    }

    @Test
    public void shouldGetTheLatestDataAvailableYearWhenCountryUUIDIsGiven() {
        String countryId = "IND";
        UUID countryUUID = randomUUID();
        Date createdDate = new Date();

        Date updatedDate = getUpdatedDate(2022);
        Date updatedDate1 = getUpdatedDate(2018);

        Country country = new Country(countryId, "India", countryUUID, "IN");
        CountrySummary countrySummary = CountrySummary.builder()
                .countrySummaryId(new CountrySummaryId(countryId, "2022"))
                .country(new Country(countryId, "INDIA ", countryUUID, "IN"))
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
                .countryResourceLinks(asList(new CountryResourceLink(new CountryResourceLinkId(countryId, "link",
                        "2022"), createdDate, updatedDate, PUBLISHED.name())))
                .status(PUBLISHED.name())
                .build();
        CountrySummary countrySummary1 = CountrySummary.builder()
                .countrySummaryId(new CountrySummaryId(countryId, "2018"))
                .country(new Country(countryId, "INDIA ", countryUUID, "IN"))
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
                .countryResourceLinks(asList(new CountryResourceLink(new CountryResourceLinkId(countryId, "link",
                        "2018"), createdDate, updatedDate1, PUBLISHED.name())))
                .status(PUBLISHED.name())
                .build();
        when(iCountrySummaryRepository.findFirstByCountryIdAndStatusNotNEWOrderByDesc(countryId)).thenReturn("2022");
        when(countryDetailRepository.findByUniqueId(countryUUID)).thenReturn(country);

        String actualYear = countryService.fetchTheYearToPrefillData(countryUUID);
        String expectedYear = "2022";

        assertEquals(expectedYear, actualYear);
    }

    @Test
    public void shouldGetTheCurrentYearForACountryWhenNoPreviousDataIsAvailable() {
        String countryId = "IND";
        UUID countryUUID = randomUUID();
        Country country = new Country(countryId, "India", countryUUID, "IN");

        when(iCountrySummaryRepository.findFirstByCountryIdAndStatusNotNEWOrderByDesc(countryId)).thenReturn(null);
        when(countryDetailRepository.findByUniqueId(countryUUID)).thenReturn(country);

        String actualYear = countryService.fetchTheYearToPrefillData(countryUUID);
        String expectedYear = getCurrentYear();

        assertEquals(expectedYear, actualYear);
    }

    @Test
    public void shouldReturnFalseWhenCountryHasNoURLGeneratedForCurrentYear() {
        String countryId = "IND";
        UUID countryUUID = randomUUID();
        Country country = new Country(countryId, "India", countryUUID, "IN");

        when(iCountrySummaryRepository.findByCountrySummaryIdCountryIdAndCountrySummaryIdYear(countryId,
                getCurrentYear())).thenReturn(emptyList());
        when(countryDetailRepository.findByUniqueId(countryUUID)).thenReturn(country);

        Boolean actualResponse = countryService.checkCountryHasEntryForCurrentYear(countryUUID);
        Boolean expectedResponse = false;

        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    public void shouldReturnTrueWhenCountryHasURLGeneratedForCurrentYear() {
        String countryId = "IND";
        UUID countryUUID = randomUUID();
        Date createdDate = new Date();

        Date updatedDate = new Date();

        Country country = new Country(countryId, "India", countryUUID, "IN");
        CountrySummary countrySummary = CountrySummary.builder()
                .countrySummaryId(new CountrySummaryId(countryId, getCurrentYear()))
                .country(new Country(countryId, "INDIA ", countryUUID, "IN"))
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
                .countryResourceLinks(asList(new CountryResourceLink(new CountryResourceLinkId(countryId, "link"
                        , getCurrentYear()), createdDate, updatedDate, PUBLISHED.name())))
                .status(PUBLISHED.name())
                .build();

        when(iCountrySummaryRepository.findByCountrySummaryIdCountryIdAndCountrySummaryIdYear(countryId,
                getCurrentYear())).thenReturn(Collections.singletonList(countrySummary));
        when(countryDetailRepository.findByUniqueId(countryUUID)).thenReturn(country);

        Boolean actualResponse = countryService.checkCountryHasEntryForCurrentYear(countryUUID);
        Boolean expectedResponse = true;

        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    public void shouldReturnCountryNameWhenCountryIdAndLanguageCodeArePassed() {
        String countryId = "IND";
        LanguageCode languageCode = en;
        Country country = new Country("IND", "India", UUID.randomUUID(), "IN");
        when(countryDetailRepository.findById(countryId)).thenReturn(country);

        countryService.getCountryName(countryId, languageCode);
        verify(countryDetailRepository).findById(countryId);
    }

    @Test
    public void shouldReturnCountryNameWhenCountryIdAndLanguageCodeArePassedAndLanguageCodeIsFr() {
        String countryId = "IND";
        LanguageCode languageCode = LanguageCode.fr;
        when(translator.getCountryTranslationForLanguage(languageCode, countryId)).thenReturn("Inde");

        countryService.getCountryName(countryId, languageCode);
        verify(translator).getCountryTranslationForLanguage(languageCode, countryId);
    }


    private Date getUpdatedDate(int year) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, Calendar.MARCH);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        return calendar.getTime();
    }

    private void assertIndicators(List<CountryHealthIndicator> expectedCountryHealthIndicators,
                                  List<HealthIndicatorDto> actualHealthIndicators) {
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
        assertEquals(expectedCountrySummary.getCountryResourceLinks().stream().map(CountryResourceLink::getLink).collect(Collectors.toList()),
                actualCountrySummary.getResources());
    }

}
