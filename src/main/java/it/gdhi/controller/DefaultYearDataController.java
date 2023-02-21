package it.gdhi.controller;

import it.gdhi.service.DefaultYearDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

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
}
