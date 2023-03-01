package it.gdhi.controller;

import it.gdhi.dto.*;
import it.gdhi.model.DevelopmentIndicator;
import it.gdhi.service.CountryHealthDataService;
import it.gdhi.service.CountryHealthIndicatorService;
import it.gdhi.service.CountryService;
import it.gdhi.service.DevelopmentIndicatorService;
import it.gdhi.utils.LanguageCode;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.sql.Timestamp;
import java.time.Year;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

import static it.gdhi.utils.FormStatus.DRAFT;
import static it.gdhi.utils.LanguageCode.en;
import static it.gdhi.utils.LanguageCode.fr;
import static java.util.Collections.emptyList;
import static java.util.Collections.emptyMap;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CountryControllerTest {

    @InjectMocks
    private CountryController countryController;

    @Mock
    private CountryService countryService;

    @Mock
    private CountryHealthDataService countryHealthDataService;

    @Mock
    private DevelopmentIndicatorService developmentIndicatorService;

    @Mock
    private CountryHealthIndicatorService countryHealthIndicatorService;

    @Test
    public void shouldListCountries() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("USER_LANGUAGE", "en");

        countryController.getCountries(request);

        verify(countryService).fetchCountries(en);
    }

    @Test
    public void shouldListCountriesInGivenLanguage() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("USER_LANGUAGE", "fr");

        countryController.getCountries(request);

        verify(countryService).fetchCountries(fr);
    }

    @Test
    public void shouldInvokeHealthIndicatorServiceCountryScore() {
        String countryId = "ARG";
        CountryHealthScoreDto countryHealthScoreMock = mock(CountryHealthScoreDto.class);
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("USER_LANGUAGE", "en");
        String year = "Version1";

        when(countryHealthIndicatorService.fetchCountryHealthScore(countryId, LanguageCode.en, year)).thenReturn(countryHealthScoreMock);

        CountryHealthScoreDto healthIndicatorForGivenCountryCode = countryController.getHealthIndicatorForGivenCountryCode(request, countryId, year);

        assertThat(healthIndicatorForGivenCountryCode, is(countryHealthScoreMock));
        verify(countryHealthIndicatorService).fetchCountryHealthScore(countryId, LanguageCode.en, year);
    }

    @Test
    public void shouldInvokeHealthIndicatorServiceWithGivenLanguageCode() {
        String countryId = "ARG";
        CountryHealthScoreDto countryHealthScoreMock = mock(CountryHealthScoreDto.class);
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("USER_LANGUAGE", "ar");
        String year = "Version1";

        when(countryHealthIndicatorService.fetchCountryHealthScore(countryId, LanguageCode.ar, year)).thenReturn(countryHealthScoreMock);

        countryController.getHealthIndicatorForGivenCountryCode(request, countryId, year);

        verify(countryHealthIndicatorService).fetchCountryHealthScore(countryId, LanguageCode.ar, year);
    }

    @Test
    public void shouldSubmitHealthIndicators() {
        GdhiQuestionnaire mock = mock(GdhiQuestionnaire.class);
        doNothing().when(countryHealthDataService).submit(mock);
        when(countryHealthDataService.validateRequiredFields(mock)).thenReturn(true);
        countryController.submitHealthIndicatorsFor(mock);
        verify(countryHealthDataService).submit(mock);
    }

    @Test
    public void shouldSaveCorrectedHealthIndicators() {
        GdhiQuestionnaire mock = mock(GdhiQuestionnaire.class);
        doNothing().when(countryHealthDataService).saveCorrection(mock);
        countryController.saveCorrectionsFor(mock);
        verify(countryHealthDataService).saveCorrection(mock);
    }

    @Test
    public void shouldSaveHealthIndicators() {
        GdhiQuestionnaire mock = mock(GdhiQuestionnaire.class);
        doNothing().when(countryHealthDataService).save(mock, DRAFT.name());
        countryController.saveHealthIndicatorsFor(mock);
        verify(countryHealthDataService).save(mock, DRAFT.name());
    }

    @Test
    public void shouldPublishHealthIndicators() {
        GdhiQuestionnaire mock = mock(GdhiQuestionnaire.class);
        doNothing().when(countryHealthDataService).publish(mock);
        when(countryHealthDataService.validateRequiredFields(mock)).thenReturn(true);
        countryController.publishHealthIndicatorsFor(mock);
        verify(countryHealthDataService).publish(mock);
    }

    @Test
    public void shouldFetchCountrySummary() {
        CountrySummaryDto countrySummary = mock(CountrySummaryDto.class);
        String countryId = "IND";
        String year = "Version1";
        when(countryService.fetchCountrySummary(countryId, year)).thenReturn(countrySummary);
        CountrySummaryDto actualCountrySummary = countryController.fetchCountrySummary(countryId, year);
        assertThat(actualCountrySummary, is(countrySummary));
    }

    @Test
    public void shouldFetchTheDevelopmentIndicators() {
        String countryId = "ARG";
        DevelopmentIndicator developmentIndicator = new DevelopmentIndicator();

        when(developmentIndicatorService.fetchCountryDevelopmentScores(countryId)).thenReturn(developmentIndicator);

        DevelopmentIndicator actualDevelopmentIndicator = countryController.
                getDevelopmentIndicatorForGivenCountryCode(countryId);

        verify(developmentIndicatorService).fetchCountryDevelopmentScores(countryId);

        assertEquals(developmentIndicator.getCountryId(), actualDevelopmentIndicator.getCountryId());
    }

    @Test
    public void shouldExportGlobalData() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        String year = "Version1";
        countryController.exportGlobalData(request, response, year);
        verify(countryHealthIndicatorService).createGlobalHealthIndicatorInExcel(request, response, year);
    }

    @Test
    public void shouldExportDataForAGivenCountry() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        String year = "Version1";
        countryController.exportCountryDetails(request, response, "IND", year);
        verify(countryHealthIndicatorService).createHealthIndicatorInExcelFor("IND", request, response, year);
    }

    @Test
    public void shouldSaveCountrySummaryAsNewStatus() throws Exception {
        String countryId = "IND";
        UUID countryUUID = UUID.randomUUID();
        CountryUrlGenerationStatusDto expected = new CountryUrlGenerationStatusDto(countryId, true, null);
        when(countryHealthDataService.saveNewCountrySummary(countryUUID)).thenReturn(expected);

        CountryUrlGenerationStatusDto actualResponse = countryController.saveNewCountrySummary(countryUUID);

        assertSame(actualResponse, expected);
    }

    @Test
    public void shouldDeleteCountryData() throws Exception {
        UUID countryUUID = UUID.randomUUID();
        String currentYear = "2020";
        doNothing().when(countryHealthDataService).deleteCountryData(countryUUID , currentYear);

        countryController.deleteCountryData(countryUUID , currentYear);

        verify(countryHealthDataService).deleteCountryData(countryUUID , currentYear);
    }

    @Test
    public void shouldGetAllCountryStatusSummaries() {
        CountrySummaryStatusYearDto countrySummaryStatusYearDto = new CountrySummaryStatusYearDto(this.getCurrentYear(), emptyList(), emptyList(), emptyList(), emptyList());
        when(countryHealthDataService.getAllCountryStatusSummaries()).thenReturn(countrySummaryStatusYearDto);
        countryController.getAllCountryStatusSummaries();
        verify(countryHealthDataService).getAllCountryStatusSummaries();
    }

    @Test
    public void shouldGetQuestionnaireForPublishedCountry() {
        UUID countryUUID = UUID.randomUUID();
        GdhiQuestionnaire gdhiQuestionnaire = mock(GdhiQuestionnaire.class);
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("USER_LANGUAGE", "en");
        String year = "Version1";
        when(countryService.getDetails(countryUUID, LanguageCode.en, true, year)).thenReturn(gdhiQuestionnaire);

        countryController.getQuestionnaireForPublishedCountry(request, countryUUID, year);

        verify(countryService).getDetails(countryUUID, LanguageCode.en, true, year);
    }


    @Test
    public void shouldGetGlobalBenchmarkDetailsFor() {
        String countryID = "IND";
        Integer benchmarkType = -1;
        String year = "Version1";
        when(countryHealthDataService.getBenchmarkDetailsFor(countryID, benchmarkType, year)).thenReturn(new HashMap<>());
        countryController.getBenchmarkDetailsFor(countryID, benchmarkType, year);
        verify(countryHealthDataService).getBenchmarkDetailsFor(countryID, benchmarkType, year);
    }

    @Test
    public void shouldCalculatePhaseForAllCountries() {
        String year = "Version1";
        doNothing().when(countryHealthDataService).calculatePhaseForAllCountries(year);
        countryController.calculateCountryPhase(year);
        verify(countryHealthDataService).calculatePhaseForAllCountries(year);
    }

    private String getCurrentYear() {
        int currentYear = Year.now().getValue();
        String year = new String(String.valueOf(currentYear));
        return year;
    }
}
