package it.gdhi.service;

import it.gdhi.internationalization.service.RegionNameTranslator;
import it.gdhi.model.Region;
import it.gdhi.repository.ICountryRepository;
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
import static org.codehaus.groovy.runtime.InvokerHelper.asList;
import static org.hamcrest.MatcherAssert.assertThat;
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

    @Test
    public void shouldFetchAllRegions(){
        //Arrange
        String id="AFRO";
        String name="African Region";
        Region region = Region.builder().region_id(id).regionName(name).build();
        List<Region> regions = asList(region);
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

}

