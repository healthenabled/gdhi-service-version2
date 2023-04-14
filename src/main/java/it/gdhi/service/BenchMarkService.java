package it.gdhi.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import it.gdhi.dto.BenchmarkDto;
import it.gdhi.model.CountryHealthIndicator;
import it.gdhi.model.CountryPhase;
import it.gdhi.model.Score;
import it.gdhi.repository.ICountryHealthIndicatorRepository;
import it.gdhi.repository.ICountryPhaseRepository;
import it.gdhi.repository.IRegionCountryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static it.gdhi.utils.FormStatus.PUBLISHED;
import static java.util.stream.Collectors.averagingInt;
import static java.util.stream.Collectors.groupingBy;

@Service
public class BenchMarkService {

    @Autowired
    private ICountryHealthIndicatorRepository iCountryHealthIndicatorRepository;

    @Autowired
    private ICountryPhaseRepository iCountryPhaseRepository;

    @Autowired
    private RegionService regionService;

    private Map<Integer, Double> calculateBenchmarkScoresForIndicators(Integer benchmarkType, String year, String region) {
        List<String> countries = regionService.fetchCountriesForARegion(region);
        List<CountryHealthIndicator> countryHealthIndicators = region == null ? iCountryHealthIndicatorRepository.findByCountryHealthIndicatorIdStatusAndCountryHealthIndicatorIdYear(PUBLISHED.name(), year)
                : iCountryHealthIndicatorRepository.findByCountryHealthIndicatorIdCountryIdInAndCountryHealthIndicatorIdYearAndCountryHealthIndicatorIdStatus(countries, year, PUBLISHED.name());
        List<CountryHealthIndicator> publishedCountryHealthIndicators = (benchmarkType == -1) ? countryHealthIndicators : countryHealthIndicators.stream().filter(countryHealthIndicator ->
                validateCountryHealthIndicatorByPhaseAndYear(countryHealthIndicator, benchmarkType, year)).collect(Collectors.toList());

        return publishedCountryHealthIndicators.stream()
                .map(row -> {
                    row.convertNullScoreToNotAvailable();
                    return row;
                })
                .filter(indicator -> indicator.getIndicator().getParentId() == null && indicator.getScore() != -1)
                .collect(groupingBy(CountryHealthIndicator::getIndicatorId,
                        averagingInt(CountryHealthIndicator::getScore)));
    }

    private boolean validateCountryHealthIndicatorByPhaseAndYear(CountryHealthIndicator countryHealthIndicator, Integer phase, String year) {
        CountryPhase countryPhase = iCountryPhaseRepository.findByCountryPhaseIdCountryIdAndCountryPhaseIdYear(countryHealthIndicator.getCountryId(), year);
        return countryPhase.getCountryOverallPhase().equals(phase);
    }

    Map<Integer, BenchmarkDto> getBenchmarkFor(String countryId, Integer benchmarkType, String year, String region) {
        Map<Integer, Double> indicatorBenchmarkScores = calculateBenchmarkScoresForIndicators(benchmarkType, year, region);

        List<CountryHealthIndicator> countryHealthIndicator = iCountryHealthIndicatorRepository
                .findByCountryHealthIndicatorIdCountryIdAndCountryHealthIndicatorIdYearAndCountryHealthIndicatorIdStatus(countryId, year, PUBLISHED.name());

        return countryHealthIndicator.stream().
                filter(indicator -> (indicator.isScoreValid()
                        && indicator.getIndicator().getParentId() == null))
                .collect(Collectors.toMap(CountryHealthIndicator::getIndicatorId,
                        indicator -> constructBenchMarkDto(
                                indicatorBenchmarkScores.get(indicator.getIndicatorId()))));
    }

    private BenchmarkDto constructBenchMarkDto(Double indicatorBenchmarkScore) {
        Score benchmarkScore = indicatorBenchmarkScore == null ? new Score(-1.0) : new Score(indicatorBenchmarkScore);
        Integer benchmarkPhase = benchmarkScore.convertToPhase();
        return new BenchmarkDto(benchmarkPhase);
    }
}
