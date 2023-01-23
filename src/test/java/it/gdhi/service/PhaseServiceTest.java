package it.gdhi.service;

import it.gdhi.dto.PhaseDto;
import it.gdhi.model.Phase;
import it.gdhi.repository.IPhaseRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PhaseServiceTest {

    @Mock
    IPhaseRepository iPhaseRepository;

    @InjectMocks
    PhaseService phaseService;

    @Test
    public void shouldReturnPhaseDtos() {
        Phase phase1 = new Phase(1,"phase 1 ", 1);
        Phase phase2 = new Phase(2,"phase 2 ", 2);
        Phase phase3 = new Phase(3,"phase 3 ", 3);

        when(iPhaseRepository.findAll()).thenReturn(Arrays.asList(phase1,phase2,phase3));

        List<PhaseDto> expectedPhaseDtos = Arrays.asList(
                new PhaseDto(phase1.getPhaseName(), phase1.getPhaseValue()),
                new PhaseDto(phase2.getPhaseName(), phase2.getPhaseValue()),
                new PhaseDto(phase3.getPhaseName(), phase3.getPhaseValue()));

        List<PhaseDto> actualPhaseDtos = phaseService.getPhaseOptions();
        assertEquals(3, actualPhaseDtos.size());
        assertThat(expectedPhaseDtos.get(0)).usingRecursiveComparison().isEqualTo(actualPhaseDtos.get(0));
        assertThat(expectedPhaseDtos.get(1)).usingRecursiveComparison().isEqualTo(actualPhaseDtos.get(1));
        assertThat(expectedPhaseDtos.get(2)).usingRecursiveComparison().isEqualTo(actualPhaseDtos.get(2));
    }
}