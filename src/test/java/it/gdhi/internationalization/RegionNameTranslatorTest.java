package it.gdhi.internationalization;

import com.google.common.collect.ImmutableList;
import it.gdhi.internationalization.model.RegionTranslation;
import it.gdhi.internationalization.model.RegionTranslationId;
import it.gdhi.internationalization.repository.IRegionTranslationRepository;
import it.gdhi.internationalization.service.RegionNameTranslator;
import it.gdhi.model.Region;
import it.gdhi.utils.LanguageCode;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class RegionNameTranslatorTest {

    @InjectMocks
    private RegionNameTranslator translator;
    @Mock
    private IRegionTranslationRepository translationRepository;

    public Region createRegion(String id, String name) {
        Region region = Region.builder().region_id(id).regionName(name).build();
        return region;
    }

    @Test
    public void shouldReturnRegionNamesInEnglishGivenUserLanguageIsNull() {
        List<Region> expectedRegions = new ArrayList<>();
        expectedRegions.add(createRegion("PAHO", "Pan American Region"));
        List<Region> actualRegions = new ArrayList<>();

        actualRegions = translator.translate(expectedRegions, null);

        assertEquals(expectedRegions, actualRegions);
    }

    @Test
    public void shouldNotInvokeTranslationRepositoryGivenUserLanguageIsNull() {
        Region paho = createRegion("PAHO", "Pan American Region");
        Region afro = createRegion("AFRO", "African Region");
        List<Region> expectedRegions = ImmutableList.of(paho, afro);

        translator.translate(expectedRegions, null);

        verify(translationRepository, never()).findByIdRegionIdAndIdLanguageId(anyString(), anyString());
    }


    @Test
    public void shouldReturnRegionNamesInFrenchGivenUserLanguageIdIsFr()
    {
        Region paho = createRegion("PAHO", "Région panaméricaine");
        Region afro = createRegion("AFRO", "Région africaine");
        List<Region> expectedRegions = ImmutableList.of(paho, afro);

        RegionTranslationId regionTranslationIdForPaho = new RegionTranslationId("PAHO","fr");
        RegionTranslation regionTranslationForPaho = new RegionTranslation(regionTranslationIdForPaho,"Région panaméricaine");

        RegionTranslationId regionTranslationIdForAfro = new RegionTranslationId("PAHO","fr");
        RegionTranslation regionTranslationForAfro = new RegionTranslation(regionTranslationIdForAfro,"Région africaine");

        when(translationRepository.findByIdRegionIdAndIdLanguageId("fr", paho.getRegion_id())).thenReturn(regionTranslationForPaho);
        when(translationRepository.findByIdRegionIdAndIdLanguageId("fr",afro.getRegion_id())).thenReturn(regionTranslationForAfro);
        List<Region> actualRegions = translator.translate(expectedRegions, LanguageCode.fr);

        assertEquals(expectedRegions, actualRegions);
    }
}
