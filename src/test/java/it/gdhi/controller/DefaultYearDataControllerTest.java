package it.gdhi.controller;

import it.gdhi.service.CountryService;
import it.gdhi.service.DefaultYearDataService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DefaultYearDataControllerTest {

    @Mock
    private DefaultYearDataService defaultYearDataService;

    @Mock
    private CountryService countryService;

    @InjectMocks
    private DefaultYearDataController defaultYearDataController;

    @Test
    public void shouldGetAllYears() {
        defaultYearDataController.getAllYears();
        verify(defaultYearDataService).fetchYears();
    }

    @Test
    public void shouldSaveDefaultYear() {
        String year = "Version1";
        doNothing().when(defaultYearDataService).saveNewYear(year);
        when(countryService.validateDefaultYear(year)).thenReturn(true);
        defaultYearDataController.saveDefaultYear(year);
        verify(defaultYearDataService).saveNewYear(year);
    }
    @Test
    public void shouldNotSaveDefaultYearWhenYearIsNotValid() {
        String year = "2002";
        when(countryService.validateDefaultYear(year)).thenReturn(false);
        defaultYearDataController.saveDefaultYear(year);
        verify(defaultYearDataService, times(0)).saveNewYear(year);
    }

}
