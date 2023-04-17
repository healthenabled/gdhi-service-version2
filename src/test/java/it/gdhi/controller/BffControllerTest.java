package it.gdhi.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import it.gdhi.dto.GdhiQuestionnaire;
import it.gdhi.dto.GdhiQuestionnaires;
import it.gdhi.service.BffService;
import it.gdhi.service.CountryHealthDataService;
import it.gdhi.service.DefaultYearDataService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static it.gdhi.utils.ApplicationConstants.defaultLimit;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BffControllerTest {

    @InjectMocks
    private BffController bffController;

    @Mock
    private BffService bffService;
    @Mock
    private CountryHealthDataService countryHealthDataService;

    @Mock
    private DefaultYearDataService defaultYearDataService;

    @Test
    public void shouldGetDistinctYears() {
        bffController.getDistinctYears();
        verify(bffService).fetchDistinctYears();
    }

    @Test
    public void shouldGetYearOnYearData() {
        bffController.getYearOnYearData(null, 1, null);
        verify(bffService).fetchPublishedYearsForACountry(null, 1);
        verify(bffService).fetchYearOnYearData(bffService.fetchPublishedYearsForACountry(null, 1), null, null);
    }

    @Test
    public void shouldGetYearOnYearDataForFiveYearsWhenLimitIsNull() {
        bffController.getYearOnYearData(null, null, null);
        verify(bffService).fetchPublishedYearsForACountry(null, defaultLimit);
        verify(bffService).fetchYearOnYearData(bffService.fetchPublishedYearsForACountry(null, defaultLimit), null, null);
    }

    @Test
    public void shouldGetPublishedYearsForACountry() {
        bffController.getPublishedYearsForACountry(null, 1);
        verify(bffService).fetchPublishedYearsForACountry(null, 1);
    }

    @Test
    public void shouldGetFivePublishedYearsForACountryWhenLimitIsNull() {
        bffController.getPublishedYearsForACountry(null, null);
        verify(bffService).fetchPublishedYearsForACountry(null, defaultLimit);
    }

    @Test
    public void shouldGetGlobalBenchmarkDetailsForGivenYear() {
        String countryID = "IND";
        Integer benchmarkType = -1;
        String year = "Version1";
        String region = null;
        when(countryHealthDataService.getBenchmarkDetailsFor(countryID, benchmarkType, year, region)).thenReturn(new HashMap<>());
        bffController.getBenchmarkDetailsFor(countryID, benchmarkType, year, region);
        verify(countryHealthDataService).getBenchmarkDetailsFor(countryID, benchmarkType, year, region);
    }

    @Test
    public void shouldGetGlobalBenchmarkDetailsForDefaultYearWhenYearIsNull() {
        String countryID = "IND";
        Integer benchmarkType = -1;
        String year = "2023";
        String region = "PAHO";
        when(defaultYearDataService.fetchDefaultYear()).thenReturn(year);
        bffController.getBenchmarkDetailsFor(countryID, benchmarkType, null, region);
        verify(countryHealthDataService).getBenchmarkDetailsFor(countryID, benchmarkType, year, region);
    }

    @Test
    public void shouldGetRegionalBenchmarkDetailsForGivenYearWhenRegionIsGiven() {
        String countryID = "IND";
        Integer benchmarkType = -1;
        String year = "Version1";
        String region = "PAHO";
        when(countryHealthDataService.getBenchmarkDetailsFor(countryID, benchmarkType, year, region)).thenReturn(new HashMap<>());
        bffController.getBenchmarkDetailsFor(countryID, benchmarkType, year, region);
        verify(countryHealthDataService).getBenchmarkDetailsFor(countryID, benchmarkType, year, region);
    }
    @Test
    public void shouldGetYearOnYearDataWhenRegionIsPassed(){
        bffController.getYearOnYearData(null, 1,"EURO");
        verify(bffService).fetchPublishedYearsForACountry(null, 1);
        verify(bffService).fetchYearOnYearData(bffService.fetchPublishedYearsForACountry(null, 1), null,"EURO");
    }

    @Test
    public void shouldSubmitCSVDataWhenListOfQuestionnairesAreGiven() {
        GdhiQuestionnaires mock = mock(GdhiQuestionnaires.class);
        bffController.shouldSubmitCSVData(mock);
        verify(bffService).submitCountryCSVData(mock);
    }
}
