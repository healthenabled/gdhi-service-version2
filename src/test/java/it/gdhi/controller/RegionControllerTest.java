package it.gdhi.controller;

import java.util.ArrayList;
import java.util.List;

import it.gdhi.model.Region;
import it.gdhi.service.RegionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.server.ResponseStatusException;

import static it.gdhi.utils.LanguageCode.en;
import static it.gdhi.utils.LanguageCode.fr;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class RegionControllerTest {

    @InjectMocks
    private RegionController regionController;

    @Mock
    private RegionService regionService;


    @Test
    public void shouldListRegions()
    {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("USER_LANGUAGE","en");
        Region regionMock = mock(Region.class);

        List<Region> regionList = regionController.fetchRegions(request);

        verify(regionService).fetchRegions(en);
    }
    @Test
    public void shouldListRegionsInGivenLanguage() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("USER_LANGUAGE", "fr");

        regionController.fetchRegions(request);

        verify(regionService).fetchRegions(fr);
    }

    @Test
    public void shouldFetchCountriesHealthScoreDataWhenRegionIdAndListOfYearsIsPassed() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("USER_LANGUAGE","en");
        String regionId = "PAHO";
        List<String> years = new ArrayList<>();
        years.add("2023");
        years.add("Version1");

        regionController.fetchRegionCountriesData(request , regionId , years);
        verify(regionService).getRegionCountriesData(regionId , years , en);
    }

    @Test
    public void shouldThrowBadRequestWhenListOfYearsIsNotPassed()  throws Exception{
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("USER_LANGUAGE","en");
        String regionId = "PAHO";
        List<String> years = new ArrayList<>();

        // TODO: Add more specific assertion for 400 error code
        assertThrows(ResponseStatusException.class,
                () -> regionController.fetchRegionCountriesData(request , regionId , years));
    }

    @Test
    public void shouldGetYearsForARegion() {
        String regionId = "PAHO";

        regionController.getYearsForARegion(regionId, 3);
        verify(regionService).fetchYearsForARegion(regionId, 3);
    }

    @Test
    public void shouldGetLatestFiveYearsForARegionWhenLimitIsNull() {
        String regionId = "PAHO";

        regionController.getYearsForARegion(regionId, null);
        verify(regionService).fetchYearsForARegion(regionId, 5);
    }
}
