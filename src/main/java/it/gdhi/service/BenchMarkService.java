package it.gdhi.service;

import it.gdhi.dto.BenchmarkDto;
import it.gdhi.model.CountryHealthIndicator;
import it.gdhi.model.CountryPhase;
import it.gdhi.model.Score;
import it.gdhi.repository.ICountryHealthIndicatorRepository;
import it.gdhi.repository.ICountryPhaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static it.gdhi.utils.FormStatus.PUBLISHED;
import static java.util.stream.Collectors.averagingInt;
import static java.util.stream.Collectors.groupingBy;

@Service
public class BenchMarkService {

    static final String BENCHMARK_AT_PAR_VALUE = "At";
    static final String BENCHMARK_ABOVE_PAR_VALUE = "Above";
    static final String BENCHMARK_BELOW_PAR_VALUE = "Below";

    @Autowired
    private ICountryHealthIndicatorRepository iCountryHealthIndicatorRepository;

    @Autowired
    private ICountryPhaseRepository iCountryPhaseRepository;


    private Map<Integer, Double> calculateBenchmarkScoresForIndicators(Integer benchmarkType, String year) {
        List<CountryHealthIndicator> countryHealthIndicators = iCountryHealthIndicatorRepository.findByCountryHealthIndicatorIdStatusAndCountryHealthIndicatorIdYear(PUBLISHED.name(), year);
        List<CountryHealthIndicator> publishedCountryHealthIndicators = new ArrayList<>();

        if (benchmarkType == -1) {
            publishedCountryHealthIndicators = countryHealthIndicators;
        } else {
            publishedCountryHealthIndicators = countryHealthIndicators.stream().filter(countryHealthIndicator ->
                    validateCountryHealthIndicatorByPhaseAndYear(countryHealthIndicator, benchmarkType, year)).collect(Collectors.toList());
        }

        Map<Integer, Double> indicatorBenchmarkScores = publishedCountryHealthIndicators.stream()
                .map(row -> {
                    row.convertNotAvailableToPhase1();
                    return row;
                })
                .filter(indicator -> indicator.getIndicator().getParentId() == null)
                .collect(groupingBy(h -> h.getIndicatorId(),
                        averagingInt(CountryHealthIndicator::getScore)));

        return indicatorBenchmarkScores;
    }

    private boolean validateCountryHealthIndicatorByPhaseAndYear(CountryHealthIndicator countryHealthIndicator, Integer phase, String year) {
        CountryPhase countryPhase = iCountryPhaseRepository.findByCountryPhaseIdYearAndCountryPhaseIdCountryId(year, countryHealthIndicator.getCountryId());
        if (countryPhase.getCountryOverallPhase() == phase) {
            return true;
        }
        return false;
    }

    Map<Integer, BenchmarkDto> getBenchmarkFor(String countryId, Integer benchmarkType, String year) {
        Map<Integer, Double> indicatorBenchmarkScores = calculateBenchmarkScoresForIndicators(benchmarkType, year);

        List<CountryHealthIndicator> countryHealthIndicator = iCountryHealthIndicatorRepository
                .findByCountryHealthIndicatorIdCountryIdAndCountryHealthIndicatorIdStatusAndCountryHealthIndicatorIdYear(countryId, PUBLISHED.name(), year);

        Map<Integer, BenchmarkDto> benchmarkScoresForCountry = countryHealthIndicator.stream()
                .filter(indicator -> (indicator.isScoreValid() &&
                        indicatorBenchmarkScores.containsKey(indicator.getIndicatorId())
                        && indicator.getIndicator().getParentId() == null))
                .collect(Collectors.toMap(CountryHealthIndicator::getIndicatorId,
                        indicator -> constructBenchMarkDto(indicator.getScore(),
                                indicatorBenchmarkScores.get(indicator.getIndicatorId()))));


        return benchmarkScoresForCountry;
    }

    private BenchmarkDto constructBenchMarkDto(Integer indicatorCountryScore, Double indicatorBenchmarkScore) {
        Score benchmarkScore = new Score(indicatorBenchmarkScore);
        Integer benchmarkPhase = benchmarkScore.convertToPhase();
        String benchmarkValue = indicatorCountryScore > benchmarkPhase ? BENCHMARK_ABOVE_PAR_VALUE :
                (indicatorCountryScore == benchmarkPhase ? BENCHMARK_AT_PAR_VALUE : BENCHMARK_BELOW_PAR_VALUE);

        return new BenchmarkDto(benchmarkPhase, benchmarkValue);
    }
}
