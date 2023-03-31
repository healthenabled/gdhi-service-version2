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
    IRegionRepository iRegionRepository;
    public List<Region> getAllRegions(LanguageCode languageCode) {
        List<Region> regionList = iRegionRepository.findAll();
        return regionList;
    }
}
