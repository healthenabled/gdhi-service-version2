package it.gdhi.controller;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.annotation.JsonView;
import it.gdhi.dto.CountryHealthScoreDto;
import it.gdhi.dto.CountrySummaryDto;
import it.gdhi.dto.CountrySummaryStatusYearDto;
import it.gdhi.dto.CountryUrlGenerationStatusDto;
import it.gdhi.dto.GdhiQuestionnaire;
import it.gdhi.model.Country;
import it.gdhi.model.DevelopmentIndicator;
import it.gdhi.service.CountryHealthDataService;
import it.gdhi.service.CountryHealthIndicatorService;
import it.gdhi.service.CountryService;
import it.gdhi.service.DefaultYearDataService;
import it.gdhi.service.DevelopmentIndicatorService;
import it.gdhi.service.RegionService;
import it.gdhi.utils.LanguageCode;
import it.gdhi.view.DevelopmentIndicatorView;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import static it.gdhi.utils.FormStatus.DRAFT;
import static it.gdhi.utils.LanguageCode.USER_LANGUAGE;
import static it.gdhi.utils.Util.getCurrentYear;

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

    @Autowired
    private RegionService regionService;

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
        }
        else {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PostMapping("/countries/saveCorrection")
    public void saveCorrectionsFor(@RequestBody GdhiQuestionnaire gdhiQuestionnaire) {
        countryHealthDataService.saveCorrection(gdhiQuestionnaire);
    }

    @PostMapping("/countries/publish/{year}")
    @ResponseBody
    public ResponseEntity publishHealthIndicatorsFor(@RequestBody GdhiQuestionnaire gdhiQuestionnaire, @PathVariable("year") String year) {
        boolean isValid;
        isValid = countryHealthDataService.validateRequiredFields(gdhiQuestionnaire);
        if (isValid && year.equals(getCurrentYear())) {
            countryHealthDataService.publish(gdhiQuestionnaire, year);
            return ResponseEntity.status(HttpStatus.CREATED).body(null);
        }
        else {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/countries/{uuid}")
    @ResponseBody
    public GdhiQuestionnaire getQuestionnaireForCountry(HttpServletRequest request,
                                                        @PathVariable("uuid") UUID countryUIID,
                                                        @RequestParam(value = "year", required = false) String year) {
        LanguageCode languageCode = LanguageCode.getValueFor(request.getHeader(USER_LANGUAGE));
        if (!countryService.checkCountryHasEntryForCurrentYear(countryUIID)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        else {
            if (year == null) {
                year = countryService.fetchTheYearToPrefillData(countryUIID);
            }
        }
        return countryService.getDetails(countryUIID, languageCode, false, year);
    }

    @GetMapping("/countries/viewPublish/{uuid}/{year}")
    public GdhiQuestionnaire getQuestionnaireForPublishedCountry(HttpServletRequest request,
                                                                 @PathVariable("uuid") UUID countryUIID,
                                                                 @PathVariable("year") String year) {
        LanguageCode languageCode = LanguageCode.getValueFor(request.getHeader(USER_LANGUAGE));
        return countryService.getDetails(countryUIID, languageCode, true, year);
    }

    @GetMapping("/export_global_data")
    public void exportGlobalData(HttpServletRequest request,
                                 HttpServletResponse response,
                                 @RequestParam(value = "year", required = false) String year) throws IOException {
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

    @PostMapping("/countries/{uuid}/generate_url")
    public CountryUrlGenerationStatusDto saveNewCountrySummary(@PathVariable("uuid") UUID countryUIID)
            throws Exception {
        return countryHealthDataService.saveNewCountrySummary(countryUIID);
    }

    @DeleteMapping("/countries/{uuid}/delete/{year}")
    public void deleteCountryData(@PathVariable("uuid") UUID countryUIID, @PathVariable("year") String year) throws Exception {
        countryHealthDataService.deleteCountryData(countryUIID, year);
    }

    @GetMapping("/countries/country_status_summaries")
    public CountrySummaryStatusYearDto getAllCountryStatusSummaries() {
        return countryHealthDataService.getAllCountryStatusSummaries();
    }

    @GetMapping("/admin/countries/calculate_phase")
    public void calculateCountryPhase(@RequestParam(value = "year", required = false) String year) {
        if (year == null) {
            year = getCurrentYear();
        }
        countryHealthDataService.calculatePhaseForAllCountries(year);
        regionService.calculatePhaseForAllRegions(year);
    }

    @ResponseStatus(value = HttpStatus.NOT_ACCEPTABLE, reason = "User language requested not found")
    @ExceptionHandler(IllegalArgumentException.class)
    public void handleIOException() {
        log.error("User language requested not found");
    }

}
