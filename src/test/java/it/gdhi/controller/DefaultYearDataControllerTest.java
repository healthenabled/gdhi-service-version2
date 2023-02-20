package it.gdhi.controller;

import it.gdhi.service.DefaultYearDataService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class DefaultYearDataControllerTest {

    @Mock
    private DefaultYearDataService defaultYearDataService;

    @InjectMocks
    private DefaultYearDataController defaultYearDataController;

    @Test
    public void shouldGetAllYears(){
        defaultYearDataController.getAllYears();
        verify(defaultYearDataService).fetchYears();
    }

}
