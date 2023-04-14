package it.gdhi.service;

import it.gdhi.internationalization.RegionNameTranslatorTest;
import it.gdhi.internationalization.service.RegionNameTranslator;
import it.gdhi.model.Region;
import it.gdhi.repository.IRegionCountryRepository;
import it.gdhi.repository.IRegionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static it.gdhi.utils.LanguageCode.en;
import static it.gdhi.utils.LanguageCode.fr;
import static org.codehaus.groovy.runtime.InvokerHelper.asList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class RegionServiceTest {
    @InjectMocks
    private RegionService regionService;

    @Mock
    private IRegionRepository iRegionRepository;

    @Mock
    private RegionNameTranslator regionNameTranslator;

    @Mock
    private IRegionCountryRepository iRegionCountryRepository;

    public Region createRegion(String id, String name) {
        Region region = Region.builder().region_id(id).regionName(name).build();
        return region;
    }

    @Test
    public void shouldFetchAllRegions() {
        String id = "AFRO";
        String name = "African Region";
        List<Region> regions = asList(createRegion(id, name));

        when(iRegionRepository.findAll()).thenReturn(regions);

        assertEquals(regions.get(0).getRegion_id(), id);
        assertEquals(regions.get(0).getRegionName(), name);
    }

    @Test
    public void shouldVerifyThatRepositoryLayerIsInvoked() {
        regionService.fetchRegions(en);
        verify(iRegionRepository).findAll();
    }

    @Test
    public void shouldFetchRegionsForAGivenLanguage() {
        List<Region> regions = new ArrayList<>();

        RegionNameTranslatorTest regionNameTranslatorTest = new RegionNameTranslatorTest();
        List<Region> listOfRegionsInEnglish = regionNameTranslatorTest.createListOfRegionsInEnglish();
        List<Region> listOfRegionsInFrench = regionNameTranslatorTest.createListOfRegionsInFrench();

        when(iRegionRepository.findAll()).thenReturn(listOfRegionsInEnglish);
        when(regionNameTranslator.translate(listOfRegionsInEnglish, fr)).thenReturn(listOfRegionsInFrench);

        assertEquals(listOfRegionsInEnglish.get(1).getRegion_id(), "AFRO");
        assertEquals(listOfRegionsInFrench.get(1).getRegionName(), "Région africaine");
    }

    @Test
    public void shouldFetchCountriesForARegion() {
        String region = "PAHO";
        List<String> countries = Arrays.asList("ARG", "ATG", "BHS");

        when(iRegionCountryRepository.findByRegionCountryIdRegionId(region)).thenReturn(countries);
        List<String> actualCountries = regionService.fetchCountriesForARegion(region);

        assertEquals(countries, actualCountries);
    }
}

