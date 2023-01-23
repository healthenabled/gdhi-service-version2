package it.gdhi.service;


import it.gdhi.model.DevelopmentIndicator;
import it.gdhi.repository.IDevelopmentIndicatorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DevelopmentIndicatorService {

    @Autowired
    private IDevelopmentIndicatorRepository iDevelopmentIndicatorRepository;

    public DevelopmentIndicator fetchCountryDevelopmentScores(String countryId) {
        return iDevelopmentIndicatorRepository.findByCountryId(countryId).orElse(new DevelopmentIndicator());
    }
}
