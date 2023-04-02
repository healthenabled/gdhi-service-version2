package it.gdhi.controller;

import it.gdhi.model.Region;
import it.gdhi.service.RegionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
public class RegionControllerTest {

    @InjectMocks
    private RegionController regionController;

    @Mock
    private RegionService regionService;

//    @Test
//    public void shouldListAllRegions()
//    {
//        //Arrange
//        MockHttpServletRequest request = new MockHttpServletRequest();
//        request.addHeader("USER_LANGUAGE","en");
//
//        //Act
//        List<Region> logic = regionController.getRegions(request);
//
//        //Assert
//        assertTrue(logic);
//    }

//    @Test
//    public void shouldListRegionsInGivenLanguage()
//    {
//        //Arrange
//        MockHttpServletRequest request = new MockHttpServletRequest();
//        request.addHeader("USER_LANGUAGE","fr");
//
//        //Act
//        boolean logic = regionController.getRegions(request);
//
//        //Assert
//        assertTrue(logic);
//    }

    @Test
    public void shouldReturnEmptyListWhenThereIsNoData()
    {
        //Arrange
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("USER_LANGUAGE","en");

        //Act
        List<Region> regionList = regionController.getRegions(request);

        //Assert
        assertEquals(regionList.size(),0);
    }
}
