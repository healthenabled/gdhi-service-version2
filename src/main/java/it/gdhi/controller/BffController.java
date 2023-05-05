package it.gdhi.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import it.gdhi.dto.BenchmarkDto;
import it.gdhi.dto.GdhiQuestionnaires;
import it.gdhi.dto.GlobalHealthScoreDto;
import it.gdhi.dto.YearDto;
import it.gdhi.dto.YearOnYearDto;
import it.gdhi.model.response.CountryStatuses;
import it.gdhi.service.BffService;
import it.gdhi.service.CountryHealthDataService;
import it.gdhi.service.CountryHealthIndicatorService;
import it.gdhi.service.CountryService;
import it.gdhi.service.DefaultYearDataService;
import it.gdhi.service.ExcelUtilService;
import it.gdhi.service.RegionService;
import it.gdhi.utils.LanguageCode;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static it.gdhi.utils.ApplicationConstants.defaultLimit;
import static it.gdhi.utils.LanguageCode.USER_LANGUAGE;

@RestController
@Slf4j
public class BffController {

    @Autowired
    private BffService bffService;

    @Autowired
    private DefaultYearDataService defaultYearDataService;

    @Autowired
    private CountryService countryService;

    @Autowired
    private CountryHealthDataService countryHealthDataService;
    @Autowired
    private RegionService regionService;

    @Autowired
    ExcelUtilService excelUtilService;

    @Value("${csvFileLocation}")
    @Getter
    private String fileWithPath;

    @Autowired
    private CountryHealthIndicatorService countryHealthIndicatorService;

    @GetMapping("/bff/distinct_year")
    public YearDto getDistinctYears(@RequestParam(value = "no_of_years", required = false) Integer limit) {
        if (limit == null) {
            limit = defaultLimit;
        }
        return bffService.fetchDistinctYears(limit);
    }

    @GetMapping("/countries/{id}/year_on_year")
    public YearOnYearDto getYearOnYearData(@PathVariable("id") String countryId,
                                           @RequestParam(value = "no_of_years", required = false) Integer limit, @RequestParam(value = "regionId",
            required = false) String regionId) {
        if (limit == null) {
            limit = defaultLimit;
        }
        List<String> years = countryService.fetchPublishCountriesDistinctYears(limit);
        return bffService.fetchYearOnYearData(years, countryId, regionId);
    }

    @GetMapping("/countries/{id}/published_years")
    public List<String> getPublishedYearsForACountry(@PathVariable("id") String countryId,
                                                     @RequestParam(value = "no_of_years", required = false) Integer limit) {
        if (limit == null) {
            limit = defaultLimit;
        }
        return bffService.fetchPublishedYearsForACountry(countryId, limit);
    }

    @GetMapping("/bff/countries/{id}/benchmark/{type}")
    public Map<Integer, BenchmarkDto> getBenchmarkDetailsFor(@PathVariable("id") String countryId, @PathVariable("type") Integer benchmarkType,
                                                             @RequestParam(value = "year", required = false) String year, @RequestParam(value =
            "regionId", required = false) String regionId) {
        if (year == null) {
            year = defaultYearDataService.fetchDefaultYear();
        }
        if (regionId == null) {
            return countryHealthDataService.getBenchmarkDetailsFor(countryId, benchmarkType, year);
        }
        else {
            return regionService.getBenchmarkDetailsForRegion(countryId, year, regionId);
        }
    }

    @PostMapping("/bff/countries/submit")
    public CountryStatuses shouldSubmitCSVData(@RequestBody GdhiQuestionnaires gdhiQuestionnaires) {
        return bffService.submitCountryCSVData(gdhiQuestionnaires);
    }

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
        if (regionId == null) {
            return countryHealthIndicatorService.getGlobalHealthIndicator(categoryId, score, languageCode, year);
        }
        else {
            return regionService.fetchRegionalHealthScores(categoryId, regionId, languageCode, year);
        }
    }

    private LanguageCode getLanguageCode(HttpServletRequest request) {
        return LanguageCode.getValueFor(request.getHeader(USER_LANGUAGE));
    }

    @GetMapping("/export_csv_template")
    public void getCsvTemplate(HttpServletRequest request, HttpServletResponse response) throws IOException {
        excelUtilService.downloadFile(request,response,this.getFileWithPath());
    }
}
