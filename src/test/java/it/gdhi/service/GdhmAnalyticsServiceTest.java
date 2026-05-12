package it.gdhi.service;

import it.gdhi.ai.dto.BedrockCountryPhaseTrendData;
import it.gdhi.ai.dto.BedrockCountryRankingData;
import it.gdhi.ai.dto.BedrockDataCompletenessData;
import it.gdhi.model.Category;
import it.gdhi.model.Country;
import it.gdhi.model.CountryHealthIndicator;
import it.gdhi.model.CountryPhase;
import it.gdhi.model.Indicator;
import it.gdhi.model.RegionCountry;
import it.gdhi.model.RegionCountryId;
import it.gdhi.model.id.CountryHealthIndicatorId;
import it.gdhi.repository.ICountryHealthIndicatorRepository;
import it.gdhi.repository.ICountryPhaseRepository;
import it.gdhi.repository.ICountryRepository;
import it.gdhi.repository.IRegionCountryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static it.gdhi.utils.FormStatus.PUBLISHED;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GdhmAnalyticsServiceTest {

    @Mock
    private ICountryPhaseRepository countryPhaseRepository;

    @Mock
    private ICountryHealthIndicatorRepository countryHealthIndicatorRepository;

    @Mock
    private ICountryRepository countryRepository;

    @Mock
    private IRegionCountryRepository regionCountryRepository;

    private GdhmAnalyticsService service;

    @BeforeEach
    public void setUp() {
        service = new GdhmAnalyticsService(countryPhaseRepository, countryHealthIndicatorRepository,
                countryRepository, regionCountryRepository);
    }

    @Test
    public void shouldRejectUnboundedOverallTrendQuery() {
        assertThrows(IllegalArgumentException.class, () -> service.analyzeCountryPhaseTrends(
                null, null, null, null, null, null, "submitted", null, null));

        verify(countryPhaseRepository, never()).findAll();
    }

    @Test
    public void shouldUseScopedCountryHistoryForCountrySpecificTrendQuery() {
        when(countryPhaseRepository.findByCountryPhaseIdCountryIdIn(List.of("BRA"))).thenReturn(List.of(
                new CountryPhase("BRA", 2, "2020"),
                new CountryPhase("BRA", 4, "2023")));
        when(countryRepository.findByIdIn(List.of("BRA"))).thenReturn(List.of(new Country("BRA", "Brazil", null,
                "BR")));

        List<BedrockCountryPhaseTrendData> trends = service.analyzeCountryPhaseTrends(
                null, "BRA", null, null, null, null, "submitted", null, null);

        assertEquals(1, trends.size());
        assertEquals(2, trends.get(0).phaseChange());
        assertEquals(List.of("2020", "2023"), trends.get(0).submissionYears());
        verify(countryPhaseRepository).findByCountryPhaseIdCountryIdIn(List.of("BRA"));
        verify(countryPhaseRepository, never()).findAll();
    }

    @Test
    public void shouldRejectUnboundedHealthIndicatorTrendQuery() {
        assertThrows(IllegalArgumentException.class, () -> service.analyzeCountryPhaseTrends(
                null, null, 5, null, null, null, "submitted", null, null));

        verify(countryHealthIndicatorRepository, never()).findByStatus(PUBLISHED.name());
    }

    @Test
    public void shouldRankScopedCountriesInRequestedOrderDirection() {
        List<String> countryIds = List.of("BRA", "ZAF");
        when(countryHealthIndicatorRepository.findLatestByCountryAndCategoryAndStatus(null, 5, PUBLISHED.name()))
                .thenReturn(List.of(
                indicator("BRA", 5, 14, "2024", 5),
                indicator("ZAF", 5, 14, "2024", 2)));
        when(countryRepository.findByIdIn(countryIds)).thenReturn(List.of(
                new Country("BRA", "Brazil", null, "BR"),
                new Country("ZAF", "South Africa", null, "ZA")));
        when(regionCountryRepository.findByRegionCountryIdCountryId("BRA"))
                .thenReturn(regionCountry("PAHO", "BRA"));
        when(regionCountryRepository.findByRegionCountryIdCountryId("ZAF"))
                .thenReturn(regionCountry("AFRO", "ZAF"));

        List<BedrockCountryRankingData> highest = service.rankCountries(
                null, countryIds, 5, null, null, null, null, "highest", null, null, null, null, null);
        List<BedrockCountryRankingData> lowest = service.rankCountries(
                null, countryIds, 5, null, null, null, null, "lowest", null, null, null, null, null);

        assertEquals("Brazil", highest.get(0).countryName());
        assertEquals("South Africa", lowest.get(0).countryName());
    }

    @Test
    public void shouldCountNullAndMinusOneScoresAsMissingData() {
        when(countryHealthIndicatorRepository.findLatestByCountryAndCategoryAndStatus(null, null, PUBLISHED.name()))
                .thenReturn(List.of(
                        indicator("BRA", 5, 14, "2024", null),
                        indicator("BRA", 5, 15, "2024", -1),
                        indicator("BRA", 5, 16, "2024", 3)));
        when(countryRepository.findAll()).thenReturn(List.of(new Country("BRA", "Brazil", null, "BR")));
        when(regionCountryRepository.findAll()).thenReturn(List.of(regionCountry("PAHO", "BRA")));

        List<BedrockDataCompletenessData> data = service.analyzeDataCompleteness(
                "missing_country_data", null, null, null, null);

        assertEquals(1, data.size());
        assertEquals(2, data.get(0).missingIndicatorCount());
        assertEquals(3, data.get(0).totalIndicatorCount());
        assertEquals("2024", data.get(0).year());
    }

    @Test
    public void shouldRejectUnknownAndUnsupportedCompletenessTypes() {
        assertThrows(IllegalArgumentException.class,
                () -> service.analyzeDataCompleteness("missing", null, null, null, null));
        assertThrows(IllegalArgumentException.class,
                () -> service.analyzeDataCompleteness("proxy_only", null, null, null, null));
        assertEquals(List.of("missing_country_data", "phase_count_by_indicator"),
                service.supportedDataCompletenessAnalysisTypes());
    }

    private CountryHealthIndicator indicator(String countryId, Integer categoryId, Integer indicatorId, String year,
                                             Integer score) {
        return new CountryHealthIndicator(
                new CountryHealthIndicatorId(countryId, categoryId, indicatorId, year),
                score,
                new Indicator(indicatorId, "Indicator " + indicatorId, "Definition", indicatorId),
                new Category(categoryId, "Standards & Interoperability"),
                PUBLISHED.name());
    }

    private RegionCountry regionCountry(String regionId, String countryId) {
        return new RegionCountry(new RegionCountryId(regionId, countryId));
    }
}
