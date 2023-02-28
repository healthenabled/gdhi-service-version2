package it.gdhi.controller;

import com.fasterxml.jackson.annotation.JsonView;
import it.gdhi.dto.*;
import it.gdhi.model.Country;
import it.gdhi.model.DevelopmentIndicator;
import it.gdhi.service.*;
import it.gdhi.utils.LanguageCode;
import it.gdhi.view.DevelopmentIndicatorView;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static it.gdhi.utils.FormStatus.DRAFT;
import static it.gdhi.utils.LanguageCode.USER_LANGUAGE;

@RestController
@Slf4j
public class CountryController {

    @Autowired
    private CountryService countryService;

    @Autowired
    private CountryHealthDataService countryHealthDataService;

    @Autowired
    private CountryHealthIndicatorService countryHealthIndicatorService;

    @Autowired
    private DevelopmentIndicatorService developmentIndicatorService;

    @Autowired
    private DefaultYearDataService defaultYearDataService;

    @GetMapping("/countries")
    public List<Country> getCountries(HttpServletRequest request) {
        LanguageCode languageCode = LanguageCode.getValueFor(request.getHeader(USER_LANGUAGE));
        return countryService.fetchCountries(languageCode);
    }

    @GetMapping("/countries/{id}/development_indicators")
    @JsonView(DevelopmentIndicatorView.class)
    public DevelopmentIndicator getDevelopmentIndicatorForGivenCountryCode(@PathVariable("id") String countryId) {
        return developmentIndicatorService.fetchCountryDevelopmentScores(countryId);
    }

    @GetMapping("/countries/{id}/health_indicators")
    public CountryHealthScoreDto getHealthIndicatorForGivenCountryCode(HttpServletRequest request,
                                                                       @PathVariable("id") String countryId,
                                                                       @RequestParam(value = "year", required = false) String year) {
        LanguageCode languageCode = LanguageCode.getValueFor(request.getHeader(USER_LANGUAGE));
        if (year == null) {
            year = defaultYearDataService.fetchDefaultYear();
        }
        return countryHealthIndicatorService.fetchCountryHealthScore(countryId, languageCode, year);
    }

    @GetMapping("/countries/{id}/country_summary")
    public CountrySummaryDto fetchCountrySummary(@PathVariable("id") String countryId,
                                                 @RequestParam(value = "year", required = false) String year) {
        if (year == null) {
            year = defaultYearDataService.fetchDefaultYear();
        }
        return countryService.fetchCountrySummary(countryId, year);
    }

    @PostMapping("/countries/save")
    public void saveHealthIndicatorsFor(@RequestBody GdhiQuestionnaire gdhiQuestionnaire) {
        countryHealthDataService.save(gdhiQuestionnaire, DRAFT.name());
    }

    @PostMapping("/countries/submit")
    @ResponseBody
    public ResponseEntity submitHealthIndicatorsFor(@RequestBody GdhiQuestionnaire gdhiQuestionnaire) {
        boolean isValid;
        isValid = countryHealthDataService.validateRequiredFields(gdhiQuestionnaire);
        if (isValid) {
            countryHealthDataService.submit(gdhiQuestionnaire);
            return ResponseEntity.status(HttpStatus.CREATED).body(null);
        } else {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PostMapping("/countries/saveCorrection")
    public void saveCorrectionsFor(@RequestBody GdhiQuestionnaire gdhiQuestionnaire) {
        countryHealthDataService.saveCorrection(gdhiQuestionnaire);
    }

    @PostMapping("/countries/publish")
    @ResponseBody
    public ResponseEntity publishHealthIndicatorsFor(@RequestBody GdhiQuestionnaire gdhiQuestionnaire) {
        boolean isValid;
        isValid = countryHealthDataService.validateRequiredFields(gdhiQuestionnaire);
        if (isValid) {
            countryHealthDataService.publish(gdhiQuestionnaire);
            return ResponseEntity.status(HttpStatus.CREATED).body(null);
        } else {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/countries/{uuid}")
    public GdhiQuestionnaire getQuestionnaireForCountry(HttpServletRequest request,
                                                        @PathVariable("uuid") UUID countryUIID,
                                                        @RequestParam(value = "year", required = false) String year) {
        LanguageCode languageCode = LanguageCode.getValueFor(request.getHeader(USER_LANGUAGE));
        if (year == null) {
            year = defaultYearDataService.fetchDefaultYear();
        }
        return countryService.getDetails(countryUIID, languageCode, false, year);
    }

    @GetMapping("/countries/viewPublish/{uuid}")
    public GdhiQuestionnaire getQuestionnaireForPublishedCountry(HttpServletRequest request,
                                                                 @PathVariable("uuid") UUID countryUIID,
                                                                 @RequestParam(value = "year", required = false) String year) {
        LanguageCode languageCode = LanguageCode.getValueFor(request.getHeader(USER_LANGUAGE));
        if (year == null) {
            year = defaultYearDataService.fetchDefaultYear();
        }
        return countryService.getDetails(countryUIID, languageCode, true, year);
    }

    @GetMapping("/export_global_data")
    public void exportGlobalData(HttpServletRequest request,
                                 HttpServletResponse response,
                                 @RequestParam(value = "year", required = false) String year) throws IOException {
        log.info("Entered export global data end point");
        if (year == null) {
            year = defaultYearDataService.fetchDefaultYear();
        }
        countryHealthIndicatorService.createGlobalHealthIndicatorInExcel(request, response, year);
    }

    @GetMapping("/export_country_data/{id}")
    public void exportCountryDetails(HttpServletRequest request,
                                     HttpServletResponse response,
                                     @PathVariable("id") String countryId,
                                     @RequestParam(value = "year", required = false) String year) throws IOException {
        if (year == null) {
            year = defaultYearDataService.fetchDefaultYear();
        }
        countryHealthIndicatorService.createHealthIndicatorInExcelFor(countryId, request, response, year);
    }

    //TODO: add integration test for this endpoint
    @PostMapping("/countries/{uuid}/generate_url")
    public CountryUrlGenerationStatusDto saveNewCountrySummary(@PathVariable("uuid") UUID countryUIID)
            throws Exception {
        return countryHealthDataService.saveNewCountrySummary(countryUIID);
    }

    @DeleteMapping("/countries/{uuid}/delete")
    public void deleteCountryData(@PathVariable("uuid") UUID countryUIID) throws Exception {
        countryHealthDataService.deleteCountryData(countryUIID);
    }

    @GetMapping("/countries/country_status_summaries")
    public Map<String, List<CountrySummaryStatusDto>> getAllCountryStatusSummaries() {
        return countryHealthDataService.getAllCountryStatusSummaries();
    }

    @GetMapping("/countries/{id}/benchmark/{type}")
    public Map<Integer, BenchmarkDto> getBenchmarkDetailsFor(@PathVariable("id") String countryId,
                                                             @PathVariable("type") Integer benchmarkType,
                                                             @RequestParam(value = "year", required = false) String year) {
        if (year == null) {
            year = defaultYearDataService.fetchDefaultYear();
        }
        return countryHealthDataService.getBenchmarkDetailsFor(countryId, benchmarkType, year);
    }

    @GetMapping("/admin/countries/calculate_phase")
    public void calculateCountryPhase(@RequestParam(value = "year", required = false) String year) {
        if (year == null) {
            year = defaultYearDataService.fetchDefaultYear();
        }
        countryHealthDataService.calculatePhaseForAllCountries(year);
    }

    @ResponseStatus(value = HttpStatus.NOT_ACCEPTABLE, reason = "User language requested not found")
    @ExceptionHandler(IllegalArgumentException.class)
    public void handleIOException() {
        log.error("User language requested not found");
    }

}
