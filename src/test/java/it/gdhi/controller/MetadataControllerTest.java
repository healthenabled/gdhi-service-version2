package it.gdhi.controller;

import it.gdhi.dto.CategoryIndicatorDto;
import it.gdhi.dto.PhaseDto;
import it.gdhi.service.CategoryIndicatorService;
import it.gdhi.service.PhaseService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;

import java.util.ArrayList;
import java.util.List;

import static it.gdhi.utils.LanguageCode.en;
import static it.gdhi.utils.LanguageCode.pt;
import static java.util.Collections.singletonList;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MetadataControllerTest {

    @InjectMocks
    private MetaDataController metaDataController;

    @Mock
    private CategoryIndicatorService categoryIndicatorService;

    @Mock
    private PhaseService phaseService;

    @Test
    public void shouldInvokeCategoryRepoToFetchHealthIndicatorOptionsInfo() {
        CategoryIndicatorDto categoryIndicatorDto = new CategoryIndicatorDto();
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("USER_LANGUAGE", "en");

        when(categoryIndicatorService.getHealthIndicatorOptions(en)).thenReturn(singletonList(categoryIndicatorDto));

        List<CategoryIndicatorDto> categoryIndicators = metaDataController.getHealthIndicatorOptions(request);
        assertThat(categoryIndicators.size(), is(1));
        assertThat(categoryIndicators.get(0), is(categoryIndicatorDto));
    }

    @Test
    public void shouldGetLanguageCodeFromHeaders() {
        CategoryIndicatorDto categoryIndicatorDto = new CategoryIndicatorDto();
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("USER_LANGUAGE", "pt");

        when(categoryIndicatorService.getHealthIndicatorOptions(pt)).thenReturn(singletonList(categoryIndicatorDto));  //Test change(en - pt)

        metaDataController.getHealthIndicatorOptions(request);

        verify(categoryIndicatorService).getHealthIndicatorOptions(pt);
    }

    @Test
    public void shouldInvokePhaseService() {
        when(phaseService.getPhaseOptions()).thenReturn(new ArrayList<PhaseDto>());
        List<PhaseDto> phaseDtos = metaDataController.getPhases();
        verify(phaseService).getPhaseOptions();
        assertEquals(0, phaseDtos.size());
    }
}