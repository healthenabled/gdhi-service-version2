package it.gdhi.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.gdhi.dto.*;

import it.gdhi.model.Country;
import it.gdhi.model.response.CountryStatus;
import it.gdhi.repository.ICountryRepository;
import it.gdhi.service.BffService;
import it.gdhi.service.CountryHealthDataService;
import it.gdhi.service.CountryService;
import it.gdhi.service.DefaultYearDataService;
import it.gdhi.utils.FormStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static it.gdhi.utils.ApplicationConstants.defaultLimit;

@RestController
@Slf4j
public class BffController {

    @Autowired
    private BffService bffService;

    @Autowired
    private DefaultYearDataService defaultYearDataService;

    @Autowired
    private CountryHealthDataService countryHealthDataService;

    @GetMapping("/bff/distinct_year")
    public YearDto getDistinctYears() {
        return bffService.fetchDistinctYears();
    }

    @GetMapping("/countries/{id}/year_on_year")
    public YearOnYearDto getYearOnYearData(@PathVariable("id") String countryId, @RequestParam(value = "no_of_years", required = false) Integer limit, @RequestParam(value = "regionId", required = false) String regionId) {
        if (limit == null) {
            limit = defaultLimit;
        }
        List<String> years = bffService.fetchPublishedYearsForACountry(countryId, limit);
        return bffService.fetchYearOnYearData(years, countryId, regionId);
    }

    @GetMapping("/countries/{id}/published_years")
    public List<String> getPublishedYearsForACountry(@PathVariable("id") String countryId, @RequestParam(value = "no_of_years", required = false) Integer limit) {
        if (limit == null) {
            limit = defaultLimit;
        }
        return bffService.fetchPublishedYearsForACountry(countryId, limit);
    }

    @GetMapping("/bff/countries/{id}/benchmark/{type}")
    public Map<Integer, BenchmarkDto> getBenchmarkDetailsFor(@PathVariable("id") String countryId, @PathVariable("type") Integer benchmarkType, @RequestParam(value = "year", required = false) String year, @RequestParam(value = "regionId", required = false) String regionId) {
        if (year == null) {
            year = defaultYearDataService.fetchDefaultYear();
        }
        return countryHealthDataService.getBenchmarkDetailsFor(countryId, benchmarkType, year, regionId);
    }

    @PostMapping("/bff/countries/submit")
    public List<CountryStatus> shouldSubmitCSVData(@RequestBody GdhiQuestionnaires gdhiQuestionnaires) {
        return bffService.submitCountryCSVData(gdhiQuestionnaires);
    }

}
