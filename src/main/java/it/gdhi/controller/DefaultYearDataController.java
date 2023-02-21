package it.gdhi.controller;

import it.gdhi.service.DefaultYearDataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
public class DefaultYearDataController {

    private DefaultYearDataService defaultYearDataService;

    @Autowired
    public DefaultYearDataController(DefaultYearDataService defaultYearDataService) {
        this.defaultYearDataService = defaultYearDataService;
    }

    @GetMapping("/years")
    public List<String> getAllYears() {
        return defaultYearDataService.fetchYears();
    }

    @PostMapping("/default_year/submit")
    public void saveDefaultYear(@RequestBody String defaultYear) {
        defaultYearDataService.saveNewYear(defaultYear);
    }

}
