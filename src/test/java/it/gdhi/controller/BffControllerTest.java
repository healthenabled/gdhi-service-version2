package it.gdhi.controller;

import java.util.Collections;

import it.gdhi.service.BffService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BffControllerTest {

    @InjectMocks
    private BffController bffController;

    @Mock
    private BffService bffService;

    @Test
    public void shouldGetDistinctYears() {
        bffController.getDistinctYears();
        verify(bffService).fetchDistinctYears();
    }
    @Test
    public void shouldGetYearOnYearData() {
        bffController.getYearOnYearData(null, 1);
        verify(bffService).fetchPublishedYearsForACountry(null, 1);
        verify(bffService).fetchYearOnYearData(bffService.fetchPublishedYearsForACountry(null, 1), null);
    }
    @Test
    public void shouldGetYearOnYearDataForFiveYearsWhenLimitIsNull() {
        bffController.getYearOnYearData(null, null);
        verify(bffService).fetchPublishedYearsForACountry(null, 5);
        verify(bffService).fetchYearOnYearData(bffService.fetchPublishedYearsForACountry(null, 5), null);
    }
}
