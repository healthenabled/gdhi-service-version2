package it.gdhi.internationalization.service;
import it.gdhi.internationalization.model.RegionTranslation;
import it.gdhi.internationalization.repository.IRegionTranslationRepository;
import it.gdhi.model.Region;
import it.gdhi.utils.LanguageCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.List;
import static it.gdhi.utils.LanguageCode.en;
import static java.util.stream.Collectors.toList;

@Component
public class RegionNameTranslator {

    @Autowired
    private IRegionTranslationRepository translationRepository;

    public List<Region> translate(List<Region> regions, LanguageCode languageCode) {
        if (languageCode == en || languageCode == null) return regions;

        return regions.stream()
                .map(region -> region.makeWithName(getRegionTranslationForLanguage(languageCode, region.getRegionId())))
                .collect(toList());
    }

    public String getRegionTranslationForLanguage(LanguageCode languageCode, String regionId) {
        RegionTranslation regionTranslation = translationRepository.findByIdRegionIdAndIdLanguageId(regionId, languageCode.toString());
        return regionTranslation.getRegion_name();
    }
}
