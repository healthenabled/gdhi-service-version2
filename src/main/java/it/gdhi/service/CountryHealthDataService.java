package it.gdhi.service;

import it.gdhi.dto.*;
import it.gdhi.model.*;
import it.gdhi.model.id.CountryHealthIndicatorId;
import it.gdhi.model.id.CountrySummaryId;
import it.gdhi.repository.*;
import it.gdhi.utils.FormStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import java.time.Year;
import java.util.*;

import static it.gdhi.utils.FormStatus.*;
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
        String currentStatus = iCountrySummaryRepository.findByCountrySummaryIdCountryIdAndCountrySummaryIdStatusNotAndCountrySummaryIdYear(gdhiQuestionnaire.getCountryId(), PUBLISHED.name(), currentYear).getStatus();
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

        CountryUrlGenerationStatusDto statusDto;

        String currentStatus = getStatusOfCountrySummary(countryId);

        if (isNull(currentStatus) || currentStatus.equalsIgnoreCase(PUBLISHED.toString())) {
            CountrySummary countrySummary = new CountrySummary(new CountrySummaryId(countryId, NEW.toString(), "Version1"),
                    new CountrySummaryDto());
            iCountrySummaryRepository.save(countrySummary);
            statusDto = new CountryUrlGenerationStatusDto(countryId, true, isNull(currentStatus) ? null :
                    FormStatus.valueOf(currentStatus));
        } else {
            statusDto = new CountryUrlGenerationStatusDto(countryId, false, FormStatus.valueOf(currentStatus));
        }
        return statusDto;
    }

    @Transactional
    public void publish(GdhiQuestionnaire gdhiQuestionnaire) {
        save(gdhiQuestionnaire, PUBLISHED.name());
        String currentYear = getCurrentYear();
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
    public void deleteCountryData(UUID countryUUID) {
        String countryId = iCountryRepository.findByUniqueId(countryUUID).getId();
        iCountryHealthIndicatorRepository.removeHealthIndicatorsBy(countryId, REVIEW_PENDING.name());
        iCountryResourceLinkRepository.deleteResources(countryId, REVIEW_PENDING.name());
        iCountrySummaryRepository.removeCountrySummary(countryId, REVIEW_PENDING.name());
    }

    @Transactional
    public void calculatePhaseForAllCountries(String year) {
        List<String> publishedCountries = iCountrySummaryRepository.findAllByStatus(PUBLISHED.name());
        publishedCountries.stream().forEach(country -> calculateAndSaveCountryPhase(country, PUBLISHED.name(), year));
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
                .findByCountryHealthIndicatorIdCountryIdAndCountryHealthIndicatorIdStatusAndCountryHealthIndicatorIdYear(countryId, status, year));
        Double overallScore = countryHealthIndicators.getOverallScore();
        Integer countryPhase = new Score(overallScore).convertToPhase();
        iCountryPhaseRepository.save(new CountryPhase(countryId, countryPhase, year));
    }

    private void removeEntriesWithStatus(String countryId, String currentStatus, String currentYear) {
        if (!currentStatus.equals(NEW.name())) {
            iCountryHealthIndicatorRepository.deleteByCountryHealthIndicatorIdCountryIdAndCountryHealthIndicatorIdStatusAndCountryHealthIndicatorIdYear(countryId, currentStatus, currentYear);
        }
        iCountryResourceLinkRepository.deleteByCountryResourceLinkIdCountryIdAndCountryResourceLinkIdStatusAndCountryResourceLinkIdYear(countryId, currentStatus, currentYear);
        iCountrySummaryRepository.deleteByCountrySummaryIdCountryIdAndCountrySummaryIdStatusAndCountrySummaryIdYear(countryId, currentStatus, currentYear);
    }

    private void sendMail(String feederName, String feederRole, String contactEmail, String countryId) {
        Country country = iCountryRepository.findById(countryId);
        mailerService.send(country, feederName, feederRole, contactEmail);

    }

    private void saveCountryContactInfo(String countryId, String status,
                                        CountrySummaryDto countrySummaryDetailDto, String year) {
        CountrySummary countrySummary = new CountrySummary(new CountrySummaryId(countryId, status, year),
                countrySummaryDetailDto);
        iCountryResourceLinkRepository.deleteByCountryResourceLinkIdCountryIdAndCountryResourceLinkIdStatusAndCountryResourceLinkIdYear(countryId, status, year);
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

    private String getStatusOfCountrySummary(String countryId) {
        String currentStatus = null;
        List<String> countrySummaryStatuses = iCountrySummaryRepository.getAllStatus(countryId);
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
                && StringUtils.hasText(countrySummary.getDataApproverEmail())
                && StringUtils.hasText(countrySummary.getDataApproverName())
                && StringUtils.hasText(countrySummary.getDataApproverRole())
                && StringUtils.hasText(countrySummary.getDataFeederEmail())
                && StringUtils.hasText(countrySummary.getDataFeederName())
                && StringUtils.hasText(countrySummary.getDataFeederRole())
                && StringUtils.hasText(countrySummary.getSummary());
    }

    private boolean verifyDateRange(Date collectedDate) {
        Calendar myCalendar = new GregorianCalendar(2010, 0, 1);
        Date backDate = myCalendar.getTime();
        Date today = new GregorianCalendar().getTime();
        return (collectedDate.equals(today)) || (collectedDate.before(today) && collectedDate.after(backDate));
    }


}