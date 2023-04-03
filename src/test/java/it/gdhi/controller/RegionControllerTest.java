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
import static it.gdhi.utils.LanguageCode.en;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RegionControllerTest {

    @InjectMocks
    private RegionController regionController;

    @Mock
    private RegionService regionService;

    @Test
    public void shouldReturnEmptyListWhenThereIsNoData()
    {
        //Arrange
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("USER_LANGUAGE","en");
        Region regionMock = mock(Region.class);

        //Act
        List<Region> regionList = regionController.fetchRegions(request);

        //Assert
        verify(regionService).fetchRegions(en);
    }
}
