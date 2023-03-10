package it.gdhi.controller;

import it.gdhi.dto.CategoryIndicatorDto;
import it.gdhi.dto.PhaseDto;
import it.gdhi.service.CategoryIndicatorService;
import it.gdhi.service.PhaseService;
import it.gdhi.utils.LanguageCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static it.gdhi.utils.LanguageCode.USER_LANGUAGE;

@RestController
public class MetaDataController {

    @Autowired
    private CategoryIndicatorService categoryIndicatorService;

    @Autowired
    private PhaseService phaseService;

    @GetMapping("/health_indicator_options")
    public List<CategoryIndicatorDto> getHealthIndicatorOptions(HttpServletRequest request) {
        LanguageCode languageCode = LanguageCode.getValueFor(request.getHeader(USER_LANGUAGE));
        return categoryIndicatorService.getHealthIndicatorOptions(languageCode);
    }

    @GetMapping("/phases")
    public List<PhaseDto> getPhases() {
        return phaseService.getPhaseOptions();
    }
}
