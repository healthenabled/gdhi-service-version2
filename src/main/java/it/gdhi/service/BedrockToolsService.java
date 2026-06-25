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
        String effectiveYear = requireCountryYear(year, countryId, "country summary data");
        CountrySummaryDto dto = countryService.fetchCountrySummary(countryId, effectiveYear);
        translateSummaryCountryName(dto, countryId, languageCode);
        Integer countryPhase = fetchCountryOverallPhaseValue(countryId, effectiveYear);
        BedrockCountrySummaryData data = BedrockCountrySummaryData.from(dto, effectiveYear, countryPhase);
        return BedrockToolResponse.ok("getCountrySummary",
                "Fetched country summary without personal contact details, including overall phase "
                        + "for the requested year",
                filters("countryId", countryId, "year", effectiveYear, "language", languageCode.name()), data);
    }

    public BedrockToolResponse<BedrockCountryPhaseData> getCountryPhase(
            String countryId, String year, String languageHeader) {
        LanguageCode languageCode = LanguageCode.getValueFor(languageHeader);
        String effectiveYear = requireCountryYear(year, countryId, "country phase data");
        BedrockCountryPhaseData data = buildCountryPhaseData(countryId, effectiveYear, languageCode);
        return BedrockToolResponse.ok("getCountryPhase", "Fetched country overall phase",
                filters("countryId", countryId, "year", effectiveYear, "language", languageCode.name()), data);
    }

    public BedrockToolResponse<CountryHealthScoreDto> getCountryHealthIndicators(
            String countryId, String year, String languageHeader) {
        LanguageCode languageCode = LanguageCode.getValueFor(languageHeader);
        String effectiveYear = requireCountryYear(year, countryId, "country health indicator scores");
        CountryHealthScoreDto dto = countryHealthIndicatorService.fetchCountryHealthScore(countryId, languageCode,
                effectiveYear);
        return BedrockToolResponse.ok("getCountryHealthIndicators", "Fetched country health indicators",
                filters("countryId", countryId, "year", effectiveYear, "language", languageCode.name()), dto);
    }

    public BedrockToolResponse<GlobalHealthScoreDto> getGlobalHealthIndicators(
            Integer categoryId, Integer phase, String regionId, String year, String languageHeader) {
        String effectiveRegionId = RegionAlias.normalize(regionId);
        requireAnyFilter("global health indicators", categoryId, phase, effectiveRegionId);
        LanguageCode languageCode = LanguageCode.getValueFor(languageHeader);
        String effectiveYear = requireYear(year, "global health indicator data");
        GlobalHealthScoreDto dto = effectiveRegionId == null
                ? countryHealthIndicatorService.getGlobalHealthIndicator(categoryId, phase, languageCode,
                        effectiveYear)
                : regionService.fetchRegionalHealthScores(categoryId, effectiveRegionId, languageCode,
                        effectiveYear);
        return BedrockToolResponse.ok("getGlobalHealthIndicators", "Fetched global health indicators",
                filters("categoryId", categoryId, "phase", phase, "regionId", effectiveRegionId, "year", effectiveYear,
                        "language", languageCode.name()), dto);
    }

    public BedrockToolResponse<CountriesHealthScoreDto> getCountriesHealthIndicatorScores(
            Integer categoryId, Integer phase, String year, String languageHeader) {
        requireAnyFilter("country score comparisons", categoryId, phase);
        LanguageCode languageCode = LanguageCode.getValueFor(languageHeader);
        String effectiveYear = requireYear(year, "country health indicator score comparisons");
        CountriesHealthScoreDto dto = countryHealthIndicatorService.fetchCountriesHealthScores(categoryId, phase,
                languageCode, effectiveYear);
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
        String effectiveYear = requireYear(year, "countries by phase data");
        List<CountryPhase> countryPhases = countryPhaseRepository.findByCountryPhaseIdYearAndCountryOverallPhase(
                effectiveYear, phase);
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
        requireYearRange(startYear, endYear, "country phase trend analysis");
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
        String effectiveYear = requireYear(year, "country ranking data");
        List<BedrockCountryRankingData> rankings = gdhmAnalyticsService.rankCountries(
                effectiveRegionId, countryIds, categoryId, indicatorId, effectiveYear, minPhase, maxPhase,
                effectiveSort, limit, secondaryCategoryId, secondaryIndicatorId, secondaryMinPhase, secondaryMaxPhase);
        return BedrockToolResponse.ok("rankCountries", "Ranked countries by GDHM score filters",
                filters("regionId", effectiveRegionId, "countryId", countryIds, "categoryId", categoryId,
                        "indicatorId", indicatorId, "year", effectiveYear, "minPhase", minPhase, "maxPhase", maxPhase,
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
        String effectiveYear = requireYear(year, "data completeness analysis");
        String effectiveRegionId = RegionAlias.normalize(regionId);
        List<BedrockDataCompletenessData> data = gdhmAnalyticsService.analyzeDataCompleteness(
                analysisType, effectiveYear, effectiveRegionId, phase, limit);
        return BedrockToolResponse.ok("analyzeDataCompleteness", "Analyzed GDHM data completeness",
                filters("analysisType", analysisType, "year", effectiveYear, "regionId", effectiveRegionId, "phase",
                        phase, "limit", limit),
                data);
    }

    public BedrockToolResponse<?> executeApiInvocation(ApiInvocationInput apiInvocationInput) {
        if (!"GET".equalsIgnoreCase(apiInvocationInput.httpMethod())) {
            throw new IllegalArgumentException("Unsupported Bedrock tool HTTP method: "
                    + apiInvocationInput.httpMethod());
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
        String message = StringUtils.hasText(ex.getMessage())
                ? ex.getMessage()
                : "The request needs more specific filters before I can safely run it.";
        Map<String, Object> data = filters("error", message);
        if (ex instanceof MissingYearException missingYearException) {
            data.putAll(missingYearException.details());
        }
        return new BedrockToolResponse<>(
                "validation",
                "error",
                message,
                filters(),
                data,
                Instant.now()
        );
    }

    public BedrockToolResponse<Map<String, Object>> serverError(Exception ex) {
        return new BedrockToolResponse<>(
                "server",
                "error",
                "Tool execution failed.",
                filters(),
                filters("error", "An internal error occurred."),
                Instant.now()
        );
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
        try {
            return Integer.valueOf(value);
        }
        catch (NumberFormatException ex) {
            throw new IllegalArgumentException("Invalid integer value for '" + key + "': " + value, ex);
        }
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
                    "Please ask for a narrower " + queryName + " query. Include at least one specific filter such as "
                            + "category, phase, or region.");
        }
    }

    private String requireYear(String year, String queryName) {
        if (StringUtils.hasText(year)) {
            return year.trim();
        }
        throw new IllegalArgumentException("A year is required for " + queryName
                + ". Ask the user which year they want before returning GDHM figures.");
    }

    private String requireCountryYear(String year, String countryId, String queryName) {
        if (StringUtils.hasText(year)) {
            return year.trim();
        }
        List<String> countryYears = countryPhaseRepository.findByCountryPhaseIdOrderByYearDesc(countryId,
                defaultLimit);
        List<String> availableYears = sortYears(countryYears == null ? List.of() : countryYears);
        String countryName = countryName(countryId);
        String countryLabel = StringUtils.hasText(countryName) ? countryName : countryId;
        String message = availableYears.isEmpty()
                ? "A year is required for " + queryName + ". Ask the user which year they want before returning "
                        + "GDHM figures."
                : "A year is required for " + queryName + ". Available years for " + countryLabel + " are: "
                        + String.join(", ", availableYears) + ". Ask the user which year they want before returning "
                        + "GDHM figures.";
        throw new MissingYearException(message, filters(
                "missingParameter", "year",
                "countryId", countryId,
                "countryName", countryName,
                "availableYears", availableYears));
    }

    private void requireYearRange(String startYear, String endYear, String queryName) {
        if (StringUtils.hasText(startYear) || StringUtils.hasText(endYear)) {
            return;
        }
        throw new IllegalArgumentException("A year or year range is required for " + queryName
                + ". Ask the user which year or range they want before returning GDHM figures.");
    }

    private Integer fetchCountryOverallPhaseValue(String countryId, String year) {
        CountryPhase countryPhase = countryPhaseRepository.findByCountryPhaseIdCountryIdAndCountryPhaseIdYear(
                countryId, year);
        return countryPhase == null ? null : countryPhase.getCountryOverallPhase();
    }

    private String countryName(String countryId) {
        Country country = countryRepository.findById(countryId);
        return country == null ? null : country.getName();
    }

    private List<String> sortYears(List<String> years) {
        return years.stream().sorted((left, right) -> yearSortValue(left).compareTo(yearSortValue(right))).toList();
    }

    private Integer yearSortValue(String year) {
        if (year != null && year.matches("\\d{4}")) {
            return Integer.valueOf(year);
        }
        return 0;
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
        CountryPhase countryPhase = countryPhaseRepository.findByCountryPhaseIdCountryIdAndCountryPhaseIdYear(
                countryId, year);
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

    private static class MissingYearException extends IllegalArgumentException {

        private final Map<String, Object> details;

        MissingYearException(String message, Map<String, Object> details) {
            super(message);
            this.details = details;
        }

        private Map<String, Object> details() {
            return details;
        }
    }
}
