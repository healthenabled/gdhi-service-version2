package it.gdhi.service;

import it.gdhi.internationalization.RegionNameTranslatorTest;
import it.gdhi.internationalization.service.RegionNameTranslator;
import it.gdhi.model.*;
import it.gdhi.model.id.RegionalCategoryId;
import it.gdhi.model.id.RegionalIndicatorId;
import it.gdhi.model.id.RegionalOverallId;
import it.gdhi.repository.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.*;

import static it.gdhi.utils.FormStatus.PUBLISHED;
import static it.gdhi.utils.LanguageCode.en;
import static it.gdhi.utils.LanguageCode.fr;
import static it.gdhi.utils.Util.getCurrentYear;
import static org.codehaus.groovy.runtime.InvokerHelper.asList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

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

    public Region createRegion(String id, String name) {
        Region region = Region.builder().region_id(id).regionName(name).build();
        return region;
    }

    @Test
    public void shouldFetchAllRegions() {
        String id = "AFRO";
        String name = "African Region";
        List<Region> regions = asList(createRegion(id, name));

        when(iRegionRepository.findAll()).thenReturn(regions);

        assertEquals(regions.get(0).getRegion_id(), id);
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

        assertEquals(listOfRegionsInEnglish.get(1).getRegion_id(), "AFRO");
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
    public void shouldSavePhaseForAllRegions(){
        String id = "AFRO";
        String name = "African Region";
        List<Region> regions = Arrays.asList(createRegion(id, name));
        String year = getCurrentYear();

        when(iRegionRepository.findAll()).thenReturn(regions);
        regionService.calculatePhaseForAllRegions(year);
    }
    @Test
    public void shouldCalculateAndSaveRegionScores(){
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
        List<CountryHealthIndicator> countryHealthIndicators = Arrays.asList(countryHealthIndicator1, countryHealthIndicator2, countryHealthIndicator3, countryHealthIndicator4, countryHealthIndicator5);
        List<RegionalIndicatorData> regionalIndicatorData = regionService.calculateRegionalIndicatorDataFor(countryHealthIndicators, region, getCurrentYear());

        RegionalIndicatorId regionalIndicatorId = RegionalIndicatorId.builder().regionId(region).indicatorId(1).year(getCurrentYear()).build();
        RegionalIndicatorId regionalIndicatorId2 = RegionalIndicatorId.builder().regionId(region).indicatorId(3).year(getCurrentYear()).build();
        RegionalIndicatorData regionalIndicatorData1 = RegionalIndicatorData.builder().regionalIndicatorId(regionalIndicatorId).score(4).build();
        RegionalIndicatorData regionalIndicatorData2 = RegionalIndicatorData.builder().regionalIndicatorId(regionalIndicatorId2).score(2).build();
        List<RegionalIndicatorData> expectedRegionalIndicatorData = Arrays.asList(regionalIndicatorData1, regionalIndicatorData2);

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
        CountryHealthIndicators countryHealthIndicators = new CountryHealthIndicators(Arrays.asList(countryHealthIndicator1, countryHealthIndicator2, countryHealthIndicator3, countryHealthIndicator4));
        List<RegionalCategoryData> regionalCategoryData = regionService.calculateRegionalCategoriesDataFor(countryHealthIndicators, region, getCurrentYear());

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

        CountryHealthIndicators countryHealthIndicators = new CountryHealthIndicators(Arrays.asList(countryHealthIndicator1, countryHealthIndicator2, countryHealthIndicator3, countryHealthIndicator4, countryHealthIndicator5));
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
        expectedMap.put("PAHO",countries);

        when(iRegionCountryRepository.findByRegionCountryIdCountryId("IND")).thenReturn(regionCountry1);
        when(iRegionCountryRepository.findByRegionCountryIdRegionId("PAHO")).thenReturn(countries);
        Map<String, List<String>> actualMap = regionService.getListOfCountriesAndRegionId("IND");

        assertEquals(expectedMap,actualMap);


    }
}

