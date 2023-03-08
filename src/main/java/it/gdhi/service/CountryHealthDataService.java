package it.gdhi.service;

import java.time.Year;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import it.gdhi.dto.BenchmarkDto;
import it.gdhi.dto.CountrySummaryDto;
import it.gdhi.dto.CountrySummaryStatusDto;
import it.gdhi.dto.CountrySummaryStatusYearDto;
import it.gdhi.dto.CountryUrlGenerationStatusDto;
import it.gdhi.dto.GdhiQuestionnaire;
import it.gdhi.dto.HealthIndicatorDto;
import it.gdhi.model.Country;
import it.gdhi.model.CountryHealthIndicator;
import it.gdhi.model.CountryHealthIndicators;
import it.gdhi.model.CountryPhase;
import it.gdhi.model.CountrySummary;
import it.gdhi.model.Score;
import it.gdhi.model.id.CountryHealthIndicatorId;
import it.gdhi.model.id.CountrySummaryId;
import it.gdhi.repository.ICountryHealthIndicatorRepository;
import it.gdhi.repository.ICountryPhaseRepository;
import it.gdhi.repository.ICountryRepository;
import it.gdhi.repository.ICountryResourceLinkRepository;
import it.gdhi.repository.ICountrySummaryRepository;
import it.gdhi.utils.FormStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import static it.gdhi.utils.FormStatus.DRAFT;
import static it.gdhi.utils.FormStatus.NEW;
import static it.gdhi.utils.FormStatus.PUBLISHED;
import static it.gdhi.utils.FormStatus.REVIEW_PENDING;
import static java.util.Objects.isNull;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

@Service
public class CountryHealthDataService {

    @Autowired
    private ICountryHealthIndicatorRepository iCountryHealthIndicatorRepository;

    @Autowired
    private ICountryRepository iCountryRepository;

    @Autowired
    private MailerService mailerService;

    @Autowired
    private ICountryResourceLinkRepository iCountryResourceLinkRepository;

    @Autowired
    private ICountrySummaryRepository iCountrySummaryRepository;

    @Autowired
    private ICountryPhaseRepository iCountryPhaseRepository;

    @Autowired
    private BenchMarkService benchmarkService;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    CategoryIndicatorService categoryIndicatorService;

    @Transactional
    public void save(GdhiQuestionnaire gdhiQuestionnaire, String nextStatus) {
        String currentYear = getCurrentYear();
        String currentStatus = iCountrySummaryRepository.findByCountrySummaryIdCountryIdAndCountrySummaryIdYearAndCountrySummaryIdStatusNot(gdhiQuestionnaire.getCountryId(), currentYear, PUBLISHED.name()).getStatus();
        if (!nextStatus.equals(currentStatus)) {
            removeEntriesWithStatus(gdhiQuestionnaire.getCountryId(), currentStatus, currentYear);
        }
        saveCountryContactInfo(gdhiQuestionnaire.getCountryId(),
                nextStatus, gdhiQuestionnaire.getCountrySummary(), currentYear);
        saveHealthIndicators(gdhiQuestionnaire.getCountryId(),
                nextStatus, gdhiQuestionnaire.getHealthIndicators(), currentYear);
    }

    @Transactional
    public CountryUrlGenerationStatusDto saveNewCountrySummary(UUID countryUUID) {
        String countryId = iCountryRepository.findByUniqueId(countryUUID).getId();
        String currentYear = this.getCurrentYear();

        CountryUrlGenerationStatusDto statusDto;

        String currentStatus = getStatusOfCountrySummary(countryId, currentYear);

        if (isNull(currentStatus)) {
            CountrySummary countrySummary = new CountrySummary(new CountrySummaryId(countryId, NEW.toString(), currentYear),
                    new CountrySummaryDto());
            iCountrySummaryRepository.save(countrySummary);
            statusDto = new CountryUrlGenerationStatusDto(countryId, true, null);
        } else {
            statusDto = new CountryUrlGenerationStatusDto(countryId, false, FormStatus.valueOf(currentStatus));
        }
        return statusDto;
    }

    @Transactional
    public void publish(GdhiQuestionnaire gdhiQuestionnaire, String currentYear) {
        save(gdhiQuestionnaire, PUBLISHED.name());
        calculateAndSaveCountryPhase(gdhiQuestionnaire.getCountryId(), PUBLISHED.name(), currentYear);
    }

    @Transactional
    public void submit(GdhiQuestionnaire gdhiQuestionnaire) {
        save(gdhiQuestionnaire, REVIEW_PENDING.name());
        sendMail(gdhiQuestionnaire.getDataFeederName(), gdhiQuestionnaire.getDataFeederRole(),
                gdhiQuestionnaire.getContactEmail(), gdhiQuestionnaire.getCountryId());
    }

    @Transactional
    public void saveCorrection(GdhiQuestionnaire gdhiQuestionnaire) {
        save(gdhiQuestionnaire, REVIEW_PENDING.name());
    }

    @Transactional
    public void deleteCountryData(UUID countryUUID, String year) {
        String countryId = iCountryRepository.findByUniqueId(countryUUID).getId();
        iCountryHealthIndicatorRepository.deleteByCountryHealthIndicatorIdCountryIdAndCountryHealthIndicatorIdYearAndCountryHealthIndicatorIdStatus(countryId, year, REVIEW_PENDING.name());
        iCountryResourceLinkRepository.deleteByCountryResourceLinkIdCountryIdAndCountryResourceLinkIdYearAndCountryResourceLinkIdStatus(countryId, year, REVIEW_PENDING.name());
        iCountrySummaryRepository.deleteByCountrySummaryIdCountryIdAndCountrySummaryIdYearAndCountrySummaryIdStatus(countryId, year, REVIEW_PENDING.name());
    }

    @Transactional
    public void calculatePhaseForAllCountries(String year) {
        List<CountrySummary> publishedCountries = iCountrySummaryRepository.findByCountrySummaryIdStatus(PUBLISHED.name());

        publishedCountries.stream().forEach(country -> calculateAndSaveCountryPhase(country.getCountrySummaryId().getCountryId(), PUBLISHED.name(), year));
    }

    public CountrySummaryStatusYearDto getAllCountryStatusSummaries() {
        String currentYear = this.getCurrentYear();
        List<CountrySummary> countrySummaries = iCountrySummaryRepository.findByCountrySummaryIdYearOrderByUpdatedAtDesc(currentYear);

        List<CountrySummaryStatusDto> countrySummaryStatusDtos = countrySummaries
                .stream().map(CountrySummaryStatusDto::new).collect(toList());

        Map<String, List<CountrySummaryStatusDto>> groupByCountrySummaryStatus = countrySummaryStatusDtos.stream()
                .collect(groupingBy(CountrySummaryStatusDto::getStatus));

        CountrySummaryStatusYearDto countrySummaryStatusYearDto = new CountrySummaryStatusYearDto(currentYear, groupByCountrySummaryStatus.get(NEW.name()),
                groupByCountrySummaryStatus.get(DRAFT.name()), groupByCountrySummaryStatus.get(PUBLISHED.name()), groupByCountrySummaryStatus.get(REVIEW_PENDING.name()));


        return countrySummaryStatusYearDto;
    }

    public Map<Integer, BenchmarkDto> getBenchmarkDetailsFor(String countryId, Integer benchmarkType, String year) {
        return benchmarkService.getBenchmarkFor(countryId, benchmarkType, year);
    }

    public boolean validateRequiredFields(GdhiQuestionnaire gdhiQuestionnaire) {
        return verifyFields(gdhiQuestionnaire.getCountrySummary())
                && verifyDateRange(gdhiQuestionnaire.getCountrySummary().getCollectedDate())
                && verifyResources(gdhiQuestionnaire.getCountrySummary().getResources())
                && verifyIndicators(gdhiQuestionnaire.getHealthIndicators());
    }

    private List<CountryHealthIndicator> transformToHealthIndicator(String countryId,
                                                                    String status,
                                                                    List<HealthIndicatorDto> healthIndicatorDto, String year) {
        return healthIndicatorDto.stream().map(dto -> {
            CountryHealthIndicatorId countryHealthIndicatorId = new CountryHealthIndicatorId(countryId,
                    dto.getCategoryId(), dto.getIndicatorId(), status, year);
            return new CountryHealthIndicator(countryHealthIndicatorId, dto.getScore(), dto.getSupportingText());
        }).collect(toList());
    }

    public String getCurrentYear() {
        int currentYear = Year.now().getValue();
        String year = new String(String.valueOf(currentYear));
        return year;
    }


    private void calculateAndSaveCountryPhase(String countryId, String status, String year) {
        CountryHealthIndicators countryHealthIndicators = new CountryHealthIndicators(iCountryHealthIndicatorRepository
                .findByCountryHealthIndicatorIdCountryIdAndCountryHealthIndicatorIdYearAndCountryHealthIndicatorIdStatus(countryId, year, status));
        Double overallScore = countryHealthIndicators.getOverallScore();
        Integer countryPhase = new Score(overallScore).convertToPhase();
        iCountryPhaseRepository.save(new CountryPhase(countryId, countryPhase, year));
    }

    private void removeEntriesWithStatus(String countryId, String currentStatus, String currentYear) {
        if (!currentStatus.equals(NEW.name())) {
            iCountryHealthIndicatorRepository.deleteByCountryHealthIndicatorIdCountryIdAndCountryHealthIndicatorIdYearAndCountryHealthIndicatorIdStatus(countryId, currentYear, currentStatus);
        }
        iCountryResourceLinkRepository.deleteByCountryResourceLinkIdCountryIdAndCountryResourceLinkIdYearAndCountryResourceLinkIdStatus(countryId, currentYear, currentStatus);
        iCountrySummaryRepository.deleteByCountrySummaryIdCountryIdAndCountrySummaryIdYearAndCountrySummaryIdStatus(countryId, currentYear, currentStatus);
    }

    private void sendMail(String feederName, String feederRole, String contactEmail, String countryId) {
        Country country = iCountryRepository.findById(countryId);
        mailerService.send(country, feederName, feederRole, contactEmail);

    }

    private void saveCountryContactInfo(String countryId, String status,
                                        CountrySummaryDto countrySummaryDetailDto, String year) {
        CountrySummary countrySummary = new CountrySummary(new CountrySummaryId(countryId, status, year),
                countrySummaryDetailDto);
        iCountryResourceLinkRepository.deleteByCountryResourceLinkIdCountryIdAndCountryResourceLinkIdYearAndCountryResourceLinkIdStatus(countryId, year, status);
        entityManager.flush();
        iCountrySummaryRepository.save(countrySummary);
    }

    private void saveHealthIndicators(String countryId, String status,
                                      List<HealthIndicatorDto> healthIndicatorDto, String year) {
        List<CountryHealthIndicator> countryHealthIndicators = transformToHealthIndicator(countryId, status,
                healthIndicatorDto, year);
        if (countryHealthIndicators != null) {
            countryHealthIndicators.forEach(health -> {
                CountryHealthIndicator countryHealthIndicator = iCountryHealthIndicatorRepository.save(health);
                entityManager.flush();
                entityManager.refresh(countryHealthIndicator);
            });
        }
    }

    private String getStatusOfCountrySummary(String countryId, String currentYear) {
        String currentStatus = null;
        List<CountrySummary> countrySummary = iCountrySummaryRepository.findByCountrySummaryIdCountryIdAndCountrySummaryIdYear(countryId, currentYear);
        List<String> countrySummaryStatuses = countrySummary.stream().map(CountrySummary::getStatus).collect(toList());
        if (!countrySummaryStatuses.isEmpty()) {
            currentStatus = countrySummaryStatuses.size() > 1 ?
                    countrySummaryStatuses.stream()
                            .filter(el -> !el.equalsIgnoreCase(PUBLISHED.toString())).findFirst().get() :
                    countrySummaryStatuses.get(0);
        }
        return currentStatus;
    }

    private boolean verifyIndicators(List<HealthIndicatorDto> healthIndicators) {
        int count = categoryIndicatorService.getHealthIndicatorCount();

        return (healthIndicators != null)
                && (count == healthIndicators.size())
                && healthIndicators.stream().noneMatch(healthIndicatorDto
                -> healthIndicatorDto == null
                || healthIndicatorDto.getScore() == null
                || (ObjectUtils.isEmpty(healthIndicatorDto.getSupportingText())
                || healthIndicatorDto.getScore() < -1)
        );
    }

    private boolean verifyResources(List<String> resources) {
        return (resources != null && !resources.isEmpty());
    }

    private boolean verifyFields(CountrySummaryDto countrySummary) {
        return countrySummary.getCollectedDate() != null
                && StringUtils.hasText(countrySummary.getCountryId())
                && StringUtils.hasText(countrySummary.getContactEmail())
                && StringUtils.hasText(countrySummary.getContactDesignation())
                && StringUtils.hasText(countrySummary.getContactName())
                && StringUtils.hasText(countrySummary.getContactOrganization())
                && StringUtils.hasText(countrySummary.getCountryName())
                && verifyApproverData(countrySummary)
                && StringUtils.hasText(countrySummary.getDataFeederEmail())
                && StringUtils.hasText(countrySummary.getDataFeederName())
                && StringUtils.hasText(countrySummary.getDataFeederRole())
                && StringUtils.hasText(countrySummary.getSummary());
    }

    private boolean verifyApproverData(CountrySummaryDto countrySummary) {
        return (countrySummary.getGovtApproved()) ? StringUtils.hasText(countrySummary.getDataApproverEmail()) && StringUtils.hasText(countrySummary.getDataApproverName()) && StringUtils.hasText(countrySummary.getDataApproverRole()) :
                !StringUtils.hasText(countrySummary.getDataApproverEmail()) && !StringUtils.hasText(countrySummary.getDataApproverName()) && !StringUtils.hasText(countrySummary.getDataApproverRole());
    }

    private boolean verifyDateRange(Date collectedDate) {
        Calendar myCalendar = new GregorianCalendar(2010, 0, 1);
        Date backDate = myCalendar.getTime();
        Date today = new GregorianCalendar().getTime();
        return (collectedDate.equals(today)) || (collectedDate.before(today) && collectedDate.after(backDate));
    }


}