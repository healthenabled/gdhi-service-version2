package it.gdhi.controller;

import it.gdhi.dto.CountriesHealthScoreDto;
import it.gdhi.dto.CountryHealthScoreDto;
import it.gdhi.dto.GlobalHealthScoreDto;
import it.gdhi.service.CountryHealthIndicatorService;
import it.gdhi.service.DefaultYearDataService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;

import static it.gdhi.utils.LanguageCode.*;
import static java.util.Collections.singletonList;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class HealthIndicatorControllerTest {

    @InjectMocks
    private HealthIndicatorController healthIndicatorController;

    @Mock
    private CountryHealthIndicatorService countryHealthIndicatorService;

    @Mock
    private DefaultYearDataService defaultYearDataService;

    @Test
    public void shouldInvokeGetGlobalHealthIndicator() {
        GlobalHealthScoreDto expected = mock(GlobalHealthScoreDto.class);
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("USER_LANGUAGE", "en");

        when(countryHealthIndicatorService.getGlobalHealthIndicator(null, 2, en, "Version1")).thenReturn(expected);
        GlobalHealthScoreDto actual = healthIndicatorController.getGlobalHealthIndicator(request, null, 2, "Version1");

        assertThat(expected, is(actual));
        verify(countryHealthIndicatorService).getGlobalHealthIndicator(null, 2, en, "Version1");
    }

    @Test
    public void shouldGetLanguageCodeOfArabicFromHeader() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("USER_LANGUAGE", "ar");

        healthIndicatorController.getGlobalHealthIndicator(request, null, 2, "Version1");

        verify(countryHealthIndicatorService).getGlobalHealthIndicator(null, 2, ar, "Version1");
    }

    @Test
    public void shouldInvokeFetchHealthScoresOnGettingGlobalInfo() {
        CountriesHealthScoreDto mockGlobalHealthScore = mock(CountriesHealthScoreDto.class);
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("USER_LANGUAGE", "fr");

        CountryHealthScoreDto countryHealthScoreDto = mock(CountryHealthScoreDto.class);
        when(countryHealthScoreDto.getCountryId()).thenReturn("ARG");
        when(mockGlobalHealthScore.getCountryHealthScores()).thenReturn(singletonList(countryHealthScoreDto));
        when(countryHealthIndicatorService.fetchCountriesHealthScores(4, null, fr, "Version1")).thenReturn(mockGlobalHealthScore);

        CountriesHealthScoreDto globalHealthIndicators = healthIndicatorController.getCountriesHealthIndicatorScores(request, 4, null, "Version1");
        int size = globalHealthIndicators.getCountryHealthScores().size();

        assertThat(size, is(1));
        assertThat(globalHealthIndicators.getCountryHealthScores().get(0).getCountryId(), is(countryHealthScoreDto.getCountryId()));
        verify(countryHealthIndicatorService).fetchCountriesHealthScores(4, null, fr, "Version1");
    }

    @Test
    public void shouldInvokeGetCountriesHealthIndicatorsInPortuguese() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("USER_LANGUAGE", "pt");

        healthIndicatorController.getCountriesHealthIndicatorScores(request, null, 2, "Version1");

        verify(countryHealthIndicatorService).fetchCountriesHealthScores(null, 2, pt, "Version1");
    }

    @Test
    public void shouldFetchDefaultYearWhenYearIsNull() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("USER_LANGUAGE", "en");

        when(defaultYearDataService.fetchDefaultYear()).thenReturn("Version1");

        healthIndicatorController.getCountriesHealthIndicatorScores(request, null, 2, null);
        healthIndicatorController.getGlobalHealthIndicator(request, null, 2, null);

        verify(countryHealthIndicatorService).fetchCountriesHealthScores(null, 2, en, "Version1");
        verify(countryHealthIndicatorService).getGlobalHealthIndicator(null, 2, en, "Version1");
    }
}