package it.gdhi.service;

import it.gdhi.dto.*;
import it.gdhi.model.*;
import it.gdhi.model.id.CountrySummaryId;
import it.gdhi.repository.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import javax.persistence.EntityManager;

import java.util.*;

import static it.gdhi.utils.FormStatus.*;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static it.gdhi.utils.Util.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class CountryHealthDataServiceTest {

    @InjectMocks
    CountryHealthDataService countryHealthDataService;
    @Mock
    ICountrySummaryRepository iCountrySummaryRepository;
    @Mock
    ICountryResourceLinkRepository iCountryResourceLinkRepository;
    @Mock
    ICountryHealthIndicatorRepository iCountryHealthIndicatorRepository;

    @Mock
    IRegionCountryRepository iRegionCountryRepository;

    @Mock
    IRegionalIndicatorDataRepository iRegionalIndicatorDataRepository;

    @Mock
    IRegionalCategoryDataRepository iRegionalCategoryDataRepository;

    @Mock
    IRegionalOverallDataRepository iRegionalOverallRepository;

    @Mock
    MailerService mailerService;
    @Mock
    ICountryRepository countryDetailRepository;
    @Mock
    EntityManager entityManager;
    @Mock
    ICountryPhaseRepository iCountryPhaseRepository;

    @Mock
    CategoryIndicatorService categoryIndicatorService;

    @Mock
    RegionService regionService;

    @Test
    public void shouldPublishDetailsForACountryForCurrentYear() throws Exception {
        List<String> resourceLinks = Collections.singletonList("Res 1");
        CountrySummaryDto countrySummaryDetailDto = CountrySummaryDto.builder().summary("Summary 1")
                .resources(resourceLinks).build();

        String status = PUBLISHED.name();
        String currentYear = getCurrentYear();
        List<HealthIndicatorDto> healthIndicatorDtos = Collections.singletonList(new HealthIndicatorDto(1, 1, status, 2, "Text"));
        String countryId = "ARG";
        GdhiQuestionnaire gdhiQuestionnaire = GdhiQuestionnaire.builder().countryId(countryId)
                .countrySummary(countrySummaryDetailDto)
                .healthIndicators(healthIndicatorDtos).build();

        CountrySummaryId countrySummaryId = CountrySummaryId.builder().countryId(countryId).year(currentYear).build();
        CountrySummary countrySummary = CountrySummary.builder().summary("Summary 1").countrySummaryId(countrySummaryId).status(status).build();

        Indicator indicator1 = Indicator.builder().indicatorId(1).parentId(null).build();
        Indicator indicator2 = Indicator.builder().indicatorId(1).parentId(null).build();
        CountryHealthIndicator countryHealthIndicator1 = CountryHealthIndicator.builder()
                .indicator(indicator1)
                .score(3)
                .category(Category.builder().id(1).indicators(asList(indicator1, indicator2)).build())
                .build();
        CountryHealthIndicator countryHealthIndicator2 = CountryHealthIndicator.builder()
                .indicator(indicator2)
                .score(-1)
                .category(Category.builder().id(1).indicators(asList(indicator1, indicator2)).build())
                .build();
        RegionCountryId regionCountryId = RegionCountryId.builder().countryId("ARG").regionId("PAHO").build();
        RegionCountry regionCountry = RegionCountry.builder().regionCountryId(regionCountryId).build();

        List<String> countries = asList("ARG");
        String region = "PAHO";

        when(iCountryHealthIndicatorRepository.findByCountryHealthIndicatorIdCountryIdAndCountryHealthIndicatorIdYearAndStatus(countryId, currentYear, status))
                .thenReturn(asList(countryHealthIndicator1, countryHealthIndicator2));
        when(iCountrySummaryRepository.findByCountrySummaryIdCountryIdAndCountrySummaryIdYearAndStatusNot(countryId, currentYear, PUBLISHED.name())).thenReturn(countrySummary);
        when(iRegionCountryRepository.findByRegionCountryIdCountryId(countryId)).thenReturn(regionCountry);
        when(iRegionCountryRepository.findByRegionCountryIdRegionId(region)).thenReturn(countries);
        when(iCountryHealthIndicatorRepository.findByCountryHealthIndicatorIdCountryIdInAndCountryHealthIndicatorIdYearAndStatus(countries, currentYear, PUBLISHED.name())).thenReturn(asList(countryHealthIndicator1, countryHealthIndicator2));

        countryHealthDataService.publish(gdhiQuestionnaire, currentYear);
        ArgumentCaptor<CountrySummary> summaryCaptor = ArgumentCaptor.forClass(CountrySummary.class);
        ArgumentCaptor<CountryHealthIndicator> healthIndicatorsCaptorList = ArgumentCaptor.forClass(CountryHealthIndicator.class);

        InOrder inOrder = inOrder(iCountryResourceLinkRepository, iCountrySummaryRepository, iCountryHealthIndicatorRepository, iCountryPhaseRepository);
        inOrder.verify(iCountryResourceLinkRepository).deleteByCountryResourceLinkIdCountryIdAndCountryResourceLinkIdYearAndStatus(countryId, currentYear, status);
        inOrder.verify(iCountrySummaryRepository).save(summaryCaptor.capture());
        inOrder.verify(iCountryHealthIndicatorRepository).save(healthIndicatorsCaptorList.capture());
        CountrySummary summaryCaptorValue = summaryCaptor.getValue();
        assertThat(summaryCaptorValue.getCountrySummaryId().getCountryId(), is(countryId));
        assertThat(summaryCaptorValue.getSummary(), is("Summary 1"));
        assertThat(summaryCaptorValue.getCountryResourceLinks().get(0).getLink(), is("Res 1"));
        assertThat(summaryCaptorValue.getStatus(), is("PUBLISHED"));
        assertThat(healthIndicatorsCaptorList.getValue().getCountryHealthIndicatorId().getCategoryId(), is(1));
        ArgumentCaptor<CountryPhase> phaseDetailsCaptor = ArgumentCaptor.forClass(CountryPhase.class);

        inOrder.verify(iCountryPhaseRepository).save(phaseDetailsCaptor.capture());
        assertThat(phaseDetailsCaptor.getValue().getCountryOverallPhase(), is(3));
        assertThat(phaseDetailsCaptor.getValue().getCountryPhaseId().getCountryId(), is(countryId));

    }

    @Test
    public void shouldSaveAsNewStatusWhenCountryDoesNotHavePublishedDataForCurrentYear() throws Exception {
        String countryId = "ARG";
        UUID countryUUID = UUID.randomUUID();
        ArgumentCaptor<CountrySummary> summaryCaptor = ArgumentCaptor.forClass(CountrySummary.class);
        Country country = new Country(countryId, "Argentina", countryUUID, "AR");

        when(countryDetailRepository.findByUniqueId(countryUUID)).thenReturn(country);
        when(iCountrySummaryRepository.findByCountrySummaryIdCountryIdAndCountrySummaryIdYear(anyString(), anyString())).thenReturn(emptyList());
        CountryUrlGenerationStatusDto countryUrlGenerationStatusDto = countryHealthDataService
                .saveNewCountrySummary(countryUUID);
        assert (countryUrlGenerationStatusDto.isSuccess());
        assertNull(countryUrlGenerationStatusDto.getExistingStatus());

        verify(iCountrySummaryRepository).save(summaryCaptor.capture());
        CountrySummary summaryCaptorValue = summaryCaptor.getValue();
        assertEquals(countryId, summaryCaptorValue.getCountrySummaryId().getCountryId());
        assertEquals(NEW.toString(), summaryCaptorValue.getStatus());
    }

    @Test
    public void shouldSendEmailOnSuccessfulSubmit() throws Exception {
        String countryId = "ARG";
        Country country = new Country(countryId, "Argentina", UUID.randomUUID(), "AR");
        List<String> resourceLinks = asList("Res 1");
        String feeder = "feeder";
        String feederRole = "feeder role";
        String contactEmail = "contact@test.com";
        String currentYear = getCurrentYear();
        CountrySummaryDto countrySummaryDetailDto = CountrySummaryDto.builder().summary("Summary 1")
                .dataFeederName(feeder)
                .dataFeederRole(feederRole)
                .contactEmail(contactEmail)
                .resources(resourceLinks).build();

        List<HealthIndicatorDto> healthIndicatorDtos = asList(new HealthIndicatorDto(1, 1, "PUBLISHED", 2, "Text"));
        GdhiQuestionnaire gdhiQuestionnaire = GdhiQuestionnaire.builder().countryId(countryId)
                .countrySummary(countrySummaryDetailDto)
                .healthIndicators(healthIndicatorDtos).build();

        CountrySummaryId countrySummaryId = CountrySummaryId.builder().countryId(countryId).year(currentYear).build();
        CountrySummary countrySummary = CountrySummary.builder().summary("Summary 1").countrySummaryId(countrySummaryId).status(DRAFT.name()).build();
        CountrySummaryId countrySummaryId2 = CountrySummaryId.builder().countryId(countryId).year(currentYear).build();
        CountrySummary countrySummary2 = CountrySummary.builder().summary("Summary 1").countrySummaryId(countrySummaryId2).status(REVIEW_PENDING.name()).build();

        when(iCountrySummaryRepository.save(any(CountrySummary.class))).thenReturn(countrySummary2);
        when(iCountryHealthIndicatorRepository.save(any(CountryHealthIndicator.class))).thenReturn(CountryHealthIndicator.builder().build());
        when(countryDetailRepository.findById(countryId)).thenReturn(country);
        when(iCountrySummaryRepository.findByCountrySummaryIdCountryIdAndCountrySummaryIdYearAndStatusNot(countryId, currentYear, PUBLISHED.name())).thenReturn(countrySummary);
        countryHealthDataService.submit(gdhiQuestionnaire);

        verify(mailerService).send(country, feeder, feederRole, contactEmail);
        assertEquals(REVIEW_PENDING.name(), countrySummary2.getStatus());
    }

    @Test
    public void shouldNotSaveAsNewStatusWhenCountryHasPublishedDataForCurrentYear() throws Exception {
        String countryId = "ARG";
        String currentYear = getCurrentYear();
        UUID countryUUID = UUID.randomUUID();
        ArgumentCaptor<CountrySummary> summaryCaptor = ArgumentCaptor.forClass(CountrySummary.class);
        Country country = new Country(countryId, "Argentina", countryUUID, "AR");
        CountrySummaryId countrySummaryId = CountrySummaryId.builder().countryId(countryId).year(currentYear).build();
        CountrySummary countrySummary = CountrySummary.builder().countrySummaryId(countrySummaryId).status(PUBLISHED.name()).build();

        when(countryDetailRepository.findByUniqueId(countryUUID)).thenReturn(country);

        when(iCountrySummaryRepository.findByCountrySummaryIdCountryIdAndCountrySummaryIdYear(anyString(), anyString())).thenReturn(asList(countrySummary));
        CountryUrlGenerationStatusDto countryUrlGenerationStatusDto = countryHealthDataService
                .saveNewCountrySummary(countryUUID);
        assert (!countryUrlGenerationStatusDto.isSuccess());
        assertEquals(PUBLISHED, countryUrlGenerationStatusDto.getExistingStatus());

        verify(iCountrySummaryRepository, times(0)).save(summaryCaptor.capture());
    }

    @Test
    public void shouldNotSaveNewCountrySummaryWhenItAlreadyHasUnpublishedDataForCurrentYear() throws Exception {
        String countryId = "ARG";
        String currentYear = getCurrentYear();
        UUID countryUUID = UUID.randomUUID();
        Country country = new Country(countryId, "Argentina", countryUUID, "AR");
        CountrySummaryId countrySummaryId = CountrySummaryId.builder().countryId(countryId).year(currentYear).build();
        CountrySummary countrySummary = CountrySummary.builder().countrySummaryId(countrySummaryId).status(NEW.name()).build();


        when(countryDetailRepository.findByUniqueId(countryUUID)).thenReturn(country);

        when(iCountrySummaryRepository.findByCountrySummaryIdCountryIdAndCountrySummaryIdYear(anyString(), anyString())).thenReturn(asList(countrySummary));

        CountryUrlGenerationStatusDto countryUrlGenerationStatusDto = countryHealthDataService
                .saveNewCountrySummary(countryUUID);
        assertFalse(countryUrlGenerationStatusDto.isSuccess());
        assertEquals(NEW, countryUrlGenerationStatusDto.getExistingStatus());

        verify(iCountrySummaryRepository, never()).save(any());
    }

    @Test
    public void shouldNotSaveNewCountrySummaryWhenItAlreadyHaveDraftDataForCurrentYear() throws Exception {
        String countryId = "ARG";
        UUID countryUUID = UUID.randomUUID();
        String currentYear = getCurrentYear();
        Country country = new Country(countryId, "Argentina", countryUUID, "AR");
        CountrySummaryId countrySummaryId = CountrySummaryId.builder().countryId(countryId).year(currentYear).build();
        CountrySummary countrySummary = CountrySummary.builder().countrySummaryId(countrySummaryId).status(DRAFT.name()).build();

        when(countryDetailRepository.findByUniqueId(countryUUID)).thenReturn(country);

        when(iCountrySummaryRepository.findByCountrySummaryIdCountryIdAndCountrySummaryIdYear(anyString(), anyString()))
                .thenReturn(asList(countrySummary));

        CountryUrlGenerationStatusDto countryUrlGenerationStatusDto = countryHealthDataService
                .saveNewCountrySummary(countryUUID);
        assertFalse(countryUrlGenerationStatusDto.isSuccess());
        assertEquals(DRAFT, countryUrlGenerationStatusDto.getExistingStatus());

        verify(iCountrySummaryRepository, never()).save(any());
    }

    @Test
    public void shouldNotSaveNewCountrySummaryWhenItAlreadyHasNewDataForCurrentYear() throws Exception {
        String countryId = "ARG";
        UUID countryUUID = UUID.randomUUID();
        String currentYear = getCurrentYear();
        Country country = new Country(countryId, "Argentina", countryUUID, "AR");
        CountrySummaryId countrySummaryId = CountrySummaryId.builder().countryId(countryId).year(currentYear).build();
        CountrySummary countrySummary = CountrySummary.builder().countrySummaryId(countrySummaryId).status(NEW.name()).build();

        when(countryDetailRepository.findByUniqueId(countryUUID)).thenReturn(country);

        when(iCountrySummaryRepository.findByCountrySummaryIdCountryIdAndCountrySummaryIdYear(anyString(), anyString()))
                .thenReturn(asList(countrySummary));

        CountryUrlGenerationStatusDto countryUrlGenerationStatusDto = countryHealthDataService
                .saveNewCountrySummary(countryUUID);
        assertFalse(countryUrlGenerationStatusDto.isSuccess());
        assertEquals(NEW, countryUrlGenerationStatusDto.getExistingStatus());

        verify(iCountrySummaryRepository, never()).save(any());
    }
    @Test
    public void shouldDeleteCountryDataForGivenYear() {
        String countryId = "AFG";
        String status = REVIEW_PENDING.name();
        UUID countryUUID = UUID.randomUUID();
        String year = "2020";
        Country country = new Country(countryId, "Argentina", countryUUID, "AR");
        when(countryDetailRepository.findByUniqueId(countryUUID)).thenReturn(country);

        countryHealthDataService.deleteCountryData(countryUUID, year);

        verify(iCountrySummaryRepository).deleteByCountrySummaryIdCountryIdAndCountrySummaryIdYearAndStatus(countryId, year, status);
        verify(iCountryHealthIndicatorRepository).deleteByCountryHealthIndicatorIdCountryIdAndCountryHealthIndicatorIdYearAndStatus(countryId, year, status);
        verify(iCountryResourceLinkRepository).deleteByCountryResourceLinkIdCountryIdAndCountryResourceLinkIdYearAndStatus(countryId, year, status);
    }

    @Test
    public void shouldGetAdminViewFormDetailsForCurrentYear() {
        CountrySummary countrySummaryIND = getCountrySummary("IND", "PUBLISHED", "INDIA",
                "IN", "Contact Name 1", "con1@gdhi.com", "Version1");
        CountrySummary countrySummaryARG = getCountrySummary("ARG", "REVIEW_PENDING",
                "ARGENTINA", "AR", "Contact Name 1", "con1@gdhi.com", "2023");
        CountrySummary countrySummaryALG = getCountrySummary("ALG", "NEW", "ALGERIA",
                "AL", "Contact Name 2", "con2@gdhi.com", "Version1");
        CountrySummary countrySummaryINDNEW = getCountrySummary("IND", "NEW", "INDIA",
                "IN", "Contact Name 1", "con1@gdhi.com", "2023");

        when(iCountrySummaryRepository.findByCountrySummaryIdYearOrderByUpdatedAtDesc("2023")).thenReturn(asList(countrySummaryARG,
                countrySummaryINDNEW));
        CountrySummaryStatusYearDto adminViewFormDetails = countryHealthDataService.getAllCountryStatusSummaries();
        assertEquals(adminViewFormDetails.getNewStatus().size(), 1);
        assertEquals(adminViewFormDetails.getReviewPendingStatus().size(), 1);

        CountrySummaryStatusDto countrySummaryStatusDto = adminViewFormDetails.getReviewPendingStatus().stream().findFirst().get();

        assertEquals(countrySummaryStatusDto.getContactName(), "Contact Name 1");
        assertEquals(countrySummaryStatusDto.getContactEmail(), "con1@gdhi.com");
        assertEquals(countrySummaryStatusDto.getCountryName(), "ARGENTINA");
    }

    @Test
    public void shouldReturnTrueWhenGovtApprovedIsTrueAndAllFieldsAreValid() {
        String countryId = "ARG";
        String countryName = "Argentina";
        List<String> resourceLinks = asList("Res 1");
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
                .resources(resourceLinks)
                .build();
        List<HealthIndicatorDto> healthIndicatorDtos = getHealthIndicatorDto(1, "some text");
        when(categoryIndicatorService.getHealthIndicatorCount()).thenReturn(30);
        GdhiQuestionnaire gdhiQuestionnaire = GdhiQuestionnaire.builder().countryId(countryId)
                .countrySummary(countrySummaryDetailDto)
                .healthIndicators(healthIndicatorDtos).build();

        assertTrue(countryHealthDataService.validateRequiredFields(gdhiQuestionnaire));
    }

    @Test
    public void shouldReturnFalseIfGovtApprovedIsTrueAndJustApproverEmailIsMissing() {
        String countryId = "ARG";
        String countryName = "Argentina";
        List<String> resourceLinks = asList("Res 1");
        CountrySummaryDto countrySummaryDetailDto = CountrySummaryDto.builder()
                .summary("Summary 1")
                .dataFeederEmail("feeder@email.com")
                .dataFeederName("feeder")
                .dataFeederRole("feeder role")
                .contactEmail("contact@test.com")
                .contactDesignation("some designation")
                .contactName("some contact name")
                .contactOrganization("contact org")
                .dataApproverName("Some approver name")
                .dataApproverRole("some approver role")
                .govtApproved(true)
                .countryId(countryId)
                .countryName(countryName)
                .resources(resourceLinks)
                .build();
        List<HealthIndicatorDto> healthIndicatorDtos = getHealthIndicatorDto(1, "some text");
        when(categoryIndicatorService.getHealthIndicatorCount()).thenReturn(30);
        GdhiQuestionnaire gdhiQuestionnaire = GdhiQuestionnaire.builder().countryId(countryId)
                .countrySummary(countrySummaryDetailDto)
                .healthIndicators(healthIndicatorDtos).build();

        assertFalse(countryHealthDataService.validateRequiredFields(gdhiQuestionnaire));
    }

    @Test
    public void shouldReturnFalseIfGovtApprovedIsTrueAndJustApproverRoleIsMissing() {
        String countryId = "ARG";
        String countryName = "Argentina";
        List<String> resourceLinks = asList("Res 1");
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
                .govtApproved(true)
                .countryId(countryId)
                .countryName(countryName)
                .resources(resourceLinks)
                .build();
        List<HealthIndicatorDto> healthIndicatorDtos = getHealthIndicatorDto(1, "some text");
        when(categoryIndicatorService.getHealthIndicatorCount()).thenReturn(30);
        GdhiQuestionnaire gdhiQuestionnaire = GdhiQuestionnaire.builder().countryId(countryId)
                .countrySummary(countrySummaryDetailDto)
                .healthIndicators(healthIndicatorDtos).build();

        assertFalse(countryHealthDataService.validateRequiredFields(gdhiQuestionnaire));
    }

    @Test
    public void shouldReturnFalseIfGovtApprovedIsTrueAndJustApproverNameIsMissing() {
        String countryId = "ARG";
        String countryName = "Argentina";
        List<String> resourceLinks = asList("Res 1");
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
                .dataApproverRole("some approver role")
                .govtApproved(true)
                .countryId(countryId)
                .countryName(countryName)
                .resources(resourceLinks)
                .build();
        List<HealthIndicatorDto> healthIndicatorDtos = getHealthIndicatorDto(1, "some text");
        when(categoryIndicatorService.getHealthIndicatorCount()).thenReturn(30);
        GdhiQuestionnaire gdhiQuestionnaire = GdhiQuestionnaire.builder().countryId(countryId)
                .countrySummary(countrySummaryDetailDto)
                .healthIndicators(healthIndicatorDtos).build();

        assertFalse(countryHealthDataService.validateRequiredFields(gdhiQuestionnaire));
    }

    @Test
    public void shouldReturnFalseIfGovtApprovedIsFalseAndAllFieldsArePresent() {
        String countryId = "ARG";
        String countryName = "Argentina";
        List<String> resourceLinks = asList("Res 1");
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
                .dataApproverRole("some approver role")
                .dataApproverName("Some approver name")
                .govtApproved(false)
                .countryId(countryId)
                .countryName(countryName)
                .resources(resourceLinks)
                .build();
        List<HealthIndicatorDto> healthIndicatorDtos = getHealthIndicatorDto(1, "some text");
        when(categoryIndicatorService.getHealthIndicatorCount()).thenReturn(30);
        GdhiQuestionnaire gdhiQuestionnaire = GdhiQuestionnaire.builder().countryId(countryId)
                .countrySummary(countrySummaryDetailDto)
                .healthIndicators(healthIndicatorDtos).build();

        assertFalse(countryHealthDataService.validateRequiredFields(gdhiQuestionnaire));
    }

    @Test
    public void shouldReturnFalseIfGovtApprovedIsFalseAndDataApproverRoleIsPresent() {
        String countryId = "ARG";
        String countryName = "Argentina";
        List<String> resourceLinks = asList("Res 1");
        CountrySummaryDto countrySummaryDetailDto = CountrySummaryDto.builder()
                .summary("Summary 1")
                .dataFeederEmail("feeder@email.com")
                .dataFeederName("feeder")
                .dataFeederRole("feeder role")
                .contactEmail("contact@test.com")
                .contactDesignation("some designation")
                .contactName("some contact name")
                .contactOrganization("contact org")
                .dataApproverRole("some approver role")
                .govtApproved(false)
                .countryId(countryId)
                .countryName(countryName)
                .resources(resourceLinks)
                .build();
        List<HealthIndicatorDto> healthIndicatorDtos = getHealthIndicatorDto(1, "some text");
        when(categoryIndicatorService.getHealthIndicatorCount()).thenReturn(30);
        GdhiQuestionnaire gdhiQuestionnaire = GdhiQuestionnaire.builder().countryId(countryId)
                .countrySummary(countrySummaryDetailDto)
                .healthIndicators(healthIndicatorDtos).build();

        assertFalse(countryHealthDataService.validateRequiredFields(gdhiQuestionnaire));
    }

    @Test
    public void shouldReturnFalseIfGovtApprovedIsFalseAndDataApproverEmailIsPresent() {
        String countryId = "ARG";
        String countryName = "Argentina";
        List<String> resourceLinks = asList("Res 1");
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
                .govtApproved(false)
                .countryId(countryId)
                .countryName(countryName)
                .resources(resourceLinks)
                .build();
        List<HealthIndicatorDto> healthIndicatorDtos = getHealthIndicatorDto(1, "some text");
        when(categoryIndicatorService.getHealthIndicatorCount()).thenReturn(30);
        GdhiQuestionnaire gdhiQuestionnaire = GdhiQuestionnaire.builder().countryId(countryId)
                .countrySummary(countrySummaryDetailDto)
                .healthIndicators(healthIndicatorDtos).build();

        assertFalse(countryHealthDataService.validateRequiredFields(gdhiQuestionnaire));
    }

    @Test
    public void shouldReturnFalseIfGovtApprovedIsFalseAndDataApproverNameIsPresent() {
        String countryId = "ARG";
        String countryName = "Argentina";
        List<String> resourceLinks = asList("Res 1");
        CountrySummaryDto countrySummaryDetailDto = CountrySummaryDto.builder()
                .summary("Summary 1")
                .dataFeederEmail("feeder@email.com")
                .dataFeederName("feeder")
                .dataFeederRole("feeder role")
                .contactEmail("contact@test.com")
                .contactDesignation("some designation")
                .contactName("some contact name")
                .contactOrganization("contact org")
                .dataApproverRole("some approver role")
                .govtApproved(false)
                .countryId(countryId)
                .countryName(countryName)
                .resources(resourceLinks)
                .build();
        List<HealthIndicatorDto> healthIndicatorDtos = getHealthIndicatorDto(1, "some text");
        when(categoryIndicatorService.getHealthIndicatorCount()).thenReturn(30);
        GdhiQuestionnaire gdhiQuestionnaire = GdhiQuestionnaire.builder().countryId(countryId)
                .countrySummary(countrySummaryDetailDto)
                .healthIndicators(healthIndicatorDtos).build();

        assertFalse(countryHealthDataService.validateRequiredFields(gdhiQuestionnaire));
    }

    @Test
    public void shouldReturnTrueIfGovtApprovedIsFalseAndAllDataApproverFieldsAreMissing() {
        String countryId = "ARG";
        String countryName = "Argentina";
        List<String> resourceLinks = asList("Res 1");
        CountrySummaryDto countrySummaryDetailDto = CountrySummaryDto.builder()
                .summary("Summary 1")
                .dataFeederEmail("feeder@email.com")
                .dataFeederName("feeder")
                .dataFeederRole("feeder role")
                .contactEmail("contact@test.com")
                .contactDesignation("some designation")
                .contactName("some contact name")
                .contactOrganization("contact org")
                .dataApproverName("Some approver name")
                .govtApproved(false)
                .countryId(countryId)
                .countryName(countryName)
                .resources(resourceLinks)
                .build();
        List<HealthIndicatorDto> healthIndicatorDtos = getHealthIndicatorDto(1, "some text");
        when(categoryIndicatorService.getHealthIndicatorCount()).thenReturn(30);
        GdhiQuestionnaire gdhiQuestionnaire = GdhiQuestionnaire.builder().countryId(countryId)
                .countrySummary(countrySummaryDetailDto)
                .healthIndicators(healthIndicatorDtos).build();

        assertFalse(countryHealthDataService.validateRequiredFields(gdhiQuestionnaire));
    }


    @Test
    public void shouldReturnFalseWhenGovtApprovedIsTrueAndJustApproverEmailIsMissing() {
        String countryId = "ARG";
        String countryName = "Argentina";
        List<String> resourceLinks = asList("Res 1");
        CountrySummaryDto countrySummaryDetailDto = CountrySummaryDto.builder()
                .summary("Summary 1")
                .dataFeederEmail("feeder@email.com")
                .dataFeederName("feeder")
                .dataFeederRole("feeder role")
                .contactEmail("contact@test.com")
                .contactDesignation("some designation")
                .contactName("some contact name")
                .contactOrganization("contact org")
                .dataApproverName("Some approver name")
                .dataApproverRole("some approver role")
                .govtApproved(true)
                .countryId(countryId)
                .countryName(countryName)
                .resources(resourceLinks)
                .build();
        List<HealthIndicatorDto> healthIndicatorDtos = getHealthIndicatorDto(1, "some text");
        when(categoryIndicatorService.getHealthIndicatorCount()).thenReturn(30);
        GdhiQuestionnaire gdhiQuestionnaire = GdhiQuestionnaire.builder().countryId(countryId)
                .countrySummary(countrySummaryDetailDto)
                .healthIndicators(healthIndicatorDtos).build();

        assertFalse(countryHealthDataService.validateRequiredFields(gdhiQuestionnaire));
    }

    @Test
    public void shouldReturnFalseWhenGovtApprovedIsTrueAndJustApproverRoleIsMissing() {
        String countryId = "ARG";
        String countryName = "Argentina";
        List<String> resourceLinks = asList("Res 1");
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
                .govtApproved(true)
                .countryId(countryId)
                .countryName(countryName)
                .resources(resourceLinks)
                .build();
        List<HealthIndicatorDto> healthIndicatorDtos = getHealthIndicatorDto(1, "some text");
        when(categoryIndicatorService.getHealthIndicatorCount()).thenReturn(30);
        GdhiQuestionnaire gdhiQuestionnaire = GdhiQuestionnaire.builder().countryId(countryId)
                .countrySummary(countrySummaryDetailDto)
                .healthIndicators(healthIndicatorDtos).build();

        assertFalse(countryHealthDataService.validateRequiredFields(gdhiQuestionnaire));
    }

    @Test
    public void shouldReturnFalseWhenGovtApprovedIsTrueAndJustApproverNameIsMissing() {
        String countryId = "ARG";
        String countryName = "Argentina";
        List<String> resourceLinks = asList("Res 1");
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
                .dataApproverRole("some approver role")
                .govtApproved(true)
                .countryId(countryId)
                .countryName(countryName)
                .resources(resourceLinks)
                .build();
        List<HealthIndicatorDto> healthIndicatorDtos = getHealthIndicatorDto(1, "some text");
        when(categoryIndicatorService.getHealthIndicatorCount()).thenReturn(30);
        GdhiQuestionnaire gdhiQuestionnaire = GdhiQuestionnaire.builder().countryId(countryId)
                .countrySummary(countrySummaryDetailDto)
                .healthIndicators(healthIndicatorDtos).build();

        assertFalse(countryHealthDataService.validateRequiredFields(gdhiQuestionnaire));
    }

    @Test
    public void shouldReturnFalseWhenGovtApprovedIsFalseAndAllFieldsArePresent() {
        String countryId = "ARG";
        String countryName = "Argentina";
        List<String> resourceLinks = asList("Res 1");
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
                .dataApproverRole("some approver role")
                .dataApproverName("Some approver name")
                .govtApproved(false)
                .countryId(countryId)
                .countryName(countryName)
                .resources(resourceLinks)
                .build();
        List<HealthIndicatorDto> healthIndicatorDtos = getHealthIndicatorDto(1, "some text");
        when(categoryIndicatorService.getHealthIndicatorCount()).thenReturn(30);
        GdhiQuestionnaire gdhiQuestionnaire = GdhiQuestionnaire.builder().countryId(countryId)
                .countrySummary(countrySummaryDetailDto)
                .healthIndicators(healthIndicatorDtos).build();

        assertFalse(countryHealthDataService.validateRequiredFields(gdhiQuestionnaire));
    }

    @Test
    public void shouldReturnFalseWhenGovtApprovedIsFalseAndDataApproverRoleIsPresent() {
        String countryId = "ARG";
        String countryName = "Argentina";
        List<String> resourceLinks = asList("Res 1");
        CountrySummaryDto countrySummaryDetailDto = CountrySummaryDto.builder()
                .summary("Summary 1")
                .dataFeederEmail("feeder@email.com")
                .dataFeederName("feeder")
                .dataFeederRole("feeder role")
                .contactEmail("contact@test.com")
                .contactDesignation("some designation")
                .contactName("some contact name")
                .contactOrganization("contact org")
                .dataApproverRole("some approver role")
                .govtApproved(false)
                .countryId(countryId)
                .countryName(countryName)
                .resources(resourceLinks)
                .build();
        List<HealthIndicatorDto> healthIndicatorDtos = getHealthIndicatorDto(1, "some text");
        when(categoryIndicatorService.getHealthIndicatorCount()).thenReturn(30);
        GdhiQuestionnaire gdhiQuestionnaire = GdhiQuestionnaire.builder().countryId(countryId)
                .countrySummary(countrySummaryDetailDto)
                .healthIndicators(healthIndicatorDtos).build();

        assertFalse(countryHealthDataService.validateRequiredFields(gdhiQuestionnaire));
    }

    @Test
    public void shouldReturnFalseWhenGovtApprovedIsFalseAndDataApproverEmailIsPresent() {
        String countryId = "ARG";
        String countryName = "Argentina";
        List<String> resourceLinks = asList("Res 1");
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
                .govtApproved(false)
                .countryId(countryId)
                .countryName(countryName)
                .resources(resourceLinks)
                .build();
        List<HealthIndicatorDto> healthIndicatorDtos = getHealthIndicatorDto(1, "some text");
        when(categoryIndicatorService.getHealthIndicatorCount()).thenReturn(30);
        GdhiQuestionnaire gdhiQuestionnaire = GdhiQuestionnaire.builder().countryId(countryId)
                .countrySummary(countrySummaryDetailDto)
                .healthIndicators(healthIndicatorDtos).build();

        assertFalse(countryHealthDataService.validateRequiredFields(gdhiQuestionnaire));
    }

    @Test
    public void shouldReturnFalseWhenGovtApprovedIsFalseAndDataApproverNameIsPresent() {
        String countryId = "ARG";
        String countryName = "Argentina";
        List<String> resourceLinks = asList("Res 1");
        CountrySummaryDto countrySummaryDetailDto = CountrySummaryDto.builder()
                .summary("Summary 1")
                .dataFeederEmail("feeder@email.com")
                .dataFeederName("feeder")
                .dataFeederRole("feeder role")
                .contactEmail("contact@test.com")
                .contactDesignation("some designation")
                .contactName("some contact name")
                .contactOrganization("contact org")
                .dataApproverRole("some approver role")
                .govtApproved(false)
                .countryId(countryId)
                .countryName(countryName)
                .resources(resourceLinks)
                .build();
        List<HealthIndicatorDto> healthIndicatorDtos = getHealthIndicatorDto(1, "some text");
        when(categoryIndicatorService.getHealthIndicatorCount()).thenReturn(30);
        GdhiQuestionnaire gdhiQuestionnaire = GdhiQuestionnaire.builder().countryId(countryId)
                .countrySummary(countrySummaryDetailDto)
                .healthIndicators(healthIndicatorDtos).build();

        assertFalse(countryHealthDataService.validateRequiredFields(gdhiQuestionnaire));
    }

    @Test
    public void shouldReturnTrueWhenGovtApprovedIsFalseAndAllDataApproverFieldsAreMissing() {
        String countryId = "ARG";
        String countryName = "Argentina";
        List<String> resourceLinks = asList("Res 1");
        CountrySummaryDto countrySummaryDetailDto = CountrySummaryDto.builder()
                .summary("Summary 1")
                .dataFeederEmail("feeder@email.com")
                .dataFeederName("feeder")
                .dataFeederRole("feeder role")
                .contactEmail("contact@test.com")
                .contactDesignation("some designation")
                .contactName("some contact name")
                .contactOrganization("contact org")
                .dataApproverName("Some approver name")
                .govtApproved(false)
                .countryId(countryId)
                .countryName(countryName)
                .resources(resourceLinks)
                .build();
        List<HealthIndicatorDto> healthIndicatorDtos = getHealthIndicatorDto(1, "some text");
        when(categoryIndicatorService.getHealthIndicatorCount()).thenReturn(30);
        GdhiQuestionnaire gdhiQuestionnaire = GdhiQuestionnaire.builder().countryId(countryId)
                .countrySummary(countrySummaryDetailDto)
                .healthIndicators(healthIndicatorDtos).build();

        assertFalse(countryHealthDataService.validateRequiredFields(gdhiQuestionnaire));
    }


    @Test
    public void shouldReturnFalseWhenAnyCountrySummaryFieldIsNotPresent() {
        String countryId = "IND";
        List<String> resourceLinks = asList("Res 1");
        CountrySummaryDto countrySummaryDetailDto = CountrySummaryDto.builder()
                .summary("Summary 1")
                .resources(resourceLinks)
                .build();

        List<HealthIndicatorDto> healthIndicatorDtos = getHealthIndicatorDto(1, "some text");
        when(categoryIndicatorService.getHealthIndicatorCount()).thenReturn(30);
        GdhiQuestionnaire gdhiQuestionnaire = GdhiQuestionnaire.builder().countryId(countryId)
                .countrySummary(countrySummaryDetailDto)
                .healthIndicators(healthIndicatorDtos).build();

        assertFalse(countryHealthDataService.validateRequiredFields(gdhiQuestionnaire));
    }

    @Test
    public void shouldReturnFalseIfIndicatorDataIsInvalid() {
        String countryId = "AUS";
        String countryName = "Australia";
        List<String> resourceLinks = asList("Res 1");
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
                .resources(resourceLinks)
                .build();

        List<HealthIndicatorDto> healthIndicatorDtoList = getHealthIndicatorDto(-2, "some text");
        when(categoryIndicatorService.getHealthIndicatorCount()).thenReturn(30);

        List<HealthIndicatorDto> healthIndicatorDtosWithInvalidScore = healthIndicatorDtoList;
        GdhiQuestionnaire gdhiQuestionnaire = GdhiQuestionnaire.builder().countryId(countryId)
                .countrySummary(countrySummaryDetailDto)
                .healthIndicators(healthIndicatorDtosWithInvalidScore).build();

        assertFalse(countryHealthDataService.validateRequiredFields(gdhiQuestionnaire));

        List<HealthIndicatorDto> healthIndicatorDtosWithInvalidSupportingText = getHealthIndicatorDto(2, "");
        ;

        GdhiQuestionnaire gdhiQuestionnaire1 = GdhiQuestionnaire.builder().countryId(countryId)
                .countrySummary(countrySummaryDetailDto)
                .healthIndicators(healthIndicatorDtosWithInvalidSupportingText).build();

        assertFalse(countryHealthDataService.validateRequiredFields(gdhiQuestionnaire1));

        List<HealthIndicatorDto> healthIndicatorDtoWithNull = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            healthIndicatorDtoList.add(null);
        }

        GdhiQuestionnaire gdhiQuestionnaire2 = GdhiQuestionnaire.builder().countryId(countryId)
                .countrySummary(countrySummaryDetailDto)
                .healthIndicators(healthIndicatorDtoWithNull).build();

        assertFalse(countryHealthDataService.validateRequiredFields(gdhiQuestionnaire2));

        List<HealthIndicatorDto> healthIndicatorDtosWithNullScore = getHealthIndicatorDto(null, "something");

        GdhiQuestionnaire gdhiQuestionnaire3 = GdhiQuestionnaire.builder().countryId(countryId)
                .countrySummary(countrySummaryDetailDto)
                .healthIndicators(healthIndicatorDtosWithNullScore).build();

        assertFalse(countryHealthDataService.validateRequiredFields(gdhiQuestionnaire3));

        List<HealthIndicatorDto> healthIndicatorDtosWithNullSupoortText = getHealthIndicatorDto(1, null);

        GdhiQuestionnaire gdhiQuestionnaire4 = GdhiQuestionnaire.builder().countryId(countryId)
                .countrySummary(countrySummaryDetailDto)
                .healthIndicators(healthIndicatorDtosWithNullSupoortText).build();

        assertFalse(countryHealthDataService.validateRequiredFields(gdhiQuestionnaire4));

        GdhiQuestionnaire gdhiQuestionnaire5 = GdhiQuestionnaire.builder().countryId(countryId)
                .countrySummary(countrySummaryDetailDto)
                .healthIndicators(null).build();

        assertFalse(countryHealthDataService.validateRequiredFields(gdhiQuestionnaire5));
    }

    @Test
    public void shouldReturnFalseIfAllIndicatorsAreNotPresent() {
        String countryId = "AUS";
        String countryName = "Australia";
        List<String> resourceLinks = asList("Res 1");
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
                .resources(resourceLinks)
                .build();

        List<HealthIndicatorDto> healthIndicatorDto = asList(new HealthIndicatorDto(1, 1, "PUBLISHED", 1, "Text"));
        when(categoryIndicatorService.getHealthIndicatorCount()).thenReturn(30);

        GdhiQuestionnaire gdhiQuestionnaire = GdhiQuestionnaire.builder().countryId(countryId)
                .countrySummary(countrySummaryDetailDto)
                .healthIndicators(healthIndicatorDto).build();

        assertFalse(countryHealthDataService.validateRequiredFields(gdhiQuestionnaire));
    }

    @Test
    public void ShouldCalculatePhaseForAllCountriesForAGivenYear() {
        String publishedStatus = "PUBLISHED";
        String year = null;
        CountrySummaryId countrySummaryId = CountrySummaryId.builder().countryId("IND").year(year).build();
        CountrySummary countrySummary = CountrySummary.builder().countrySummaryId(countrySummaryId).status(PUBLISHED.name()).build();
        when(iCountrySummaryRepository.findByCountrySummaryIdYearAndStatus(year, publishedStatus)).thenReturn(asList(countrySummary));

        Indicator indicator = Indicator.builder().indicatorId(1).parentId(null).build();
        CountryHealthIndicator countryHealthIndicator = CountryHealthIndicator.builder()
                .indicator(indicator)
                .score(2)
                .category(Category.builder().id(1).indicators(asList(indicator)).build())
                .build();

        when(iCountryHealthIndicatorRepository.findByCountryHealthIndicatorIdCountryIdAndCountryHealthIndicatorIdYearAndStatus("IND", year, publishedStatus))
                .thenReturn(asList(countryHealthIndicator));

        countryHealthDataService.calculatePhaseForAllCountries(year);

        InOrder inOrder = inOrder(iCountryPhaseRepository);


        ArgumentCaptor<CountryPhase> phaseDetailsCaptor = ArgumentCaptor.forClass(CountryPhase.class);
        inOrder.verify(iCountryPhaseRepository, times(1)).save(phaseDetailsCaptor.capture());
        assertThat(phaseDetailsCaptor.getValue().getCountryPhaseId().getCountryId(), is("IND"));
        assertThat(phaseDetailsCaptor.getValue().getCountryOverallPhase(), is(2));
    }

    @Test
    public void ShouldCalculatePhaseForAllCountriesIgnoringIndicatorsWithNegativeScoreForAGivenYear() {
        String publishedStatus = "PUBLISHED";
        String year = getCurrentYear();
        CountrySummaryId countrySummaryId = CountrySummaryId.builder().countryId("IND").year(year).build();
        CountrySummary countrySummary = CountrySummary.builder().countrySummaryId(countrySummaryId).status(PUBLISHED.name()).build();
        when(iCountrySummaryRepository.findByCountrySummaryIdYearAndStatus(year, publishedStatus)).thenReturn(Collections.singletonList(countrySummary));

        Indicator indicator1 = Indicator.builder().indicatorId(1).parentId(null).build();
        Indicator indicator2 = Indicator.builder().indicatorId(2).parentId(null).build();

        CountryHealthIndicator countryHealthIndicator1 = CountryHealthIndicator.builder()
                .indicator(indicator1)
                .score(3)
                .category(Category.builder().id(1).indicators(asList(indicator1, indicator2)).build())
                .build();

        CountryHealthIndicator countryHealthIndicator2 = CountryHealthIndicator.builder()
                .indicator(indicator2)
                .score(-1)
                .category(Category.builder().id(1).indicators(asList(indicator1, indicator2)).build())
                .build();

        when(iCountryHealthIndicatorRepository.findByCountryHealthIndicatorIdCountryIdAndCountryHealthIndicatorIdYearAndStatus("IND", year, publishedStatus))
                .thenReturn(asList(countryHealthIndicator1, countryHealthIndicator2));

        countryHealthDataService.calculatePhaseForAllCountries(year);

        InOrder inOrder = inOrder(iCountryPhaseRepository);


        ArgumentCaptor<CountryPhase> phaseDetailsCaptor = ArgumentCaptor.forClass(CountryPhase.class);
        inOrder.verify(iCountryPhaseRepository, times(1)).save(phaseDetailsCaptor.capture());
        assertThat(phaseDetailsCaptor.getValue().getCountryPhaseId().getCountryId(), is("IND"));
        assertThat(phaseDetailsCaptor.getValue().getCountryOverallPhase(), is(3));
    }

    @Test
    public void ShouldCalculatePhaseForAllCountriesIgnoringCategoriesWithNoScoreForAGivenYear() {
        String publishedStatus = "PUBLISHED";
        String year = getCurrentYear();
        CountrySummaryId countrySummaryId = CountrySummaryId.builder().countryId("IND").year(year).build();
        CountrySummary countrySummary = CountrySummary.builder().countrySummaryId(countrySummaryId).status(PUBLISHED.name()).build();
        when(iCountrySummaryRepository.findByCountrySummaryIdYearAndStatus(year, publishedStatus)).thenReturn(Collections.singletonList(countrySummary));

        Indicator indicator1 = Indicator.builder().indicatorId(1).parentId(null).build();
        Indicator indicator2 = Indicator.builder().indicatorId(2).parentId(null).build();
        Indicator indicator3 = Indicator.builder().indicatorId(3).parentId(null).build();
        Indicator indicator4 = Indicator.builder().indicatorId(4).parentId(null).build();

        CountryHealthIndicator countryHealthIndicator1 = CountryHealthIndicator.builder()
                .indicator(indicator1)
                .score(-1)
                .category(Category.builder().id(1).indicators(asList(indicator1, indicator2)).build())
                .build();

        CountryHealthIndicator countryHealthIndicator2 = CountryHealthIndicator.builder()
                .indicator(indicator2)
                .score(-1)
                .category(Category.builder().id(1).indicators(asList(indicator1, indicator2)).build())
                .build();

        CountryHealthIndicator countryHealthIndicator3 = CountryHealthIndicator.builder()
                .indicator(indicator3)
                .score(5)
                .category(Category.builder().id(2).indicators(asList(indicator3, indicator4)).build())
                .build();

        CountryHealthIndicator countryHealthIndicator4 = CountryHealthIndicator.builder()
                .indicator(indicator4)
                .score(3)
                .category(Category.builder().id(2).indicators(asList(indicator3, indicator4)).build())
                .build();

        when(iCountryHealthIndicatorRepository.findByCountryHealthIndicatorIdCountryIdAndCountryHealthIndicatorIdYearAndStatus("IND", year, publishedStatus))
                .thenReturn(asList(countryHealthIndicator1, countryHealthIndicator2, countryHealthIndicator3, countryHealthIndicator4));

        countryHealthDataService.calculatePhaseForAllCountries(year);

        InOrder inOrder = inOrder(iCountryPhaseRepository);


        ArgumentCaptor<CountryPhase> phaseDetailsCaptor = ArgumentCaptor.forClass(CountryPhase.class);
        inOrder.verify(iCountryPhaseRepository, times(1)).save(phaseDetailsCaptor.capture());
        assertThat(phaseDetailsCaptor.getValue().getCountryPhaseId().getCountryId(), is("IND"));
        assertThat(phaseDetailsCaptor.getValue().getCountryOverallPhase(), is(4));
    }

    @Test
    public void shouldSaveNewCountrySummaryWhenItIsAlreadyPublishedInADifferentYear() {
        String countryId = "ARG";
        UUID countryUUID = UUID.randomUUID();
        String year = "Version1";
        String currentYear = getCurrentYear();
        Country country = new Country(countryId, "Argentina", countryUUID, "AR");
        CountrySummaryId countrySummaryId1 = CountrySummaryId.builder().countryId(countryId).year(year).build();
        CountrySummary countrySummary1 = CountrySummary.builder().countrySummaryId(countrySummaryId1).status(PUBLISHED.name()).build();
        CountrySummaryId countrySummaryId2 = CountrySummaryId.builder().countryId(countryId).year(currentYear).build();
        CountrySummary countrySummary2 = CountrySummary.builder().countrySummaryId(countrySummaryId2).status(null).build();

        when(countryDetailRepository.findByUniqueId(countryUUID)).thenReturn(country);

        when(iCountrySummaryRepository.findByCountrySummaryIdCountryIdAndCountrySummaryIdYear(countryId, currentYear))
                .thenReturn(asList(countrySummary2));

        CountryUrlGenerationStatusDto countryUrlGenerationStatusDto = countryHealthDataService
                .saveNewCountrySummary(countryUUID);
        assertTrue(countryUrlGenerationStatusDto.isSuccess());
        assertEquals(null, countryUrlGenerationStatusDto.getExistingStatus());

        verify(iCountrySummaryRepository).save(any());

    }


    private CountrySummary getCountrySummary(String countryId, String statusValue, String countryName,
                                             String alpha2code, String contactName, String contactEmail, String year) {
        UUID countryUUID = UUID.randomUUID();
        Country country = new Country(countryId, countryName, countryUUID, alpha2code);
        return CountrySummary.builder()
                .countrySummaryId(new CountrySummaryId(countryId, year))
                .country(country)
                .contactName(contactName)
                .contactEmail(contactEmail)
                .countryResourceLinks(emptyList())
                .status(statusValue)
                .build();
    }

    private List<HealthIndicatorDto> getHealthIndicatorDto(Integer score, String supportText) {
        List<HealthIndicatorDto> healthIndicatorDtoList = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            healthIndicatorDtoList.add(new HealthIndicatorDto(1, 1, "PUBLISHED", score, supportText));
        }
        return healthIndicatorDtoList;
    }
}