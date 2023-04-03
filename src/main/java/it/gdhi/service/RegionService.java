package it.gdhi.service;

import it.gdhi.model.Region;
import it.gdhi.repository.IRegionRepository;
import it.gdhi.utils.LanguageCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RegionService {

    @Autowired
    private IRegionRepository iRegionRepository;

    public List<Region> fetchRegions(LanguageCode languageCode) {
        List<Region> regions = iRegionRepository.findAll();
        return regions;
    }
}
