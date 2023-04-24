package it.gdhi.service;

import it.gdhi.dto.BenchmarkDto;
import it.gdhi.dto.CategoryHealthScoreDto;
import it.gdhi.dto.GlobalHealthScoreDto;
import it.gdhi.internationalization.service.HealthIndicatorTranslator;
import it.gdhi.model.*;
import it.gdhi.model.id.RegionalCategoryId;
import it.gdhi.model.id.RegionalIndicatorId;
import it.gdhi.model.id.RegionalOverallId;
import it.gdhi.repository.*;
import it.gdhi.utils.LanguageCode;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static it.gdhi.utils.FormStatus.PUBLISHED;
import static java.util.stream.Collectors.*;

import it.gdhi.internationalization.service.RegionNameTranslator;
import it.gdhi.model.Region;
import it.gdhi.repository.IRegionCountryRepository;
import it.gdhi.repository.IRegionRepository;
import org.springframework.stereotype.Service;

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
    private HealthIndicatorTranslator healthIndicatorTranslator;

    public List<Region> fetchRegions(LanguageCode languageCode) {
        List<Region> regions = iRegionRepository.findAll();
        return regionNameTranslator.translate(regions, languageCode);
    }

    public List<String> fetchCountriesForARegion(String regionId) {
        return iRegionCountryRepository.findByRegionCountryIdRegionId(regionId);
    }

    public Integer fetchRegionOverallScore(String regionID, String year) {
        RegionalOverallData regionalOverallData = iRegionalOverallRepository.findByRegionalOverallIdRegionIdAndRegionalOverallIdYear(regionID, year);
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
            RegionalOverallData regionalOverallData = calculateRegionalOverallDataFor(countryHealthIndicators1, regionId, year);
            iRegionalOverallRepository.deleteByRegionalOverallIdRegionIdAndRegionalOverallIdYear(regionId, year);
            if (regionalOverallData != null) {
                RegionalOverallData regionalOverallData1 = iRegionalOverallRepository.save(regionalOverallData);
                entityManager.flush();
                entityManager.refresh(regionalOverallData1);
            }
        }
    }

    @Transactional
    private void saveRegionalIndicatorData(String regionId, List<String> countries, String year) {

        List<CountryHealthIndicator> countryHealthIndicators =
                iCountryHealthIndicatorRepository.findByCountryHealthIndicatorIdCountryIdInAndCountryHealthIndicatorIdYearAndCountryHealthIndicatorIdStatus(countries, year, PUBLISHED.name());
        List<RegionalIndicatorData> regionalIndicatorsData = calculateRegionalIndicatorDataFor(countryHealthIndicators, regionId, year);

        iRegionalIndicatorDataRepository.deleteByRegionalIndicatorIdRegionIdAndRegionalIndicatorIdYear(regionId, year);

        if (regionalIndicatorsData != null) {
            regionalIndicatorsData.forEach(regionalIndicator -> {
                RegionalIndicatorData regionalIndicatorData1 = iRegionalIndicatorDataRepository.save(regionalIndicator);
                entityManager.flush();
                entityManager.refresh(regionalIndicatorData1);
            });
        }
    }

    @Transactional
    public void saveRegionalCategoryData(String regionId, List<String> countries, String year) {

        List<CountryHealthIndicator> countryHealthIndicators =
                iCountryHealthIndicatorRepository.findByCountryHealthIndicatorIdCountryIdInAndCountryHealthIndicatorIdYearAndCountryHealthIndicatorIdStatus(countries, year, PUBLISHED.name());
        if (countryHealthIndicators.size() > 0) {
            CountryHealthIndicators countryHealthIndicators1 = new CountryHealthIndicators(countryHealthIndicators);
            List<RegionalCategoryData> regionalCategoriesData = calculateRegionalCategoriesDataFor(countryHealthIndicators1, regionId, year);

            iRegionalCategoryDataRepository.deleteByRegionalCategoryIdRegionIdAndRegionalCategoryIdYear(regionId, year);

            if (regionalCategoriesData != null) {
                regionalCategoriesData.forEach(regionalCategoryData -> {
                    RegionalCategoryData regionalCategoryData1 = iRegionalCategoryDataRepository.save(regionalCategoryData);
                    entityManager.flush();
                    entityManager.refresh(regionalCategoryData1);
                });
            }
        }
    }

    public List<RegionalCategoryData> calculateRegionalCategoriesDataFor(CountryHealthIndicators countryHealthIndicators, String regionId,
                                                                         String year) {
        Map<Integer, Double> CategoryScore = countryHealthIndicators.groupByCategoryIdWithoutNullAndNegativeScores();

        List<RegionalCategoryData> regionalCategoryData = countryHealthIndicators.groupByCategory().entrySet().stream().map(entry -> {
            Category category = entry.getKey();
            RegionalCategoryId regionalCategoryId = new RegionalCategoryId(regionId, category.getId(), year);
            Double score = CategoryScore.get(category.getId()) == null ? -1 : CategoryScore.get(category.getId());
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
                        .filter(indicator -> indicator.getIndicator().getParentId() == null)
                        .collect(groupingBy(CountryHealthIndicator::getIndicatorId,
                                averagingInt(CountryHealthIndicator::getScore)));

        return regionalIndicators.entrySet().stream().map(dto -> {
            return RegionalIndicatorData.builder().regionalIndicatorId(new RegionalIndicatorId(regionId, dto.getKey(), year))
                    .score(new Score(dto.getValue()).convertToPhase()).build();
        }).collect(toList());
    }

    public RegionalOverallData calculateRegionalOverallDataFor(CountryHealthIndicators countryHealthIndicators, String regionId, String year) {
        Map<Integer, Double> CategoryScore = countryHealthIndicators.groupByCategoryIdWithoutNullAndNegativeScores();
        Double averageOverallScore = CategoryScore.entrySet().stream().mapToDouble(Map.Entry::getValue).average().getAsDouble();
        RegionalOverallData regionalOverallData =
                RegionalOverallData.builder().regionalOverallId(RegionalOverallId.builder().regionId(regionId).year(year).build())
                        .overAllScore(Score.builder().value(averageOverallScore).build().convertToPhase()).build();
        return regionalOverallData;
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
        regions.stream().forEach(region -> calculateAndSaveRegionScores(region.getRegion_id(), year));
    }

    @Transactional
    void saveRegionalIndicatorData(String countryId, String year) {
        Map<String, List<String>> map = getListOfCountriesAndRegionId(countryId);
        String regionId = map.keySet().stream().toList().get(0);
        List<String> countries = map.get(regionId);

        List<CountryHealthIndicator> countryHealthIndicators =
                iCountryHealthIndicatorRepository.findByCountryHealthIndicatorIdCountryIdInAndCountryHealthIndicatorIdYearAndCountryHealthIndicatorIdStatus(countries, year, PUBLISHED.name());
        List<RegionalIndicatorData> regionalIndicatorsData = calculateRegionalIndicatorDataFor(countryHealthIndicators, regionId, year);

        iRegionalIndicatorDataRepository.deleteByRegionalIndicatorIdRegionIdAndRegionalIndicatorIdYear(regionId, year);

        if (regionalIndicatorsData != null) {
            regionalIndicatorsData.forEach(regionalIndicator -> {
                RegionalIndicatorData regionalIndicatorData1 = iRegionalIndicatorDataRepository.save(regionalIndicator);
                entityManager.flush();
                entityManager.refresh(regionalIndicatorData1);
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
        List<RegionalCategoryData> regionalCategoriesData = calculateRegionalCategoriesDataFor(countryHealthIndicators1, regionId, year);

        iRegionalCategoryDataRepository.deleteByRegionalCategoryIdRegionIdAndRegionalCategoryIdYear(regionId, year);

        if (regionalCategoriesData != null) {
            regionalCategoriesData.forEach(regionalCategoryData -> {
                RegionalCategoryData regionalCategoryData1 = iRegionalCategoryDataRepository.save(regionalCategoryData);
                entityManager.flush();
                entityManager.refresh(regionalCategoryData1);
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
        RegionalOverallData regionalOverallData = calculateRegionalOverallDataFor(countryHealthIndicators1, regionId, year);
        iRegionalOverallRepository.deleteByRegionalOverallIdRegionIdAndRegionalOverallIdYear(regionId, year);
        if (regionalOverallData != null) {
            RegionalOverallData regionalOverallData1 = iRegionalOverallRepository.save(regionalOverallData);
            entityManager.flush();
            entityManager.refresh(regionalOverallData1);
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

    public GlobalHealthScoreDto fetchRegionalHealthScores(Integer categoryId, String regionId, LanguageCode languageCode, String year) {
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
        return translateCategoryNames(new GlobalHealthScoreDto(fetchRegionOverallScore(regionId, year), categoryHealthScoreDtos), languageCode);
    }

    public Map<Integer, Integer> fetchRegionalIndicatorScoreData(String regionId, String year) {
        List<RegionalIndicatorData> regionalIndicatorsData =
                iRegionalIndicatorDataRepository.findByRegionalIndicatorIdRegionIdAndRegionalIndicatorIdYear(regionId, year);
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
                    String translatedCategory = healthIndicatorTranslator.getTranslatedCategory(category.getName(), code);
                    category.translateCategoryName(translatedCategory);
                });
        return globalHealthScoreDto;
    }
}
