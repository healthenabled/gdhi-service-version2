package it.gdhi.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import it.gdhi.dto.*;
import it.gdhi.internationalization.service.HealthIndicatorTranslator;
import it.gdhi.model.Country;
import it.gdhi.model.CountrySummary;
import it.gdhi.model.id.CountrySummaryId;
import it.gdhi.model.response.CountryStatus;
import it.gdhi.model.response.CountryStatuses;
import it.gdhi.repository.ICountryHealthIndicatorRepository;
import it.gdhi.repository.ICountryPhaseRepository;
import it.gdhi.repository.ICountryRepository;
import it.gdhi.repository.ICountrySummaryRepository;
import it.gdhi.utils.FormStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import static com.google.common.collect.ImmutableList.of;
import static it.gdhi.utils.FormStatus.*;
import static it.gdhi.utils.LanguageCode.en;
import static it.gdhi.utils.Util.getCurrentYear;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BffServiceTest {

    @InjectMocks
    private BffService bffService;

    @Mock
    private CountryService countryService;

    @Mock
    private ICountryPhaseRepository iCountryPhaseRepository;

    @Mock
    private ICountryHealthIndicatorRepository iCountryHealthIndicatorRepository;

    @Mock
    private DefaultYearDataService defaultYearDataService;

    @Mock
    private CountryHealthIndicatorService countryHealthIndicatorService;

    @Mock
    private CountryHealthDataService countryHealthDataService;

    @Mock
    private HealthIndicatorTranslator indicatorTranslator;

    @Mock
    private ICountryRepository iCountryRepository;

    @Mock
    private ICountrySummaryRepository iCountrySummaryRepository;

    @Mock
    private CategoryIndicatorService categoryIndicatorService;


    @Test
    public void shouldReturnDistinctYearsAndDefaultYear() {
        when(countryService.fetchPublishCountriesDistinctYears()).thenReturn(asList("Version1", "2023"));
        when(defaultYearDataService.fetchDefaultYear()).thenReturn("2023");

        YearDto expectedYearData = YearDto.builder().years(asList("Version1", "2023")).defaultYear("2023").build();
        YearDto actualYearData = bffService.fetchDistinctYears();

        assertThat(expectedYearData.getDefaultYear(), equalTo(actualYearData.getDefaultYear()));
        assertThat(expectedYearData.getYears(), equalTo(actualYearData.getYears()));
    }

    @Test
    public void shouldReturnPublishedYearsForACountry() {
        List<String> publishedYears = asList("2023", "2022", "Version1");
        when(iCountryPhaseRepository.findByCountryPhaseIdOrderByYearDesc("IND", 3)).thenReturn(publishedYears);
        List<String> actual = bffService.fetchPublishedYearsForACountry("IND", 3);
        List<String> expected = publishedYears;

        assertEquals(expected, actual);
    }

    @Test
    public void shouldReturnYearOnYearData() {
        String year = getCurrentYear();

        CategoryHealthScoreDto categoryHealthScoreDto1 = new CategoryHealthScoreDto(2, "Category 2", 2.0, 2, of(new IndicatorScoreDto(1, null, null, null, null, 2, null, "Not Available")));
        CategoryHealthScoreDto categoryHealthScoreDto2 = new CategoryHealthScoreDto(1, "Category 1", 5.0, 5, of(new IndicatorScoreDto(1, null, null, null, null, 5, null, "Not Available")));
        CountryHealthScoreDto countryHealthScoreDtoIN = new CountryHealthScoreDto("IND", "India", "IN", of(categoryHealthScoreDto1, categoryHealthScoreDto2), 4, "");
        CategoryHealthScoreDto categoryHealthScoreDto4 = new CategoryHealthScoreDto(2, "Category 2", -1.0, -1, of(new IndicatorScoreDto(1, null, null, null, null, null, null, "Not Available")));
        CategoryHealthScoreDto categoryHealthScoreDto5 = new CategoryHealthScoreDto(1, "Category 1", 2.0, 2, of(new IndicatorScoreDto(1, null, null, null, null, 2, null, "Not Available")));

        GlobalHealthScoreDto globalHealthScore = GlobalHealthScoreDto.builder().overAllScore(3).categories(asList(categoryHealthScoreDto1, categoryHealthScoreDto2, categoryHealthScoreDto4, categoryHealthScoreDto5)).build();
        when(countryHealthIndicatorService.getGlobalHealthIndicator(null, null, null, en, year)).thenReturn(globalHealthScore);
        when(countryHealthIndicatorService.fetchCountryHealthScore("IND", en, year)).thenReturn(countryHealthScoreDtoIN);
        when(defaultYearDataService.fetchDefaultYear()).thenReturn(year);

        YearOnYearDto actual = bffService.fetchYearOnYearData((Collections.singletonList(getCurrentYear())), "IND", null);
        YearHealthScoreDto data = YearHealthScoreDto.builder().country(countryHealthScoreDtoIN).average(globalHealthScore).build();
        List<YearScoreDto> yearScoreDtos = Collections.singletonList(YearScoreDto.builder().year(year).data(data).build());
        YearOnYearDto expected = YearOnYearDto.builder().currentYear(year).defaultYear(year).yearOnYearData(yearScoreDtos).build();
        assertEquals(expected, actual);
    }

    @Test
    public void shouldReturnYearOnYearDataForMultipleYears() {
        List<String> years = asList("2023", "2022");

        CategoryHealthScoreDto categoryHealthScoreDto1 = new CategoryHealthScoreDto(2, "Category 2", 2.0, 2, of(new IndicatorScoreDto(1, null, null, null, null, 2, null, "Not Available")));
        CategoryHealthScoreDto categoryHealthScoreDto2 = new CategoryHealthScoreDto(1, "Category 1", 5.0, 5, of(new IndicatorScoreDto(1, null, null, null, null, 5, null, "Not Available")));
        CountryHealthScoreDto countryHealthScoreDtoIN = new CountryHealthScoreDto("IND", "India", "IN", of(categoryHealthScoreDto1, categoryHealthScoreDto2), 4, "");
        CategoryHealthScoreDto categoryHealthScoreDto4 = new CategoryHealthScoreDto(2, "Category 2", -1.0, -1, of(new IndicatorScoreDto(1, null, null, null, null, null, null, "Not Available")));
        CategoryHealthScoreDto categoryHealthScoreDto5 = new CategoryHealthScoreDto(1, "Category 1", 2.0, 2, of(new IndicatorScoreDto(1, null, null, null, null, 2, null, "Not Available")));

        GlobalHealthScoreDto globalHealthScore = GlobalHealthScoreDto.builder().overAllScore(3).categories(asList(categoryHealthScoreDto1, categoryHealthScoreDto2, categoryHealthScoreDto4, categoryHealthScoreDto5)).build();
        when(countryHealthIndicatorService.getGlobalHealthIndicator(null, null, null, en, "2023")).thenReturn(globalHealthScore);
        when(countryHealthIndicatorService.getGlobalHealthIndicator(null, null, null, en, "2022")).thenReturn(globalHealthScore);
        when(countryHealthIndicatorService.fetchCountryHealthScore("IND", en, "2023")).thenReturn(countryHealthScoreDtoIN);
        when(countryHealthIndicatorService.fetchCountryHealthScore("IND", en, "2022")).thenReturn(countryHealthScoreDtoIN);
        when(defaultYearDataService.fetchDefaultYear()).thenReturn("2022");

        YearOnYearDto actual = bffService.fetchYearOnYearData(years, "IND", null);
        YearHealthScoreDto data1 = YearHealthScoreDto.builder().country(countryHealthScoreDtoIN).average(globalHealthScore).build();
        YearHealthScoreDto data2 = YearHealthScoreDto.builder().country(countryHealthScoreDtoIN).average(globalHealthScore).build();
        List<YearScoreDto> yearScoreDtos = asList(YearScoreDto.builder().year("2023").data(data1).build(), YearScoreDto.builder().year("2022").data(data2).build());
        YearOnYearDto expected = YearOnYearDto.builder().currentYear(getCurrentYear()).defaultYear("2022").yearOnYearData(yearScoreDtos).build();
        assertEquals(expected, actual);
    }

    @Test
    public void shouldReturnYearOnYearDataWhenARegionIsGiven() {
        String year = getCurrentYear();
        String region = "PAHO";

        CategoryHealthScoreDto categoryHealthScoreDto1 = new CategoryHealthScoreDto(2, "Category 2", 2.0, 2, of(new IndicatorScoreDto(1, null, null, null, null, 2, null, "Not Available")));
        CategoryHealthScoreDto categoryHealthScoreDto2 = new CategoryHealthScoreDto(1, "Category 1", 5.0, 5, of(new IndicatorScoreDto(1, null, null, null, null, 5, null, "Not Available")));
        CountryHealthScoreDto countryHealthScoreDtoIN = new CountryHealthScoreDto("IND", "India", "IN", of(categoryHealthScoreDto1, categoryHealthScoreDto2), 4, "");
        CategoryHealthScoreDto categoryHealthScoreDto4 = new CategoryHealthScoreDto(2, "Category 2", -1.0, -1, of(new IndicatorScoreDto(1, null, null, null, null, null, null, "Not Available")));
        CategoryHealthScoreDto categoryHealthScoreDto5 = new CategoryHealthScoreDto(1, "Category 1", 2.0, 2, of(new IndicatorScoreDto(1, null, null, null, null, 2, null, "Not Available")));

        GlobalHealthScoreDto globalHealthScore = GlobalHealthScoreDto.builder().overAllScore(3).categories(asList(categoryHealthScoreDto1, categoryHealthScoreDto2, categoryHealthScoreDto4, categoryHealthScoreDto5)).build();
        when(countryHealthIndicatorService.getGlobalHealthIndicator(null, null, region, en, year)).thenReturn(globalHealthScore);
        when(countryHealthIndicatorService.fetchCountryHealthScore("IND", en, year)).thenReturn(countryHealthScoreDtoIN);
        when(defaultYearDataService.fetchDefaultYear()).thenReturn(year);

        YearOnYearDto actual = bffService.fetchYearOnYearData((Collections.singletonList(getCurrentYear())), "IND", region);
        YearHealthScoreDto data = YearHealthScoreDto.builder().country(countryHealthScoreDtoIN).average(globalHealthScore).build();
        List<YearScoreDto> yearScoreDtos = Collections.singletonList(YearScoreDto.builder().year(year).data(data).build());
        YearOnYearDto expected = YearOnYearDto.builder().currentYear(year).defaultYear(year).yearOnYearData(yearScoreDtos).build();
        assertEquals(expected, actual);
    }

    @Test
    public void shouldReturnTrueWhenCountryURLGenerationStatusIsDraft() {
        CountryUrlGenerationStatusDto countryUrlGenerationStatusDto = new CountryUrlGenerationStatusDto("IND", false, FormStatus.DRAFT);
        Boolean actual = bffService.canSubmitDataForCountry(countryUrlGenerationStatusDto);

        assertEquals(true, actual);
    }

    @Test
    public void shouldReturnTrueWhenCountryURLGenerationStatusIsNEW() {
        CountryUrlGenerationStatusDto countryUrlGenerationStatusDto = new CountryUrlGenerationStatusDto("IND", false, FormStatus.NEW);
        Boolean actual = bffService.canSubmitDataForCountry(countryUrlGenerationStatusDto);

        assertEquals(true, actual);
    }

    @Test
    public void shouldReturnTrueWhenCountryURLGenerationStatusIsNullAndSuccessIsTrue() {
        CountryUrlGenerationStatusDto countryUrlGenerationStatusDto = new CountryUrlGenerationStatusDto("IND", true, null);
        Boolean actual = bffService.canSubmitDataForCountry(countryUrlGenerationStatusDto);

        assertEquals(true, actual);
    }

    @Test
    public void shouldReturnFalseWhenCountryURLGenerationStatusIsReviewPending() {
        CountryUrlGenerationStatusDto countryUrlGenerationStatusDto = new CountryUrlGenerationStatusDto("IND", false, FormStatus.REVIEW_PENDING);
        Boolean actual = bffService.canSubmitDataForCountry(countryUrlGenerationStatusDto);

        assertEquals(false, actual);
    }

    @Test
    public void shouldReturnFalseWhenCountryURLGenerationStatusIsPublished() {
        CountryUrlGenerationStatusDto countryUrlGenerationStatusDto = new CountryUrlGenerationStatusDto("IND", false, FormStatus.PUBLISHED);
        Boolean actual = bffService.canSubmitDataForCountry(countryUrlGenerationStatusDto);

        assertEquals(false, actual);
    }

    @Test
    public void shouldSubmitCountryCSVDataWhenQuestionnaireIsGiven() {
        String countryId = "IND";
        String countryName = "India";
        UUID countryUUID = UUID.randomUUID();
        List<String> resourceLinks = asList("Res 1");
        String status = DRAFT.name();
        CountrySummaryDto countrySummaryDetailDto = CountrySummaryDto.builder()
                .summary("Summary 1")
                .dataFeederEmail("feeder@email.com")
                .dataFeederName("feeder")
                .dataFeederRole("feeder role")
                .contactEmail("contact@test.com")
                .contactDesignation("some designation")
                .contactName("some contact name")
                .contactOrganization("contact org")
                .dataApproverEmail("approver@email.com")
                .dataApproverName("Some approver name")
                .dataApproverRole("some approver role")
                .govtApproved(true)
                .countryId(countryId)
                .countryName(countryName)
                .countryAlpha2Code("IN")
                .resources(resourceLinks)
                .build();
        List<HealthIndicatorDto> healthIndicatorDtos = getHealthIndicatorDto(1, "some text");
        GdhiQuestionnaire gdhiQuestionnaire = GdhiQuestionnaire.builder().countryId(countryId)
                .countrySummary(countrySummaryDetailDto)
                .healthIndicators(healthIndicatorDtos).build();

        Country country = new Country(countryId, countryName, countryUUID, "IN");
        CountryUrlGenerationStatusDto countryUrlGenerationStatusDto = new CountryUrlGenerationStatusDto(countryId, false, DRAFT);
        GdhiQuestionnaire gdhiQuestionnaire1 = GdhiQuestionnaire.builder().countryId(countryId).status(status).currentYear(getCurrentYear()).dataAvailableForYear("2022").
                countrySummary(countrySummaryDetailDto).healthIndicators(healthIndicatorDtos).updatedDate("").build();

        when(iCountryRepository.findByName(countryName)).thenReturn(country);
        when(countryService.fetchTheYearToPrefillData(countryUUID)).thenReturn("2022");
        when(countryHealthDataService.saveNewCountrySummary(countryUUID)).thenReturn(countryUrlGenerationStatusDto);
        when(countryHealthDataService.validateRequiredFields(gdhiQuestionnaire1)).thenReturn(true);

        List<GdhiQuestionnaire> gdhiQuestionnaireList = new ArrayList<>();
        gdhiQuestionnaireList.add(gdhiQuestionnaire);
        GdhiQuestionnaires gdhiQuestionnaires = new GdhiQuestionnaires(gdhiQuestionnaireList);

        CountryStatuses actual = bffService.submitCountryCSVData(gdhiQuestionnaires);

        CountryStatuses expected = new CountryStatuses();
        expected.add(countryName, true, REVIEW_PENDING, "");

        assertEquals(expected, actual);
    }

    @Test
    public void shouldNotSubmitCountryCSVDataWhenQuestionnaireForCountryIsAlreadyPublished() {
        String countryId = "IND";
        String countryName = "India";
        UUID countryUUID = UUID.randomUUID();
        List<String> resourceLinks = asList("Res 1");
        String status = PUBLISHED.name();
        CountrySummaryDto countrySummaryDetailDto = CountrySummaryDto.builder()
                .summary("Summary 1")
                .dataFeederEmail("feeder@email.com")
                .dataFeederName("feeder")
                .dataFeederRole("feeder role")
                .contactEmail("contact@test.com")
                .contactDesignation("some designation")
                .contactName("some contact name")
                .contactOrganization("contact org")
                .dataApproverEmail("approver@email.com")
                .dataApproverName("Some approver name")
                .dataApproverRole("some approver role")
                .govtApproved(true)
                .countryId(countryId)
                .countryName(countryName)
                .countryAlpha2Code("IN")
                .resources(resourceLinks)
                .build();
        List<HealthIndicatorDto> healthIndicatorDtos = getHealthIndicatorDto(1, "some text");
        GdhiQuestionnaire gdhiQuestionnaire = GdhiQuestionnaire.builder().countryId(countryId)
                .countrySummary(countrySummaryDetailDto)
                .healthIndicators(healthIndicatorDtos).build();

        Country country = new Country(countryId, countryName, countryUUID, "IN");
        CountryUrlGenerationStatusDto countryUrlGenerationStatusDto = new CountryUrlGenerationStatusDto(countryId, false, PUBLISHED);

        when(iCountryRepository.findByName(countryName)).thenReturn(country);
        when(countryHealthDataService.saveNewCountrySummary(countryUUID)).thenReturn(countryUrlGenerationStatusDto);

        List<GdhiQuestionnaire> gdhiQuestionnaireList = new ArrayList<>();
        gdhiQuestionnaireList.add(gdhiQuestionnaire);
        GdhiQuestionnaires gdhiQuestionnaires = new GdhiQuestionnaires(gdhiQuestionnaireList);

        CountryStatuses actual = bffService.submitCountryCSVData(gdhiQuestionnaires);

        CountryStatuses expected = new CountryStatuses();
        expected.add(countryName, false, PUBLISHED, "Country is already in " + PUBLISHED + " state");

        assertEquals(expected, actual);
    }

    @Test
    public void shouldNotSubmitCountryCSVDataWhenQuestionnaireForCountryIsInReviewPending() {
        String countryId = "IND";
        String countryName = "India";
        UUID countryUUID = UUID.randomUUID();
        List<String> resourceLinks = asList("Res 1");
        String status = REVIEW_PENDING.name();
        CountrySummaryDto countrySummaryDetailDto = CountrySummaryDto.builder()
                .summary("Summary 1")
                .dataFeederEmail("feeder@email.com")
                .dataFeederName("feeder")
                .dataFeederRole("feeder role")
                .contactEmail("contact@test.com")
                .contactDesignation("some designation")
                .contactName("some contact name")
                .contactOrganization("contact org")
                .dataApproverEmail("approver@email.com")
                .dataApproverName("Some approver name")
                .dataApproverRole("some approver role")
                .govtApproved(true)
                .countryId(countryId)
                .countryName(countryName)
                .countryAlpha2Code("IN")
                .resources(resourceLinks)
                .build();
        List<HealthIndicatorDto> healthIndicatorDtos = getHealthIndicatorDto(1, "some text");
        GdhiQuestionnaire gdhiQuestionnaire = GdhiQuestionnaire.builder().countryId(countryId)
                .countrySummary(countrySummaryDetailDto)
                .healthIndicators(healthIndicatorDtos).build();

        Country country = new Country(countryId, countryName, countryUUID, "IN");
        CountryUrlGenerationStatusDto countryUrlGenerationStatusDto = new CountryUrlGenerationStatusDto(countryId, false, REVIEW_PENDING);

        when(iCountryRepository.findByName(countryName)).thenReturn(country);
        when(countryHealthDataService.saveNewCountrySummary(countryUUID)).thenReturn(countryUrlGenerationStatusDto);

        List<GdhiQuestionnaire> gdhiQuestionnaireList = new ArrayList<>();
        gdhiQuestionnaireList.add(gdhiQuestionnaire);
        GdhiQuestionnaires gdhiQuestionnaires = new GdhiQuestionnaires(gdhiQuestionnaireList);

        CountryStatuses actual = bffService.submitCountryCSVData(gdhiQuestionnaires);

        CountryStatuses expected = new CountryStatuses();
        expected.add(countryName, false, REVIEW_PENDING, "Country is already in " + REVIEW_PENDING + " state");

        assertEquals(expected, actual);
    }

    @Test
    public void shouldSubmitCountryCSVDataWhenQuestionnaireIsGivenAndStatusIsNew() {
        String countryId = "IND";
        String countryName = "India";
        UUID countryUUID = UUID.randomUUID();
        List<String> resourceLinks = asList("Res 1");
        String status = NEW.name();
        CountrySummaryDto countrySummaryDetailDto = CountrySummaryDto.builder()
                .summary("Summary 1")
                .dataFeederEmail("feeder@email.com")
                .dataFeederName("feeder")
                .dataFeederRole("feeder role")
                .contactEmail("contact@test.com")
                .contactDesignation("some designation")
                .contactName("some contact name")
                .contactOrganization("contact org")
                .dataApproverEmail("approver@email.com")
                .dataApproverName("Some approver name")
                .dataApproverRole("some approver role")
                .govtApproved(true)
                .countryId(countryId)
                .countryName(countryName)
                .countryAlpha2Code("IN")
                .resources(resourceLinks)
                .build();
        List<HealthIndicatorDto> healthIndicatorDtos = getHealthIndicatorDto(1, "some text");
        GdhiQuestionnaire gdhiQuestionnaire = GdhiQuestionnaire.builder().countryId(countryId)
                .countrySummary(countrySummaryDetailDto)
                .healthIndicators(healthIndicatorDtos).build();

        Country country = new Country(countryId, countryName, countryUUID, "IN");
        CountryUrlGenerationStatusDto countryUrlGenerationStatusDto = new CountryUrlGenerationStatusDto(countryId, false, NEW);
        GdhiQuestionnaire gdhiQuestionnaire1 = GdhiQuestionnaire.builder().countryId(countryId).status(status).currentYear(getCurrentYear()).dataAvailableForYear("2022").
                countrySummary(countrySummaryDetailDto).healthIndicators(healthIndicatorDtos).updatedDate("").build();

        when(iCountryRepository.findByName(countryName)).thenReturn(country);
        when(countryService.fetchTheYearToPrefillData(countryUUID)).thenReturn("2022");
        when(countryHealthDataService.saveNewCountrySummary(countryUUID)).thenReturn(countryUrlGenerationStatusDto);
        when(countryHealthDataService.validateRequiredFields(gdhiQuestionnaire1)).thenReturn(true);

        List<GdhiQuestionnaire> gdhiQuestionnaireList = new ArrayList<>();
        gdhiQuestionnaireList.add(gdhiQuestionnaire);
        GdhiQuestionnaires gdhiQuestionnaires = new GdhiQuestionnaires(gdhiQuestionnaireList);

        CountryStatuses actual = bffService.submitCountryCSVData(gdhiQuestionnaires);

        CountryStatuses expected = new CountryStatuses();
        expected.add(countryName, true, REVIEW_PENDING, "");

        assertEquals(expected, actual);
    }

    @Test
    public void shouldSubmitCountryCSVDataWhenQuestionnaireIsGivenAndStatusIsNull() {
        String countryId = "IND";
        String countryName = "India";
        UUID countryUUID = UUID.randomUUID();
        List<String> resourceLinks = asList("Res 1");
        String status = null;
        CountrySummaryDto countrySummaryDetailDto = CountrySummaryDto.builder()
                .summary("Summary 1")
                .dataFeederEmail("feeder@email.com")
                .dataFeederName("feeder")
                .dataFeederRole("feeder role")
                .contactEmail("contact@test.com")
                .contactDesignation("some designation")
                .contactName("some contact name")
                .contactOrganization("contact org")
                .dataApproverEmail("approver@email.com")
                .dataApproverName("Some approver name")
                .dataApproverRole("some approver role")
                .govtApproved(true)
                .countryId(countryId)
                .countryName(countryName)
                .countryAlpha2Code("IN")
                .resources(resourceLinks)
                .build();
        List<HealthIndicatorDto> healthIndicatorDtos = getHealthIndicatorDto(1, "some text");
        GdhiQuestionnaire gdhiQuestionnaire = GdhiQuestionnaire.builder().countryId(countryId)
                .countrySummary(countrySummaryDetailDto)
                .healthIndicators(healthIndicatorDtos).build();

        Country country = new Country(countryId, countryName, countryUUID, "IN");
        CountryUrlGenerationStatusDto countryUrlGenerationStatusDto = new CountryUrlGenerationStatusDto(countryId, true, null);
        GdhiQuestionnaire gdhiQuestionnaire1 = GdhiQuestionnaire.builder().countryId(countryId).status(NEW.name()).currentYear(getCurrentYear()).dataAvailableForYear("2022").
                countrySummary(countrySummaryDetailDto).healthIndicators(healthIndicatorDtos).updatedDate("").build();

        when(iCountryRepository.findByName(countryName)).thenReturn(country);
        when(countryService.fetchTheYearToPrefillData(countryUUID)).thenReturn("2022");
        when(countryHealthDataService.saveNewCountrySummary(countryUUID)).thenReturn(countryUrlGenerationStatusDto);
        when(countryHealthDataService.validateRequiredFields(gdhiQuestionnaire1)).thenReturn(true);

        List<GdhiQuestionnaire> gdhiQuestionnaireList = new ArrayList<>();
        gdhiQuestionnaireList.add(gdhiQuestionnaire);
        GdhiQuestionnaires gdhiQuestionnaires = new GdhiQuestionnaires(gdhiQuestionnaireList);

        CountryStatuses actual = bffService.submitCountryCSVData(gdhiQuestionnaires);

        CountryStatuses expected = new CountryStatuses();
        expected.add(countryName, true, REVIEW_PENDING, "");

        assertEquals(expected, actual);
    }

    @Test
    public void shouldConstructGDHIQuestionnaireGivenQuestionnaireAndGenerateURLDto() {
        String countryId = "IND";
        String countryName = "India";
        UUID countryUUID = UUID.randomUUID();
        List<String> resourceLinks = asList("Res 1");
        String status = DRAFT.name();
        CountrySummaryDto countrySummaryDetailDto = CountrySummaryDto.builder()
                .summary("Summary 1")
                .dataFeederEmail("feeder@email.com")
                .dataFeederName("feeder")
                .dataFeederRole("feeder role")
                .contactEmail("contact@test.com")
                .contactDesignation("some designation")
                .contactName("some contact name")
                .contactOrganization("contact org")
                .dataApproverEmail("approver@email.com")
                .dataApproverName("Some approver name")
                .dataApproverRole("some approver role")
                .govtApproved(true)
                .countryId(countryId)
                .countryName(countryName)
                .countryAlpha2Code("IN")
                .resources(resourceLinks)
                .build();
        List<HealthIndicatorDto> healthIndicatorDtos = getHealthIndicatorDto(1, "some text");
        GdhiQuestionnaire gdhiQuestionnaire = GdhiQuestionnaire.builder().countryId(countryId)
                .countrySummary(countrySummaryDetailDto)
                .healthIndicators(healthIndicatorDtos).build();

        Country country = new Country(countryId, countryName, countryUUID, "IN");
        CountryUrlGenerationStatusDto countryUrlGenerationStatusDto = new CountryUrlGenerationStatusDto(countryId, false, DRAFT);

        when(countryService.fetchTheYearToPrefillData(countryUUID)).thenReturn("2022");

        GdhiQuestionnaire expected = bffService.constructGdhiQuestionnaire(gdhiQuestionnaire, countryUrlGenerationStatusDto, country);
        GdhiQuestionnaire actual = GdhiQuestionnaire.builder().countryId(countryId).status(status).currentYear(getCurrentYear()).dataAvailableForYear("2022").
                countrySummary(countrySummaryDetailDto).healthIndicators(healthIndicatorDtos).updatedDate("").build();

        assertEquals(expected, actual);
    }

    private List<HealthIndicatorDto> getHealthIndicatorDto(Integer score, String supportText) {
        List<HealthIndicatorDto> healthIndicatorDtoList = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            healthIndicatorDtoList.add(new HealthIndicatorDto(1, 1, "DRAFT", score, supportText));
        }
        return healthIndicatorDtoList;
    }

}
