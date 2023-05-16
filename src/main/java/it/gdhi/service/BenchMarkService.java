package it.gdhi.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import it.gdhi.dto.BenchmarkDto;
import it.gdhi.model.CountryHealthIndicator;
import it.gdhi.model.CountryPhase;
import it.gdhi.model.RegionalIndicatorData;
import it.gdhi.model.Score;
import it.gdhi.repository.ICountryHealthIndicatorRepository;
import it.gdhi.repository.ICountryPhaseRepository;
import it.gdhi.repository.IRegionalIndicatorDataRepository;
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
    private IRegionalIndicatorDataRepository iRegionalIndicatorDataRepository;

    private Map<Integer, Double> calculateBenchmarkScoresForIndicators(Integer benchmarkType, String year) {
        List<CountryHealthIndicator> countryHealthIndicators =
                iCountryHealthIndicatorRepository.findByStatusAndCountryHealthIndicatorIdYear(PUBLISHED.name(), year);
        List<CountryHealthIndicator> publishedCountryHealthIndicators = (benchmarkType == -1) ? countryHealthIndicators :
                countryHealthIndicators.stream().filter(countryHealthIndicator ->
                        validateCountryHealthIndicatorByPhaseAndYear(countryHealthIndicator, benchmarkType, year)).toList();

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
        CountryPhase countryPhase =
                iCountryPhaseRepository.findByCountryPhaseIdCountryIdAndCountryPhaseIdYear(countryHealthIndicator.getCountryId(), year);
        return countryPhase.getCountryOverallPhase().equals(phase);
    }

    public Map<Integer, BenchmarkDto> getBenchmarkFor(String countryId, Integer benchmarkType, String year) {
        List<CountryHealthIndicator> countryHealthIndicator = iCountryHealthIndicatorRepository
                .findByCountryHealthIndicatorIdCountryIdAndCountryHealthIndicatorIdYearAndStatus(countryId, year,
                        PUBLISHED.name());
        Map<Integer, Double> indicatorBenchmarkScores = calculateBenchmarkScoresForIndicators(benchmarkType, year);

        return countryHealthIndicator.stream().
                filter(indicator -> (indicator.isScoreValid()
                        && indicator.getIndicator().getParentId() == null))
                .collect(Collectors.toMap(CountryHealthIndicator::getIndicatorId,
                        indicator -> constructBenchMarkDto(
                                indicatorBenchmarkScores.get(indicator.getIndicatorId()))));
    }

    public Map<Integer, BenchmarkDto> getBenchMarkForRegion(String countryId, String year, String region) {
        List<CountryHealthIndicator> countryHealthIndicator = iCountryHealthIndicatorRepository
                .findByCountryHealthIndicatorIdCountryIdAndCountryHealthIndicatorIdYearAndStatus(countryId, year,
                        PUBLISHED.name());
        Map<Integer, Integer> regionalIndicatorBenchmarkScores = fetchRegionalIndicatorScoreData(region, year);
        return countryHealthIndicator.stream().
                filter(indicator -> (indicator.isScoreValid()
                        && indicator.getIndicator().getParentId() == null))
                .collect(Collectors.toMap(CountryHealthIndicator::getIndicatorId,
                        indicator -> constructBenchMarkDtoForRegionalData(
                                regionalIndicatorBenchmarkScores.get(indicator.getIndicatorId()))));
    }

    private BenchmarkDto constructBenchMarkDto(Double indicatorBenchmarkScore) {
        Score benchmarkScore = indicatorBenchmarkScore == null ? new Score(-1.0) : new Score(indicatorBenchmarkScore);
        Integer benchmarkPhase = benchmarkScore.convertToPhase();
        return new BenchmarkDto(benchmarkPhase);
    }

    private BenchmarkDto constructBenchMarkDtoForRegionalData(Integer indicatorBenchmarkScore) {
        Integer benchmarkPhase = (indicatorBenchmarkScore == null) ? -1 : indicatorBenchmarkScore;
        return new BenchmarkDto(benchmarkPhase);
    }

    private Map<Integer, Integer> fetchRegionalIndicatorScoreData(String regionId, String year) {
        List<RegionalIndicatorData> regionalIndicatorsData =
                iRegionalIndicatorDataRepository.findByRegionalIndicatorIdRegionIdAndRegionalIndicatorIdYear(regionId, year);
        return regionalIndicatorsData.stream().collect(Collectors.toMap(RegionalIndicatorData::getRegionalIndicatorId,
                RegionalIndicatorData::getScore));
    }
}
