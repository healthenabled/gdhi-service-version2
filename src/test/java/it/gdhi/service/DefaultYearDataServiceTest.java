package it.gdhi.service;

import it.gdhi.model.DefaultYearData;
import it.gdhi.repository.IDefaultYearData;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DefaultYearDataServiceTest {

    @Mock
    private IDefaultYearData iDefaultYearData;

    @InjectMocks
    private DefaultYearDataService defaultYearDataService;

    @Test
    public void shouldReturnAllYears() {
        DefaultYearData year2021 = DefaultYearData.builder().year("2021").build();
        DefaultYearData year2020 = DefaultYearData.builder().year("2020").build();

        when(iDefaultYearData.findAll()).thenReturn(asList(year2021, year2020));
        List<String> expectedYears = Arrays.asList("2021", "2020");
        List<String> actualYears = defaultYearDataService.fetchYears();

        assertEquals(expectedYears, actualYears);
    }

    @Test
    public void shouldReturnLatestDefaultYear() {
        DefaultYearData year2021 = DefaultYearData.builder().year("2021").build();

        when(iDefaultYearData.findFirstByOrderByCreatedAtDesc()).thenReturn(year2021);
        String expectedDefaultYear = "2021";
        String actualDefaultYear = defaultYearDataService.fetchDefaultYear();

        assertEquals(expectedDefaultYear, actualDefaultYear);
    }

    @Test
    public void shouldReturnNullWhenThereIsNoDefaultYear() {
        DefaultYearData year2021 = null;

        when(iDefaultYearData.findFirstByOrderByCreatedAtDesc()).thenReturn(year2021);
        String expectedDefaultYear = null;
        String actualDefaultYear = defaultYearDataService.fetchDefaultYear();

        assertEquals(expectedDefaultYear, actualDefaultYear);
    }
}
