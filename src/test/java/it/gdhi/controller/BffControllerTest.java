package it.gdhi.controller;

import it.gdhi.service.BffService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
}
