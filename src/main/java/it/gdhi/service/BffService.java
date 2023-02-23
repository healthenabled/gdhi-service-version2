package it.gdhi.service;

import it.gdhi.dto.YearDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BffService {

    private CountryService countryService;
    private DefaultYearDataService defaultYearDataService;

    @Autowired
    public BffService(CountryService countryService, DefaultYearDataService defaultYearDataService) {
        this.countryService = countryService;
        this.defaultYearDataService = defaultYearDataService;
    }

    public YearDto fetchDistinctYears() {
        List<String> years = countryService.fetchPublishCountriesDistinctYears();
        String defaultYear = defaultYearDataService.fetchDefaultYear();
        YearDto yearDto = YearDto.builder().years(years).defaultYear(defaultYear).build();
        return yearDto;
    }
}

