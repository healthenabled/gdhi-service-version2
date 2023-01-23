package it.gdhi.service;

import it.gdhi.dto.PhaseDto;
import it.gdhi.repository.IPhaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PhaseService {

    @Autowired
    private IPhaseRepository iPhaseRepository;

    public List<PhaseDto> getPhaseOptions() {
        return iPhaseRepository.findAll().stream()
                .map(phase -> new PhaseDto(phase.getPhaseName(),phase.getPhaseValue()))
                .collect(Collectors.toList());
    }
}
