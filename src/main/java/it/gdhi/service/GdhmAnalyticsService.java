package it.gdhi.service;

import it.gdhi.ai.dto.BedrockCountryPhaseTrendData;
import it.gdhi.ai.dto.BedrockCountryRankingData;
import it.gdhi.ai.dto.BedrockDataCompletenessData;
import it.gdhi.model.Country;
import it.gdhi.model.CountryHealthIndicator;
import it.gdhi.model.CountryPhase;
import it.gdhi.repository.ICountryHealthIndicatorRepository;
import it.gdhi.repository.ICountryPhaseRepository;
import it.gdhi.repository.ICountryRepository;
import it.gdhi.repository.IRegionCountryRepository;
import it.gdhi.service.analytics.DataCompletenessAnalysisType;
import it.gdhi.service.analytics.RegionAlias;
import it.gdhi.service.analytics.TrendDirection;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import static it.gdhi.service.analytics.AnalyticsLimit.resolve;
import static it.gdhi.utils.FormStatus.PUBLISHED;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class GdhmAnalyticsService {

    private final ICountryPhaseRepository countryPhaseRepository;
    private final ICountryHealthIndicatorRepository countryHealthIndicatorRepository;
    private final ICountryRepository countryRepository;
    private final IRegionCountryRepository regionCountryRepository;

    public List<BedrockCountryPhaseTrendData> analyzeCountryPhaseTrends(
            String regionId,
            String countryId,
            Integer categoryId,
            Integer indicatorId,
            String startYear,
            String endYear,
            String direction,
            Integer minSubmissionYears,
            Integer limit) {
        String effectiveRegionId = RegionAlias.normalize(regionId);
        TrendDirection trendDirection = TrendDirection.from(direction);
        Set<String> scopedCountries = scopedCountryIds(effectiveRegionId, countryId);
        Map<String, Country> countries = countriesById(scopedCountries);

        if (categoryId != null || indicatorId != null) {
            return analyzeHealthIndicatorTrends(effectiveRegionId, scopedCountries, countries, categoryId, indicatorId,
                    startYear, endYear, trendDirection, minSubmissionYears, limit);
        }

        return countryPhasesForTrend(scopedCountries, startYear, endYear).stream()
                .collect(Collectors.groupingBy(phase -> phase.getCountryPhaseId().getCountryId()))
                .entrySet()
                .stream()
                .map(entry -> buildTrend(entry.getKey(), entry.getValue(), effectiveRegionId,
                        countries.get(entry.getKey())))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .filter(trendDirection::matches)
                .filter(trend -> minSubmissionYears == null
                        || trend.submissionYears().size() >= minSubmissionYears)
                .sorted(trendDirection.comparator())
                .limit(resolve(limit))
                .toList();
    }

    private List<BedrockCountryPhaseTrendData> analyzeHealthIndicatorTrends(
            String regionId,
            Set<String> scopedCountries,
            Map<String, Country> countries,
            Integer categoryId,
            Integer indicatorId,
            String startYear,
            String endYear,
            TrendDirection trendDirection,
            Integer minSubmissionYears,
            Integer limit) {
        return healthIndicatorsForTrend(scopedCountries, categoryId, indicatorId, startYear, endYear).stream()
                .filter(this::hasAvailableScore)
                .collect(Collectors.groupingBy(
                        indicator -> indicator.getCountryHealthIndicatorId().getCountryId(),
                        Collectors.groupingBy(indicator -> indicator.getCountryHealthIndicatorId().getYear())))
                .entrySet()
                .stream()
                .map(entry -> buildHealthIndicatorTrend(entry.getKey(), entry.getValue(), regionId,
                        countries.get(entry.getKey()), categoryId, indicatorId))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .filter(trendDirection::matches)
                .filter(trend -> minSubmissionYears == null
                        || trend.submissionYears().size() >= minSubmissionYears)
                .sorted(trendDirection.comparator())
                .limit(resolve(limit))
                .toList();
    }

    public List<BedrockCountryRankingData> rankCountries(
            String regionId,
            List<String> countryIds,
            Integer categoryId,
            Integer indicatorId,
            String year,
            Integer minPhase,
            Integer maxPhase,
            String sort,
            Integer limit,
            Integer secondaryCategoryId,
            Integer secondaryIndicatorId,
            Integer secondaryMinPhase,
            Integer secondaryMaxPhase) {
        String effectiveRegionId = RegionAlias.normalize(regionId);
        Set<String> scopedCountries = scopedCountryIds(effectiveRegionId, countryIds);
        List<CountryHealthIndicator> indicators = healthIndicatorsFor(year, scopedCountries, categoryId, null);
        Map<String, Country> countries = countriesById(scopedCountries);
        Map<String, String> regionByCountry = regionByCountryId(scopedCountries);
        Map<String, List<CountryHealthIndicator>> byCountry = indicators.stream()
                .filter(indicator -> scopedCountries.isEmpty()
                        || scopedCountries.contains(indicator.getCountryHealthIndicatorId().getCountryId()))
                .collect(Collectors.groupingBy(indicator -> indicator.getCountryHealthIndicatorId().getCountryId()));

        return byCountry.entrySet().stream()
                .map(entry -> rankingFor(entry.getKey(), entry.getValue(), countries.get(entry.getKey()),
                        regionByCountry.get(entry.getKey()), year, categoryId, indicatorId, secondaryCategoryId,
                        secondaryIndicatorId))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .filter(ranking -> inRange(ranking.primaryScore().phase(), minPhase, maxPhase))
                .filter(ranking -> ranking.secondaryScore() == null
                        || inRange(ranking.secondaryScore().phase(), secondaryMinPhase, secondaryMaxPhase))
                .sorted(rankingComparator(sort))
                .limit(resolve(limit))
                .toList();
    }

    public List<BedrockDataCompletenessData> analyzeDataCompleteness(
            String analysisType,
            String year,
            String regionId,
            Integer phase,
            Integer limit) {
        String effectiveRegionId = RegionAlias.normalize(regionId);
        return switch (DataCompletenessAnalysisType.from(analysisType)) {
            case PHASE_COUNT_BY_INDICATOR -> phaseCountByIndicator(year, effectiveRegionId, phase, limit);
            case MISSING_COUNTRY_DATA -> missingCountryData(year, effectiveRegionId, limit);
            case PROXY_ONLY -> throw new IllegalArgumentException("analysisType 'proxy_only' is not supported yet.");
        };
    }

    public List<String> supportedTrendDirections() {
        return Arrays.stream(TrendDirection.values()).map(TrendDirection::value).toList();
    }

    public List<String> supportedDataCompletenessAnalysisTypes() {
        return Arrays.stream(DataCompletenessAnalysisType.values())
                .filter(DataCompletenessAnalysisType::isSupported)
                .map(DataCompletenessAnalysisType::value)
                .toList();
    }

    private Optional<BedrockCountryPhaseTrendData> buildTrend(
            String countryId,
            List<CountryPhase> phases,
            String requestedRegionId,
            Country country) {
        List<CountryPhase> sorted = phases.stream()
                .sorted(Comparator.comparing(CountryPhase::getYear, this::compareYears))
                .toList();
        if (sorted.size() < 2) {
            return Optional.empty();
        }

        CountryPhase first = sorted.get(0);
        CountryPhase last = sorted.get(sorted.size() - 1);
        List<BedrockCountryPhaseTrendData.YearPhase> trajectory = sorted.stream()
                .map(phase -> new BedrockCountryPhaseTrendData.YearPhase(phase.getYear(),
                        phase.getCountryOverallPhase()))
                .toList();
        List<String> submissionYears = sorted.stream().map(CountryPhase::getYear).toList();

        return Optional.of(new BedrockCountryPhaseTrendData(
                countryId,
                countryName(country, countryId),
                resolveRegionId(requestedRegionId, countryId),
                "overall",
                null,
                "Overall digital health phase",
                first.getYear(),
                first.getCountryOverallPhase(),
                last.getYear(),
                last.getCountryOverallPhase(),
                safePhase(last.getCountryOverallPhase()) - safePhase(first.getCountryOverallPhase()),
                submissionYears,
                trajectory
        ));
    }

    private Optional<BedrockCountryPhaseTrendData> buildHealthIndicatorTrend(
            String countryId,
            Map<String, List<CountryHealthIndicator>> indicatorsByYear,
            String requestedRegionId,
            Country country,
            Integer categoryId,
            Integer indicatorId) {
        List<TrendPoint> points = indicatorsByYear.entrySet().stream()
                .map(entry -> buildHealthIndicatorTrendPoint(entry.getKey(), entry.getValue(), categoryId,
                        indicatorId))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .sorted(Comparator.comparing(TrendPoint::year, this::compareYears))
                .toList();
        if (points.size() < 2) {
            return Optional.empty();
        }

        TrendPoint first = points.get(0);
        TrendPoint last = points.get(points.size() - 1);
        List<BedrockCountryPhaseTrendData.YearPhase> trajectory = points.stream()
                .map(point -> new BedrockCountryPhaseTrendData.YearPhase(point.year(), point.phase()))
                .toList();
        List<String> submissionYears = points.stream().map(TrendPoint::year).toList();

        return Optional.of(new BedrockCountryPhaseTrendData(
                countryId,
                countryName(country, countryId),
                resolveRegionId(requestedRegionId, countryId),
                last.scoreType(),
                last.scoreId(),
                last.scoreName(),
                first.year(),
                first.phase(),
                last.year(),
                last.phase(),
                safePhase(last.phase()) - safePhase(first.phase()),
                submissionYears,
                trajectory
        ));
    }

    private Optional<TrendPoint> buildHealthIndicatorTrendPoint(
            String year,
            List<CountryHealthIndicator> indicators,
            Integer categoryId,
            Integer indicatorId) {
        List<CountryHealthIndicator> validIndicators = indicators.stream()
                .filter(this::hasAvailableScore)
                .toList();
        if (validIndicators.isEmpty()) {
            return Optional.empty();
        }

        CountryHealthIndicator first = validIndicators.get(0);
        if (indicatorId != null) {
            return Optional.of(new TrendPoint(
                    year,
                    first.getScore(),
                    "indicator",
                    first.getCountryHealthIndicatorId().getIndicatorId(),
                    first.getIndicatorName()
            ));
        }

        double average = validIndicators.stream().mapToInt(CountryHealthIndicator::getScore).average().orElse(0);
        return Optional.of(new TrendPoint(
                year,
                convertAverageToPhase(average),
                "category",
                categoryId,
                first.getCategory().getName()
        ));
    }

    private Optional<BedrockCountryRankingData> rankingFor(
            String countryId,
            List<CountryHealthIndicator> indicators,
            Country country,
            String regionId,
            String year,
            Integer categoryId,
            Integer indicatorId,
            Integer secondaryCategoryId,
            Integer secondaryIndicatorId) {
        Optional<BedrockCountryRankingData.Score> primary =
                scoreFor(indicators, categoryId, indicatorId);
        if (primary.isEmpty()) {
            return Optional.empty();
        }

        Optional<BedrockCountryRankingData.Score> secondary =
                secondaryCategoryId == null && secondaryIndicatorId == null
                        ? Optional.empty()
                        : scoreFor(indicators, secondaryCategoryId, secondaryIndicatorId);

        if ((secondaryCategoryId != null || secondaryIndicatorId != null) && secondary.isEmpty()) {
            return Optional.empty();
        }

        String effectiveYear = StringUtils.hasText(year) ? year : latestYearFor(indicators);
        return Optional.of(new BedrockCountryRankingData(
                countryId,
                countryName(country, countryId),
                regionId,
                effectiveYear,
                primary.get(),
                secondary.orElse(null)
        ));
    }

    private Optional<BedrockCountryRankingData.Score> scoreFor(
            List<CountryHealthIndicator> indicators,
            Integer categoryId,
            Integer indicatorId) {
        List<CountryHealthIndicator> filtered = indicators.stream()
                .filter(indicator -> categoryId == null
                        || categoryId.equals(indicator.getCountryHealthIndicatorId().getCategoryId()))
                .filter(indicator -> indicatorId == null
                        || indicatorId.equals(indicator.getCountryHealthIndicatorId().getIndicatorId()))
                .filter(this::hasAvailableScore)
                .toList();
        if (filtered.isEmpty()) {
            return Optional.empty();
        }

        if (indicatorId != null) {
            CountryHealthIndicator indicator = filtered.get(0);
            return Optional.of(new BedrockCountryRankingData.Score(
                    "indicator",
                    indicator.getCountryHealthIndicatorId().getIndicatorId(),
                    indicator.getIndicatorName(),
                    indicator.getScore()
            ));
        }

        double average = filtered.stream().mapToInt(CountryHealthIndicator::getScore).average().orElse(0);
        CountryHealthIndicator first = filtered.get(0);
        return Optional.of(new BedrockCountryRankingData.Score(
                "category",
                first.getCountryHealthIndicatorId().getCategoryId(),
                first.getCategory().getName(),
                convertAverageToPhase(average)
        ));
    }

    private List<BedrockDataCompletenessData> missingCountryData(String year, String regionId, Integer limit) {
        Set<String> scopedCountries = scopedCountryIds(regionId, List.of());
        List<CountryHealthIndicator> indicators = healthIndicatorsFor(year, scopedCountries, null, null);
        Map<String, Country> countries = countriesById(scopedCountries);
        Map<String, String> regionByCountry = regionByCountryId(scopedCountries);

        return indicators.stream()
                .filter(indicator -> scopedCountries.isEmpty()
                        || scopedCountries.contains(indicator.getCountryHealthIndicatorId().getCountryId()))
                .collect(Collectors.groupingBy(indicator -> indicator.getCountryHealthIndicatorId().getCountryId()))
                .entrySet()
                .stream()
                .map(entry -> missingDataForCountry(entry.getKey(), entry.getValue(),
                        countries.get(entry.getKey()), regionByCountry.get(entry.getKey()), year))
                .sorted(Comparator.comparing(BedrockDataCompletenessData::missingIndicatorCount).reversed())
                .limit(resolve(limit))
                .toList();
    }

    private List<BedrockDataCompletenessData> phaseCountByIndicator(
            String year,
            String regionId,
            Integer phase,
            Integer limit) {
        Integer effectivePhase = phase == null ? 1 : phase;
        Set<String> scopedCountries = scopedCountryIds(regionId, List.of());
        List<CountryHealthIndicator> indicators = healthIndicatorsFor(year, scopedCountries, null, null);
        return indicators.stream()
                .filter(indicator -> scopedCountries.isEmpty()
                        || scopedCountries.contains(indicator.getCountryHealthIndicatorId().getCountryId()))
                .filter(indicator -> effectivePhase.equals(indicator.getScore()))
                .collect(Collectors.groupingBy(indicator -> indicator.getCountryHealthIndicatorId().getIndicatorId()))
                .entrySet()
                .stream()
                .map(entry -> phaseCountForIndicator(entry.getValue(), year, effectivePhase))
                .sorted(Comparator.comparing(BedrockDataCompletenessData::countryCount).reversed())
                .limit(resolve(limit))
                .toList();
    }

    private BedrockDataCompletenessData missingDataForCountry(
            String countryId,
            List<CountryHealthIndicator> indicators,
            Country country,
            String regionId,
            String year) {
        int missing = (int) indicators.stream()
                .filter(indicator -> indicator.getScore() == null || indicator.getScore() == -1)
                .count();
        return new BedrockDataCompletenessData(
                "missing_country_data",
                countryId,
                countryName(country, countryId),
                regionId,
                StringUtils.hasText(year) ? year : latestYearFor(indicators),
                missing,
                indicators.size(),
                null,
                null,
                null,
                null
        );
    }

    private BedrockDataCompletenessData phaseCountForIndicator(
            List<CountryHealthIndicator> indicators,
            String year,
            Integer phase) {
        CountryHealthIndicator first = indicators.get(0);
        return new BedrockDataCompletenessData(
                "phase_count_by_indicator",
                null,
                null,
                null,
                StringUtils.hasText(year) ? year : latestYearFor(indicators),
                null,
                null,
                first.getCountryHealthIndicatorId().getIndicatorId(),
                first.getIndicatorName(),
                phase,
                indicators.size()
        );
    }

    private List<CountryPhase> countryPhasesForTrend(Set<String> scopedCountries, String startYear, String endYear) {
        List<String> countryIds = new ArrayList<>(scopedCountries);
        if (!StringUtils.hasText(startYear) && !StringUtils.hasText(endYear)) {
            if (!countryIds.isEmpty()) {
                return countryPhaseRepository.findByCountryPhaseIdCountryIdIn(countryIds);
            }
            return countryPhaseRepository.findByLatestTrue();
        }
        return countryPhaseRepository.findAll(countryPhaseSpec(countryIds, startYear, endYear));
    }

    private List<CountryHealthIndicator> healthIndicatorsForTrend(Set<String> scopedCountries, Integer categoryId,
                                                                  Integer indicatorId, String startYear,
                                                                  String endYear) {
        List<String> countryIds = new ArrayList<>(scopedCountries);
        if (!StringUtils.hasText(startYear) && !StringUtils.hasText(endYear)) {
            if (!countryIds.isEmpty()) {
                return countryHealthIndicatorRepository.findAll(healthIndicatorSpec(countryIds, categoryId,
                        indicatorId, null, null));
            }
            return filterIndicators(countryHealthIndicatorRepository.findLatestByCountryAndCategoryAndStatus(null,
                    categoryId, PUBLISHED.name()), List.of(), indicatorId);
        }
        return countryHealthIndicatorRepository.findAll(healthIndicatorSpec(countryIds, categoryId, indicatorId,
                startYear, endYear));
    }

    private List<CountryHealthIndicator> healthIndicatorsFor(String year, Set<String> scopedCountries,
                                                             Integer categoryId, Integer indicatorId) {
        List<String> countryIds = new ArrayList<>(scopedCountries);
        if (StringUtils.hasText(year)) {
            return countryHealthIndicatorRepository.findAll(healthIndicatorSpec(countryIds, categoryId, indicatorId,
                    year, year));
        }
        return filterIndicators(countryHealthIndicatorRepository.findLatestByCountryAndCategoryAndStatus(null,
                categoryId, PUBLISHED.name()), countryIds, indicatorId);
    }

    private List<CountryHealthIndicator> filterIndicators(List<CountryHealthIndicator> indicators,
                                                          List<String> countryIds,
                                                          Integer indicatorId) {
        return indicators.stream()
                .filter(indicator -> countryIds.isEmpty()
                        || countryIds.contains(indicator.getCountryHealthIndicatorId().getCountryId()))
                .filter(indicator -> indicatorId == null
                        || indicatorId.equals(indicator.getCountryHealthIndicatorId().getIndicatorId()))
                .toList();
    }

    private Specification<CountryPhase> countryPhaseSpec(List<String> countryIds, String startYear, String endYear) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (!countryIds.isEmpty()) {
                predicates.add(root.get("countryPhaseId").get("countryId").in(countryIds));
            }
            if (StringUtils.hasText(startYear)) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(
                        root.get("countryPhaseId").get("year"), startYear));
            }
            if (StringUtils.hasText(endYear)) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(
                        root.get("countryPhaseId").get("year"), endYear));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

    private Specification<CountryHealthIndicator> healthIndicatorSpec(List<String> countryIds,
                                                                      Integer categoryId,
                                                                      Integer indicatorId,
                                                                      String startYear,
                                                                      String endYear) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(criteriaBuilder.equal(root.get("status"), PUBLISHED.name()));
            if (!countryIds.isEmpty()) {
                predicates.add(root.get("countryHealthIndicatorId").get("countryId").in(countryIds));
            }
            if (categoryId != null) {
                predicates.add(criteriaBuilder.equal(root.get("countryHealthIndicatorId").get("categoryId"),
                        categoryId));
            }
            if (indicatorId != null) {
                predicates.add(criteriaBuilder.equal(root.get("countryHealthIndicatorId").get("indicatorId"),
                        indicatorId));
            }
            if (StringUtils.hasText(startYear)) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(
                        root.get("countryHealthIndicatorId").get("year"), startYear));
            }
            if (StringUtils.hasText(endYear)) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(
                        root.get("countryHealthIndicatorId").get("year"), endYear));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

    private Set<String> scopedCountryIds(String regionId, String countryId) {
        if (StringUtils.hasText(countryId)) {
            return Set.of(countryId.toUpperCase());
        }
        return scopedCountryIds(regionId, List.of());
    }

    private Set<String> scopedCountryIds(String regionId, List<String> countryIds) {
        if (countryIds != null && !countryIds.isEmpty()) {
            return countryIds.stream()
                    .filter(StringUtils::hasText)
                    .map(String::trim)
                    .map(String::toUpperCase)
                    .collect(Collectors.toCollection(LinkedHashSet::new));
        }
        if (StringUtils.hasText(regionId)) {
            return new HashSet<>(regionCountryRepository.findByRegionCountryIdRegionId(RegionAlias.normalize(regionId)));
        }
        return Set.of();
    }

    private Map<String, Country> countriesById(Set<String> scopedCountryIds) {
        List<Country> countries = scopedCountryIds.isEmpty()
                ? countryRepository.findAll()
                : countryRepository.findByIdIn(new ArrayList<>(scopedCountryIds));
        return countries.stream().collect(Collectors.toMap(Country::getId, Function.identity()));
    }

    private Map<String, String> regionByCountryId(Set<String> scopedCountryIds) {
        if (!scopedCountryIds.isEmpty()) {
            return scopedCountryIds.stream().collect(Collectors.toMap(Function.identity(), this::resolveRegionId));
        }
        Map<String, String> out = new LinkedHashMap<>();
        regionCountryRepository.findAll().forEach(regionCountry ->
                out.put(regionCountry.getRegionCountryId().getCountryId(),
                        regionCountry.getRegionCountryId().getRegionId()));
        return out;
    }

    private String resolveRegionId(String requestedRegionId, String countryId) {
        return StringUtils.hasText(requestedRegionId) ? requestedRegionId : resolveRegionId(countryId);
    }

    private String resolveRegionId(String countryId) {
        if (!StringUtils.hasText(countryId)) {
            return null;
        }
        return Optional.ofNullable(regionCountryRepository.findByRegionCountryIdCountryId(countryId))
                .map(regionCountry -> regionCountry.getRegionCountryId().getRegionId())
                .orElse(null);
    }

    private Comparator<BedrockCountryRankingData> rankingComparator(String sort) {
        Comparator<BedrockCountryRankingData> comparator =
                Comparator.comparing(data -> data.primaryScore().phase());
        if ("lowest".equalsIgnoreCase(sort)) {
            return comparator;
        }
        return comparator.reversed();
    }

    private boolean inRange(Integer value, Integer min, Integer max) {
        return value != null && (min == null || value >= min) && (max == null || value <= max);
    }

    private int convertAverageToPhase(double average) {
        return (int) Math.round(average);
    }

    private boolean hasAvailableScore(CountryHealthIndicator indicator) {
        return indicator.getScore() != null && indicator.getScore() != -1;
    }

    private int safePhase(Integer phase) {
        return phase == null ? 0 : phase;
    }

    private int compareYears(String left, String right) {
        return yearSortValue(left).compareTo(yearSortValue(right));
    }

    private Integer yearSortValue(String year) {
        if (year != null && year.matches("\\d{4}")) {
            return Integer.valueOf(year);
        }
        return 0;
    }

    private String latestYearFor(List<CountryHealthIndicator> indicators) {
        return indicators.stream()
                .map(indicator -> indicator.getCountryHealthIndicatorId().getYear())
                .max(this::compareYears)
                .orElse(null);
    }

    private String countryName(Country country, String fallbackId) {
        return country == null ? fallbackId : country.getName();
    }

    private record TrendPoint(
            String year,
            Integer phase,
            String scoreType,
            Integer scoreId,
            String scoreName) {
    }
}
