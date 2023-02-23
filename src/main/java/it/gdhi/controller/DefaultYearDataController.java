package it.gdhi.controller;

import it.gdhi.service.CountryService;
import it.gdhi.service.DefaultYearDataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
public class DefaultYearDataController {

    private DefaultYearDataService defaultYearDataService;

    private CountryService countryService;

    @Autowired
    public DefaultYearDataController(DefaultYearDataService defaultYearDataService, CountryService countryService) {
        this.defaultYearDataService = defaultYearDataService;
        this.countryService = countryService;
    }

    @GetMapping("/years")
    public List<String> getAllYears() {
        return defaultYearDataService.fetchYears();
    }

    @PostMapping("/default_year/submit")
    @ResponseBody
    public ResponseEntity saveDefaultYear(@RequestBody String defaultYear) {
        Boolean isValid = countryService.validateDefaultYear(defaultYear);
        if (isValid) {
            defaultYearDataService.saveDefaultYear(defaultYear);
            return ResponseEntity.status(HttpStatus.CREATED).body(null);
        } else {
            return ResponseEntity.badRequest().body(null);
        }
    }
}
