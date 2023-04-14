package it.gdhi.service;

import it.gdhi.internationalization.service.RegionNameTranslator;
import it.gdhi.model.Region;
import it.gdhi.repository.IRegionCountryRepository;
import it.gdhi.repository.IRegionRepository;
import it.gdhi.utils.LanguageCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RegionService {

    @Autowired
    private IRegionRepository iRegionRepository;

    @Autowired
    private RegionNameTranslator regionNameTranslator;

    @Autowired
    private IRegionCountryRepository iRegionCountryRepository;

    public List<Region> fetchRegions(LanguageCode languageCode) {
        List<Region> regions = iRegionRepository.findAll();
        return regionNameTranslator.translate(regions, languageCode);
    }

    public List<String> fetchCountriesForARegion(String regionId) {
        return iRegionCountryRepository.findByRegionCountryIdRegionId(regionId);
    }
}
