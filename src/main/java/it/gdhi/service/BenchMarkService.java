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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    Map<Integer, BenchmarkDto> getBenchmarkFor(String countryId, Integer benchmarkType, String year) {
        Map<Integer, Double> indicatorBenchmarkScores = calculateBenchmarkScoresForIndicators(benchmarkType, year);

        List<CountryHealthIndicator> countryHealthIndicator = iCountryHealthIndicatorRepository
                .findByCountryHealthIndicatorIdCountryIdAndCountryHealthIndicatorIdYearAndCountryHealthIndicatorIdStatus(countryId, year, PUBLISHED.name());

        return countryHealthIndicator.stream()
                .filter(indicator -> (indicator.isScoreValid() &&
                        indicatorBenchmarkScores.containsKey(indicator.getIndicatorId())
                        && indicator.getIndicator().getParentId() == null))
                .collect(Collectors.toMap(CountryHealthIndicator::getIndicatorId,
                        indicator -> constructBenchMarkDto(indicator.getScore(),
                                indicatorBenchmarkScores.get(indicator.getIndicatorId()))));
    }

    private BenchmarkDto constructBenchMarkDto(Integer indicatorCountryScore, Double indicatorBenchmarkScore) {
        Score benchmarkScore = new Score(indicatorBenchmarkScore);
        Integer benchmarkPhase = benchmarkScore.convertToPhase();
        String benchmarkValue = indicatorCountryScore > benchmarkPhase ? BENCHMARK_ABOVE_PAR_VALUE :
                (indicatorCountryScore == benchmarkPhase ? BENCHMARK_AT_PAR_VALUE : BENCHMARK_BELOW_PAR_VALUE);

        return new BenchmarkDto(benchmarkPhase, benchmarkValue);
    }
}
