package it.gdhi.controller;

import it.gdhi.dto.CountriesHealthScoreDto;
import it.gdhi.dto.GlobalHealthScoreDto;
import it.gdhi.service.CountryHealthIndicatorService;
import it.gdhi.service.DefaultYearDataService;
import it.gdhi.utils.LanguageCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

import static it.gdhi.utils.LanguageCode.USER_LANGUAGE;

@RestController
@Slf4j
public class HealthIndicatorController {

    @Autowired
    private CountryHealthIndicatorService countryHealthIndicatorService;

    @Autowired
    private DefaultYearDataService defaultYearDataService;

    @GetMapping("/global_health_indicators")
    public GlobalHealthScoreDto getGlobalHealthIndicator(
            HttpServletRequest request,
            @RequestParam(value = "categoryId", required = false) Integer categoryId,
            @RequestParam(value = "phase", required = false) Integer score,
            @RequestParam(value = "regionId", required = false) String regionId,
            @RequestParam(value = "year", required = false) String year) {
        LanguageCode languageCode = getLanguageCode(request);
        if (year == null) {
            year = defaultYearDataService.fetchDefaultYear();
        }
        return countryHealthIndicatorService.getGlobalHealthIndicator(categoryId, score, regionId, languageCode, year);
    }

    @GetMapping("/countries_health_indicator_scores")
    public CountriesHealthScoreDto getCountriesHealthIndicatorScores(
            HttpServletRequest request,
            @RequestParam(value = "categoryId", required = false) Integer categoryId,
            @RequestParam(value = "phase", required = false) Integer score,
            @RequestParam(value = "regionId", required = false) String regionId,
            @RequestParam(value = "year", required = false) String year) {
        LanguageCode languageCode = getLanguageCode(request);
        if (year == null) {
            year = defaultYearDataService.fetchDefaultYear();
        }
        return countryHealthIndicatorService.fetchCountriesHealthScores(categoryId, score, regionId, languageCode, year);
    }

    private LanguageCode getLanguageCode(HttpServletRequest request) {
        return LanguageCode.getValueFor(request.getHeader(USER_LANGUAGE));
    }


}
