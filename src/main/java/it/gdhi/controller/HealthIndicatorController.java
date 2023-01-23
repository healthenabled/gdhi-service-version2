package it.gdhi.controller;

import it.gdhi.dto.CountriesHealthScoreDto;
import it.gdhi.dto.GlobalHealthScoreDto;
import it.gdhi.service.CountryHealthIndicatorService;
import it.gdhi.utils.LanguageCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

import static it.gdhi.utils.LanguageCode.USER_LANGUAGE;

@RestController
@Slf4j
public class HealthIndicatorController {

    @Autowired
    private CountryHealthIndicatorService countryHealthIndicatorService;

    @RequestMapping("/global_health_indicators")
    public GlobalHealthScoreDto getGlobalHealthIndicator(
                                            HttpServletRequest request,
                                            @RequestParam(value = "categoryId", required = false) Integer categoryId,
                                            @RequestParam(value = "phase", required = false) Integer score) {
        LanguageCode languageCode = getLanguageCode(request);
        return countryHealthIndicatorService.getGlobalHealthIndicator(categoryId, score, languageCode);
    }

    @RequestMapping("/countries_health_indicator_scores")
    public CountriesHealthScoreDto getCountriesHealthIndicatorScores(
                                            HttpServletRequest request,
                                            @RequestParam(value = "categoryId", required = false) Integer categoryId,
                                            @RequestParam(value = "phase", required = false) Integer score) {
        LanguageCode languageCode = getLanguageCode(request);
        return countryHealthIndicatorService.fetchCountriesHealthScores(categoryId, score, languageCode);
    }

    private LanguageCode getLanguageCode(HttpServletRequest request) {
        return LanguageCode.getValueFor(request.getHeader(USER_LANGUAGE));
    }


}
