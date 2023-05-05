package it.gdhi.controller;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import it.gdhi.dto.GdhiQuestionnaires;
import it.gdhi.dto.GlobalHealthScoreDto;
import it.gdhi.service.BffService;
import it.gdhi.service.CountryHealthDataService;
import it.gdhi.service.CountryHealthIndicatorService;
import it.gdhi.service.CountryService;
import it.gdhi.service.DefaultYearDataService;
import it.gdhi.service.ExcelUtilService;
import it.gdhi.service.RegionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;

import static it.gdhi.utils.ApplicationConstants.defaultLimit;
import static it.gdhi.utils.LanguageCode.ar;
import static it.gdhi.utils.LanguageCode.en;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BffControllerTest {

    @InjectMocks
    @Spy
    private BffController bffController;

    @Mock
    private BffService bffService;

    @Mock
    private CountryService countryService;
    @Mock
    private CountryHealthDataService countryHealthDataService;

    @Mock
    private DefaultYearDataService defaultYearDataService;
    @Mock
    private CountryHealthIndicatorService countryHealthIndicatorService;

    @Mock
    private RegionService regionService;

    @Mock
    private ExcelUtilService excelUtilService;
    @Test
     void shouldGetDistinctYearsWithDefaultLimitWhenLimitIsNull() {
        bffController.getDistinctYears(null);
        verify(bffService).fetchDistinctYears(defaultLimit);
    }

    @Test
     void shouldGetDistinctYears() {
        bffController.getDistinctYears(5);
        verify(bffService).fetchDistinctYears(5);
    }

    @Test
     void shouldGetYearOnYearData() {
        bffController.getYearOnYearData(null, 1, null);
        verify(countryService).fetchPublishCountriesDistinctYears(1);
        verify(bffService).fetchYearOnYearData(countryService.fetchPublishCountriesDistinctYears(1), null, null);
    }

    @Test
     void shouldGetYearOnYearDataForFiveYearsWhenLimitIsNull() {
        bffController.getYearOnYearData(null, null, null);
        verify(countryService).fetchPublishCountriesDistinctYears(defaultLimit);
        verify(bffService).fetchYearOnYearData(countryService.fetchPublishCountriesDistinctYears(defaultLimit), null, null);
    }

    @Test
     void shouldGetPublishedYearsForACountry() {
        bffController.getPublishedYearsForACountry(null, 1);
        verify(bffService).fetchPublishedYearsForACountry(null, 1);
    }

    @Test
     void shouldGetFivePublishedYearsForACountryWhenLimitIsNull() {
        bffController.getPublishedYearsForACountry(null, null);
        verify(bffService).fetchPublishedYearsForACountry(null, defaultLimit);
    }

    @Test
     void shouldGetGlobalBenchmarkDetailsForGivenYear() {
        String countryID = "IND";
        Integer benchmarkType = -1;
        String year = "Version1";
        String region = null;
        when(countryHealthDataService.getBenchmarkDetailsFor(countryID, benchmarkType, year)).thenReturn(new HashMap<>());
        bffController.getBenchmarkDetailsFor(countryID, benchmarkType, year, region);
        verify(countryHealthDataService).getBenchmarkDetailsFor(countryID, benchmarkType, year);
    }

    @Test
     void shouldGetGlobalBenchmarkDetailsForDefaultYearWhenYearIsNull() {
        String countryID = "IND";
        Integer benchmarkType = -1;
        String year = "2023";
        when(defaultYearDataService.fetchDefaultYear()).thenReturn(year);
        bffController.getBenchmarkDetailsFor(countryID, benchmarkType, null, null);
        verify(countryHealthDataService).getBenchmarkDetailsFor(countryID, benchmarkType, year);
    }

    @Test
     void shouldGetRegionalBenchmarkDetailsForGivenYearWhenRegionIsGiven() {
        String countryID = "IND";
        Integer benchmarkType = -1;
        String year = "Version1";
        String region = "PAHO";
        when(regionService.getBenchmarkDetailsForRegion(countryID, year, region)).thenReturn(new HashMap<>());
        bffController.getBenchmarkDetailsFor(countryID, benchmarkType, year, region);
        verify(regionService).getBenchmarkDetailsForRegion(countryID, year, region);
    }

    @Test
     void shouldGetYearOnYearDataWhenRegionIsPassed() {
        bffController.getYearOnYearData(null, 1, "EURO");
        verify(countryService).fetchPublishCountriesDistinctYears(1);
        verify(bffService).fetchYearOnYearData(countryService.fetchPublishCountriesDistinctYears(1), null, "EURO");
    }

    @Test
     void shouldSubmitCSVDataWhenListOfQuestionnairesAreGiven() {
        GdhiQuestionnaires mock = mock(GdhiQuestionnaires.class);
        bffController.shouldSubmitCSVData(mock);
        verify(bffService).submitCountryCSVData(mock);
    }

    @Test
     void shouldFetchGivenYearWhenYearIsPassed() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("USER_LANGUAGE", "en");

        bffController.getGlobalHealthIndicator(request, null, 2, null, "2023");

        verify(countryHealthIndicatorService).getGlobalHealthIndicator(null, 2, en, "2023");
    }

    @Test
     void shouldFetchDefaultYearWhenYearIsNull() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("USER_LANGUAGE", "en");

        when(defaultYearDataService.fetchDefaultYear()).thenReturn("Version1");

        bffController.getGlobalHealthIndicator(request, null, 2, null, null);

        verify(countryHealthIndicatorService).getGlobalHealthIndicator(null, 2, en, "Version1");
    }

    @Test
     void shouldGetLanguageCodeOfArabicFromHeader() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("USER_LANGUAGE", "ar");

        bffController.getGlobalHealthIndicator(request, null, 2, null, "Version1");

        verify(countryHealthIndicatorService).getGlobalHealthIndicator(null, 2, ar, "Version1");
    }

    @Test
     void shouldInvokeGetGlobalHealthIndicator() {
        GlobalHealthScoreDto expected = mock(GlobalHealthScoreDto.class);
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("USER_LANGUAGE", "en");

        String year = "Version1";

        when(countryHealthIndicatorService.getGlobalHealthIndicator(null, 2, en, year)).thenReturn(expected);
        GlobalHealthScoreDto actual = bffController.getGlobalHealthIndicator(request, null, 2, null, year);

        assertThat(expected, is(actual));
        verify(countryHealthIndicatorService).getGlobalHealthIndicator(null, 2, en, year);
    }

    @Test
     void shouldInvokeGetRegionalHealthIndicatorWhenRegionIsProvided() {
        GlobalHealthScoreDto expected = mock(GlobalHealthScoreDto.class);
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("USER_LANGUAGE", "en");
        String regionId = "PAHO";
        String year = "Version1";
        when(regionService.fetchRegionalHealthScores(null, regionId, en, year)).thenReturn(expected);

        GlobalHealthScoreDto actual = bffController.getGlobalHealthIndicator(request, null, null, regionId, year);

        assertThat(expected, is(actual));
        verify(regionService).fetchRegionalHealthScores(null, regionId, en, year);
    }

    @Test
     void shouldGetCsvTemplate() throws IOException {
        doReturn("/tmp/Digital Health Data.xlsx").when(bffController).getFileWithPath();
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        bffController.getCsvTemplate(request,response);
        verify(excelUtilService).downloadFile(any(),any(),eq("/tmp/Digital Health Data.xlsx"));
    }
}
