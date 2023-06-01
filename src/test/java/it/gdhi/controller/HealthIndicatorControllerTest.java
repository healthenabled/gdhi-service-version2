package it.gdhi.controller;

import it.gdhi.dto.CountriesHealthScoreDto;
import it.gdhi.dto.CountryHealthScoreDto;
import it.gdhi.service.CountryHealthIndicatorService;
import it.gdhi.service.DefaultYearDataService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;

import static it.gdhi.utils.LanguageCode.fr;
import static it.gdhi.utils.LanguageCode.pt;
import static java.util.Collections.singletonList;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class HealthIndicatorControllerTest {

    @InjectMocks
    private HealthIndicatorController healthIndicatorController;

    @Mock
    private CountryHealthIndicatorService countryHealthIndicatorService;

    @Mock
    private DefaultYearDataService defaultYearDataService;

    @Test
    public void shouldInvokeFetchHealthScoresOnGettingGlobalInfo() {
        CountriesHealthScoreDto mockGlobalHealthScore = mock(CountriesHealthScoreDto.class);
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("USER_LANGUAGE", "fr");

        CountryHealthScoreDto countryHealthScoreDto = mock(CountryHealthScoreDto.class);
        when(countryHealthScoreDto.getCountryId()).thenReturn("ARG");
        when(mockGlobalHealthScore.getCountryHealthScores()).thenReturn(singletonList(countryHealthScoreDto));
        String year = "Version1";
        when(countryHealthIndicatorService.fetchCountriesHealthScores(4, null, fr, year)).thenReturn(mockGlobalHealthScore);

        CountriesHealthScoreDto globalHealthIndicators = healthIndicatorController.getCountriesHealthIndicatorScores(request, 4, null, null, year);
        int size = globalHealthIndicators.getCountryHealthScores().size();

        assertThat(size, is(1));
        assertThat(globalHealthIndicators.getCountryHealthScores().get(0).getCountryId(), is(countryHealthScoreDto.getCountryId()));
        verify(countryHealthIndicatorService).fetchCountriesHealthScores(4, null, fr, year);
    }

    @Test
    public void shouldInvokeGetCountriesHealthIndicatorsInPortuguese() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("USER_LANGUAGE", "pt");

        healthIndicatorController.getCountriesHealthIndicatorScores(request, null, 2, null, "Version1");

        verify(countryHealthIndicatorService).fetchCountriesHealthScores(null, 2, pt, "Version1");
    }

    @Test
    public void shouldInvokeFetchHealthScoresOnGettingRegionInfo() {
        CountriesHealthScoreDto mockGlobalHealthScore = mock(CountriesHealthScoreDto.class);
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("USER_LANGUAGE", "fr");

        CountryHealthScoreDto countryHealthScoreDto = mock(CountryHealthScoreDto.class);
        when(countryHealthScoreDto.getCountryId()).thenReturn("ARG");
        when(mockGlobalHealthScore.getCountryHealthScores()).thenReturn(singletonList(countryHealthScoreDto));
        String year = "Version1";
        String region = "PAHO";
        when(countryHealthIndicatorService.fetchCountriesHealthScores(4, null, fr, year)).thenReturn(mockGlobalHealthScore);

        CountriesHealthScoreDto globalHealthIndicators = healthIndicatorController.getCountriesHealthIndicatorScores(request, 4, null, region, year);
        int size = globalHealthIndicators.getCountryHealthScores().size();

        assertThat(size, is(1));
        assertThat(globalHealthIndicators.getCountryHealthScores().get(0).getCountryId(), is(countryHealthScoreDto.getCountryId()));
        verify(countryHealthIndicatorService).fetchCountriesHealthScores(4, null, fr, year);
    }

}