package it.gdhi.service;

import it.gdhi.dto.BenchmarkDto;
import it.gdhi.model.CountryHealthIndicator;
import it.gdhi.model.Score;
import it.gdhi.repository.ICountryHealthIndicatorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static it.gdhi.utils.FormStatus.PUBLISHED;
import static java.util.stream.Collectors.averagingInt;
import static java.util.stream.Collectors.groupingBy;

@Service
public class BenchMarkService {

    static final String BENCHMARK_AT_PAR_VALUE="At";
    static final String BENCHMARK_ABOVE_PAR_VALUE="Above";
    static final String BENCHMARK_BELOW_PAR_VALUE="Below";

    @Autowired
    private ICountryHealthIndicatorRepository iCountryHealthIndicatorRepository;


    private Map<Integer, Double> calculateBenchmarkScoresForIndicators(Integer benchmarkType) {
        List<CountryHealthIndicator> publishedCountryHealthIndicators =
                iCountryHealthIndicatorRepository.findByStatusAndPhase(PUBLISHED.name(), benchmarkType);


        Map<Integer, Double> indicatorBenchmarkScores = publishedCountryHealthIndicators.stream()
                .map( row -> {
                    row.convertNotAvailableToPhase1();
                    return row;
                })
                .filter(indicator -> indicator.getIndicator().getParentId() == null)
                .collect(groupingBy(h -> h.getIndicatorId(),
                        averagingInt(CountryHealthIndicator::getScore)));

        return indicatorBenchmarkScores;
    }


    Map<Integer, BenchmarkDto> getBenchmarkFor(String countryId, Integer benchmarkType) {
        Map<Integer, Double> indicatorBenchmarkScores = calculateBenchmarkScoresForIndicators(benchmarkType);

        List<CountryHealthIndicator> countryHealthIndicator = iCountryHealthIndicatorRepository
                .findByCountryIdAndStatus(countryId, PUBLISHED.name());

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
