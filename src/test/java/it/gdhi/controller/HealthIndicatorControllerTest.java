package it.gdhi.controller;

import it.gdhi.dto.CountriesHealthScoreDto;
import it.gdhi.dto.CountryHealthScoreDto;
import it.gdhi.dto.GlobalHealthScoreDto;
import it.gdhi.service.CountryHealthIndicatorService;
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

    @Test
    public void shouldInvokeGetGlobalHealthIndicator() {
        GlobalHealthScoreDto expected = mock(GlobalHealthScoreDto.class);
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("USER_LANGUAGE", "en");

        when(countryHealthIndicatorService.getGlobalHealthIndicator(null, 2, en)).thenReturn(expected);
        GlobalHealthScoreDto actual = healthIndicatorController.getGlobalHealthIndicator(request, null, 2);

        assertThat(expected, is(actual));
        verify(countryHealthIndicatorService).getGlobalHealthIndicator(null, 2, en);
    }

    @Test
    public void shouldGetLanguageCodeOfArabicFromHeader() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("USER_LANGUAGE", "ar");

        healthIndicatorController.getGlobalHealthIndicator(request, null, 2);

        verify(countryHealthIndicatorService).getGlobalHealthIndicator(null, 2, ar);
    }

    @Test
    public void shouldInvokeFetchHealthScoresOnGettingGlobalInfo() {
        CountriesHealthScoreDto mockGlobalHealthScore = mock(CountriesHealthScoreDto.class);
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("USER_LANGUAGE", "fr");

        CountryHealthScoreDto countryHealthScoreDto = mock(CountryHealthScoreDto.class);
        when(countryHealthScoreDto.getCountryId()).thenReturn("ARG");
        when(mockGlobalHealthScore.getCountryHealthScores()).thenReturn(singletonList(countryHealthScoreDto));
        when(countryHealthIndicatorService.fetchCountriesHealthScores(4, null, fr)).thenReturn(mockGlobalHealthScore);

        CountriesHealthScoreDto globalHealthIndicators = healthIndicatorController.getCountriesHealthIndicatorScores(request, 4, null);
        int size = globalHealthIndicators.getCountryHealthScores().size();

        assertThat(size, is(1));
        assertThat(globalHealthIndicators.getCountryHealthScores().get(0).getCountryId(), is(countryHealthScoreDto.getCountryId()));
        verify(countryHealthIndicatorService).fetchCountriesHealthScores(4, null, fr);
    }

    @Test
    public void shouldInvokeGetCountriesHealthIndicatorsInPortuguese() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("USER_LANGUAGE", "pt");

        healthIndicatorController.getCountriesHealthIndicatorScores(request, null, 2);

        verify(countryHealthIndicatorService).fetchCountriesHealthScores(null, 2, pt);
    }
}