package it.gdhi.service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import it.gdhi.dto.BenchmarkDto;
import it.gdhi.dto.CategoryHealthScoreDto;
import it.gdhi.dto.GlobalHealthScoreDto;
import it.gdhi.dto.RegionCountriesDto;
import it.gdhi.dto.RegionCountryHealthScoreDto;
import it.gdhi.dto.RegionCountryHealthScoreYearDto;
import it.gdhi.internationalization.service.CountryNameTranslator;
import it.gdhi.internationalization.service.HealthIndicatorTranslator;
import it.gdhi.internationalization.service.RegionNameTranslator;
import it.gdhi.model.Category;
import it.gdhi.model.CountryHealthIndicator;
import it.gdhi.model.CountryHealthIndicators;
import it.gdhi.model.CountryPhase;
import it.gdhi.model.Region;
import it.gdhi.model.RegionCountry;
import it.gdhi.model.RegionalCategoryData;
import it.gdhi.model.RegionalIndicatorData;
import it.gdhi.model.RegionalOverallData;
import it.gdhi.model.Score;
import it.gdhi.model.id.RegionalCategoryId;
import it.gdhi.model.id.RegionalIndicatorId;
import it.gdhi.model.id.RegionalOverallId;
import it.gdhi.repository.ICountryHealthIndicatorRepository;
import it.gdhi.repository.ICountryPhaseRepository;
import it.gdhi.repository.ICountryRepository;
import it.gdhi.repository.IRegionCountryRepository;
import it.gdhi.repository.IRegionRepository;
import it.gdhi.repository.IRegionalCategoryDataRepository;
import it.gdhi.repository.IRegionalIndicatorDataRepository;
import it.gdhi.repository.IRegionalOverallDataRepository;
import it.gdhi.utils.LanguageCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static it.gdhi.controller.strategy.FilterStrategy.getCategoryPhaseFilter;
import static it.gdhi.utils.FormStatus.PUBLISHED;
import static java.util.stream.Collectors.averagingInt;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

@Service
public class RegionService {

    @Autowired
    private IRegionRepository iRegionRepository;

    @Autowired
    private RegionNameTranslator regionNameTranslator;

    @Autowired
    private IRegionCountryRepository iRegionCountryRepository;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private ICountryHealthIndicatorRepository iCountryHealthIndicatorRepository;

    @Autowired
    private IRegionalIndicatorDataRepository iRegionalIndicatorDataRepository;

    @Autowired
    private IRegionalCategoryDataRepository iRegionalCategoryDataRepository;

    @Autowired
    private IRegionalOverallDataRepository iRegionalOverallRepository;

    @Autowired
    private CategoryIndicatorService categoryIndicatorService;

    @Autowired
    private BenchMarkService benchmarkService;

    @Autowired
    private CountryHealthIndicatorService countryHealthIndicatorService;

    @Autowired
    private HealthIndicatorTranslator healthIndicatorTranslator;

    @Autowired
    private ICountryRepository iCountryRepository;

    @Autowired
    private CountryNameTranslator countryNameTranslator;

    @Autowired
    private CountryService countryService;

    @Autowired
    private ICountryPhaseRepository iCountryPhaseRepository;


    public List<Region> fetchRegions(LanguageCode languageCode) {
        List<Region> regions = iRegionRepository.findAll();
        return regionNameTranslator.translate(regions, languageCode);
    }

    public List<String> fetchCountriesForARegion(String regionId) {
        return iRegionCountryRepository.findByRegionCountryIdRegionId(regionId);
    }

    public Integer fetchRegionOverallScore(String regionID, String year) {
        RegionalOverallData regionalOverallData =
                iRegionalOverallRepository.findByRegionalOverallIdRegionIdAndRegionalOverallIdYear(regionID, year);
        return regionalOverallData.getOverAllScore();
    }

    public void calculateAndSaveRegionalData(String countryId, String currentYear) {
        saveRegionalIndicatorData(countryId, currentYear);
        saveRegionalCategoryData(countryId, currentYear);
        saveRegionalOverallData(countryId, currentYear);
    }

    @Transactional
    private void saveRegionalOverallData(String regionId, List<String> countries, String year) {

        List<CountryHealthIndicator> countryHealthIndicators =
                iCountryHealthIndicatorRepository.findByCountryHealthIndicatorIdCountryIdInAndCountryHealthIndicatorIdYearAndCountryHealthIndicatorIdStatus(countries, year, PUBLISHED.name());
        if (countryHealthIndicators.size() > 0) {
            CountryHealthIndicators countryHealthIndicators1 = new CountryHealthIndicators(countryHealthIndicators);
            RegionalOverallData regionalOverallData = calculateRegionalOverallDataFor(countryHealthIndicators1,
                    regionId, year);

            if (regionalOverallData != null) {
                iRegionalOverallRepository.save(regionalOverallData);
            }
        }
    }

    @Transactional
    private void saveRegionalIndicatorData(String regionId, List<String> countries, String year) {

        List<CountryHealthIndicator> countryHealthIndicators =
                iCountryHealthIndicatorRepository.findByCountryHealthIndicatorIdCountryIdInAndCountryHealthIndicatorIdYearAndCountryHealthIndicatorIdStatus(countries, year, PUBLISHED.name());
        List<RegionalIndicatorData> regionalIndicatorsData =
                calculateRegionalIndicatorDataFor(countryHealthIndicators, regionId, year);

        if (regionalIndicatorsData != null) {
            regionalIndicatorsData.forEach(regionalIndicator -> {
                iRegionalIndicatorDataRepository.save(regionalIndicator);
            });
        }
    }

    @Transactional
    public void saveRegionalCategoryData(String regionId, List<String> countries, String year) {

        List<CountryHealthIndicator> countryHealthIndicators =
                iCountryHealthIndicatorRepository.findByCountryHealthIndicatorIdCountryIdInAndCountryHealthIndicatorIdYearAndCountryHealthIndicatorIdStatus(countries, year, PUBLISHED.name());
        if (countryHealthIndicators.size() > 0) {
            CountryHealthIndicators countryHealthIndicators1 = new CountryHealthIndicators(countryHealthIndicators);
            List<RegionalCategoryData> regionalCategoriesData =
                    calculateRegionalCategoriesDataFor(countryHealthIndicators1, regionId, year);

            if (regionalCategoriesData != null) {
                regionalCategoriesData.forEach(regionalCategoryData -> {
                    iRegionalCategoryDataRepository.save(regionalCategoryData);
                });
            }
        }
    }

    public List<RegionalCategoryData> calculateRegionalCategoriesDataFor(CountryHealthIndicators countryHealthIndicators, String regionId,
                                                                         String year) {
        Map<Integer, Double> CategoryScore = countryHealthIndicators.groupByCategoryIdWithoutNullAndNegativeScores();

        List<RegionalCategoryData> regionalCategoryData =
                countryHealthIndicators.groupByCategory().entrySet().stream().map(entry -> {
                    Category category = entry.getKey();
                    RegionalCategoryId regionalCategoryId = new RegionalCategoryId(regionId, category.getId(), year);
                    Double score = CategoryScore.get(category.getId()) == null ? -1 :
                            CategoryScore.get(category.getId());
                    return new RegionalCategoryData(regionalCategoryId, new Score(score).convertToPhase());
                }).collect(toList());
        return regionalCategoryData;
    }

    public List<RegionalIndicatorData> calculateRegionalIndicatorDataFor(List<CountryHealthIndicator> countryHealthIndicators, String regionId,
                                                                         String year) {
        Map<Integer, Double> regionalIndicators =
                countryHealthIndicators.stream()
                        .map(regionalIndicator -> {
                            regionalIndicator.convertNullScoreToNotAvailable();
                            return regionalIndicator;
                        })
                        .filter(indicator -> indicator.getIndicator().getParentId() == null && indicator.getScore() != -1)
                        .collect(groupingBy(CountryHealthIndicator::getIndicatorId,
                                averagingInt(CountryHealthIndicator::getScore)));

        regionalIndicators.putAll(defaultToNAWhenAllIndicatorsAreNA(countryHealthIndicators));

        return regionalIndicators.entrySet().stream().map(dto -> {
            return RegionalIndicatorData.builder().regionalIndicatorId(new RegionalIndicatorId(regionId, dto.getKey()
                            , year))
                    .score(new Score(dto.getValue()).convertToPhase()).build();
        }).collect(toList());
    }

    public RegionalOverallData calculateRegionalOverallDataFor(CountryHealthIndicators countryHealthIndicators,
                                                               String regionId, String year) {
        Map<Integer, Double> CategoryScore = countryHealthIndicators.groupByCategoryIdWithoutNullAndNegativeScores();
        Double averageOverallScore =
                CategoryScore == null ? -1.0 : CategoryScore.entrySet().stream().mapToDouble(Map.Entry::getValue).average().getAsDouble();

        return RegionalOverallData.builder().regionalOverallId(RegionalOverallId.builder().regionId(regionId).year(year).build())
                .overAllScore(Score.builder().value(averageOverallScore).build().convertToPhase()).build();
    }

    @Transactional
    public void calculateAndSaveRegionScores(String regionId, String year) {
        List<String> countriesForRegions = iRegionCountryRepository.findByRegionCountryIdRegionId(regionId);
        saveRegionalIndicatorData(regionId, countriesForRegions, year);
        saveRegionalCategoryData(regionId, countriesForRegions, year);
        saveRegionalOverallData(regionId, countriesForRegions, year);
    }

    @Transactional
    public void calculatePhaseForAllRegions(String year) {
        List<Region> regions = iRegionRepository.findAll();
        regions.stream().forEach(region -> calculateAndSaveRegionScores(region.getRegionId(), year));
    }

    @Transactional
    void saveRegionalIndicatorData(String countryId, String year) {
        Map<String, List<String>> map = getListOfCountriesAndRegionId(countryId);
        String regionId = map.keySet().stream().toList().get(0);
        List<String> countries = map.get(regionId);

        List<CountryHealthIndicator> countryHealthIndicators =
                iCountryHealthIndicatorRepository.findByCountryHealthIndicatorIdCountryIdInAndCountryHealthIndicatorIdYearAndCountryHealthIndicatorIdStatus(countries, year, PUBLISHED.name());
        List<RegionalIndicatorData> regionalIndicatorsData =
                calculateRegionalIndicatorDataFor(countryHealthIndicators, regionId, year);

        if (regionalIndicatorsData != null) {
            regionalIndicatorsData.forEach(regionalIndicator -> {
                iRegionalIndicatorDataRepository.save(regionalIndicator);
            });
        }
    }

    @Transactional
    void saveRegionalCategoryData(String countryId, String year) {
        Map<String, List<String>> map = getListOfCountriesAndRegionId(countryId);
        String regionId = map.keySet().stream().toList().get(0);
        List<String> countries = map.get(regionId);

        List<CountryHealthIndicator> countryHealthIndicators =
                iCountryHealthIndicatorRepository.findByCountryHealthIndicatorIdCountryIdInAndCountryHealthIndicatorIdYearAndCountryHealthIndicatorIdStatus(countries, year, PUBLISHED.name());
        CountryHealthIndicators countryHealthIndicators1 = new CountryHealthIndicators(countryHealthIndicators);
        List<RegionalCategoryData> regionalCategoriesData =
                calculateRegionalCategoriesDataFor(countryHealthIndicators1, regionId, year);

        if (regionalCategoriesData != null) {
            regionalCategoriesData.forEach(regionalCategoryData -> {
                iRegionalCategoryDataRepository.save(regionalCategoryData);
            });
        }
    }

    @Transactional
    void saveRegionalOverallData(String countryId, String year) {
        Map<String, List<String>> map = getListOfCountriesAndRegionId(countryId);
        String regionId = map.keySet().stream().toList().get(0);
        List<String> countries = map.get(regionId);

        List<CountryHealthIndicator> countryHealthIndicators = iCountryHealthIndicatorRepository.
                findByCountryHealthIndicatorIdCountryIdInAndCountryHealthIndicatorIdYearAndCountryHealthIndicatorIdStatus(countries, year,
                        PUBLISHED.name());
        CountryHealthIndicators countryHealthIndicators1 = new CountryHealthIndicators(countryHealthIndicators);
        RegionalOverallData regionalOverallData = calculateRegionalOverallDataFor(countryHealthIndicators1, regionId,
                year);

        if (regionalOverallData != null) {
            iRegionalOverallRepository.save(regionalOverallData);
        }
    }

    public Map<String, List<String>> getListOfCountriesAndRegionId(String countryId) {
        RegionCountry regionCountry = iRegionCountryRepository.findByRegionCountryIdCountryId(countryId);
        String regionId = regionCountry.getRegionCountryId().getRegionId();
        List<String> countries = iRegionCountryRepository.findByRegionCountryIdRegionId(regionId);

        Map<String, List<String>> map = new HashMap<>();
        map.put(regionId, countries);
        return map;
    }

    public List<RegionalCategoryData> fetchRegionalCategoryScores(String regionId, String year) {
        return iRegionalCategoryDataRepository.findByRegionalCategoryIdRegionIdAndRegionalCategoryIdYearOrderByRegionalCategoryIdCategoryId(regionId, year);
    }

    public GlobalHealthScoreDto fetchRegionalHealthScores(Integer categoryId, String regionId,
                                                          LanguageCode languageCode, String year) {
        if (isRegionalCategoryDataPresent(regionId, year)) {
            List<RegionalCategoryData> regionalCategoriesData = (categoryId == null) ?
                    iRegionalCategoryDataRepository.findByRegionalCategoryIdRegionIdAndRegionalCategoryIdYearOrderByRegionalCategoryIdCategoryId(regionId, year)
                    : Collections.singletonList(iRegionalCategoryDataRepository.
                    findByRegionalCategoryIdRegionIdAndRegionalCategoryIdYearAndRegionalCategoryIdCategoryIdOrderByRegionalCategoryIdCategoryId(regionId, year, categoryId));
            List<Category> categories = categoryIndicatorService.getAllCategories();
            List<CategoryHealthScoreDto> categoryHealthScoreDtos =
                    regionalCategoriesData.stream().map(regionalCategoryData -> CategoryHealthScoreDto.builder()
                            .id(regionalCategoryData.getRegionalCategoryId().getCategoryId())
                            .name(categories.get(regionalCategoryData.getRegionalCategoryId().getCategoryId() - 1).getName())
                            .overallScore(null)
                            .phase(regionalCategoryData.getScore())
                            .indicators(null)
                            .build()).collect(Collectors.toList());
            return translateCategoryNames(new GlobalHealthScoreDto(fetchRegionOverallScore(regionId, year),
                    categoryHealthScoreDtos), languageCode);
        }
        else {
            return new GlobalHealthScoreDto(null, Collections.emptyList());
        }
    }

    public Map<Integer, Integer> fetchRegionalIndicatorScoreData(String regionId, String year) {
        List<RegionalIndicatorData> regionalIndicatorsData =
                iRegionalIndicatorDataRepository.findByRegionalIndicatorIdRegionIdAndRegionalIndicatorIdYear(regionId
                        , year);
        return regionalIndicatorsData.stream().collect(Collectors.toMap(RegionalIndicatorData::getRegionalIndicatorId,
                RegionalIndicatorData::getScore));
    }

    public Map<Integer, BenchmarkDto> getBenchmarkDetailsForRegion(String countryId, String year, String region) {
        return benchmarkService.getBenchMarkForRegion(countryId, year, region);
    }

    private GlobalHealthScoreDto translateCategoryNames(GlobalHealthScoreDto globalHealthScoreDto, LanguageCode code) {
        globalHealthScoreDto
                .getCategories()
                .forEach((category) -> {
                    String translatedCategory = healthIndicatorTranslator.getTranslatedCategory(category.getName(),
                            code);
                    category.translateCategoryName(translatedCategory);
                });
        return globalHealthScoreDto;
    }

    public boolean isRegionalCategoryDataPresent(String regionId, String year) {
        List<RegionalCategoryData> regionalCategoriesData =
                iRegionalCategoryDataRepository.findDistinctByRegionalCategoryIdRegionIdOrderByRegionalCategoryIdCategoryId(regionId);
        List<String> years = regionalCategoriesData.stream().map(RegionalCategoryData::getRegionalCategoryId).
                map(RegionalCategoryId::getYear).distinct().toList();
        if (years.contains(year)) {
            return true;
        }
        else {
            return false;
        }
    }

    public RegionCountriesDto getRegionCountriesData(String regionId, List<String> years,
                                                     LanguageCode languageCode) {
        List<String> countries = iRegionCountryRepository.findByRegionCountryIdRegionId(regionId);
        return fetchRegionCountriesHealthScoresForGivenYears(countries, years, languageCode);
    }

    public RegionCountriesDto fetchRegionCountriesHealthScoresForGivenYears(List<String> countryIds,
                                                                            List<String> years,
                                                                            LanguageCode languageCode) {
        List<CountryHealthIndicator> countryHealthIndicators = iCountryHealthIndicatorRepository.
                findByCountryHealthIndicatorIdCountryIdInAndCountryHealthIndicatorIdYearInAndCountryHealthIndicatorIdStatus(countryIds, years,
                        PUBLISHED.name());
        List<CountryPhase> countryPhases =
                iCountryPhaseRepository.findByCountryPhaseIdCountryIdInAndCountryPhaseIdYearIn(countryIds, years);
        return constructRegionCountriesDto(countryHealthIndicators, countryPhases, languageCode);
    }

    public RegionCountriesDto constructRegionCountriesDto(List<CountryHealthIndicator> countryHealthIndicators,
                                                          List<CountryPhase> countryPhases, LanguageCode languageCode) {
        RegionCountriesDto regionCountriesDto = new RegionCountriesDto();
        Map<String, Map<String, List<CountryHealthIndicator>>> countryHealthIndicatorsMap =
                countryHealthIndicators.stream().collect(groupingBy(countryHealthIndicator -> countryHealthIndicator.getCountryHealthIndicatorId().getCountryId(),
                        groupingBy(countryHealthIndicator -> countryHealthIndicator.getCountryHealthIndicatorId().getYear())));

        countryHealthIndicatorsMap.forEach((countryId, regionCountriesHealthIndicatorsMap) -> {
            String countryName = countryService.getCountryName(countryId, languageCode);
            List<RegionCountryHealthScoreYearDto> regionCountryHealthScoreYearDtos =
                    constructRegionCountryHealthScoreYearDto(countryId, regionCountriesHealthIndicatorsMap,
                            countryPhases);
            regionCountriesDto.add(countryId, countryName, regionCountryHealthScoreYearDtos);
        });

        return regionCountriesDto;
    }

    public List<RegionCountryHealthScoreYearDto> constructRegionCountryHealthScoreYearDto(String countryId,
                                                                                          Map<String,
                                                                                                  List<CountryHealthIndicator>> regionCountryHealthIndicatorsMap,
                                                                                          List<CountryPhase> allCountryPhases) {
        return regionCountryHealthIndicatorsMap.entrySet().stream().map(regionHealthIndicator -> {
            CountryPhase countryPhase = allCountryPhases.stream().filter(countryPhase1 ->
                    countryPhase1.getCountryPhaseId().getCountryId().equals(countryId) && countryPhase1.getCountryPhaseId().getYear().equals(regionHealthIndicator.getKey())
            ).findFirst().orElse(null);

            RegionCountryHealthScoreDto regionCountryHealthScoreDto =
                    constructRegionCountryHealthScoreDto(new CountryHealthIndicators(regionHealthIndicator.getValue()),
                            countryPhase);
            return RegionCountryHealthScoreYearDto.builder().year(regionHealthIndicator.getKey()).country(regionCountryHealthScoreDto).build();
        }).collect(toList());

    }

    public RegionCountryHealthScoreDto constructRegionCountryHealthScoreDto(CountryHealthIndicators countryHealthIndicators,
                                                                            CountryPhase countryPhase) {
        List<CategoryHealthScoreDto> categoryDtos =
                countryHealthIndicatorService.getCategoriesWithIndicators(countryHealthIndicators,
                        getCategoryPhaseFilter(null, null));

        List<CategoryHealthScoreDto> categoryDtosWithoutIndicators =
                categoryDtos.stream().map(categoryHealthScoreDto -> {
                    return CategoryHealthScoreDto.builder().id(categoryHealthScoreDto.getId()).phase(categoryHealthScoreDto.getPhase()).
                            overallScore(categoryHealthScoreDto.getOverallScore()).name(categoryHealthScoreDto.getName()).build();
                }).collect(toList());

        return RegionCountryHealthScoreDto.builder().countryPhase(countryPhase.getCountryOverallPhase()).categories(categoryDtosWithoutIndicators).build();
    }

    public List<String> fetchYearsForARegion(String regionId, Integer limit) {
        return iRegionalOverallRepository.findByRegionIdOrderByUpdatedAtDesc(regionId, limit);
    }

    private Map<Integer, Double> defaultToNAWhenAllIndicatorsAreNA(List<CountryHealthIndicator> countryHealthIndicators) {
        Map<Integer, List<Integer>> indIdScoresMap = countryHealthIndicators.stream()
                .filter(indicator -> indicator.getIndicator().getParentId() == null)
                .collect(groupingBy(CountryHealthIndicator::getIndicatorId, Collectors.mapping(CountryHealthIndicator::getScore, toList())));

        Map<Integer, Double> indIdAvgScoresMap = new HashMap<>();
        indIdScoresMap.forEach((indId, indScores) -> {
            if (areAllIndicatorsNA(indScores)) {
                indIdAvgScoresMap.put(indId, -1.0);
            }
        });
        return indIdAvgScoresMap;
    }

    private boolean areAllIndicatorsNA(List<Integer> indScores) {
        return indScores.stream().allMatch(indScore -> indScore == -1);
    }
}
