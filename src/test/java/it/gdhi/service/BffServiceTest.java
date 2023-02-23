package it.gdhi.service;

import it.gdhi.dto.YearDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BffServiceTest {

    @InjectMocks
    private BffService bffService;

    @Mock
    private CountryService countryService;

    @Mock
    private DefaultYearDataService defaultYearDataService;

    @Test
    public void shouldReturnDistinctYearsAndDefaultYear() {
        when(countryService.fetchPublishCountriesDistinctYears()).thenReturn(asList("Version1", "2023"));
        when(defaultYearDataService.fetchDefaultYear()).thenReturn("2023");

        YearDto expectedYearData = YearDto.builder().years(asList("Version1", "2023")).defaultYear("2023").build();
        YearDto actualYearData = bffService.fetchDistinctYears();

        assertThat(expectedYearData.getDefaultYear(), equalTo(actualYearData.getDefaultYear()));
        assertThat(expectedYearData.getYears(), equalTo(actualYearData.getYears()));
    }
}
