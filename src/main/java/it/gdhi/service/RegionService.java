package it.gdhi.service;
import it.gdhi.model.*;
import it.gdhi.model.id.RegionalCategoryId;
import it.gdhi.model.id.RegionalIndicatorId;
import it.gdhi.model.id.RegionalOverallId;
import it.gdhi.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static it.gdhi.utils.FormStatus.PUBLISHED;
import static it.gdhi.utils.Util.getCurrentYear;
import static java.util.stream.Collectors.*;
import it.gdhi.internationalization.service.RegionNameTranslator;
import it.gdhi.model.Region;
import it.gdhi.repository.IRegionCountryRepository;
import it.gdhi.repository.IRegionRepository;
import it.gdhi.utils.LanguageCode;
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

    public List<Region> fetchRegions(LanguageCode languageCode) {
        List<Region> regions = iRegionRepository.findAll();
        return regionNameTranslator.translate(regions, languageCode);
    }

    public List<String> fetchCountriesForARegion(String regionId) {
        return iRegionCountryRepository.findByRegionCountryIdRegionId(regionId);
    }

    @Transactional
    private void saveRegionalOverallData(String regionId, List<String> countries, String year) {

        List<CountryHealthIndicator> countryHealthIndicators = iCountryHealthIndicatorRepository.findByCountryHealthIndicatorIdCountryIdInAndCountryHealthIndicatorIdYearAndCountryHealthIndicatorIdStatus(countries, year, PUBLISHED.name());
        if(countryHealthIndicators.size()>0)
        {
            CountryHealthIndicators countryHealthIndicators1 = new CountryHealthIndicators(countryHealthIndicators);
            RegionalOverallData regionalOverallData = calculateRegionalOverallDataFor(countryHealthIndicators1, regionId);
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

        List<CountryHealthIndicator> countryHealthIndicators = iCountryHealthIndicatorRepository.findByCountryHealthIndicatorIdCountryIdInAndCountryHealthIndicatorIdYearAndCountryHealthIndicatorIdStatus(countries, year, PUBLISHED.name());
        List<RegionalIndicatorData> regionalIndicatorsData = calculateRegionalIndicatorDataFor(countryHealthIndicators, regionId);

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

        List<CountryHealthIndicator> countryHealthIndicators = iCountryHealthIndicatorRepository.findByCountryHealthIndicatorIdCountryIdInAndCountryHealthIndicatorIdYearAndCountryHealthIndicatorIdStatus(countries, year, PUBLISHED.name());
        if(countryHealthIndicators.size()>0)
        {
            CountryHealthIndicators countryHealthIndicators1 = new CountryHealthIndicators(countryHealthIndicators);
            List<RegionalCategoryData> regionalCategoriesData = calculateRegionalCategoriesDataFor(countryHealthIndicators1, regionId);

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

    public RegionalOverallData calculateRegionalOverallDataFor(CountryHealthIndicators countryHealthIndicators, String regionId) {
        Map<Integer, Double> CategoryScore = countryHealthIndicators.groupByCategoryIdWithoutNullAndNegativeScores();
        Double averageOverallScore = CategoryScore.entrySet().stream().mapToDouble(Map.Entry::getValue).average().getAsDouble();
        RegionalOverallData regionalOverallData = RegionalOverallData.builder().regionalOverallId(RegionalOverallId.builder().regionId(regionId).year(getCurrentYear()).build())
                .overAllScore(Score.builder().value(averageOverallScore).build().convertToPhase()).build();
        return regionalOverallData;
    }
    @Transactional
    public void calculateAndSaveRegionScores(String regionId, String year){
        List<String> countriesForRegions = iRegionCountryRepository.findByRegionCountryIdRegionId(regionId);
        saveRegionalIndicatorData(regionId,countriesForRegions,year);
        saveRegionalCategoryData(regionId,countriesForRegions,year);
        saveRegionalOverallData(regionId,countriesForRegions,year);
    }
    @Transactional
    public void calculatePhaseForAllRegions(String year){
        List<Region> regions = iRegionRepository.findAll();
        regions.stream().forEach(region -> calculateAndSaveRegionScores(region.getRegion_id(),year));
    }

    @Transactional
    void saveRegionalIndicatorData(String countryId, String currentYear) {
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

    @Transactional
    void saveRegionalCategoryData(String countryId, String currentYear) {
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

    @Transactional
    void saveRegionalOverallData(String countryId, String year) {
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
    public Map<String, List<String>> getListOfCountriesAndRegionId(String countryId) {
        RegionCountry regionCountry = iRegionCountryRepository.findByRegionCountryIdCountryId(countryId);
        String regionId = regionCountry.getRegionCountryId().getRegionId();
        List<String> countries = iRegionCountryRepository.findByRegionCountryIdRegionId(regionId);

        Map<String, List<String>> map = new HashMap<>();
        map.put(regionId, countries);
        return map;
    }
}
