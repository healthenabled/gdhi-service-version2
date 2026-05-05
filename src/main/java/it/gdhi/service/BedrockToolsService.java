package it.gdhi.service;

import it.gdhi.ai.dto.BedrockCountryPhaseData;
import it.gdhi.ai.dto.BedrockCountryPhaseTrendData;
import it.gdhi.ai.dto.BedrockCountryRankingData;
import it.gdhi.ai.dto.BedrockCountrySummaryData;
import it.gdhi.ai.dto.BedrockDataCompletenessData;
import it.gdhi.ai.dto.BedrockToolResponse;
import it.gdhi.dto.CategoryIndicatorDto;
import it.gdhi.dto.CountriesHealthScoreDto;
import it.gdhi.dto.CountryHealthScoreDto;
import it.gdhi.dto.CountrySummaryDto;
import it.gdhi.dto.GlobalHealthScoreDto;
import it.gdhi.dto.PhaseDto;
import it.gdhi.dto.RegionCountriesDto;
import it.gdhi.internationalization.service.CountryNameTranslator;
import it.gdhi.model.Country;
import it.gdhi.model.CountryPhase;
import it.gdhi.model.Region;
import it.gdhi.repository.ICountryPhaseRepository;
import it.gdhi.repository.ICountryRepository;
import it.gdhi.service.analytics.RegionAlias;
import it.gdhi.utils.LanguageCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import software.amazon.awssdk.services.bedrockagentruntime.model.ApiInvocationInput;
import software.amazon.awssdk.services.bedrockagentruntime.model.ApiParameter;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static it.gdhi.utils.ApplicationConstants.defaultLimit;
import static it.gdhi.utils.LanguageCode.USER_LANGUAGE;
import static it.gdhi.utils.Util.getCurrentYear;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BedrockToolsService {

    private final CountryService countryService;
    private final CountryHealthIndicatorService countryHealthIndicatorService;
    private final RegionService regionService;
    private final CategoryIndicatorService categoryIndicatorService;
    private final PhaseService phaseService;
    private final DefaultYearDataService defaultYearDataService;
    private final ICountryPhaseRepository countryPhaseRepository;
    private final ICountryRepository countryRepository;
    private final CountryNameTranslator countryNameTranslator;
    private final GdhmAnalyticsService gdhmAnalyticsService;

    public BedrockToolResponse<List<Country>> listCountries(String languageHeader) {
        LanguageCode languageCode = LanguageCode.getValueFor(languageHeader);
        List<Country> countries = countryService.fetchCountries(languageCode);
        return BedrockToolResponse.ok("listCountries", "Fetched countries", filters("language", languageCode.name()),
                countries);
    }

    public BedrockToolResponse<BedrockCountrySummaryData> getCountrySummary(
            String countryId, String year, String languageHeader) {
        LanguageCode languageCode = LanguageCode.getValueFor(languageHeader);
        CountryPhase latestCountryPhase = null;
        String effectiveYear = year;
        if (!StringUtils.hasText(effectiveYear)) {
            latestCountryPhase = countryPhaseRepository.findLatestByCountryId(countryId);
            effectiveYear = latestCountryPhase == null ? resolveYear(year) : latestCountryPhase.getYear();
        }
        CountrySummaryDto dto = countryService.fetchCountrySummary(countryId, effectiveYear);
        translateSummaryCountryName(dto, countryId, languageCode);
        Integer countryPhase = latestCountryPhase == null
                ? fetchCountryOverallPhaseValue(countryId, effectiveYear)
                : latestCountryPhase.getCountryOverallPhase();
        BedrockCountrySummaryData data = BedrockCountrySummaryData.from(dto, effectiveYear, countryPhase);
        return BedrockToolResponse.ok("getCountrySummary",
                "Fetched country summary without personal contact details, including overall phase for the requested year",
                filters("countryId", countryId, "year", effectiveYear, "language", languageCode.name()), data);
    }

    public BedrockToolResponse<BedrockCountryPhaseData> getCountryPhase(
            String countryId, String year, String languageHeader) {
        LanguageCode languageCode = LanguageCode.getValueFor(languageHeader);
        CountryPhase countryPhase = null;
        String effectiveYear = year;
        if (!StringUtils.hasText(effectiveYear)) {
            countryPhase = countryPhaseRepository.findLatestByCountryId(countryId);
            effectiveYear = countryPhase == null ? resolveYear(year) : countryPhase.getYear();
        }
        BedrockCountryPhaseData data = countryPhase == null
                ? buildCountryPhaseData(countryId, effectiveYear, languageCode)
                : buildCountryPhaseData(countryId, effectiveYear, languageCode, countryPhase);
        return BedrockToolResponse.ok("getCountryPhase", "Fetched country overall phase",
                filters("countryId", countryId, "year", effectiveYear, "language", languageCode.name()), data);
    }

    public BedrockToolResponse<CountryHealthScoreDto> getCountryHealthIndicators(
            String countryId, String year, String languageHeader) {
        LanguageCode languageCode = LanguageCode.getValueFor(languageHeader);
        CountryPhase latestCountryPhase = null;
        String effectiveYear = year;
        CountryHealthScoreDto dto;
        if (StringUtils.hasText(effectiveYear)) {
            dto = countryHealthIndicatorService.fetchCountryHealthScore(countryId, languageCode, effectiveYear);
        }
        else {
            latestCountryPhase = countryPhaseRepository.findLatestByCountryId(countryId);
            effectiveYear = latestCountryPhase == null ? resolveYear(year) : latestCountryPhase.getYear();
            dto = latestCountryPhase == null
                    ? countryHealthIndicatorService.fetchCountryHealthScore(countryId, languageCode, effectiveYear)
                    : countryHealthIndicatorService.fetchLatestCountryHealthScore(countryId, languageCode);
        }
        return BedrockToolResponse.ok("getCountryHealthIndicators", "Fetched country health indicators",
                filters("countryId", countryId, "year", effectiveYear, "language", languageCode.name()), dto);
    }

    public BedrockToolResponse<GlobalHealthScoreDto> getGlobalHealthIndicators(
            Integer categoryId, Integer phase, String regionId, String year, String languageHeader) {
        String effectiveRegionId = RegionAlias.normalize(regionId);
        requireAnyFilter("global health indicators", categoryId, phase, effectiveRegionId);
        LanguageCode languageCode = LanguageCode.getValueFor(languageHeader);
        String effectiveYear = year;
        GlobalHealthScoreDto dto;
        if (StringUtils.hasText(effectiveYear)) {
            dto = effectiveRegionId == null
                    ? countryHealthIndicatorService.getGlobalHealthIndicator(categoryId, phase, languageCode,
                            effectiveYear)
                    : regionService.fetchRegionalHealthScores(categoryId, effectiveRegionId, languageCode,
                            effectiveYear);
        }
        else if (effectiveRegionId == null) {
            effectiveYear = "latest";
            dto = countryHealthIndicatorService.getLatestGlobalHealthIndicator(categoryId, phase, languageCode);
        }
        else {
            effectiveYear = resolveLatestRegionalYear(effectiveRegionId);
            dto = regionService.fetchRegionalHealthScores(categoryId, effectiveRegionId, languageCode, effectiveYear);
        }
        return BedrockToolResponse.ok("getGlobalHealthIndicators", "Fetched global health indicators",
                filters("categoryId", categoryId, "phase", phase, "regionId", effectiveRegionId, "year", effectiveYear,
                        "language", languageCode.name()), dto);
    }

    public BedrockToolResponse<CountriesHealthScoreDto> getCountriesHealthIndicatorScores(
            Integer categoryId, Integer phase, String year, String languageHeader) {
        requireAnyFilter("country score comparisons", categoryId, phase);
        LanguageCode languageCode = LanguageCode.getValueFor(languageHeader);
        String effectiveYear = year;
        CountriesHealthScoreDto dto;
        if (StringUtils.hasText(effectiveYear)) {
            dto = countryHealthIndicatorService.fetchCountriesHealthScores(categoryId, phase, languageCode,
                    effectiveYear);
        }
        else {
            effectiveYear = "latest";
            dto = countryHealthIndicatorService.fetchCountriesLatestHealthScores(categoryId, phase, languageCode);
        }
        return BedrockToolResponse.ok("getCountriesHealthIndicatorScores", "Fetched countries health indicator scores",
                filters("categoryId", categoryId, "phase", phase, "year", effectiveYear, "language",
                        languageCode.name()), dto);
    }

    public BedrockToolResponse<List<Region>> listRegions(String languageHeader) {
        LanguageCode languageCode = LanguageCode.getValueFor(languageHeader);
        List<Region> regions = regionService.fetchRegions(languageCode);
        return BedrockToolResponse.ok("listRegions", "Fetched regions", filters("language", languageCode.name()),
                regions);
    }

    public BedrockToolResponse<RegionCountriesDto> getRegionCountries(
            String regionId, List<String> years, String languageHeader) {
        LanguageCode languageCode = LanguageCode.getValueFor(languageHeader);
        String effectiveRegionId = RegionAlias.normalize(regionId);
        RegionCountriesDto dto = regionService.getRegionCountriesData(effectiveRegionId, years, languageCode);
        return BedrockToolResponse.ok("getRegionCountries", "Fetched region countries data",
                filters("regionId", effectiveRegionId, "list_of_years", years, "language", languageCode.name()), dto);
    }

    public BedrockToolResponse<List<String>> getRegionYears(String regionId, Integer limit) {
        Integer effectiveLimit = limit == null ? defaultLimit : limit;
        String effectiveRegionId = RegionAlias.normalize(regionId);
        List<String> years = regionService.fetchYearsForARegion(effectiveRegionId, effectiveLimit);
        return BedrockToolResponse.ok("getRegionYears", "Fetched region years",
                filters("regionId", effectiveRegionId, "limit", effectiveLimit), years);
    }

    public BedrockToolResponse<List<CategoryIndicatorDto>> getHealthIndicatorOptions(String languageHeader) {
        LanguageCode languageCode = LanguageCode.getValueFor(languageHeader);
        List<CategoryIndicatorDto> options = categoryIndicatorService.getHealthIndicatorOptions(languageCode);
        return BedrockToolResponse.ok("getHealthIndicatorOptions", "Fetched metadata options",
                filters("language", languageCode.name()), options);
    }

    public BedrockToolResponse<List<PhaseDto>> getPhases() {
        List<PhaseDto> phases = phaseService.getPhaseOptions();
        return BedrockToolResponse.ok("getPhases", "Fetched phase metadata", filters(), phases);
    }

    public BedrockToolResponse<List<String>> listYears() {
        List<String> years = defaultYearDataService.fetchYears();
        return BedrockToolResponse.ok("listYears", "Fetched available years", filters(), years);
    }

    public BedrockToolResponse<List<BedrockCountryPhaseData>> listCountriesByPhase(
            Integer phase, String year, String languageHeader) {
        if (phase == null) {
            throw new IllegalArgumentException("Missing required parameter: phase");
        }

        LanguageCode languageCode = LanguageCode.getValueFor(languageHeader);
        String effectiveYear = year;
        List<CountryPhase> countryPhases;
        if (StringUtils.hasText(effectiveYear)) {
            countryPhases = countryPhaseRepository.findByCountryPhaseIdYearAndCountryOverallPhase(effectiveYear, phase);
        }
        else {
            effectiveYear = "latest";
            countryPhases = countryPhaseRepository.findByLatestTrueAndCountryOverallPhase(phase);
        }
        List<BedrockCountryPhaseData> countries = countryPhases.stream()
                .map(countryPhase -> buildCountryPhaseData(countryPhase.getCountryPhaseId().getCountryId(),
                        countryPhase.getYear(),
                        languageCode, countryPhase))
                .filter(Objects::nonNull)
                .sorted((left, right) -> left.countryName().compareToIgnoreCase(right.countryName()))
                .toList();

        return BedrockToolResponse.ok("listCountriesByPhase", "Fetched countries by overall phase",
                filters("phase", phase, "year", effectiveYear, "language", languageCode.name()), countries);
    }

    public BedrockToolResponse<List<BedrockCountryPhaseTrendData>> analyzeCountryPhaseTrends(
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
        String effectiveDirection = StringUtils.hasText(direction) ? direction : "advanced";
        List<BedrockCountryPhaseTrendData> trends = gdhmAnalyticsService.analyzeCountryPhaseTrends(
                effectiveRegionId, countryId, categoryId, indicatorId, startYear, endYear, effectiveDirection,
                minSubmissionYears, limit);
        return BedrockToolResponse.ok("analyzeCountryPhaseTrends", "Analyzed country phase trends",
                filters("regionId", effectiveRegionId, "countryId", countryId, "categoryId", categoryId,
                        "indicatorId", indicatorId, "startYear", startYear, "endYear", endYear, "direction",
                        effectiveDirection, "minSubmissionYears", minSubmissionYears, "limit", limit),
                trends);
    }

    public BedrockToolResponse<List<BedrockCountryRankingData>> rankCountries(
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
        String effectiveSort = StringUtils.hasText(sort) ? sort : "highest";
        List<BedrockCountryRankingData> rankings = gdhmAnalyticsService.rankCountries(
                effectiveRegionId, countryIds, categoryId, indicatorId, year, minPhase, maxPhase, effectiveSort, limit,
                secondaryCategoryId, secondaryIndicatorId, secondaryMinPhase, secondaryMaxPhase);
        return BedrockToolResponse.ok("rankCountries", "Ranked countries by GDHM score filters",
                filters("regionId", effectiveRegionId, "countryId", countryIds, "categoryId", categoryId,
                        "indicatorId", indicatorId, "year", year, "minPhase", minPhase, "maxPhase", maxPhase,
                        "sort", effectiveSort, "limit", limit, "secondaryCategoryId", secondaryCategoryId,
                        "secondaryIndicatorId", secondaryIndicatorId, "secondaryMinPhase", secondaryMinPhase,
                        "secondaryMaxPhase", secondaryMaxPhase),
                rankings);
    }

    public BedrockToolResponse<List<BedrockDataCompletenessData>> analyzeDataCompleteness(
            String analysisType,
            String year,
            String regionId,
            Integer phase,
            Integer limit) {
        if (!StringUtils.hasText(analysisType)) {
            throw new IllegalArgumentException("Missing required parameter: analysisType");
        }
        String effectiveRegionId = RegionAlias.normalize(regionId);
        List<BedrockDataCompletenessData> data = gdhmAnalyticsService.analyzeDataCompleteness(
                analysisType, year, effectiveRegionId, phase, limit);
        return BedrockToolResponse.ok("analyzeDataCompleteness", "Analyzed GDHM data completeness",
                filters("analysisType", analysisType, "year", year, "regionId", effectiveRegionId, "phase", phase,
                        "limit", limit),
                data);
    }

    public BedrockToolResponse<?> executeApiInvocation(ApiInvocationInput apiInvocationInput) {
        if (!"GET".equalsIgnoreCase(apiInvocationInput.httpMethod())) {
            throw new IllegalArgumentException("Unsupported Bedrock tool HTTP method: " + apiInvocationInput.httpMethod());
        }

        Map<String, List<String>> parameters = groupParameters(apiInvocationInput.parameters());
        String apiPath = apiInvocationInput.apiPath();

        if ("/countries".equals(apiPath)) {
            return listCountries(optionalString(parameters, USER_LANGUAGE));
        }
        if ("/countries/{id}/summary".equals(apiPath)) {
            return getCountrySummary(requiredString(parameters, "id"), optionalString(parameters, "year"),
                    optionalString(parameters, USER_LANGUAGE));
        }
        if ("/countries/{id}/phase".equals(apiPath)) {
            return getCountryPhase(requiredString(parameters, "id"), optionalString(parameters, "year"),
                    optionalString(parameters, USER_LANGUAGE));
        }
        if ("/countries/{id}/health-indicators".equals(apiPath)) {
            return getCountryHealthIndicators(requiredString(parameters, "id"), optionalString(parameters, "year"),
                    optionalString(parameters, USER_LANGUAGE));
        }
        if ("/countries-by-phase".equals(apiPath)) {
            return listCountriesByPhase(optionalInteger(parameters, "phase"), optionalString(parameters, "year"),
                    optionalString(parameters, USER_LANGUAGE));
        }
        if ("/global-health-indicators".equals(apiPath)) {
            return getGlobalHealthIndicators(optionalInteger(parameters, "categoryId"),
                    optionalInteger(parameters, "phase"), optionalString(parameters, "regionId"),
                    optionalString(parameters, "year"), optionalString(parameters, USER_LANGUAGE));
        }
        if ("/countries-health-indicator-scores".equals(apiPath)) {
            return getCountriesHealthIndicatorScores(optionalInteger(parameters, "categoryId"),
                    optionalInteger(parameters, "phase"), optionalString(parameters, "year"),
                    optionalString(parameters, USER_LANGUAGE));
        }
        if ("/regions".equals(apiPath)) {
            return listRegions(optionalString(parameters, USER_LANGUAGE));
        }
        if ("/regions/{id}/countries".equals(apiPath)) {
            return getRegionCountries(requiredString(parameters, "id"), requiredList(parameters, "list_of_years"),
                    optionalString(parameters, USER_LANGUAGE));
        }
        if ("/regions/{id}/years".equals(apiPath)) {
            return getRegionYears(requiredString(parameters, "id"), optionalInteger(parameters, "limit"));
        }
        if ("/metadata/health-indicator-options".equals(apiPath)) {
            return getHealthIndicatorOptions(optionalString(parameters, USER_LANGUAGE));
        }
        if ("/metadata/phases".equals(apiPath)) {
            return getPhases();
        }
        if ("/metadata/years".equals(apiPath)) {
            return listYears();
        }
        if ("/analytics/country-phase-trends".equals(apiPath)) {
            return analyzeCountryPhaseTrends(optionalString(parameters, "regionId"),
                    optionalString(parameters, "countryId"), optionalInteger(parameters, "categoryId"),
                    optionalInteger(parameters, "indicatorId"), optionalString(parameters, "startYear"),
                    optionalString(parameters, "endYear"), optionalString(parameters, "direction"),
                    optionalInteger(parameters, "minSubmissionYears"), optionalInteger(parameters, "limit"));
        }
        if ("/analytics/country-rankings".equals(apiPath)) {
            return rankCountries(optionalString(parameters, "regionId"), countryIdValues(parameters),
                    optionalInteger(parameters, "categoryId"), optionalInteger(parameters, "indicatorId"),
                    optionalString(parameters, "year"), optionalInteger(parameters, "minPhase"),
                    optionalInteger(parameters, "maxPhase"), optionalString(parameters, "sort"),
                    optionalInteger(parameters, "limit"),
                    optionalInteger(parameters, "secondaryCategoryId"),
                    optionalInteger(parameters, "secondaryIndicatorId"),
                    optionalInteger(parameters, "secondaryMinPhase"),
                    optionalInteger(parameters, "secondaryMaxPhase"));
        }
        if ("/analytics/data-completeness".equals(apiPath)) {
            return analyzeDataCompleteness(requiredString(parameters, "analysisType"),
                    optionalString(parameters, "year"), optionalString(parameters, "regionId"),
                    optionalInteger(parameters, "phase"), optionalInteger(parameters, "limit"));
        }

        throw new IllegalArgumentException("Unsupported Bedrock tool path: " + apiPath);
    }

    private List<String> countryIdValues(Map<String, List<String>> parameters) {
        List<String> values = new ArrayList<>(listValues(parameters, "countryId"));
        values.addAll(listValues(parameters, "countryIds"));
        return values;
    }

    public BedrockToolResponse<Map<String, Object>> validationError(Exception ex) {
        return new BedrockToolResponse<>(
                "validation",
                "error",
                "The request needs more specific filters before I can safely run it.",
                filters(),
                filters("error", ex.getMessage()),
                Instant.now()
        );
    }

    public BedrockToolResponse<Map<String, Object>> serverError(Exception ex) {
        return new BedrockToolResponse<>(
                "server",
                "error",
                "Tool execution failed.",
                filters(),
                filters("error", ex.getMessage()),
                Instant.now()
        );
    }

    private String resolveYear(String year) {
        if (year != null && !year.isBlank()) {
            return year;
        }
        String defaultYear = defaultYearDataService.fetchDefaultYear();
        if (defaultYear != null && !defaultYear.isBlank()) {
            return defaultYear;
        }
        return getCurrentYear();
    }

    private Map<String, List<String>> groupParameters(List<ApiParameter> parameters) {
        Map<String, List<String>> grouped = new LinkedHashMap<>();
        for (ApiParameter parameter : parameters) {
            grouped.computeIfAbsent(parameter.name(), ignored -> new ArrayList<>()).add(parameter.value());
        }
        return grouped;
    }

    private String requiredString(Map<String, List<String>> parameters, String key) {
        String value = optionalString(parameters, key);
        if (!StringUtils.hasText(value)) {
            throw new IllegalArgumentException("Missing required parameter: " + key);
        }
        return value;
    }

    private String optionalString(Map<String, List<String>> parameters, String key) {
        List<String> values = parameters.get(key);
        if (values == null || values.isEmpty()) {
            return null;
        }
        String value = values.get(0);
        return StringUtils.hasText(value) ? value : null;
    }

    private Integer optionalInteger(Map<String, List<String>> parameters, String key) {
        String value = optionalString(parameters, key);
        if (!StringUtils.hasText(value)) {
            return null;
        }
        return Integer.valueOf(value);
    }

    private List<String> requiredList(Map<String, List<String>> parameters, String key) {
        List<String> values = listValues(parameters, key);
        if (values.isEmpty()) {
            throw new IllegalArgumentException("Missing required parameter: " + key);
        }
        return values;
    }

    private List<String> listValues(Map<String, List<String>> parameters, String key) {
        List<String> values = parameters.get(key);
        if (values == null || values.isEmpty()) {
            return List.of();
        }
        return values.stream()
                .filter(StringUtils::hasText)
                .flatMap(value -> Arrays.stream(value.split(",")))
                .map(String::trim)
                .filter(StringUtils::hasText)
                .toList();
    }

    private Map<String, Object> filters(Object... kv) {
        Map<String, Object> out = new LinkedHashMap<>();
        for (int i = 0; i < kv.length; i += 2) {
            Object value = kv[i + 1];
            if (value != null) {
                out.put(String.valueOf(kv[i]), value);
            }
        }
        return out;
    }

    private void requireAnyFilter(String queryName, Object... filters) {
        boolean hasFilter = Arrays.stream(filters).anyMatch(value -> {
            if (value instanceof String stringValue) {
                return StringUtils.hasText(stringValue);
            }
            return value != null;
        });
        if (!hasFilter) {
            throw new IllegalArgumentException(
                    "Please ask for a narrower " + queryName + " query. Include at least one specific filter such as category, phase, or region.");
        }
    }

    private Integer fetchCountryOverallPhaseValue(String countryId, String year) {
        CountryPhase countryPhase = countryPhaseRepository.findByCountryPhaseIdCountryIdAndCountryPhaseIdYear(countryId, year);
        return countryPhase == null ? null : countryPhase.getCountryOverallPhase();
    }

    private String resolveLatestRegionalYear(String regionId) {
        List<String> years = regionService.fetchYearsForARegion(regionId, 1);
        if (years != null && !years.isEmpty() && StringUtils.hasText(years.get(0))) {
            return years.get(0);
        }
        return resolveYear(null);
    }

    private void translateSummaryCountryName(CountrySummaryDto dto, String countryId, LanguageCode languageCode) {
        if (languageCode == null || languageCode == LanguageCode.en) {
            return;
        }

        String translatedCountryName = countryNameTranslator.getCountryTranslationForLanguage(languageCode, countryId);
        if (StringUtils.hasText(translatedCountryName)) {
            dto.translateCountryName(translatedCountryName);
        }
    }

    private BedrockCountryPhaseData buildCountryPhaseData(String countryId, String year, LanguageCode languageCode) {
        CountryPhase countryPhase = countryPhaseRepository.findByCountryPhaseIdCountryIdAndCountryPhaseIdYear(countryId, year);
        return buildCountryPhaseData(countryId, year, languageCode, countryPhase);
    }

    private BedrockCountryPhaseData buildCountryPhaseData(String countryId, String year, LanguageCode languageCode,
                                                          CountryPhase countryPhase) {
        Country country = countryRepository.findById(countryId);
        if (country == null) {
            return null;
        }

        String translatedName = country.getName();
        if (languageCode != null && languageCode != LanguageCode.en) {
            String candidate = countryNameTranslator.getCountryTranslationForLanguage(languageCode, countryId);
            if (StringUtils.hasText(candidate)) {
                translatedName = candidate;
            }
        }

        Integer countryOverallPhase = countryPhase == null ? null : countryPhase.getCountryOverallPhase();
        return new BedrockCountryPhaseData(
                country.getId(),
                translatedName,
                country.getAlpha2Code(),
                year,
                countryOverallPhase,
                BedrockCountryPhaseData.phaseLabelFor(countryOverallPhase)
        );
    }
}
