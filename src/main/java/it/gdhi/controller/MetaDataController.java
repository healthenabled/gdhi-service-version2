package it.gdhi.controller;

import it.gdhi.dto.CategoryIndicatorDto;
import it.gdhi.dto.PhaseDto;
import it.gdhi.service.CategoryIndicatorService;
import it.gdhi.service.PhaseService;
import it.gdhi.utils.LanguageCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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

    @RequestMapping("/health_indicator_options")
    public List<CategoryIndicatorDto> getHealthIndicatorOptions(HttpServletRequest request) {
        LanguageCode languageCode = LanguageCode.getValueFor(request.getHeader(USER_LANGUAGE));
        return categoryIndicatorService.getHealthIndicatorOptions(languageCode);
    }

    @RequestMapping(value = "/phases", method = RequestMethod.GET)
    public List<PhaseDto> getPhases() {
        return phaseService.getPhaseOptions();
    }
}
