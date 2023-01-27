package it.gdhi.controller;

import com.fasterxml.jackson.annotation.JsonView;
import it.gdhi.dto.*;
import it.gdhi.model.Country;
import it.gdhi.model.DevelopmentIndicator;
import it.gdhi.service.CountryHealthDataService;
import it.gdhi.service.CountryHealthIndicatorService;
import it.gdhi.service.CountryService;
import it.gdhi.service.DevelopmentIndicatorService;
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

    @GetMapping("/countries")
    public List<Country> getCountries(HttpServletRequest request) {
        LanguageCode languageCode = LanguageCode.getValueFor(request.getHeader(USER_LANGUAGE));
        return countryService.fetchCountries(languageCode);
    }

    @GetMapping("/helloWorld")
    public ResponseEntity<String> helloWorld(HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body("HELLO WORLD");
    }

    @GetMapping("/countries/{id}/development_indicators")
    @JsonView(DevelopmentIndicatorView.class)
    public DevelopmentIndicator getDevelopmentIndicatorForGivenCountryCode(@PathVariable("id") String countryId) {
        return developmentIndicatorService.fetchCountryDevelopmentScores(countryId);
    }

    @GetMapping("/countries/{id}/health_indicators")
    public CountryHealthScoreDto getHealthIndicatorForGivenCountryCode(HttpServletRequest request,
                                                                       @PathVariable("id") String countryId) {
        LanguageCode languageCode = LanguageCode.getValueFor(request.getHeader(USER_LANGUAGE));
        return countryHealthIndicatorService.fetchCountryHealthScore(countryId, languageCode);
    }

    @GetMapping("/countries/{id}/country_summary")
    public CountrySummaryDto fetchCountrySummary(@PathVariable("id") String countryId) {
        return countryService.fetchCountrySummary(countryId);
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
                                                        @PathVariable("uuid") UUID countryUIID) {
        LanguageCode languageCode = LanguageCode.getValueFor(request.getHeader(USER_LANGUAGE));
        return countryService.getDetails(countryUIID, languageCode, false);
    }

    @GetMapping("/countries/viewPublish/{uuid}")
    public GdhiQuestionnaire getQuestionnaireForPublishedCountry(HttpServletRequest request,
                                                                 @PathVariable("uuid") UUID countryUIID) {
        LanguageCode languageCode = LanguageCode.getValueFor(request.getHeader(USER_LANGUAGE));
        return countryService.getDetails(countryUIID, languageCode, true);
    }

    @GetMapping("/export_global_data")
    public void exportGlobalData(HttpServletRequest request,
                                 HttpServletResponse response) throws IOException {
        log.info("Entered export global data end point");
        countryHealthIndicatorService.createGlobalHealthIndicatorInExcel(request, response);
    }

    @GetMapping("/export_country_data/{id}")
    public void exportCountryDetails(HttpServletRequest request,
                                     HttpServletResponse response,
                                     @PathVariable("id") String countryId) throws IOException {
        countryHealthIndicatorService.createHealthIndicatorInExcelFor(countryId, request, response);
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
                                                             @PathVariable("type") Integer benchmarkType) {
        return countryHealthDataService.getBenchmarkDetailsFor(countryId, benchmarkType);
    }

    @GetMapping("/admin/countries/calculate_phase")
    public void calculateCountryPhase() {
        countryHealthDataService.calculatePhaseForAllCountries();
    }

    @ResponseStatus(value = HttpStatus.NOT_ACCEPTABLE, reason = "User language requested not found")
    @ExceptionHandler(IllegalArgumentException.class)
    public void handleIOException() {
        log.error("User language requested not found");
    }
}
