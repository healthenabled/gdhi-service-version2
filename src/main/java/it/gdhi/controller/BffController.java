package it.gdhi.controller;

import it.gdhi.dto.YearDto;
import it.gdhi.service.BffService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class BffController {

    @Autowired
    private BffService bffService;

    @GetMapping("/bff/distinct_year")
    public YearDto getDistinctYears() {
        return bffService.fetchDistinctYears();
    }

}
