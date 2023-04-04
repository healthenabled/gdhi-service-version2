package it.gdhi.service;

import it.gdhi.internationalization.service.RegionNameTranslator;
import it.gdhi.model.Region;
import it.gdhi.repository.IRegionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.ArrayList;
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

    public Region createRegion(String id, String name){
        Region region = Region.builder().region_id(id).regionName(name).build();
        return region;
    }

    @Test
    public void shouldFetchAllRegions(){
        //Arrange
        String id="AFRO";
        String name="African Region";

        List<Region> regions = asList(createRegion(id,name));
        //Act
        when(iRegionRepository.findAll()).thenReturn(regions);
        //Assert
        assertEquals(regions.get(0).getRegion_id(),id);
        assertEquals(regions.get(0).getRegionName(),name);
    }
    @Test
    public void shouldInvoke() {
        regionService.fetchRegions(en);
        verify(iRegionRepository).findAll();
    }

    @Test
    public void shouldFetchRegionsForAGivenLanguage(){
        //Arrange
        List<Region> regions = new ArrayList<>();
        String id="AFRO";
        String name="African Region";
        regions.add(createRegion(id,name));
        String id2="PAHO";
        String name2="Pan American Region";
        regions.add(createRegion(id2,name2));

        List<Region> translatedRegions = new ArrayList<>();
        String id3 = "AFRO";
        String name3 = "Région africaine";
        translatedRegions.add(createRegion(id3,name3));
        String id4="PAHO";
        String name4="Région panaméricaine";
        translatedRegions.add(createRegion(id4,name4));

        //Act
        when(iRegionRepository.findAll()).thenReturn(regions);
        when(regionNameTranslator.translate(regions,fr)).thenReturn(translatedRegions);

        //Assert
        assertEquals(regions.get(1).getRegion_id(),id2);
        assertEquals(translatedRegions.get(1).getRegionName(),name4);
    }

}

