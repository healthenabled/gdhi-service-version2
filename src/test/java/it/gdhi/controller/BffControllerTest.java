package it.gdhi.controller;

import it.gdhi.service.BffService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static it.gdhi.utils.ApplicationConstants.defaultLimit;
import static org.mockito.Mockito.verify;

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
        bffController.getYearOnYearData(null, 1,null);
        verify(bffService).fetchPublishedYearsForACountry(null, 1);
        verify(bffService).fetchYearOnYearData(bffService.fetchPublishedYearsForACountry(null, 1), null,null);
    }

    @Test
    public void shouldGetYearOnYearDataForFiveYearsWhenLimitIsNull() {
        bffController.getYearOnYearData(null, null,null);
        verify(bffService).fetchPublishedYearsForACountry(null, defaultLimit);
        verify(bffService).fetchYearOnYearData(bffService.fetchPublishedYearsForACountry(null, defaultLimit), null,null);
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
}
