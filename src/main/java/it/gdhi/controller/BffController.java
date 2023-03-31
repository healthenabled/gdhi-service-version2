package it.gdhi.controller;

import java.util.List;

import it.gdhi.dto.YearDto;

import it.gdhi.dto.YearOnYearDto;
import it.gdhi.service.BffService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static it.gdhi.utils.ApplicationConstants.defaultLimit;

@RestController
@Slf4j
public class BffController {

    @Autowired
    private BffService bffService;

    @GetMapping("/bff/distinct_year")
    public YearDto getDistinctYears() {
        return bffService.fetchDistinctYears();
    }

    @GetMapping("/countries/{id}/year_on_year")
    public YearOnYearDto getYearOnYearData(@PathVariable("id") String countryId,
                                           @RequestParam(value = "no_of_years", required = false) Integer limit) {
        if (limit == null) {
            limit = defaultLimit;
        }
        List<String> years = bffService.fetchPublishedYearsForACountry(countryId, limit);
        return bffService.fetchYearOnYearData(years, countryId);
    }
}
