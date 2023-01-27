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
        List<HealthIndicatorDto> healthIndicatorDtos = asList(new HealthIndicatorDto(1, 1, status, 2, "Text"));
        String countryId = "ARG";
        GdhiQuestionnaire gdhiQuestionnaire = GdhiQuestionnaire.builder().countryId(countryId)
                .countrySummary(countrySummaryDetailDto)
                .healthIndicators(healthIndicatorDtos).build();

        Indicator indicator1 = Indicator.builder().indicatorId(1).parentId(null).build();
        CountryHealthIndicator countryHealthIndicator1 = CountryHealthIndicator.builder()
                .indicator(indicator1)
                .score(2)
                .category(Category.builder().id(1).indicators(asList(indicator1)).build())
                .build();


        when(iCountryHealthIndicatorRepository.findByCountryIdAndStatus(countryId, status))
                .thenReturn(asList(countryHealthIndicator1));
        when(iCountrySummaryRepository.getCountrySummaryStatus(countryId)).thenReturn(status);
        countryHealthDataService.publish(gdhiQuestionnaire);

        ArgumentCaptor<CountrySummary> summaryCaptor = ArgumentCaptor.forClass(CountrySummary.class);
        ArgumentCaptor<CountryHealthIndicator> healthIndicatorsCaptorList = ArgumentCaptor.forClass(CountryHealthIndicator.class);
        InOrder inOrder = inOrder(iCountryResourceLinkRepository, iCountrySummaryRepository,
                iCountryHealthIndicatorRepository, iCountryPhaseRepository);
        inOrder.verify(iCountryResourceLinkRepository).deleteResources(countryId, status);
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
        assertThat(phaseDetailsCaptor.getValue().getCountryId(), is(countryId));

    }

    @Test
    public void shouldSendEmailOnSuccessfulSubmitOfCountryDetailsAndIndicators() throws Exception {
        String countryId = "ARG";
        Country country = new Country(countryId, "Argentina", UUID.randomUUID(), "AR");
        List<String> resourceLinks = asList("Res 1");
        String feeder = "feeder";
        String feederRole = "feeder role";
        String contactEmail = "contact@test.com";
        CountrySummaryDto countrySummaryDetailDto = CountrySummaryDto.builder().summary("Summary 1")
                .dataFeederName(feeder)
                .dataFeederRole(feederRole)
                .contactEmail(contactEmail)
                .resources(resourceLinks).build();

        List<HealthIndicatorDto> healthIndicatorDtos = asList(new HealthIndicatorDto(1, 1, "PUBLISHED", 2, "Text"));
        GdhiQuestionnaire gdhiQuestionnaire = GdhiQuestionnaire.builder().countryId(countryId)
                .countrySummary(countrySummaryDetailDto)
                .healthIndicators(healthIndicatorDtos).build();

        when(iCountrySummaryRepository.save(any(CountrySummary.class))).thenReturn(CountrySummary.builder().build());
        when(iCountryHealthIndicatorRepository.save(any(CountryHealthIndicator.class))).thenReturn(CountryHealthIndicator.builder().build());
        when(countryDetailRepository.findById(countryId)).thenReturn(country);
        when(iCountrySummaryRepository.getCountrySummaryStatus(countryId)).thenReturn("DRAFT");
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
    public void shouldSaveAsNewStatusWhenCountryHasPublishedData() throws Exception {
        String countryId = "ARG";
        UUID countryUUID = UUID.randomUUID();
        ArgumentCaptor<CountrySummary> summaryCaptor = ArgumentCaptor.forClass(CountrySummary.class);
        Country country = new Country(countryId, "Argentina", countryUUID, "AR");

        when(countryDetailRepository.findByUniqueId(countryUUID)).thenReturn(country);

        when(iCountrySummaryRepository.getAllStatus(anyString())).thenReturn(asList(PUBLISHED.toString()));
        CountryUrlGenerationStatusDto countryUrlGenerationStatusDto = countryHealthDataService
                .saveNewCountrySummary(countryUUID);
        assert (countryUrlGenerationStatusDto.isSuccess());
        assertEquals(PUBLISHED, countryUrlGenerationStatusDto.getExistingStatus());

        verify(iCountrySummaryRepository).save(summaryCaptor.capture());
        CountrySummary summaryCaptorValue = summaryCaptor.getValue();
        assertEquals(countryId, summaryCaptorValue.getCountrySummaryId().getCountryId());
        assertEquals(NEW.toString(), summaryCaptorValue.getCountrySummaryId().getStatus());
    }

    @Test
    public void shouldNotSaveNewCountrySummaryWhenItAlreadyHasUnpublishedData() throws Exception {
        String countryId = "ARG";
        UUID countryUUID = UUID.randomUUID();
        Country country = new Country(countryId, "Argentina", countryUUID, "AR");

        when(countryDetailRepository.findByUniqueId(countryUUID)).thenReturn(country);

        when(iCountrySummaryRepository.getAllStatus(anyString())).thenReturn(asList(NEW.toString()));

        CountryUrlGenerationStatusDto countryUrlGenerationStatusDto = countryHealthDataService
                .saveNewCountrySummary(countryUUID);
        assertFalse(countryUrlGenerationStatusDto.isSuccess());
        assertEquals(NEW, countryUrlGenerationStatusDto.getExistingStatus());

        verify(iCountrySummaryRepository, never()).save(any());
    }

    @Test
    public void shouldNotSaveNewCountrySummaryWhenItAlreadyHasBothPublishedAndDraftData() throws Exception {
        String countryId = "ARG";
        UUID countryUUID = UUID.randomUUID();
        Country country = new Country(countryId, "Argentina", countryUUID, "AR");

        when(countryDetailRepository.findByUniqueId(countryUUID)).thenReturn(country);

        when(iCountrySummaryRepository.getAllStatus(anyString()))
                .thenReturn(asList(PUBLISHED.toString(), DRAFT.toString()));

        CountryUrlGenerationStatusDto countryUrlGenerationStatusDto = countryHealthDataService
                .saveNewCountrySummary(countryUUID);
        assertFalse(countryUrlGenerationStatusDto.isSuccess());
        assertEquals(DRAFT, countryUrlGenerationStatusDto.getExistingStatus());

        verify(iCountrySummaryRepository, never()).save(any());
    }

    @Test
    public void shouldNotSaveNewCountrySummaryWhenItAlreadyHasBothPublishedAndNewData() throws Exception {
        String countryId = "ARG";
        UUID countryUUID = UUID.randomUUID();
        Country country = new Country(countryId, "Argentina", countryUUID, "AR");

        when(countryDetailRepository.findByUniqueId(countryUUID)).thenReturn(country);

        when(iCountrySummaryRepository.getAllStatus(anyString()))
                .thenReturn(asList(PUBLISHED.toString(), NEW.toString()));

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
        Country country = new Country(countryId, "Argentina", countryUUID, "AR");
        when(countryDetailRepository.findByUniqueId(countryUUID)).thenReturn(country);

        countryHealthDataService.deleteCountryData(countryUUID);

        verify(iCountrySummaryRepository).removeCountrySummary(countryId, status);
        verify(iCountryHealthIndicatorRepository).removeHealthIndicatorsBy(countryId, status);
        verify(iCountryResourceLinkRepository).deleteResources(countryId, status);
    }

    @Test
    public void shouldGetAdminViewFormDetails() {
        CountrySummary countrySummaryIND = getCountrySummary("IND","PUBLISHED","INDIA",
                "IN","Contact Name 1","con1@gdhi.com");
        CountrySummary countrySummaryARG = getCountrySummary("ARG","REVIEW_PENDING",
                "ARGENTINA", "AR","Contact Name 1","con1@gdhi.com");
        CountrySummary countrySummaryALG = getCountrySummary("ALG","NEW","ALGERIA",
                "AL","Contact Name 2","con2@gdhi.com");
        CountrySummary countrySummaryINDNEW = getCountrySummary("IND","NEW","INDIA",
                "IN","Contact Name 1","con1@gdhi.com");

        when(iCountrySummaryRepository.getAll()).thenReturn(asList(countrySummaryIND,countrySummaryARG,
                countrySummaryALG,countrySummaryINDNEW));
        Map<String, List<CountrySummaryStatusDto>> adminViewFormDetails = countryHealthDataService.getAllCountryStatusSummaries();
        assertEquals(adminViewFormDetails.get("NEW").size(), 2);
        assertEquals(adminViewFormDetails.get("REVIEW_PENDING").size(), 1);

        CountrySummaryStatusDto countrySummaryStatusDto = adminViewFormDetails.get("REVIEW_PENDING").stream().findFirst().get();

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

        List<HealthIndicatorDto> healthIndicatorDtosWithInvalidSupportingText = getHealthIndicatorDto(2, "");;

        GdhiQuestionnaire gdhiQuestionnaire1 = GdhiQuestionnaire.builder().countryId(countryId)
                .countrySummary(countrySummaryDetailDto)
                .healthIndicators(healthIndicatorDtosWithInvalidSupportingText).build();

        assertFalse(countryHealthDataService.validateRequiredFields(gdhiQuestionnaire1));

        List<HealthIndicatorDto> healthIndicatorDtoWithNull =  new ArrayList<>();
        for(int i=0;i<30;i++){
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
    public void ShouldCalculatePhaseForAllCountries(){
        String publishedStatus = "PUBLISHED";
        when(iCountrySummaryRepository.findAllByStatus(publishedStatus)).thenReturn(asList("IND"));

        Indicator indicator = Indicator.builder().indicatorId(1).parentId(null).build();
        CountryHealthIndicator countryHealthIndicator = CountryHealthIndicator.builder()
                .indicator(indicator)
                .score(2)
                .category(Category.builder().id(1).indicators(asList(indicator)).build())
                .build();

        when(iCountryHealthIndicatorRepository.findByCountryIdAndStatus("IND", publishedStatus))
                .thenReturn(asList(countryHealthIndicator));

        countryHealthDataService.calculatePhaseForAllCountries();

        InOrder inOrder = inOrder(iCountryPhaseRepository);


        ArgumentCaptor<CountryPhase> phaseDetailsCaptor = ArgumentCaptor.forClass(CountryPhase.class);
        inOrder.verify(iCountryPhaseRepository, times(1)).save(phaseDetailsCaptor.capture());
        assertThat( phaseDetailsCaptor.getValue().getCountryId(),is("IND"));
        assertThat(phaseDetailsCaptor.getValue().getCountryOverallPhase(), is (2));
    }

    private CountrySummary getCountrySummary(String countryId , String statusValue , String countryName ,
                                             String alpha2code, String contactName  , String contactEmail) {
        UUID countryUUID = UUID.randomUUID();
        Country country = new Country(countryId, countryName, countryUUID, alpha2code);
        return CountrySummary.builder()
                .countrySummaryId(new CountrySummaryId(countryId, statusValue))
                .country(country)
                .contactName(contactName)
                .contactEmail(contactEmail)
                .countryResourceLinks(emptyList())
                .build();
    }

    private List<HealthIndicatorDto> getHealthIndicatorDto(Integer score, String supportText) {
        List<HealthIndicatorDto> healthIndicatorDtoList =  new ArrayList<>();
        for(int i=0;i<30;i++){
            healthIndicatorDtoList.add(new HealthIndicatorDto(1, 1, "PUBLISHED", score, supportText));
        }
        return healthIndicatorDtoList;
    }
}