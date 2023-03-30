package it.gdhi.service;

import it.gdhi.dto.YearDto;
import it.gdhi.dto.YearHealthScoreDto;
import it.gdhi.dto.YearOnYearDto;
import it.gdhi.dto.YearScoreDto;
import it.gdhi.repository.ICountryPhaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static it.gdhi.utils.LanguageCode.en;
import static it.gdhi.utils.Util.getCurrentYear;

@Service
public class BffService {

    private CountryService countryService;
    private DefaultYearDataService defaultYearDataService;
    private final ICountryPhaseRepository iCountryPhaseRepository;
    private CountryHealthIndicatorService countryHealthIndicatorService;

    @Autowired
    public BffService(CountryService countryService, DefaultYearDataService defaultYearDataService,
                      ICountryPhaseRepository iCountryPhaseRepository, CountryHealthIndicatorService countryHealthIndicatorService) {
        this.countryService = countryService;
        this.defaultYearDataService = defaultYearDataService;
        this.iCountryPhaseRepository = iCountryPhaseRepository;
        this.countryHealthIndicatorService = countryHealthIndicatorService;
    }

    public YearDto fetchDistinctYears() {
        List<String> years = countryService.fetchPublishCountriesDistinctYears();
        String defaultYear = defaultYearDataService.fetchDefaultYear();
        YearDto yearDto = YearDto.builder().years(years).defaultYear(defaultYear).build();
        return yearDto;
    }

    public List<String> fetchPublishedYearsForACountry(String countryId, Integer limit) {
        return iCountryPhaseRepository.findByCountryPhaseIdOrderByYearDesc(countryId, limit);
    }

    public YearOnYearDto fetchYearOnYearData(List<String> years, String countryId) {
        List<YearScoreDto> yearScoreDtos = years.stream().map(year -> new YearScoreDto(year, new YearHealthScoreDto(countryHealthIndicatorService.fetchCountryHealthScore(countryId, en, year), countryHealthIndicatorService.getGlobalHealthIndicator(null, null, en, year)))).collect(Collectors.toList());
        return new YearOnYearDto(getCurrentYear(), defaultYearDataService.fetchDefaultYear(), yearScoreDtos);
    }
}

