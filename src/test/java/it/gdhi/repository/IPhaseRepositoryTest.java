package it.gdhi.repository;

import it.gdhi.model.Phase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.transaction.Transactional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@Transactional
@ActiveProfiles("test")
public class IPhaseRepositoryTest {

    @Autowired
    IPhaseRepository phaseRepository;

    @Test
    public void shouldReturnAllPhasesOrderedByPhaseId(){
        List<Phase> phases = phaseRepository.findAll();

        assertEquals(5, phases.size());
        assertTrue(phases.get(0).getPhaseId() < phases.get(2).getPhaseId());
        assertTrue(phases.get(2).getPhaseId() < phases.get(4).getPhaseId());
    }

}