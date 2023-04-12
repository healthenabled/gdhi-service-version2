package it.gdhi.service;

import java.util.Collections;
import java.util.List;

import it.gdhi.dto.CategoryHealthScoreDto;
import it.gdhi.dto.CountryHealthScoreDto;
import it.gdhi.dto.GlobalHealthScoreDto;
import it.gdhi.dto.IndicatorScoreDto;
import it.gdhi.dto.YearDto;
import it.gdhi.dto.YearHealthScoreDto;
import it.gdhi.dto.YearOnYearDto;
import it.gdhi.dto.YearScoreDto;
import it.gdhi.internationalization.service.HealthIndicatorTranslator;
import it.gdhi.repository.ICountryHealthIndicatorRepository;
import it.gdhi.repository.ICountryPhaseRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.google.common.collect.ImmutableList.of;
import static it.gdhi.utils.LanguageCode.en;
import static it.gdhi.utils.Util.getCurrentYear;
import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BffServiceTest {

    @InjectMocks
    private BffService bffService;

    @Mock
    private CountryService countryService;

    @Mock
    private ICountryPhaseRepository iCountryPhaseRepository;

    @Mock
    private ICountryHealthIndicatorRepository iCountryHealthIndicatorRepository;

    @Mock
    private DefaultYearDataService defaultYearDataService;

    @Mock
    private CountryHealthIndicatorService countryHealthIndicatorService;

    @Mock
    private HealthIndicatorTranslator indicatorTranslator;


    @Test
    public void shouldReturnDistinctYearsAndDefaultYear() {
        when(countryService.fetchPublishCountriesDistinctYears()).thenReturn(asList("Version1", "2023"));
        when(defaultYearDataService.fetchDefaultYear()).thenReturn("2023");

        YearDto expectedYearData = YearDto.builder().years(asList("Version1", "2023")).defaultYear("2023").build();
        YearDto actualYearData = bffService.fetchDistinctYears();

        assertThat(expectedYearData.getDefaultYear(), equalTo(actualYearData.getDefaultYear()));
        assertThat(expectedYearData.getYears(), equalTo(actualYearData.getYears()));
    }

    @Test
    public void shouldReturnPublishedYearsForACountry() {
        List<String> publishedYears = asList("2023", "2022", "Version1");
        when(iCountryPhaseRepository.findByCountryPhaseIdOrderByYearDesc("IND", 3)).thenReturn(publishedYears);
        List<String> actual = bffService.fetchPublishedYearsForACountry("IND", 3);
        List<String> expected = publishedYears;

        assertEquals(expected, actual);
    }

    @Test
    public void shouldReturnYearOnYearData() {
        String year = getCurrentYear();

        CategoryHealthScoreDto categoryHealthScoreDto1 = new CategoryHealthScoreDto(2, "Category 2", 2.0, 2, of(new IndicatorScoreDto(1, null, null, null, null, 2, null, "Not Available")));
        CategoryHealthScoreDto categoryHealthScoreDto2 = new CategoryHealthScoreDto(1, "Category 1", 5.0, 5, of(new IndicatorScoreDto(1, null, null, null, null, 5, null, "Not Available")));
        CountryHealthScoreDto countryHealthScoreDtoIN = new CountryHealthScoreDto("IND", "India", "IN", of(categoryHealthScoreDto1, categoryHealthScoreDto2), 4, "");
        CategoryHealthScoreDto categoryHealthScoreDto4 = new CategoryHealthScoreDto(2, "Category 2", -1.0, -1, of(new IndicatorScoreDto(1, null, null, null, null, null, null, "Not Available")));
        CategoryHealthScoreDto categoryHealthScoreDto5 = new CategoryHealthScoreDto(1, "Category 1", 2.0, 2, of(new IndicatorScoreDto(1, null, null, null, null, 2, null, "Not Available")));

        GlobalHealthScoreDto globalHealthScore = GlobalHealthScoreDto.builder().overAllScore(3).categories(asList(categoryHealthScoreDto1, categoryHealthScoreDto2, categoryHealthScoreDto4, categoryHealthScoreDto5)).build();
        when(countryHealthIndicatorService.getGlobalHealthIndicator(null, null,null, en, year)).thenReturn(globalHealthScore);
        when(countryHealthIndicatorService.fetchCountryHealthScore("IND", en, year)).thenReturn(countryHealthScoreDtoIN);
        when(defaultYearDataService.fetchDefaultYear()).thenReturn(year);

        YearOnYearDto actual = bffService.fetchYearOnYearData((Collections.singletonList(getCurrentYear())), "IND",null);
        YearHealthScoreDto data = YearHealthScoreDto.builder().country(countryHealthScoreDtoIN).average(globalHealthScore).build();
        List<YearScoreDto> yearScoreDtos = Collections.singletonList(YearScoreDto.builder().year(year).data(data).build());
        YearOnYearDto expected = YearOnYearDto.builder().currentYear(year).defaultYear(year).yearOnYearData(yearScoreDtos).build();
        assertEquals(expected, actual);
    }

    @Test
    public void shouldReturnYearOnYearDataForMultipleYears() {
        List<String> years = asList("2023", "2022");

        CategoryHealthScoreDto categoryHealthScoreDto1 = new CategoryHealthScoreDto(2, "Category 2", 2.0, 2, of(new IndicatorScoreDto(1, null, null, null, null, 2, null, "Not Available")));
        CategoryHealthScoreDto categoryHealthScoreDto2 = new CategoryHealthScoreDto(1, "Category 1", 5.0, 5, of(new IndicatorScoreDto(1, null, null, null, null, 5, null, "Not Available")));
        CountryHealthScoreDto countryHealthScoreDtoIN = new CountryHealthScoreDto("IND", "India", "IN", of(categoryHealthScoreDto1, categoryHealthScoreDto2), 4, "");
        CategoryHealthScoreDto categoryHealthScoreDto4 = new CategoryHealthScoreDto(2, "Category 2", -1.0, -1, of(new IndicatorScoreDto(1, null, null, null, null, null, null, "Not Available")));
        CategoryHealthScoreDto categoryHealthScoreDto5 = new CategoryHealthScoreDto(1, "Category 1", 2.0, 2, of(new IndicatorScoreDto(1, null, null, null, null, 2, null, "Not Available")));

        GlobalHealthScoreDto globalHealthScore = GlobalHealthScoreDto.builder().overAllScore(3).categories(asList(categoryHealthScoreDto1, categoryHealthScoreDto2, categoryHealthScoreDto4, categoryHealthScoreDto5)).build();
        when(countryHealthIndicatorService.getGlobalHealthIndicator(null, null,null, en, "2023")).thenReturn(globalHealthScore);
        when(countryHealthIndicatorService.getGlobalHealthIndicator(null, null,null, en, "2022")).thenReturn(globalHealthScore);
        when(countryHealthIndicatorService.fetchCountryHealthScore("IND", en, "2023")).thenReturn(countryHealthScoreDtoIN);
        when(countryHealthIndicatorService.fetchCountryHealthScore("IND", en, "2022")).thenReturn(countryHealthScoreDtoIN);
        when(defaultYearDataService.fetchDefaultYear()).thenReturn("2022");

        YearOnYearDto actual = bffService.fetchYearOnYearData(years, "IND",null);
        YearHealthScoreDto data1 = YearHealthScoreDto.builder().country(countryHealthScoreDtoIN).average(globalHealthScore).build();
        YearHealthScoreDto data2 = YearHealthScoreDto.builder().country(countryHealthScoreDtoIN).average(globalHealthScore).build();
        List<YearScoreDto> yearScoreDtos = asList(YearScoreDto.builder().year("2023").data(data1).build(), YearScoreDto.builder().year("2022").data(data2).build());
        YearOnYearDto expected = YearOnYearDto.builder().currentYear(getCurrentYear()).defaultYear("2022").yearOnYearData(yearScoreDtos).build();
        assertEquals(expected, actual);
    }
}
