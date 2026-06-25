package it.gdhi.service;

import it.gdhi.ai.dto.BedrockCountrySummaryData;
import it.gdhi.ai.dto.BedrockToolResponse;
import it.gdhi.ai.dto.BedrockCountryPhaseData;
import it.gdhi.dto.CountriesHealthScoreDto;
import it.gdhi.dto.GlobalHealthScoreDto;
import it.gdhi.dto.PhaseDto;
import it.gdhi.dto.CountryHealthScoreDto;
import it.gdhi.dto.CountrySummaryDto;
import it.gdhi.internationalization.service.CountryNameTranslator;
import it.gdhi.model.Country;
import it.gdhi.model.CountryPhase;
import it.gdhi.model.Region;
import it.gdhi.repository.ICountryPhaseRepository;
import it.gdhi.repository.ICountryRepository;
import it.gdhi.utils.LanguageCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import software.amazon.awssdk.services.bedrockagentruntime.model.ApiInvocationInput;
import software.amazon.awssdk.services.bedrockagentruntime.model.ApiParameter;

import java.util.List;
import java.util.Map;

import static it.gdhi.utils.ApplicationConstants.defaultLimit;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BedrockToolsServiceTest {

    @Mock
    private CountryService countryService;

    @Mock
    private CountryHealthIndicatorService countryHealthIndicatorService;

    @Mock
    private RegionService regionService;

    @Mock
    private CategoryIndicatorService categoryIndicatorService;

    @Mock
    private PhaseService phaseService;

    @Mock
    private DefaultYearDataService defaultYearDataService;

    @Mock
    private ICountryPhaseRepository countryPhaseRepository;

    @Mock
    private ICountryRepository countryRepository;

    @Mock
    private CountryNameTranslator countryNameTranslator;

    @Mock
    private GdhmAnalyticsService gdhmAnalyticsService;

    private BedrockToolsService service;

    @BeforeEach
    public void setUp() {
        service = new BedrockToolsService(countryService, countryHealthIndicatorService, regionService,
                categoryIndicatorService, phaseService, defaultYearDataService, countryPhaseRepository,
                countryRepository, countryNameTranslator, gdhmAnalyticsService);
    }

    @Test
    public void shouldTranslateCountrySummaryCountryNameForRequestedLanguage() {
        CountrySummaryDto summary = CountrySummaryDto.builder()
                .countryId("IND")
                .countryName("India")
                .countryAlpha2Code("IN")
                .summary("Summary")
                .build();
        when(countryService.fetchCountrySummary("IND", "2024")).thenReturn(summary);
        when(countryNameTranslator.getCountryTranslationForLanguage(LanguageCode.fr, "IND")).thenReturn("Inde");

        BedrockToolResponse<BedrockCountrySummaryData> response = service.getCountrySummary("IND", "2024", "fr");

        assertEquals("Inde", response.data().countryName());
        assertEquals("fr", response.filters().get("language"));
        verify(countryNameTranslator).getCountryTranslationForLanguage(LanguageCode.fr, "IND");
    }

    @Test
    public void shouldRejectCountrySummaryWhenYearIsMissing() {
        when(countryPhaseRepository.findByCountryPhaseIdOrderByYearDesc("KEN", defaultLimit))
                .thenReturn(List.of("2026", "2025", "2024"));
        when(countryRepository.findById("KEN")).thenReturn(new Country("KEN", "Kenya", null, "KE"));

        IllegalArgumentException ex = assertRequiresYear(
                () -> service.getCountrySummary("KEN", null, "en"));

        assertTrue(ex.getMessage().contains("country summary data"));
        assertTrue(ex.getMessage().contains("Available years for Kenya are: 2024, 2025, 2026"));
        verify(countryPhaseRepository, never()).findLatestByCountryId("KEN");
    }

    @Test
    public void shouldRejectCountryPhaseWhenYearIsMissing() {
        IllegalArgumentException ex = assertRequiresYear(
                () -> service.getCountryPhase("KEN", null, "en"));

        assertTrue(ex.getMessage().contains("country phase data"));
        verify(countryPhaseRepository, never()).findLatestByCountryId("KEN");
    }

    @Test
    public void shouldRejectCountryHealthIndicatorsWhenYearIsMissing() {
        IllegalArgumentException ex = assertRequiresYear(
                () -> service.getCountryHealthIndicators("KEN", null, "en"));

        assertTrue(ex.getMessage().contains("country health indicator scores"));
        verify(countryHealthIndicatorService, never()).fetchLatestCountryHealthScore("KEN", LanguageCode.en);
    }

    @Test
    public void shouldDispatchCountryPhaseTrendAnalyticsTool() {
        ApiInvocationInput input = ApiInvocationInput.builder()
                .httpMethod("GET")
                .apiPath("/analytics/country-phase-trends")
                .parameters(List.of(
                        ApiParameter.builder().name("regionId").value("African region").type("string").build(),
                        ApiParameter.builder().name("categoryId").value("1").type("integer").build(),
                        ApiParameter.builder().name("indicatorId").value("4").type("integer").build(),
                        ApiParameter.builder().name("startYear").value("2020").type("string").build(),
                        ApiParameter.builder().name("endYear").value("2024").type("string").build(),
                        ApiParameter.builder().name("direction").value("advanced").type("string").build()))
                .build();

        service.executeApiInvocation(input);

        verify(gdhmAnalyticsService).analyzeCountryPhaseTrends("AFRO", null, 1, 4, "2020", "2024",
                "advanced", null, null);
    }

    @Test
    public void shouldRejectCountryPhaseTrendWhenYearRangeIsMissing() {
        IllegalArgumentException ex = assertRequiresYearRange(() -> service.analyzeCountryPhaseTrends(
                "AFRO", null, 1, 4, null, null, "advanced", null, null));

        assertTrue(ex.getMessage().contains("country phase trend analysis"));
    }

    @Test
    public void shouldDispatchCountryRankingAnalyticsToolWithMultipleCountryIds() {
        ApiInvocationInput input = ApiInvocationInput.builder()
                .httpMethod("GET")
                .apiPath("/analytics/country-rankings")
                .parameters(List.of(
                        ApiParameter.builder().name("countryId").value("BRA").type("string").build(),
                        ApiParameter.builder().name("countryId").value("ZAF").type("string").build(),
                        ApiParameter.builder().name("countryIds").value("KEN,GHA").type("string").build(),
                        ApiParameter.builder().name("categoryId").value("5").type("integer").build(),
                        ApiParameter.builder().name("year").value("2024").type("string").build(),
                        ApiParameter.builder().name("sort").value("highest").type("string").build()))
                .build();

        service.executeApiInvocation(input);

        verify(gdhmAnalyticsService).rankCountries(null, List.of("BRA", "ZAF", "KEN", "GHA"), 5, null, "2024",
                null, null, "highest", null, null, null, null, null);
    }

    @Test
    public void shouldRejectCountryRankingWhenYearIsMissing() {
        IllegalArgumentException ex = assertRequiresYear(() -> service.rankCountries(null, List.of("KEN"),
                5, null, null, null, null, "highest", null, null, null, null, null));

        assertTrue(ex.getMessage().contains("country ranking data"));
    }

    @Test
    public void shouldRejectUnsupportedHttpMethods() {
        ApiInvocationInput input = ApiInvocationInput.builder()
                .httpMethod("POST")
                .apiPath("/countries")
                .parameters(List.of())
                .build();

        assertThrows(IllegalArgumentException.class, () -> service.executeApiInvocation(input));
    }

    @Test
    public void shouldRejectUnsupportedToolPaths() {
        ApiInvocationInput input = ApiInvocationInput.builder()
                .httpMethod("GET")
                .apiPath("/does-not-exist")
                .parameters(List.of())
                .build();

        assertThrows(IllegalArgumentException.class, () -> service.executeApiInvocation(input));
    }

    @Test
    public void shouldRejectMissingRequiredCountryIdForCountryPhaseTool() {
        ApiInvocationInput input = ApiInvocationInput.builder()
                .httpMethod("GET")
                .apiPath("/countries/{id}/phase")
                .parameters(List.of())
                .build();

        assertThrows(IllegalArgumentException.class, () -> service.executeApiInvocation(input));
    }

    @Test
    public void shouldRejectUnboundedGlobalHealthIndicatorsQuery() {
        assertThrows(IllegalArgumentException.class, () -> service.getGlobalHealthIndicators(null, null, null, null,
                "en"));
    }

    @Test
    public void shouldRejectGlobalHealthIndicatorsWhenYearIsMissing() {
        IllegalArgumentException ex = assertRequiresYear(
                () -> service.getGlobalHealthIndicators(1, null, null, null, "en"));

        assertTrue(ex.getMessage().contains("global health indicator data"));
        verify(countryHealthIndicatorService, never()).getLatestGlobalHealthIndicator(1, null, LanguageCode.en);
    }

    @Test
    public void shouldUseRequestedRegionalYearForRegionGlobalIndicators() {
        GlobalHealthScoreDto dto = GlobalHealthScoreDto.builder().overAllScore(2).build();
        when(regionService.fetchRegionalHealthScores(1, "AFRO", LanguageCode.en, "2024")).thenReturn(dto);

        BedrockToolResponse<GlobalHealthScoreDto> response = service.getGlobalHealthIndicators(1, null, "AFRO", "2024",
                "en");

        assertEquals("2024", response.filters().get("year"));
        assertEquals(dto, response.data());
    }

    @Test
    public void shouldUseRequestedYearForCountriesHealthScores() {
        CountriesHealthScoreDto dto = new CountriesHealthScoreDto(List.of(new CountryHealthScoreDto()));
        when(countryHealthIndicatorService.fetchCountriesHealthScores(1, null, LanguageCode.en, "2024"))
                .thenReturn(dto);

        BedrockToolResponse<CountriesHealthScoreDto> response = service.getCountriesHealthIndicatorScores(1, null,
                "2024", "en");

        assertEquals("2024", response.filters().get("year"));
        assertEquals(dto, response.data());
    }

    @Test
    public void shouldRejectCountriesHealthScoresWhenYearIsMissing() {
        IllegalArgumentException ex = assertRequiresYear(
                () -> service.getCountriesHealthIndicatorScores(1, null, null, "en"));

        assertTrue(ex.getMessage().contains("country health indicator score comparisons"));
        verify(countryHealthIndicatorService, never()).fetchCountriesLatestHealthScores(1, null, LanguageCode.en);
    }

    @Test
    public void shouldRejectCountriesByPhaseWhenYearIsMissing() {
        IllegalArgumentException ex = assertRequiresYear(
                () -> service.listCountriesByPhase(2, null, "en"));

        assertTrue(ex.getMessage().contains("countries by phase data"));
        verify(countryPhaseRepository, never()).findByLatestTrueAndCountryOverallPhase(2);
    }

    @Test
    public void shouldUseTranslatedNameWhenBuildingCountryPhaseData() {
        CountryPhase phase = new CountryPhase("KEN", 2, "2024", true);
        when(countryPhaseRepository.findByCountryPhaseIdCountryIdAndCountryPhaseIdYear("KEN", "2024"))
                .thenReturn(phase);
        when(countryRepository.findById("KEN")).thenReturn(new Country("KEN", "Kenya", null, "KE"));
        when(countryNameTranslator.getCountryTranslationForLanguage(LanguageCode.fr, "KEN")).thenReturn("Kenya-fr");

        BedrockToolResponse<BedrockCountryPhaseData> response = service.getCountryPhase("KEN", "2024", "fr");

        assertEquals("Kenya-fr", response.data().countryName());
    }

    @Test
    public void shouldSkipTranslationWhenLanguageIsEnglish() {
        CountrySummaryDto summary = CountrySummaryDto.builder()
                .countryId("KEN")
                .countryName("Kenya")
                .countryAlpha2Code("KE")
                .summary("Summary")
                .build();
        when(countryService.fetchCountrySummary("KEN", "2024")).thenReturn(summary);

        BedrockToolResponse<BedrockCountrySummaryData> response = service.getCountrySummary("KEN", "2024", "en");

        assertEquals("Kenya", response.data().countryName());
        verify(countryNameTranslator, never()).getCountryTranslationForLanguage(LanguageCode.en, "KEN");
    }

    @Test
    public void shouldReturnNullCountryPhaseDataWhenCountryMissing() {
        when(countryPhaseRepository.findByCountryPhaseIdYearAndCountryOverallPhase("2024", 2)).thenReturn(List.of(
                new CountryPhase("XXX", 2, "2024")));
        when(countryRepository.findById("XXX")).thenReturn(null);

        BedrockToolResponse<List<BedrockCountryPhaseData>> response = service.listCountriesByPhase(2, "2024", "en");

        assertEquals(List.of(), response.data());
    }

    @Test
    public void shouldSortCountriesByNameInListCountriesByPhase() {
        when(countryPhaseRepository.findByCountryPhaseIdYearAndCountryOverallPhase("2024", 2)).thenReturn(List.of(
                new CountryPhase("ZAF", 2, "2024"),
                new CountryPhase("BRA", 2, "2024")));
        when(countryRepository.findById("ZAF")).thenReturn(new Country("ZAF", "South Africa", null, "ZA"));
        when(countryRepository.findById("BRA")).thenReturn(new Country("BRA", "Brazil", null, "BR"));

        BedrockToolResponse<List<BedrockCountryPhaseData>> response = service.listCountriesByPhase(2, "2024", "en");

        assertEquals(List.of("Brazil", "South Africa"),
                response.data().stream().map(BedrockCountryPhaseData::countryName).toList());
    }

    @Test
    public void shouldDispatchDataCompletenessAnalyticsTool() {
        ApiInvocationInput input = ApiInvocationInput.builder()
                .httpMethod("GET")
                .apiPath("/analytics/data-completeness")
                .parameters(List.of(
                        ApiParameter.builder().name("analysisType").value("missing_country_data").type("string")
                                .build(),
                        ApiParameter.builder().name("regionId").value("AFRO").type("string").build(),
                        ApiParameter.builder().name("year").value("2024").type("string").build(),
                        ApiParameter.builder().name("limit").value("10").type("integer").build()))
                .build();
        when(gdhmAnalyticsService.analyzeDataCompleteness("missing_country_data", "2024", "AFRO", null, 10))
                .thenReturn(List.of());

        service.executeApiInvocation(input);

        verify(gdhmAnalyticsService).analyzeDataCompleteness("missing_country_data", "2024", "AFRO", null, 10);
    }

    @Test
    public void shouldRejectDataCompletenessWhenYearIsMissing() {
        IllegalArgumentException ex = assertRequiresYear(
                () -> service.analyzeDataCompleteness("missing_country_data", null, "AFRO", null, 10));

        assertTrue(ex.getMessage().contains("data completeness analysis"));
    }

    @Test
    public void shouldDispatchListYearsAndMetadataTools() {
        when(defaultYearDataService.fetchYears()).thenReturn(List.of("2023", "2024"));
        when(phaseService.getPhaseOptions()).thenReturn(List.of(new PhaseDto("Phase 1", 1)));
        when(regionService.fetchRegions(LanguageCode.en)).thenReturn(List.of(new Region("AFRO", "AFRO")));
        when(categoryIndicatorService.getHealthIndicatorOptions(LanguageCode.en)).thenReturn(List.of());

        assertEquals(List.of("2023", "2024"), service.listYears().data());
        assertEquals(1, service.getPhases().data().size());
        assertEquals(1, service.listRegions("en").data().size());
        assertEquals(0, service.getHealthIndicatorOptions("en").data().size());
    }

    @Test
    public void shouldReturnSpecificValidationMessage() {
        when(countryPhaseRepository.findByCountryPhaseIdOrderByYearDesc("KEN", defaultLimit))
                .thenReturn(List.of("2026", "2025", "2024"));
        when(countryRepository.findById("KEN")).thenReturn(new Country("KEN", "Kenya", null, "KE"));
        IllegalArgumentException ex = assertRequiresYear(
                () -> service.getCountrySummary("KEN", null, "en"));

        BedrockToolResponse<?> response = service.validationError(ex);
        Map<?, ?> data = (Map<?, ?>) response.data();

        assertEquals(ex.getMessage(), response.message());
        assertEquals("KEN", data.get("countryId"));
        assertEquals("Kenya", data.get("countryName"));
        assertEquals(List.of("2024", "2025", "2026"), data.get("availableYears"));
    }

    private IllegalArgumentException assertRequiresYear(Executable executable) {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, executable);
        assertTrue(ex.getMessage().contains("A year is required"));
        assertTrue(ex.getMessage().contains("Ask the user which year they want"));
        return ex;
    }

    private IllegalArgumentException assertRequiresYearRange(Executable executable) {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, executable);
        assertTrue(ex.getMessage().contains("A year or year range is required"));
        assertTrue(ex.getMessage().contains("Ask the user which year or range they want"));
        return ex;
    }
}
