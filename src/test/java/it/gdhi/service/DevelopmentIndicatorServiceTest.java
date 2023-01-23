package it.gdhi.service;

import it.gdhi.model.DevelopmentIndicator;
import it.gdhi.repository.IDevelopmentIndicatorRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class DevelopmentIndicatorServiceTest {

    @InjectMocks
    DevelopmentIndicatorService developmentIndicatorService;

    @Mock
    private IDevelopmentIndicatorRepository iDevelopmentIndicatorRepository;

    @Test
    public void shouldInvokeDevelopmentIndicatorRepoToFetchCountryContextInfo() {
        String countryId = "ARG";
        when(iDevelopmentIndicatorRepository.findByCountryId(countryId)).thenReturn(Optional.of(new DevelopmentIndicator()));
        developmentIndicatorService.fetchCountryDevelopmentScores(countryId);
        verify(iDevelopmentIndicatorRepository).findByCountryId(countryId);
    }

    @Test
    public void shouldReturnEmptyIfNoContextAvailable() {
        String countryId = "ARG";
        when(iDevelopmentIndicatorRepository.findByCountryId(countryId)).thenReturn(Optional.empty());

        DevelopmentIndicator developmentIndicator = developmentIndicatorService.fetchCountryDevelopmentScores(countryId);

        assertNotNull(developmentIndicator);
    }
}