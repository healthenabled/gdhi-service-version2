package it.gdhi.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.UUID;

import javax.persistence.EntityManager;

import it.gdhi.dto.CountrySummaryDto;
import it.gdhi.dto.CountrySummaryStatusDto;
import it.gdhi.dto.CountrySummaryStatusYearDto;
import it.gdhi.dto.CountryUrlGenerationStatusDto;
import it.gdhi.dto.GdhiQuestionnaire;
import it.gdhi.dto.HealthIndicatorDto;
import it.gdhi.model.Category;
import it.gdhi.model.Country;
import it.gdhi.model.CountryHealthIndicator;
import it.gdhi.model.CountryPhase;
import it.gdhi.model.CountrySummary;
import it.gdhi.model.Indicator;
import it.gdhi.model.id.CountrySummaryId;
import it.gdhi.repository.ICountryHealthIndicatorRepository;
import it.gdhi.repository.ICountryPhaseRepository;
import it.gdhi.repository.ICountryRepository;
import it.gdhi.repository.ICountryResourceLinkRepository;
import it.gdhi.repository.ICountrySummaryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import static it.gdhi.utils.FormStatus.DRAFT;
import static it.gdhi.utils.FormStatus.NEW;
import static it.gdhi.utils.FormStatus.PUBLISHED;
import static it.gdhi.utils.FormStatus.REVIEW_PENDING;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
    MailerService mailerService;
    @Mock
    ICountryRepository countryDetailRepository;
    @Mock
    EntityManager entityManager;
    @Mock
    ICountryPhaseRepository iCountryPhaseRepository;

    @Mock
    CategoryIndicatorService categoryIndicatorService;

    @Test
    public void shouldPublishDetailsForACountry() throws Exception {
        List<String> resourceLinks = asList("Res 1");
        CountrySummaryDto countrySummaryDetailDto = CountrySummaryDto.builder().summary("Summary 1")
                .resources(resourceLinks).build();

        String status = PUBLISHED.name();
        String currentYear = countryHealthDataService.getCurrentYear();
        List<HealthIndicatorDto> healthIndicatorDtos = asList(new HealthIndicatorDto(1, 1, status, 2, "Text"));
        String countryId = "ARG";
        GdhiQuestionnaire gdhiQuestionnaire = GdhiQuestionnaire.builder().countryId(countryId)
                .countrySummary(countrySummaryDetailDto)
                .healthIndicators(healthIndicatorDtos).build();

        CountrySummaryId countrySummaryId = CountrySummaryId.builder().countryId(countryId).status(status).year(currentYear).build();
        CountrySummary countrySummary = CountrySummary.builder().summary("Summary 1").countrySummaryId(countrySummaryId).build();

        Indicator indicator1 = Indicator.builder().indicatorId(1).parentId(null).build();
        CountryHealthIndicator countryHealthIndicator1 = CountryHealthIndicator.builder()
                .indicator(indicator1)
                .score(2)
                .category(Category.builder().id(1).indicators(asList(indicator1)).build())
                .build();


        when(iCountryHealthIndicatorRepository.findByCountryHealthIndicatorIdCountryIdAndCountryHealthIndicatorIdStatusAndCountryHealthIndicatorIdYear(countryId, status, currentYear))
                .thenReturn(asList(countryHealthIndicator1));
        when(iCountrySummaryRepository.findByCountrySummaryIdCountryIdAndCountrySummaryIdStatusNotAndCountrySummaryIdYear(countryId, PUBLISHED.name(), currentYear)).thenReturn(countrySummary);
        countryHealthDataService.publish(gdhiQuestionnaire , currentYear);
        ArgumentCaptor<CountrySummary> summaryCaptor = ArgumentCaptor.forClass(CountrySummary.class);
        ArgumentCaptor<CountryHealthIndicator> healthIndicatorsCaptorList = ArgumentCaptor.forClass(CountryHealthIndicator.class);
        InOrder inOrder = inOrder(iCountryResourceLinkRepository, iCountrySummaryRepository,
                iCountryHealthIndicatorRepository, iCountryPhaseRepository);
        inOrder.verify(iCountryResourceLinkRepository).deleteByCountryResourceLinkIdCountryIdAndCountryResourceLinkIdStatusAndCountryResourceLinkIdYear(countryId, status, currentYear);
        inOrder.verify(iCountrySummaryRepository).save(summaryCaptor.capture());
        inOrder.verify(iCountryHealthIndicatorRepository).save(healthIndicatorsCaptorList.capture());
        CountrySummary summaryCaptorValue = summaryCaptor.getValue();
        assertThat(summaryCaptorValue.getCountrySummaryId().getCountryId(), is(countryId));
        assertThat(summaryCaptorValue.getSummary(), is("Summary 1"));
        assertThat(summaryCaptorValue.getCountryResourceLinks().get(0).getLink(), is("Res 1"));
        assertThat(healthIndicatorsCaptorList.getValue().getCountryHealthIndicatorId().getCategoryId(), is(1));
        ArgumentCaptor<CountryPhase> phaseDetailsCaptor = ArgumentCaptor.forClass(CountryPhase.class);
        inOrder.verify(iCountryPhaseRepository).save(phaseDetailsCaptor.capture());
        assertThat(phaseDetailsCaptor.getValue().getCountryOverallPhase(), is(2));
        assertThat(phaseDetailsCaptor.getValue().getCountryPhaseId().getCountryId(), is(countryId));

    }

    @Test
    public void shouldSendEmailOnSuccessfulSubmitOfCountryDetailsAndIndicators() throws Exception {
        String countryId = "ARG";
        Country country = new Country(countryId, "Argentina", UUID.randomUUID(), "AR");
        List<String> resourceLinks = asList("Res 1");
        String feeder = "feeder";
        String feederRole = "feeder role";
        String contactEmail = "contact@test.com";
        String currentYear = countryHealthDataService.getCurrentYear();
        CountrySummaryDto countrySummaryDetailDto = CountrySummaryDto.builder().summary("Summary 1")
                .dataFeederName(feeder)
                .dataFeederRole(feederRole)
                .contactEmail(contactEmail)
                .resources(resourceLinks).build();

        List<HealthIndicatorDto> healthIndicatorDtos = asList(new HealthIndicatorDto(1, 1, "PUBLISHED", 2, "Text"));
        GdhiQuestionnaire gdhiQuestionnaire = GdhiQuestionnaire.builder().countryId(countryId)
                .countrySummary(countrySummaryDetailDto)
                .healthIndicators(healthIndicatorDtos).build();

        CountrySummaryId countrySummaryId = CountrySummaryId.builder().countryId(countryId).status(DRAFT.name()).year(currentYear).build();
        CountrySummary countrySummary = CountrySummary.builder().summary("Summary 1").countrySummaryId(countrySummaryId).build();

        when(iCountrySummaryRepository.save(any(CountrySummary.class))).thenReturn(CountrySummary.builder().build());
        when(iCountryHealthIndicatorRepository.save(any(CountryHealthIndicator.class))).thenReturn(CountryHealthIndicator.builder().build());
        when(countryDetailRepository.findById(countryId)).thenReturn(country);
        when(iCountrySummaryRepository.findByCountrySummaryIdCountryIdAndCountrySummaryIdStatusNotAndCountrySummaryIdYear(countryId, PUBLISHED.name(), currentYear)).thenReturn(countrySummary);
        countryHealthDataService.submit(gdhiQuestionnaire);

        verify(mailerService).send(country, feeder, feederRole, contactEmail);
    }

    @Test
    public void shouldSaveAsNewStatusWhenCountryDoesNotHavePublishedData() throws Exception {
        String countryId = "ARG";
        UUID countryUUID = UUID.randomUUID();
        ArgumentCaptor<CountrySummary> summaryCaptor = ArgumentCaptor.forClass(CountrySummary.class);
        Country country = new Country(countryId, "Argentina", countryUUID, "AR");

        when(countryDetailRepository.findByUniqueId(countryUUID)).thenReturn(country);
        when(iCountrySummaryRepository.getAllStatus(anyString())).thenReturn(emptyList());
        CountryUrlGenerationStatusDto countryUrlGenerationStatusDto = countryHealthDataService
                .saveNewCountrySummary(countryUUID);
        assert (countryUrlGenerationStatusDto.isSuccess());
        assertNull(countryUrlGenerationStatusDto.getExistingStatus());

        verify(iCountrySummaryRepository).save(summaryCaptor.capture());
        CountrySummary summaryCaptorValue = summaryCaptor.getValue();
        assertEquals(countryId, summaryCaptorValue.getCountrySummaryId().getCountryId());
        assertEquals(NEW.toString(), summaryCaptorValue.getCountrySummaryId().getStatus());
    }


    @Test
    public void shouldNotSaveAsNewStatusWhenCountryHasPublishedDataForTheSameYear() throws Exception {
        String countryId = "ARG";
        String currentYear = countryHealthDataService.getCurrentYear();
        UUID countryUUID = UUID.randomUUID();
        ArgumentCaptor<CountrySummary> summaryCaptor = ArgumentCaptor.forClass(CountrySummary.class);
        Country country = new Country(countryId, "Argentina", countryUUID, "AR");
        CountrySummaryId countrySummaryId = CountrySummaryId.builder().countryId(countryId).status(PUBLISHED.name()).year(currentYear).build();
        CountrySummary countrySummary = CountrySummary.builder().countrySummaryId(countrySummaryId).build();

        when(countryDetailRepository.findByUniqueId(countryUUID)).thenReturn(country);

        when(iCountrySummaryRepository.findByCountrySummaryIdCountryIdAndCountrySummaryIdYear(anyString(), anyString())).thenReturn(asList(countrySummary));
        CountryUrlGenerationStatusDto countryUrlGenerationStatusDto = countryHealthDataService
                .saveNewCountrySummary(countryUUID);
        assert (!countryUrlGenerationStatusDto.isSuccess());
        assertEquals(PUBLISHED, countryUrlGenerationStatusDto.getExistingStatus());

        verify(iCountrySummaryRepository, times(0)).save(summaryCaptor.capture());
    }

    @Test
    public void shouldNotSaveNewCountrySummaryWhenItAlreadyHasUnpublishedDataForTheSameYear() throws Exception {
        String countryId = "ARG";
        String currentYear = countryHealthDataService.getCurrentYear();
        UUID countryUUID = UUID.randomUUID();
        Country country = new Country(countryId, "Argentina", countryUUID, "AR");
        CountrySummaryId countrySummaryId = CountrySummaryId.builder().countryId(countryId).status(NEW.name()).year(currentYear).build();
        CountrySummary countrySummary = CountrySummary.builder().countrySummaryId(countrySummaryId).build();


        when(countryDetailRepository.findByUniqueId(countryUUID)).thenReturn(country);

        when(iCountrySummaryRepository.findByCountrySummaryIdCountryIdAndCountrySummaryIdYear(anyString(), anyString())).thenReturn(asList(countrySummary));

        CountryUrlGenerationStatusDto countryUrlGenerationStatusDto = countryHealthDataService
                .saveNewCountrySummary(countryUUID);
        assertFalse(countryUrlGenerationStatusDto.isSuccess());
        assertEquals(NEW, countryUrlGenerationStatusDto.getExistingStatus());

        verify(iCountrySummaryRepository, never()).save(any());
    }

    @Test
    public void shouldNotSaveNewCountrySummaryWhenItAlreadyHaveDraftData() throws Exception {
        String countryId = "ARG";
        UUID countryUUID = UUID.randomUUID();
        String currentYear = countryHealthDataService.getCurrentYear();
        Country country = new Country(countryId, "Argentina", countryUUID, "AR");
        CountrySummaryId countrySummaryId = CountrySummaryId.builder().countryId(countryId).status(DRAFT.name()).year(currentYear).build();
        CountrySummary countrySummary = CountrySummary.builder().countrySummaryId(countrySummaryId).build();

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
    public void shouldNotSaveNewCountrySummaryWhenItAlreadyHasNewData() throws Exception {
        String countryId = "ARG";
        UUID countryUUID = UUID.randomUUID();
        String currentYear = countryHealthDataService.getCurrentYear();
        Country country = new Country(countryId, "Argentina", countryUUID, "AR");
        CountrySummaryId countrySummaryId = CountrySummaryId.builder().countryId(countryId).status(NEW.name()).year(currentYear).build();
        CountrySummary countrySummary = CountrySummary.builder().countrySummaryId(countrySummaryId).build();

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
    public void shouldDeleteCountryData() {
        String countryId = "AFG";
        String status = REVIEW_PENDING.name();
        UUID countryUUID = UUID.randomUUID();
        String year = "2020";
        Country country = new Country(countryId, "Argentina", countryUUID, "AR");
        when(countryDetailRepository.findByUniqueId(countryUUID)).thenReturn(country);

        countryHealthDataService.deleteCountryData(countryUUID, year);

        verify(iCountrySummaryRepository).deleteByCountrySummaryIdCountryIdAndCountrySummaryIdStatusAndCountrySummaryIdYear(countryId, status, year);
        verify(iCountryHealthIndicatorRepository).deleteByCountryHealthIndicatorIdCountryIdAndCountryHealthIndicatorIdStatusAndCountryHealthIndicatorIdYear(countryId, status, year);
        verify(iCountryResourceLinkRepository).deleteByCountryResourceLinkIdCountryIdAndCountryResourceLinkIdStatusAndCountryResourceLinkIdYear(countryId, status, year);
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
    public void shouldReturnTrueIfAllFieldsAreValid() {
        String countryId = "ARG";
        String countryName = "Argentina";
        List<String> resourceLinks = asList("Res 1");
        CountrySummaryDto countrySummaryDetailDto = CountrySummaryDto.builder()
                .summary("Summary 1")
                .collectedDate(new GregorianCalendar().getTime())
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
    public void shouldReturnFalseIfAnyCountrySummaryFieldIsNotPresent() {
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
    public void shouldReturnFalseIfResourceIsEmpty() {
        String countryId = "ARG";
        String countryName = "Argentina";
        CountrySummaryDto countrySummaryDetailDto = CountrySummaryDto.builder()
                .summary("Summary 1")
                .collectedDate(new GregorianCalendar().getTime())
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
                .countryId(countryId)
                .countryName(countryName)
                .build();

        List<HealthIndicatorDto> healthIndicatorDtos = getHealthIndicatorDto(1, "some text");
        when(categoryIndicatorService.getHealthIndicatorCount()).thenReturn(30);

        GdhiQuestionnaire gdhiQuestionnaire = GdhiQuestionnaire.builder().countryId(countryId)
                .countrySummary(countrySummaryDetailDto)
                .healthIndicators(healthIndicatorDtos).build();

        assertFalse(countryHealthDataService.validateRequiredFields(gdhiQuestionnaire));

    }

    @Test
    public void shouldReturnFalseIfDateIsInvalid() {
        String countryId = "ALB";
        String countryName = "Albania";
        List<String> resourceLinks = asList("Res 1");
        Date collectedDateBefore2010 = new GregorianCalendar(2009, 0, 1).getTime();
        GregorianCalendar gregCal = new GregorianCalendar();
        gregCal.add(Calendar.DAY_OF_MONTH, 1);
        Date collectedDateAfterCurrentDate = gregCal.getTime();
        CountrySummaryDto countrySummaryDetailBefore2010 = CountrySummaryDto.builder()
                .summary("Summary 1")
                .collectedDate(collectedDateBefore2010)
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
                .countryId(countryId)
                .countryName(countryName)
                .resources(resourceLinks)
                .build();

        CountrySummaryDto countrySummaryDetailDtoFutureDate = CountrySummaryDto.builder()
                .summary("Summary 1")
                .collectedDate(collectedDateAfterCurrentDate)
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
                .countryId(countryId)
                .countryName(countryName)
                .resources(resourceLinks)
                .build();

        List<HealthIndicatorDto> healthIndicatorDtos = getHealthIndicatorDto(1, "some text");
        when(categoryIndicatorService.getHealthIndicatorCount()).thenReturn(30);

        GdhiQuestionnaire gdhiQuestionnaire = GdhiQuestionnaire.builder().countryId(countryId)
                .countrySummary(countrySummaryDetailBefore2010)
                .healthIndicators(healthIndicatorDtos).build();

        assertFalse(countryHealthDataService.validateRequiredFields(gdhiQuestionnaire));

        GdhiQuestionnaire gdhiQuestionnaire1 = GdhiQuestionnaire.builder().countryId(countryId)
                .countrySummary(countrySummaryDetailDtoFutureDate)
                .healthIndicators(healthIndicatorDtos).build();

        assertFalse(countryHealthDataService.validateRequiredFields(gdhiQuestionnaire1));

    }

    @Test
    public void shouldReturnFalseIfIndicatorDataIsInvalid() {
        String countryId = "AUS";
        String countryName = "Australia";
        List<String> resourceLinks = asList("Res 1");
        CountrySummaryDto countrySummaryDetailDto = CountrySummaryDto.builder()
                .summary("Summary 1")
                .collectedDate(new Date())
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
                .collectedDate(new Date())
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
    public void ShouldCalculatePhaseForAllCountries() {
        String publishedStatus = "PUBLISHED";
        String year = null;
        when(iCountrySummaryRepository.findAllByStatus(publishedStatus)).thenReturn(asList("IND"));

        Indicator indicator = Indicator.builder().indicatorId(1).parentId(null).build();
        CountryHealthIndicator countryHealthIndicator = CountryHealthIndicator.builder()
                .indicator(indicator)
                .score(2)
                .category(Category.builder().id(1).indicators(asList(indicator)).build())
                .build();

        when(iCountryHealthIndicatorRepository.findByCountryHealthIndicatorIdCountryIdAndCountryHealthIndicatorIdStatusAndCountryHealthIndicatorIdYear("IND", publishedStatus, year))
                .thenReturn(asList(countryHealthIndicator));

        countryHealthDataService.calculatePhaseForAllCountries(year);

        InOrder inOrder = inOrder(iCountryPhaseRepository);


        ArgumentCaptor<CountryPhase> phaseDetailsCaptor = ArgumentCaptor.forClass(CountryPhase.class);
        inOrder.verify(iCountryPhaseRepository, times(1)).save(phaseDetailsCaptor.capture());
        assertThat(phaseDetailsCaptor.getValue().getCountryPhaseId().getCountryId(), is("IND"));
        assertThat(phaseDetailsCaptor.getValue().getCountryOverallPhase(), is(2));
    }

    @Test
    public void ShouldSaveNewCountrySummaryWhenItIsAlreadyPublishedInADifferentYear() {
        String countryId = "ARG";
        UUID countryUUID = UUID.randomUUID();
        String year = "Version1";
        String currentYear = countryHealthDataService.getCurrentYear();
        Country country = new Country(countryId, "Argentina", countryUUID, "AR");
        CountrySummaryId countrySummaryId1 = CountrySummaryId.builder().countryId(countryId).status(PUBLISHED.name()).year(year).build();
        CountrySummary countrySummary1 = CountrySummary.builder().countrySummaryId(countrySummaryId1).build();
        CountrySummaryId countrySummaryId2 = CountrySummaryId.builder().countryId(countryId).status(null).year(currentYear).build();
        CountrySummary countrySummary2 = CountrySummary.builder().countrySummaryId(countrySummaryId2).build();

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
                .countrySummaryId(new CountrySummaryId(countryId, statusValue, year))
                .country(country)
                .contactName(contactName)
                .contactEmail(contactEmail)
                .countryResourceLinks(emptyList())
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