package it.gdhi.service;

import it.gdhi.dto.YearDto;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.relational.core.sql.TrueCondition;

import static java.util.Arrays.asList;
import static org.assertj.core.internal.bytebuddy.matcher.ElementMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
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
        when(countryService.fetchPublishCountriesDistinctYears()).thenReturn(asList("Version1","2023"));
        when(defaultYearDataService.fetchDefaultYear()).thenReturn("2023");
        YearDto ExpectedYearData = YearDto.builder().years(asList("Version1","2023")).defaultYear("2023").build();
        YearDto ActualYearData = bffService.fetchDistinctYears();

        //assertThat(ActualYearData.equals(ExpectedYearData), is(equalTo(true)));
        assertThat(ExpectedYearData.getDefaultYear(), equalTo(ActualYearData.getDefaultYear()));
        assertThat(ExpectedYearData.getYears(), equalTo(ActualYearData.getYears()));
    }
}
