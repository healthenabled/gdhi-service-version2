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
    private ICountryHealthIndicatorRepository iCountryHealthIndicatorRepository;

    @Autowired
    private IRegionalIndicatorDataRepository iRegionalIndicatorDataRepository;

    @Autowired
    private IRegionalCategoryDataRepository iRegionalCategoryDataRepository;

    @Autowired
    private IRegionalOverallDataRepository iRegionalOverallRepository;

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

    @Autowired
    private IRegionCountryRepository iRegionCountryRepository;


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
        String currentYear = getCurrentYear();

        CountryUrlGenerationStatusDto statusDto;

        String currentStatus = getStatusOfCountrySummary(countryId, currentYear);

        if (isNull(currentStatus)) {
            CountrySummary countrySummary = new CountrySummary(new CountrySummaryId(countryId, NEW.toString(), currentYear),
                    new CountrySummaryDto(false));
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
        saveRegionalIndicatorData(gdhiQuestionnaire.getCountryId(), currentYear);
        saveRegionalCategoryData(gdhiQuestionnaire.getCountryId(), currentYear);
        saveRegionalOverallData(gdhiQuestionnaire.getCountryId(), currentYear);
        calculateAndSaveCountryPhase(gdhiQuestionnaire.getCountryId(), PUBLISHED.name(), currentYear);
    }

    public Map<String, List<String>> getListOfCountriesAndRegionId(String countryId) {
        RegionCountry regionCountry = iRegionCountryRepository.findByRegionCountryIdCountryId(countryId);
        String regionId = regionCountry.getRegionCountryId().getRegionId();
        List<String> countries = iRegionCountryRepository.findByRegionCountryIdRegionId(regionId);

        Map<String, List<String>> map = new HashMap<>();
        map.put(regionId, countries);
        return map;
    }

    public List<RegionalIndicatorData> calculateRegionalIndicatorDataFor(List<CountryHealthIndicator> countryHealthIndicators, String regionId) {
        Map<Integer, Double> regionalIndicators =
                countryHealthIndicators.stream()
                        .map(regionalIndicator -> {
                            regionalIndicator.convertNullScoreToNotAvailable();
                            return regionalIndicator;
                        })
                        .filter(indicator -> indicator.getIndicator().getParentId() == null && indicator.getScore() != -1)
                        .collect(groupingBy(CountryHealthIndicator::getIndicatorId,
                                averagingInt(CountryHealthIndicator::getScore)));

        return regionalIndicators.entrySet().stream().map(dto -> {
            return RegionalIndicatorData.builder().regionalIndicatorId(new RegionalIndicatorId(regionId, dto.getKey(), getCurrentYear()))
                    .score(new Score(dto.getValue()).convertToPhase()).build();
        }).collect(toList());
    }

    @Transactional
    private void saveRegionalIndicatorData(String countryId, String currentYear) {
        Map<String, List<String>> map = getListOfCountriesAndRegionId(countryId);
        String regionId = map.keySet().stream().toList().get(0);
        List<String> countries = map.get(regionId);

        List<CountryHealthIndicator> countryHealthIndicators = iCountryHealthIndicatorRepository.findByCountryHealthIndicatorIdCountryIdInAndCountryHealthIndicatorIdYearAndCountryHealthIndicatorIdStatus(countries, currentYear, PUBLISHED.name());
        List<RegionalIndicatorData> regionalIndicatorsData = calculateRegionalIndicatorDataFor(countryHealthIndicators, regionId);

        iRegionalIndicatorDataRepository.deleteByRegionalIndicatorIdRegionIdAndRegionalIndicatorIdYear(regionId, currentYear);

        if (regionalIndicatorsData != null) {
            regionalIndicatorsData.forEach(regionalIndicator -> {
                RegionalIndicatorData regionalIndicatorData1 = iRegionalIndicatorDataRepository.save(regionalIndicator);
                entityManager.flush();
                entityManager.refresh(regionalIndicatorData1);
            });
        }
    }

    public List<RegionalCategoryData> calculateRegionalCategoriesDataFor(CountryHealthIndicators countryHealthIndicators, String regionId) {
        Map<Integer, Double> CategoryScore = countryHealthIndicators.groupByCategoryIdWithoutNullAndNegativeScores();

        List<RegionalCategoryData> regionalCategoryData = countryHealthIndicators.groupByCategory().entrySet().stream().map(entry -> {
            Category category = entry.getKey();
            RegionalCategoryId regionalCategoryId = new RegionalCategoryId(regionId, category.getId(), getCurrentYear());
            Double score = CategoryScore.get(category.getId()) == null ? -1 : CategoryScore.get(category.getId());
            return new RegionalCategoryData(regionalCategoryId, new Score(score).convertToPhase());
        }).collect(toList());
        return regionalCategoryData;
    }

    @Transactional
    private void saveRegionalCategoryData(String countryId, String currentYear) {
        Map<String, List<String>> map = getListOfCountriesAndRegionId(countryId);
        String regionId = map.keySet().stream().toList().get(0);
        List<String> countries = map.get(regionId);

        List<CountryHealthIndicator> countryHealthIndicators = iCountryHealthIndicatorRepository.findByCountryHealthIndicatorIdCountryIdInAndCountryHealthIndicatorIdYearAndCountryHealthIndicatorIdStatus(countries, currentYear, PUBLISHED.name());
        CountryHealthIndicators countryHealthIndicators1 = new CountryHealthIndicators(countryHealthIndicators);
        List<RegionalCategoryData> regionalCategoriesData = calculateRegionalCategoriesDataFor(countryHealthIndicators1, regionId);

        iRegionalCategoryDataRepository.deleteByRegionalCategoryIdRegionIdAndRegionalCategoryIdYear(regionId, currentYear);

        if (regionalCategoriesData != null) {
            regionalCategoriesData.forEach(regionalCategoryData -> {
                RegionalCategoryData regionalCategoryData1 = iRegionalCategoryDataRepository.save(regionalCategoryData);
                entityManager.flush();
                entityManager.refresh(regionalCategoryData1);
            });
        }
    }

    public RegionalOverallData calculateRegionalOverallDataFor(CountryHealthIndicators countryHealthIndicators, String regionId) {
        Map<Integer, Double> CategoryScore = countryHealthIndicators.groupByCategoryIdWithoutNullAndNegativeScores();
        Double averageOverallScore = CategoryScore.entrySet().stream().mapToDouble(Map.Entry::getValue).average().getAsDouble();
        RegionalOverallData regionalOverallData = RegionalOverallData.builder().regionalOverallId(RegionalOverallId.builder().regionId(regionId).year(getCurrentYear()).build())
                .overAllScore(Score.builder().value(averageOverallScore).build().convertToPhase()).build();
        return regionalOverallData;
    }

    @Transactional
    private void saveRegionalOverallData(String countryId, String year) {
        Map<String, List<String>> map = getListOfCountriesAndRegionId(countryId);
        String regionId = map.keySet().stream().toList().get(0);
        List<String> countries = map.get(regionId);

        List<CountryHealthIndicator> countryHealthIndicators = iCountryHealthIndicatorRepository.
                findByCountryHealthIndicatorIdCountryIdInAndCountryHealthIndicatorIdYearAndCountryHealthIndicatorIdStatus(countries, year, PUBLISHED.name());
        CountryHealthIndicators countryHealthIndicators1 = new CountryHealthIndicators(countryHealthIndicators);
        RegionalOverallData regionalOverallData = calculateRegionalOverallDataFor(countryHealthIndicators1, regionId);
        iRegionalOverallRepository.deleteByRegionalOverallIdRegionIdAndRegionalOverallIdYear(regionId, year);
        if (regionalOverallData != null) {
            RegionalOverallData regionalOverallData1 = iRegionalOverallRepository.save(regionalOverallData);
            entityManager.flush();
            entityManager.refresh(regionalOverallData1);
        }
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
        List<CountrySummary> publishedCountries = iCountrySummaryRepository.findByCountrySummaryIdYearAndCountrySummaryIdStatus(year, PUBLISHED.name());

        publishedCountries.stream().forEach(country -> calculateAndSaveCountryPhase(country.getCountrySummaryId().getCountryId(), PUBLISHED.name(), year));
    }

    public CountrySummaryStatusYearDto getAllCountryStatusSummaries() {
        String currentYear = getCurrentYear();
        List<CountrySummary> countrySummaries = iCountrySummaryRepository.findByCountrySummaryIdYearOrderByUpdatedAtDesc(currentYear);

        List<CountrySummaryStatusDto> countrySummaryStatusDtos = countrySummaries
                .stream().map(CountrySummaryStatusDto::new).collect(toList());

        Map<String, List<CountrySummaryStatusDto>> groupByCountrySummaryStatus = countrySummaryStatusDtos.stream()
                .collect(groupingBy(CountrySummaryStatusDto::getStatus));

        CountrySummaryStatusYearDto countrySummaryStatusYearDto = new CountrySummaryStatusYearDto(currentYear, groupByCountrySummaryStatus.get(NEW.name()),
                groupByCountrySummaryStatus.get(DRAFT.name()), groupByCountrySummaryStatus.get(PUBLISHED.name()), groupByCountrySummaryStatus.get(REVIEW_PENDING.name()));


        return countrySummaryStatusYearDto;
    }

    public Map<Integer, BenchmarkDto> getBenchmarkDetailsFor(String countryId, Integer benchmarkType, String year, String region) {
        return benchmarkService.getBenchmarkFor(countryId, benchmarkType, year, region);
    }

    public boolean validateRequiredFields(GdhiQuestionnaire gdhiQuestionnaire) {
        return verifyFields(gdhiQuestionnaire.getCountrySummary())
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