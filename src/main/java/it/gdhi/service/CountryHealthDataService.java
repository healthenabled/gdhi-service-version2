package it.gdhi.service;

import java.util.*;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import it.gdhi.dto.*;
import it.gdhi.model.*;
import it.gdhi.model.id.*;
import it.gdhi.model.CountryHealthIndicators;
import it.gdhi.repository.*;
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
import static it.gdhi.utils.Util.*;
import static java.util.stream.Collectors.*;

@Service
public class CountryHealthDataService {

    @Autowired
    CategoryIndicatorService categoryIndicatorService;
    @Autowired
    RegionService regionService;
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
    private IRegionCountryRepository iRegionCountryRepository;

    @Transactional
    public void save(GdhiQuestionnaire gdhiQuestionnaire, String nextStatus , String currentYear) {
        String currentStatus =
                iCountrySummaryRepository.findByCountrySummaryIdCountryIdAndCountrySummaryIdYearAndStatusNot(gdhiQuestionnaire.getCountryId(), currentYear, PUBLISHED.name()).getStatus();
        if (!nextStatus.equals(currentStatus)) {
            removeEntriesWithStatus(gdhiQuestionnaire.getCountryId(), currentStatus, currentYear);
        }
        saveCountryDetails(gdhiQuestionnaire, nextStatus, currentYear);
    }

    @Transactional
    public void saveCountryDetails(GdhiQuestionnaire gdhiQuestionnaire, String nextStatus, String currentYear) {
        saveCountryContactInfo(gdhiQuestionnaire.getCountryId(),
                nextStatus, gdhiQuestionnaire.getCountrySummary(), currentYear);

        saveHealthIndicators(gdhiQuestionnaire.getCountryId(),
                nextStatus, gdhiQuestionnaire.getHealthIndicators(), currentYear);
    }

    @Transactional
    public CountryUrlGenerationStatusDto saveNewCountrySummary(UUID countryUUID) {
        String countryId = iCountryRepository.findByUniqueId(countryUUID).getId();
        String currentYear = getCurrentYear();

        CountryUrlGenerationStatusDto statusDto;

        String currentStatus = getStatusOfCountrySummary(countryId, currentYear);

        if (isNull(currentStatus)) {
            CountrySummary countrySummary = new CountrySummary(new CountrySummaryId(countryId,currentYear), new CountrySummaryDto(false),NEW.toString());
            iCountrySummaryRepository.save(countrySummary);
            statusDto = new CountryUrlGenerationStatusDto(countryId, true, null);
        }
        else {
            statusDto = new CountryUrlGenerationStatusDto(countryId, false, FormStatus.valueOf(currentStatus));
        }
        return statusDto;
    }

    @Transactional
    public void calculateOverallPhase(String countryId, String currentYear) {
        regionService.calculateAndSaveRegionalData(countryId, currentYear);
        calculateAndSaveCountryPhase(countryId, PUBLISHED.name(), currentYear);
    }

    @Transactional
    public void publish(GdhiQuestionnaire gdhiQuestionnaire, String currentYear) {
        save(gdhiQuestionnaire, PUBLISHED.name() , currentYear);
        calculateOverallPhase(gdhiQuestionnaire.getCountryId(), currentYear);
    }

    @Transactional
    public void republish(GdhiQuestionnaire gdhiQuestionnaire, String currentYear) {
        saveCountryDetails(gdhiQuestionnaire, PUBLISHED.name(), currentYear);
        calculateOverallPhase(gdhiQuestionnaire.getCountryId(), currentYear);
    }

    @Transactional
    public void submit(GdhiQuestionnaire gdhiQuestionnaire) {
        String currentYear = getCurrentYear();
        save(gdhiQuestionnaire, REVIEW_PENDING.name() , currentYear);
        sendMail(gdhiQuestionnaire.getDataFeederName(), gdhiQuestionnaire.getDataFeederRole(),
                gdhiQuestionnaire.getContactEmail(), gdhiQuestionnaire.getCountryId());
    }

    @Transactional
    public void saveCorrection(GdhiQuestionnaire gdhiQuestionnaire) {
        String currentYear = getCurrentYear();
        save(gdhiQuestionnaire, REVIEW_PENDING.name() , currentYear);
    }

    @Transactional
    public void deleteCountryData(UUID countryUUID, String year) {
        String countryId = iCountryRepository.findByUniqueId(countryUUID).getId();
        iCountryHealthIndicatorRepository.deleteByCountryHealthIndicatorIdCountryIdAndCountryHealthIndicatorIdYearAndStatus(countryId, year, REVIEW_PENDING.name());
        iCountryResourceLinkRepository.deleteByCountryResourceLinkIdCountryIdAndCountryResourceLinkIdYearAndStatus(countryId,
                year, REVIEW_PENDING.name());
        iCountrySummaryRepository.deleteByCountrySummaryIdCountryIdAndCountrySummaryIdYearAndStatus(countryId, year,
                REVIEW_PENDING.name());
    }

    @Transactional
    public void calculatePhaseForAllCountries(String year) {
        List<CountrySummary> publishedCountries =
                iCountrySummaryRepository.findByStatusAndCountrySummaryIdYear(year,
                        PUBLISHED.name());

        publishedCountries.stream().forEach(country -> calculateAndSaveCountryPhase(country.getCountrySummaryId().getCountryId(), PUBLISHED.name(),
                year));
    }

    public CountrySummaryStatusYearDto getAllCountryStatusSummaries() {
        String currentYear = getCurrentYear();
        List<CountrySummary> countrySummaries =
                iCountrySummaryRepository.findByCountrySummaryIdYearOrderByUpdatedAtDesc(currentYear);

        List<CountrySummaryStatusDto> countrySummaryStatusDtos = countrySummaries
                .stream().map(CountrySummaryStatusDto::new).collect(toList());

        Map<String, List<CountrySummaryStatusDto>> groupByCountrySummaryStatus = countrySummaryStatusDtos.stream()
                .collect(groupingBy(CountrySummaryStatusDto::getStatus));

        CountrySummaryStatusYearDto countrySummaryStatusYearDto = new CountrySummaryStatusYearDto(currentYear,
                groupByCountrySummaryStatus.get(NEW.name()),
                groupByCountrySummaryStatus.get(DRAFT.name()), groupByCountrySummaryStatus.get(PUBLISHED.name()),
                groupByCountrySummaryStatus.get(REVIEW_PENDING.name()));


        return countrySummaryStatusYearDto;
    }

    public Map<Integer, BenchmarkDto> getBenchmarkDetailsFor(String countryId, Integer benchmarkType, String year) {
        return benchmarkService.getBenchmarkFor(countryId, benchmarkType, year);
    }

    public boolean validateRequiredFields(GdhiQuestionnaire gdhiQuestionnaire) {
        return verifyFields(gdhiQuestionnaire.getCountrySummary())
                && verifyIndicators(gdhiQuestionnaire.getHealthIndicators());
    }

    private List<CountryHealthIndicator> transformToHealthIndicator(String countryId,
                                                                    String status,
                                                                    List<HealthIndicatorDto> healthIndicatorDto,
                                                                    String year) {
        return healthIndicatorDto.stream().map(dto -> {
            CountryHealthIndicatorId countryHealthIndicatorId = new CountryHealthIndicatorId(countryId,
                    dto.getCategoryId(), dto.getIndicatorId(), year);
            return new CountryHealthIndicator(countryHealthIndicatorId, dto.getScore(), dto.getSupportingText(), status);
        }).collect(toList());
    }

    private void calculateAndSaveCountryPhase(String countryId, String status, String year) {
        CountryHealthIndicators countryHealthIndicators = new CountryHealthIndicators(iCountryHealthIndicatorRepository
                .findByCountryHealthIndicatorIdCountryIdAndCountryHealthIndicatorIdYearAndStatus(countryId, year, status));
        Double overallScore = countryHealthIndicators.getOverallScore();
        Integer countryPhaseVal = new Score(overallScore).convertToPhase();
        CountryPhase countryPhase =
                iCountryPhaseRepository.findByCountryPhaseIdCountryIdAndCountryPhaseIdYear(countryId, year);
        if (countryPhase == null || !Objects.equals(countryPhaseVal, countryPhase.getCountryOverallPhase())) {
            iCountryPhaseRepository.save(new CountryPhase(countryId, countryPhaseVal, year));
        }
    }

    private void removeEntriesWithStatus(String countryId, String currentStatus, String currentYear) {
        if (!currentStatus.equals(NEW.name())) {
            iCountryHealthIndicatorRepository.deleteByCountryHealthIndicatorIdCountryIdAndCountryHealthIndicatorIdYearAndStatus(countryId, currentYear, currentStatus);
        }
        iCountryResourceLinkRepository.deleteByCountryResourceLinkIdCountryIdAndCountryResourceLinkIdYearAndStatus(countryId,
                currentYear, currentStatus);
        iCountrySummaryRepository.deleteByCountrySummaryIdCountryIdAndCountrySummaryIdYearAndStatus(countryId, currentYear,
                currentStatus);
    }

    private void sendMail(String feederName, String feederRole, String contactEmail, String countryId) {
        Country country = iCountryRepository.findById(countryId);
        mailerService.send(country, feederName, feederRole, contactEmail);

    }

    private void saveCountryContactInfo(String countryId, String status,
                                        CountrySummaryDto countrySummaryDetailDto, String year) {
        CountrySummary countrySummary = new CountrySummary(new CountrySummaryId(countryId, year),
                countrySummaryDetailDto, status);
        iCountryResourceLinkRepository.deleteByCountryResourceLinkIdCountryIdAndCountryResourceLinkIdYearAndStatus(countryId,
                year, status);
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
        List<CountrySummary> countrySummary =
                iCountrySummaryRepository.findByCountrySummaryIdCountryIdAndCountrySummaryIdYear(countryId,
                        currentYear);
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

    private boolean verifyFields(CountrySummaryDto countrySummary) {
        return StringUtils.hasText(countrySummary.getCountryId())
                && StringUtils.hasText(countrySummary.getCountryName())
                && hasValidApproverData(countrySummary)
                && StringUtils.hasText(countrySummary.getDataFeederEmail())
                && StringUtils.hasText(countrySummary.getDataFeederName())
                && StringUtils.hasText(countrySummary.getDataFeederRole())
                && StringUtils.hasText(countrySummary.getSummary());
    }

    private boolean hasValidApproverData(CountrySummaryDto countrySummary) {
        return Boolean.TRUE.equals((countrySummary.getGovtApproved())) ?
                StringUtils.hasText(countrySummary.getDataApproverEmail()) &&
                        StringUtils.hasText(countrySummary.getDataApproverName()) &&
                        StringUtils.hasText(countrySummary.getDataApproverRole()) :

                !StringUtils.hasText(countrySummary.getDataApproverEmail()) &&
                        !StringUtils.hasText(countrySummary.getDataApproverName()) &&
                        !StringUtils.hasText(countrySummary.getDataApproverRole());
    }

}