package it.gdhi.service;

import java.util.List;

import it.gdhi.dto.YearDto;
import it.gdhi.dto.YearHealthScoreDto;
import it.gdhi.dto.YearOnYearDto;
import it.gdhi.dto.YearScoreDto;
import it.gdhi.repository.ICountryPhaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        List<YearScoreDto> yearScoreDtos = years.stream().map(year -> getYearScoreDto(countryId, year)).toList();
        return YearOnYearDto.builder().currentYear(getCurrentYear()).yearOnYearData(yearScoreDtos).defaultYear(defaultYearDataService.fetchDefaultYear()).build();
    }

    private YearScoreDto getYearScoreDto(String countryId, String year) {
        return YearScoreDto.builder().year(year).data(getYearHealthScoreDto(countryId, year)).build();
    }

    private YearHealthScoreDto getYearHealthScoreDto(String countryId, String year) {
        return YearHealthScoreDto.builder().country(countryHealthIndicatorService.fetchCountryHealthScore(countryId, en, year))
                .average(countryHealthIndicatorService.getGlobalHealthIndicator(null, null, en, year)).build();
    }
}

