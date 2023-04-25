package it.gdhi.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.gdhi.dto.CategoryHealthScoreDto;
import it.gdhi.dto.GlobalHealthScoreDto;
import it.gdhi.internationalization.RegionNameTranslatorTest;
import it.gdhi.internationalization.service.HealthIndicatorTranslator;
import it.gdhi.internationalization.service.RegionNameTranslator;
import it.gdhi.model.Category;
import it.gdhi.model.CountryHealthIndicator;
import it.gdhi.model.CountryHealthIndicators;
import it.gdhi.model.Indicator;
import it.gdhi.model.Region;
import it.gdhi.model.RegionCountry;
import it.gdhi.model.RegionCountryId;
import it.gdhi.model.RegionalCategoryData;
import it.gdhi.model.RegionalIndicatorData;
import it.gdhi.model.RegionalOverallData;
import it.gdhi.model.id.RegionalCategoryId;
import it.gdhi.model.id.RegionalIndicatorId;
import it.gdhi.model.id.RegionalOverallId;
import it.gdhi.repository.ICountryHealthIndicatorRepository;
import it.gdhi.repository.IRegionCountryRepository;
import it.gdhi.repository.IRegionRepository;
import it.gdhi.repository.IRegionalCategoryDataRepository;
import it.gdhi.repository.IRegionalIndicatorDataRepository;
import it.gdhi.repository.IRegionalOverallDataRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import static it.gdhi.utils.FormStatus.PUBLISHED;
import static it.gdhi.utils.LanguageCode.en;
import static it.gdhi.utils.LanguageCode.fr;
import static it.gdhi.utils.Util.getCurrentYear;
import static org.codehaus.groovy.runtime.InvokerHelper.asList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class RegionServiceTest {
    @InjectMocks
    private RegionService regionService;
    @Mock
    private IRegionRepository iRegionRepository;
    @Mock
    private RegionNameTranslator regionNameTranslator;
    @Mock
    private IRegionCountryRepository iRegionCountryRepository;
    @Mock
    private ICountryHealthIndicatorRepository iCountryHealthIndicatorRepository;

    @Mock
    private IRegionalIndicatorDataRepository iRegionalIndicatorDataRepository;
    @Mock
    private IRegionalOverallDataRepository iRegionOverallDataRepository;

    @Mock
    private IRegionalCategoryDataRepository iRegionalCategoryDataRepository;

    @Mock
    private CategoryIndicatorService categoryIndicatorService;

    @Mock
    private HealthIndicatorTranslator healthIndicatorTranslator;

    public Region createRegion(String id, String name) {
        Region region = Region.builder().regionId(id).regionName(name).build();
        return region;
    }

    @Test
    public void shouldFetchAllRegions() {
        String id = "AFRO";
        String name = "African Region";
        List<Region> regions = asList(createRegion(id, name));

        when(iRegionRepository.findAll()).thenReturn(regions);

        assertEquals(regions.get(0).getRegionId(), id);
        assertEquals(regions.get(0).getRegionName(), name);
    }

    @Test
    public void shouldVerifyThatRepositoryLayerIsInvoked() {
        regionService.fetchRegions(en);
        verify(iRegionRepository).findAll();
    }

    @Test
    public void shouldFetchRegionsForAGivenLanguage() {
        List<Region> regions = new ArrayList<>();

        RegionNameTranslatorTest regionNameTranslatorTest = new RegionNameTranslatorTest();
        List<Region> listOfRegionsInEnglish = regionNameTranslatorTest.createListOfRegionsInEnglish();
        List<Region> listOfRegionsInFrench = regionNameTranslatorTest.createListOfRegionsInFrench();

        when(iRegionRepository.findAll()).thenReturn(listOfRegionsInEnglish);
        when(regionNameTranslator.translate(listOfRegionsInEnglish, fr)).thenReturn(listOfRegionsInFrench);

        assertEquals(listOfRegionsInEnglish.get(1).getRegionId(), "AFRO");
        assertEquals(listOfRegionsInFrench.get(1).getRegionName(), "Région africaine");
    }

    @Test
    public void shouldFetchCountriesForARegion() {
        String region = "PAHO";
        List<String> countries = Arrays.asList("ARG", "ATG", "BHS");

        when(iRegionCountryRepository.findByRegionCountryIdRegionId(region)).thenReturn(countries);
        List<String> actualCountries = regionService.fetchCountriesForARegion(region);

        assertEquals(countries, actualCountries);
    }

    @Test
    public void shouldSavePhaseForAllRegions() {
        String id = "AFRO";
        String name = "African Region";
        List<Region> regions = Arrays.asList(createRegion(id, name));
        String year = getCurrentYear();

        when(iRegionRepository.findAll()).thenReturn(regions);
        regionService.calculatePhaseForAllRegions(year);
    }

    @Test
    public void shouldCalculateAndSaveRegionScores() {
        Indicator indicator1 = Indicator.builder().indicatorId(1).parentId(null).build();
        Indicator indicator2 = Indicator.builder().indicatorId(1).parentId(null).build();
        CountryHealthIndicator countryHealthIndicator1 = CountryHealthIndicator.builder()
                .indicator(indicator1)
                .score(3)
                .category(Category.builder().id(1).indicators(Arrays.asList(indicator1, indicator2)).build())
                .build();
        CountryHealthIndicator countryHealthIndicator2 = CountryHealthIndicator.builder()
                .indicator(indicator2)
                .score(-1)
                .category(Category.builder().id(1).indicators(Arrays.asList(indicator1, indicator2)).build())
                .build();

        List<CountryHealthIndicator> countryHealthIndicators = new ArrayList<>();
        countryHealthIndicators.add(countryHealthIndicator1);
        countryHealthIndicators.add(countryHealthIndicator2);
        RegionCountryId regionCountryId = RegionCountryId.builder().countryId("ARG").regionId("PAHO").build();
        RegionCountry regionCountry = RegionCountry.builder().regionCountryId(regionCountryId).build();

        List<String> countries = Arrays.asList("ARG");
        String year = getCurrentYear();
        String region = "PAHO";

        when(iRegionCountryRepository.findByRegionCountryIdRegionId(region)).thenReturn(countries);
        when(iCountryHealthIndicatorRepository.findByCountryHealthIndicatorIdCountryIdInAndCountryHealthIndicatorIdYearAndCountryHealthIndicatorIdStatus(countries, year, PUBLISHED.name())).thenReturn(countryHealthIndicators);

    }

    @Test
    public void shouldGetRegionalIndicatorScoreIgnoringSubIndicatorsAndNotAvailableForARegion() {
        Indicator indicator1 = Indicator.builder().indicatorId(1).parentId(null).build();
        Indicator indicator2 = Indicator.builder().indicatorId(2).parentId(null).build();
        Indicator indicator3 = Indicator.builder().indicatorId(3).parentId(null).build();
        Indicator indicator4 = Indicator.builder().indicatorId(4).parentId(3).build();

        CountryHealthIndicator countryHealthIndicator1 = CountryHealthIndicator.builder()
                .indicator(indicator1)
                .score(3)
                .category(Category.builder().id(1).indicators(Arrays.asList(indicator1, indicator2, indicator3, indicator4)).build())
                .build();
        CountryHealthIndicator countryHealthIndicator2 = CountryHealthIndicator.builder()
                .indicator(indicator2)
                .score(-1)
                .category(Category.builder().id(2).indicators(Arrays.asList(indicator1, indicator2, indicator3, indicator4)).build())
                .build();
        CountryHealthIndicator countryHealthIndicator3 = CountryHealthIndicator.builder()
                .indicator(indicator3)
                .score(2)
                .category(Category.builder().id(3).indicators(Arrays.asList(indicator1, indicator2, indicator3, indicator4)).build())
                .build();
        CountryHealthIndicator countryHealthIndicator4 = CountryHealthIndicator.builder()
                .indicator(indicator4)
                .score(3)
                .category(Category.builder().id(4).indicators(Arrays.asList(indicator1, indicator2, indicator3, indicator4)).build())
                .build();
        CountryHealthIndicator countryHealthIndicator5 = CountryHealthIndicator.builder()
                .indicator(indicator1)
                .score(4)
                .category(Category.builder().id(1).indicators(Arrays.asList(indicator1, indicator2, indicator3, indicator4)).build())
                .build();

        String region = "PAHO";
        List<CountryHealthIndicator> countryHealthIndicators = Arrays.asList(countryHealthIndicator1, countryHealthIndicator2,
                countryHealthIndicator3, countryHealthIndicator4, countryHealthIndicator5);
        List<RegionalIndicatorData> regionalIndicatorData = regionService.calculateRegionalIndicatorDataFor(countryHealthIndicators, region,
                getCurrentYear());

        RegionalIndicatorId regionalIndicatorId = RegionalIndicatorId.builder().regionId(region).indicatorId(1).year(getCurrentYear()).build();
        RegionalIndicatorId regionalIndicatorId2 = RegionalIndicatorId.builder().regionId(region).indicatorId(2).year(getCurrentYear()).build();
        RegionalIndicatorId regionalIndicatorId3 = RegionalIndicatorId.builder().regionId(region).indicatorId(3).year(getCurrentYear()).build();
        RegionalIndicatorData regionalIndicatorData1 = RegionalIndicatorData.builder().regionalIndicatorId(regionalIndicatorId).score(4).build();
        RegionalIndicatorData regionalIndicatorData2 = RegionalIndicatorData.builder().regionalIndicatorId(regionalIndicatorId2).score(-1).build();
        RegionalIndicatorData regionalIndicatorData3 = RegionalIndicatorData.builder().regionalIndicatorId(regionalIndicatorId3).score(2).build();
        List<RegionalIndicatorData> expectedRegionalIndicatorData = Arrays.asList(regionalIndicatorData1, regionalIndicatorData2,
                regionalIndicatorData3);
        assertEquals(expectedRegionalIndicatorData, regionalIndicatorData);
    }

    @Test
    public void shouldGetRegionalCategoryScoreIgnoringSubIndicatorsAndNotAvailableForARegion() {
        Indicator indicator1 = Indicator.builder().indicatorId(1).parentId(null).build();
        Indicator indicator2 = Indicator.builder().indicatorId(2).parentId(null).build();
        Indicator indicator3 = Indicator.builder().indicatorId(3).parentId(null).build();
        Indicator indicator4 = Indicator.builder().indicatorId(4).parentId(3).build();

        Category category1 = Category.builder().id(1).indicators((Arrays.asList(indicator1, indicator2))).build();
        Category category2 = Category.builder().id(2).indicators((Arrays.asList(indicator1, indicator2))).build();

        CountryHealthIndicator countryHealthIndicator1 = CountryHealthIndicator.builder()
                .indicator(indicator1)
                .score(3)
                .category(category1)
                .build();
        CountryHealthIndicator countryHealthIndicator2 = CountryHealthIndicator.builder()
                .indicator(indicator2)
                .score(2)
                .category(category2)
                .build();
        CountryHealthIndicator countryHealthIndicator3 = CountryHealthIndicator.builder()
                .indicator(indicator3)
                .score(5)
                .category(category1)
                .build();
        CountryHealthIndicator countryHealthIndicator4 = CountryHealthIndicator.builder()
                .indicator(indicator4)
                .score(4)
                .category(category1)
                .build();

        String region = "PAHO";
        CountryHealthIndicators countryHealthIndicators = new CountryHealthIndicators(Arrays.asList(countryHealthIndicator1,
                countryHealthIndicator2, countryHealthIndicator3, countryHealthIndicator4));
        List<RegionalCategoryData> regionalCategoryData = regionService.calculateRegionalCategoriesDataFor(countryHealthIndicators, region,
                getCurrentYear());

        RegionalCategoryId regionalCategoryId1 = RegionalCategoryId.builder().regionId(region).categoryId(1).year(getCurrentYear()).build();
        RegionalCategoryId regionalCategoryId2 = RegionalCategoryId.builder().regionId(region).categoryId(2).year(getCurrentYear()).build();
        RegionalCategoryData regionalCategoryData1 = RegionalCategoryData.builder().regionalCategoryId(regionalCategoryId1).score(4).build();
        RegionalCategoryData regionalCategoryData2 = RegionalCategoryData.builder().regionalCategoryId(regionalCategoryId2).score(2).build();

        List<RegionalCategoryData> expectedRegionalCategoryData = Arrays.asList(regionalCategoryData1, regionalCategoryData2);

        assertEquals(expectedRegionalCategoryData.size(), regionalCategoryData.size());
        assertTrue(expectedRegionalCategoryData.containsAll(regionalCategoryData));
        assertTrue(regionalCategoryData.containsAll(expectedRegionalCategoryData));
    }

    @Test
    public void shouldGetRegionalOverallScoreIgnoringSubIndicatorsAndNotAvailableForARegion() {
        Indicator indicator1 = Indicator.builder().indicatorId(1).parentId(null).build();
        Indicator indicator2 = Indicator.builder().indicatorId(2).parentId(null).build();
        Indicator indicator3 = Indicator.builder().indicatorId(3).parentId(null).build();
        Indicator indicator4 = Indicator.builder().indicatorId(4).parentId(3).build();
        Indicator indicator5 = Indicator.builder().indicatorId(5).parentId(null).build();

        Category category1 = Category.builder().id(1).indicators((Arrays.asList(indicator1, indicator3, indicator4))).build();
        Category category2 = Category.builder().id(2).indicators((Arrays.asList(indicator2, indicator5))).build();

        CountryHealthIndicator countryHealthIndicator1 = CountryHealthIndicator.builder()
                .indicator(indicator1)
                .score(2)
                .category(category1)
                .build();
        CountryHealthIndicator countryHealthIndicator2 = CountryHealthIndicator.builder()
                .indicator(indicator2)
                .score(2)
                .category(category2)
                .build();
        CountryHealthIndicator countryHealthIndicator3 = CountryHealthIndicator.builder()
                .indicator(indicator3)
                .score(3)
                .category(category1)
                .build();
        CountryHealthIndicator countryHealthIndicator4 = CountryHealthIndicator.builder()
                .indicator(indicator4)
                .score(4)
                .category(category1)
                .build();
        CountryHealthIndicator countryHealthIndicator5 = CountryHealthIndicator.builder()
                .indicator(indicator5)
                .score(5)
                .category(category2)
                .build();

        String region = "PAHO";

        CountryHealthIndicators countryHealthIndicators = new CountryHealthIndicators(Arrays.asList(countryHealthIndicator1,
                countryHealthIndicator2, countryHealthIndicator3, countryHealthIndicator4, countryHealthIndicator5));
        RegionalOverallData regionalOverallData = regionService.calculateRegionalOverallDataFor(countryHealthIndicators, region, getCurrentYear());

        RegionalOverallId regionalOverallId = RegionalOverallId.builder().regionId(region).year(getCurrentYear()).build();
        RegionalOverallData expectedRegionalOverallData = RegionalOverallData.builder().regionalOverallId(regionalOverallId).overAllScore(3).build();

        assertEquals(expectedRegionalOverallData, regionalOverallData);
    }

    @Test
    public void shouldGetListOfCountriesAndRegionId() {
        RegionCountryId regionCountryId = new RegionCountryId();
        regionCountryId.setCountryId("IND");
        regionCountryId.setRegionId("PAHO");
        RegionCountry regionCountry1 = new RegionCountry();
        regionCountry1.setRegionCountryId(regionCountryId);

        List<String> countries = new ArrayList<>();
        countries.add("IND");

        Map<String, List<String>> expectedMap = new HashMap<>();
        expectedMap.put("PAHO", countries);

        when(iRegionCountryRepository.findByRegionCountryIdCountryId("IND")).thenReturn(regionCountry1);
        when(iRegionCountryRepository.findByRegionCountryIdRegionId("PAHO")).thenReturn(countries);
        Map<String, List<String>> actualMap = regionService.getListOfCountriesAndRegionId("IND");

        assertEquals(expectedMap, actualMap);


    }

    @Test
    public void shouldGetOverallScoreOfARegionWhenRegionIdAndYearIsGiven() {
        Integer expectedRegionScore = 3;
        String regionID = "PAHO";
        String year = "2023";
        RegionalOverallId regionalOverallId = RegionalOverallId.builder().regionId(regionID).year(year).build();
        RegionalOverallData regionalOverallData =
                RegionalOverallData.builder().regionalOverallId(regionalOverallId).overAllScore(expectedRegionScore).build();

        when(iRegionOverallDataRepository.findByRegionalOverallIdRegionIdAndRegionalOverallIdYear(regionID, year)).thenReturn(regionalOverallData);
        Integer actualRegionScore = regionService.fetchRegionOverallScore(regionID, year);

        assertEquals(expectedRegionScore, actualRegionScore);

    }

    @Test
    public void shouldGetRegionalCategoryScoresWhenRegionIdAndYearIsGiven() {
        String regionID = "PAHO";
        String year = "2023";

        RegionalCategoryId regionalCategoryId1 = RegionalCategoryId.builder().categoryId(1).regionId(regionID).year(year).build();
        RegionalCategoryId regionalCategoryId2 = RegionalCategoryId.builder().categoryId(2).regionId(regionID).year(year).build();
        RegionalCategoryData regionalCategoryData1 = RegionalCategoryData.builder().regionalCategoryId(regionalCategoryId1).score(3).build();
        RegionalCategoryData regionalCategoryData2 = RegionalCategoryData.builder().regionalCategoryId(regionalCategoryId2).score(2).build();

        when(iRegionalCategoryDataRepository.findByRegionalCategoryIdRegionIdAndRegionalCategoryIdYearOrderByRegionalCategoryIdCategoryId(regionID,
                year)).thenReturn(Arrays.asList(regionalCategoryData1, regionalCategoryData2));

        List<RegionalCategoryData> regionalCategoryData = regionService.fetchRegionalCategoryScores(regionID, year);

        assertEquals(Arrays.asList(regionalCategoryData1, regionalCategoryData2), regionalCategoryData);
    }

    @Test
    void shouldGetRegionalHealthScoresWhenRegionIdAndYearIsGiven() {
        String regionId = "PAHO";
        String year = "2023";
        RegionalCategoryId regionalCategoryId1 = RegionalCategoryId.builder().categoryId(1).regionId(regionId).year(year).build();
        RegionalCategoryId regionalCategoryId2 = RegionalCategoryId.builder().categoryId(2).regionId(regionId).year(year).build();
        RegionalCategoryData regionalCategoryData1 = RegionalCategoryData.builder().regionalCategoryId(regionalCategoryId1).score(3).build();
        RegionalCategoryData regionalCategoryData2 = RegionalCategoryData.builder().regionalCategoryId(regionalCategoryId2).score(2).build();
        when(iRegionalCategoryDataRepository.findByRegionalCategoryIdRegionIdAndRegionalCategoryIdYearOrderByRegionalCategoryIdCategoryId(regionId,
                year))
                .thenReturn(Arrays.asList(regionalCategoryData1, regionalCategoryData2));
        Indicator indicator1 = new Indicator(5, "Ind 1", "Ind Def 1", 1);
        Indicator indicator2 = new Indicator(1, "Ind 5", "Ind Def 5", 5);
        Category category1 = Category.builder().id(1).name("Cat 1").indicators(Arrays.asList(indicator1, indicator2)).build();
        Indicator indicator4 = new Indicator(8, "Ind 8", "Ind Def 8", 8);
        Indicator indicator3 = new Indicator(2, "Ind 2", "Ind Def 2", 2);
        Category category2 = Category.builder().id(2).name("Cat 2").indicators(Arrays.asList(indicator3, indicator4)).build();
        when(categoryIndicatorService.getAllCategories()).thenReturn(Arrays.asList(category1, category2));
        CategoryHealthScoreDto categoryHealthScoreDto1 =
                CategoryHealthScoreDto.builder().overallScore(null).phase(3).indicators(null).name("Cat 1").id(1).build();
        CategoryHealthScoreDto categoryHealthScoreDto2 =
                CategoryHealthScoreDto.builder().overallScore(null).phase(2).indicators(null).name("Cat 2").id(2).build();
        RegionalOverallId regionalOverallId = RegionalOverallId.builder().regionId(regionId).year(year).build();
        RegionalOverallData regionalOverallData = RegionalOverallData.builder().regionalOverallId(regionalOverallId).overAllScore(3).build();
        when(iRegionOverallDataRepository.findByRegionalOverallIdRegionIdAndRegionalOverallIdYear(regionId, year)).thenReturn(regionalOverallData);
        when(iRegionalCategoryDataRepository.findDistinctByRegionalCategoryIdRegionIdOrderByRegionalCategoryIdCategoryId(regionId)).
                thenReturn(Arrays.asList(regionalCategoryData1, regionalCategoryData2));

        List<CategoryHealthScoreDto> expectedRegionalCategoriesScore = Arrays.asList(categoryHealthScoreDto1, categoryHealthScoreDto2);
        GlobalHealthScoreDto regionalHealthScore = regionService.fetchRegionalHealthScores(null, regionId, en, year);

        assertEquals(3, regionalHealthScore.getOverAllScore());
        assertEquals(expectedRegionalCategoriesScore, regionalHealthScore.getCategories());
    }

    @Test
    void shouldGetRegionalHealthScoresFilteredByCategoryWhenCategoryIdIsGiven() {
        String regionId = "PAHO";
        String year = "2023";
        RegionalCategoryId regionalCategoryId1 = RegionalCategoryId.builder().categoryId(1).regionId(regionId).year(year).build();
        RegionalCategoryId regionalCategoryId2 = RegionalCategoryId.builder().categoryId(2).regionId(regionId).year(year).build();
        RegionalCategoryData regionalCategoryData1 = RegionalCategoryData.builder().regionalCategoryId(regionalCategoryId1).score(3).build();
        RegionalCategoryData regionalCategoryData2 = RegionalCategoryData.builder().regionalCategoryId(regionalCategoryId2).score(2).build();
        when(iRegionalCategoryDataRepository.
                findByRegionalCategoryIdRegionIdAndRegionalCategoryIdYearAndRegionalCategoryIdCategoryIdOrderByRegionalCategoryIdCategoryId(regionId,
                        year, 2)).thenReturn(regionalCategoryData2);
        Indicator indicator1 = new Indicator(5, "Ind 1", "Ind Def 1", 1);
        Indicator indicator2 = new Indicator(1, "Ind 5", "Ind Def 5", 5);
        Category category1 = Category.builder().id(1).name("Cat 1").indicators(Arrays.asList(indicator1, indicator2)).build();
        Indicator indicator4 = new Indicator(8, "Ind 8", "Ind Def 8", 8);
        Indicator indicator3 = new Indicator(2, "Ind 2", "Ind Def 2", 2);
        Category category2 = Category.builder().id(2).name("Cat 2").indicators(Arrays.asList(indicator3, indicator4)).build();
        when(categoryIndicatorService.getAllCategories()).thenReturn(Arrays.asList(category1, category2));
        CategoryHealthScoreDto categoryHealthScoreDto1 =
                CategoryHealthScoreDto.builder().overallScore(null).phase(3).indicators(null).name("Cat 1").id(1).build();
        CategoryHealthScoreDto categoryHealthScoreDto2 =
                CategoryHealthScoreDto.builder().overallScore(null).phase(2).indicators(null).name("Cat 2").id(2).build();
        RegionalOverallId regionalOverallId = RegionalOverallId.builder().regionId(regionId).year(year).build();
        RegionalOverallData regionalOverallData = RegionalOverallData.builder().regionalOverallId(regionalOverallId).overAllScore(3).build();
        when(iRegionOverallDataRepository.findByRegionalOverallIdRegionIdAndRegionalOverallIdYear(regionId, year)).thenReturn(regionalOverallData);
        when(iRegionalCategoryDataRepository.findDistinctByRegionalCategoryIdRegionIdOrderByRegionalCategoryIdCategoryId(regionId)).
                thenReturn(Arrays.asList(regionalCategoryData1, regionalCategoryData2));

        List<CategoryHealthScoreDto> expectedRegionalCategoriesScore = Collections.singletonList(categoryHealthScoreDto2);
        GlobalHealthScoreDto regionalHealthScore = regionService.fetchRegionalHealthScores(2, regionId, en, year);

        assertEquals(3, regionalHealthScore.getOverAllScore());
        assertEquals(expectedRegionalCategoriesScore, regionalHealthScore.getCategories());
    }

    @Test
    void shouldGetEmptyRegionalHealthScoreWhenRegionDataIsNotAvailableForAGivenYear() {
        String regionId = "PAHO";
        String year = "2023";
        RegionalCategoryId regionalCategoryId1 = RegionalCategoryId.builder().categoryId(1).regionId(regionId).year(year).build();
        RegionalCategoryId regionalCategoryId2 = RegionalCategoryId.builder().categoryId(2).regionId(regionId).year(year).build();
        RegionalCategoryData regionalCategoryData1 = RegionalCategoryData.builder().regionalCategoryId(regionalCategoryId1).score(3).build();
        RegionalCategoryData regionalCategoryData2 = RegionalCategoryData.builder().regionalCategoryId(regionalCategoryId2).score(2).build();
        when(iRegionalCategoryDataRepository.findDistinctByRegionalCategoryIdRegionIdOrderByRegionalCategoryIdCategoryId(regionId)).
                thenReturn(Arrays.asList(regionalCategoryData1, regionalCategoryData2));

        GlobalHealthScoreDto regionalHealthScore = regionService.fetchRegionalHealthScores(2, regionId, en, "2024");

        assertEquals(null, regionalHealthScore.getOverAllScore());
        assertEquals(Collections.emptyList(), regionalHealthScore.getCategories());
    }

    @Test
    public void shouldGetRegionalIndicatorDataWhenRegionIdAndYearIsGiven() {
        String regionID = "PAHO";
        String year = "2023";
        RegionalIndicatorId regionalIndicatorId = RegionalIndicatorId.builder().regionId(regionID).indicatorId(1).year(year).build();
        RegionalIndicatorData regionalIndicatorData = RegionalIndicatorData.builder().regionalIndicatorId(regionalIndicatorId).score(3).build();
        when(iRegionalIndicatorDataRepository.findByRegionalIndicatorIdRegionIdAndRegionalIndicatorIdYear(regionID, year)).thenReturn(Collections.singletonList(regionalIndicatorData));

        Map<Integer, Integer> expectedRegionalData = new HashMap<>();
        expectedRegionalData.put(1, 3);
        Map<Integer, Integer> actualRegionalData = regionService.fetchRegionalIndicatorScoreData(regionID, year);

        assertEquals(expectedRegionalData, actualRegionalData);
    }

    @Test
    public void shouldVerifyIfRegionalCategoryDataIsPresentForAGivenYearWhenRegionAndYearIsProvided() {
        String regionID = "PAHO";
        List<String> years = Arrays.asList("2023", "2022");
        RegionalCategoryId regionalCategoryId1 = RegionalCategoryId.builder().categoryId(1).regionId(regionID).year("2023").build();
        RegionalCategoryId regionalCategoryId2 = RegionalCategoryId.builder().categoryId(2).regionId(regionID).year("2022").build();
        RegionalCategoryData regionalCategoryData1 = RegionalCategoryData.builder().regionalCategoryId(regionalCategoryId1).score(3).build();
        RegionalCategoryData regionalCategoryData2 = RegionalCategoryData.builder().regionalCategoryId(regionalCategoryId2).score(2).build();
        when(iRegionalCategoryDataRepository.findDistinctByRegionalCategoryIdRegionIdOrderByRegionalCategoryIdCategoryId(regionID)).
                thenReturn(Arrays.asList(regionalCategoryData1, regionalCategoryData2));

        assertEquals(true, regionService.isRegionalCategoryDataPresent(regionID, "2022"));
        assertEquals(false, regionService.isRegionalCategoryDataPresent(regionID, "Version1"));
    }
}

