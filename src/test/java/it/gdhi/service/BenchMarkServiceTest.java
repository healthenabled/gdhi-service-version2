package it.gdhi.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import it.gdhi.dto.BenchmarkDto;
import it.gdhi.model.Country;
import it.gdhi.model.CountryHealthIndicator;
import it.gdhi.model.CountryPhase;
import it.gdhi.model.Indicator;
import it.gdhi.model.IndicatorScore;
import it.gdhi.model.RegionalIndicatorData;
import it.gdhi.model.id.CountryPhaseId;
import it.gdhi.model.id.RegionalIndicatorId;
import it.gdhi.repository.ICountryHealthIndicatorRepository;
import it.gdhi.repository.ICountryPhaseRepository;
import it.gdhi.repository.IRegionalIndicatorDataRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static it.gdhi.utils.FormStatus.PUBLISHED;
import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BenchMarkServiceTest {

    @Mock
    ICountryHealthIndicatorRepository iCountryHealthIndicatorRepository;

    @Mock
    ICountryPhaseRepository iCountryPhaseRepository;

    @Mock
    IRegionalIndicatorDataRepository iRegionalIndicatorDataRepository;

    @InjectMocks
    BenchMarkService benchMarkService;

    @Test
    public void shouldGetGlobalBenchmarkDataForACountry() {
        String countryId = "IND";
        Integer benchmarkType = -1;
        Integer indicatorId1 = 1;
        Integer indicatorId2 = 2;
        Integer indicatorId3 = 3;
        String year = "Version1";

        CountryHealthIndicator countryHealthIndicator1 = buildCountryHealthIndicator(indicatorId1, "PAK", 2);
        CountryHealthIndicator countryHealthIndicator2 = buildCountryHealthIndicator(indicatorId2, "AFG", 1);
        CountryHealthIndicator countryHealthIndicator3 = buildCountryHealthIndicator(indicatorId3, "IRE", 1);

        CountryHealthIndicator countryHealthIndicatorForInd1 = buildCountryHealthIndicator(indicatorId1, countryId, 1);
        CountryHealthIndicator countryHealthIndicatorForInd2 = buildCountryHealthIndicator(indicatorId2, countryId, 1);
        CountryHealthIndicator countryHealthIndicatorForInd3 = buildCountryHealthIndicator(indicatorId3, countryId, 3);

        when(iCountryHealthIndicatorRepository.findByCountryHealthIndicatorIdYearAndStatus(year, PUBLISHED.name()))
                .thenReturn(asList(countryHealthIndicator1, countryHealthIndicator2, countryHealthIndicator3,
                        countryHealthIndicatorForInd1, countryHealthIndicatorForInd2, countryHealthIndicatorForInd3));

        when(iCountryHealthIndicatorRepository.findByCountryHealthIndicatorIdCountryIdAndCountryHealthIndicatorIdYearAndStatus(countryId, year, PUBLISHED.name()))
                .thenReturn(
                        asList(countryHealthIndicatorForInd1, countryHealthIndicatorForInd2, countryHealthIndicatorForInd3
                        ));

        Map<Integer, BenchmarkDto> expectedBenchMark = new HashMap<>();
        expectedBenchMark.put(indicatorId1, new BenchmarkDto(2));
        expectedBenchMark.put(indicatorId2, new BenchmarkDto(1));
        expectedBenchMark.put(indicatorId3, new BenchmarkDto(2));

        Map<Integer, BenchmarkDto> benchmarkFor = benchMarkService.getBenchmarkFor(countryId, benchmarkType, year);

        assertThat(expectedBenchMark.get(indicatorId1)).usingRecursiveComparison().isEqualTo(benchmarkFor.get(indicatorId1));
        assertThat(expectedBenchMark.get(indicatorId2)).usingRecursiveComparison().isEqualTo(benchmarkFor.get(indicatorId2));
        assertThat(expectedBenchMark.get(indicatorId3)).usingRecursiveComparison().isEqualTo(benchmarkFor.get(indicatorId3));
    }

    @Test
    public void shouldGetRegionalBenchmarkDataForACountry() {
        Integer indicatorId1 = 1;
        Integer indicatorId2 = 2;
        String regionId = "PAHO";
        String year = "2023";
        String countryId = "IND";
        RegionalIndicatorId regionalIndicatorId1 = RegionalIndicatorId.builder().regionId(regionId).indicatorId(1).year(year).build();
        RegionalIndicatorData regionalIndicatorData1 = RegionalIndicatorData.builder().regionalIndicatorId(regionalIndicatorId1).score(3).build();
        RegionalIndicatorId regionalIndicatorId2 = RegionalIndicatorId.builder().regionId(regionId).indicatorId(2).year(year).build();
        RegionalIndicatorData regionalIndicatorData2 = RegionalIndicatorData.builder().regionalIndicatorId(regionalIndicatorId2).score(4).build();
        when(iRegionalIndicatorDataRepository.findByRegionalIndicatorIdRegionIdAndRegionalIndicatorIdYear(regionId, year)).thenReturn(asList(regionalIndicatorData1, regionalIndicatorData2));
        CountryHealthIndicator countryHealthIndicatorForInd1 = buildCountryHealthIndicator(indicatorId1, countryId, -1);
        CountryHealthIndicator countryHealthIndicatorForInd2 = buildCountryHealthIndicator(indicatorId2, countryId, -1);
        when(iCountryHealthIndicatorRepository.findByCountryHealthIndicatorIdCountryIdAndCountryHealthIndicatorIdYearAndStatus(countryId, year, PUBLISHED.name()))
                .thenReturn(
                        asList(countryHealthIndicatorForInd1, countryHealthIndicatorForInd2
                        ));

        Map<Integer, BenchmarkDto> expectedBenchMark = new HashMap<>();
        expectedBenchMark.put(indicatorId1, new BenchmarkDto(3));
        expectedBenchMark.put(indicatorId2, new BenchmarkDto(4));
        Map<Integer, BenchmarkDto> actualBenchMark = benchMarkService.getBenchMarkForRegion(countryId, year, regionId);

        assertThat(expectedBenchMark.get(indicatorId1)).usingRecursiveComparison().isEqualTo(actualBenchMark.get(indicatorId1));
        assertThat(expectedBenchMark.get(indicatorId2)).usingRecursiveComparison().isEqualTo(actualBenchMark.get(indicatorId2));
    }

    @Test
    public void shouldGetGlobalBenchmarkForACountryWithSomeCountryHavingNA() {
        String countryId = "IND";
        Integer benchmarkType = -1;
        Integer indicatorId1 = 1;
        Integer indicatorId2 = 2;
        Integer indicatorId3 = 3;
        String year = "Version1";

        CountryHealthIndicator countryHealthIndicator1 = buildCountryHealthIndicator(indicatorId1, "PAK", -1);
        CountryHealthIndicator countryHealthIndicator2 = buildCountryHealthIndicator(indicatorId2, "AFG", 1);
        CountryHealthIndicator countryHealthIndicator3 = buildCountryHealthIndicator(indicatorId3, "IRE", 1);

        CountryHealthIndicator countryHealthIndicatorForInd1 = buildCountryHealthIndicator(indicatorId1, countryId, -1);
        CountryHealthIndicator countryHealthIndicatorForInd2 = buildCountryHealthIndicator(indicatorId2, countryId, -1);

        when(iCountryHealthIndicatorRepository.findByCountryHealthIndicatorIdYearAndStatus(year, PUBLISHED.name()))
                .thenReturn(asList(countryHealthIndicator1, countryHealthIndicator2,
                        countryHealthIndicatorForInd1, countryHealthIndicatorForInd2, countryHealthIndicator3));

        when(iCountryHealthIndicatorRepository.findByCountryHealthIndicatorIdCountryIdAndCountryHealthIndicatorIdYearAndStatus(countryId, year, PUBLISHED.name()))
                .thenReturn(
                        asList(countryHealthIndicatorForInd1, countryHealthIndicatorForInd2
                        ));

        Map<Integer, BenchmarkDto> benchmarkFor = benchMarkService.getBenchmarkFor(countryId, benchmarkType, year);

        assertEquals(2, benchmarkFor.size());

    }

    @Test
    public void shouldGetGlobalBenchmarkForACountryOnlyUsingMainIndicators() {
        String countryId = "IND";
        Integer benchmarkType = -1;
        Integer indicatorId1 = 1;
        Integer indicatorId2 = 2;
        Integer indicatorId3 = 3;
        String year = "Version1";

        CountryHealthIndicator countryHealthIndicator2 = buildCountryHealthIndicator(indicatorId2, "AFG", 1);

        CountryHealthIndicator countryHealthIndicatorForInd1 = buildCountryHealthIndicator(indicatorId1, countryId, 1);
        CountryHealthIndicator countryHealthIndicatorForInd2 = buildCountryHealthIndicator(indicatorId2, countryId, 1);

        CountryHealthIndicator countryHealthIndicatorForInd3 = CountryHealthIndicator.builder()
                .indicator(new Indicator(indicatorId3, "indicator 3", "code",
                        3, 1, new ArrayList<IndicatorScore>(), "some definition"))
                .country(new Country(countryId, "India", UUID.randomUUID(), "PK"))
                .score(1).build();


        when(iCountryHealthIndicatorRepository.findByCountryHealthIndicatorIdYearAndStatus(year, PUBLISHED.name()))
                .thenReturn(asList(countryHealthIndicatorForInd3, countryHealthIndicator2,
                        countryHealthIndicatorForInd1, countryHealthIndicatorForInd2));

        when(iCountryHealthIndicatorRepository.findByCountryHealthIndicatorIdCountryIdAndCountryHealthIndicatorIdYearAndStatus(countryId, year, PUBLISHED.name()))
                .thenReturn(
                        asList(countryHealthIndicatorForInd1, countryHealthIndicatorForInd2
                        ));

        Map<Integer, BenchmarkDto> expectedBenchMark = new HashMap<>();
        expectedBenchMark.put(indicatorId1, new BenchmarkDto(1));
        expectedBenchMark.put(indicatorId2, new BenchmarkDto(1));

        Map<Integer, BenchmarkDto> benchmarkFor = benchMarkService.getBenchmarkFor(countryId, benchmarkType, year);

        assertThat(expectedBenchMark.get(indicatorId1)).usingRecursiveComparison().isEqualTo(benchmarkFor.get(indicatorId1));
        assertThat(expectedBenchMark.get(indicatorId2)).usingRecursiveComparison().isEqualTo(benchmarkFor.get(indicatorId2));
    }

    @Test
    public void shouldGetBenchmarkDataForACountryForSelectedPhase() {
        String countryId = "IND";
        Integer benchmarkType = 2;
        Integer indicatorId1 = 1;
        Integer indicatorId2 = 2;
        Integer indicatorId3 = 3;
        String year = "Version1";

        CountryHealthIndicator countryHealthIndicator1 = buildCountryHealthIndicator(indicatorId1, "PAK", 2);
        CountryHealthIndicator countryHealthIndicator3 = buildCountryHealthIndicator(indicatorId3, "PAK", 1);

        CountryHealthIndicator countryHealthIndicatorForInd1 = buildCountryHealthIndicator(indicatorId1, countryId, 1);
        CountryHealthIndicator countryHealthIndicatorForInd2 = buildCountryHealthIndicator(indicatorId2, countryId, 1);
        CountryHealthIndicator countryHealthIndicatorForInd3 = buildCountryHealthIndicator(indicatorId3, countryId, 3);

        when(iCountryHealthIndicatorRepository.findByCountryHealthIndicatorIdYearAndStatus(year, PUBLISHED.name()))
                .thenReturn(asList(countryHealthIndicator1, countryHealthIndicator3,
                        countryHealthIndicatorForInd1, countryHealthIndicatorForInd2, countryHealthIndicatorForInd3));

        when(iCountryHealthIndicatorRepository.findByCountryHealthIndicatorIdCountryIdAndCountryHealthIndicatorIdYearAndStatus(countryId, year, PUBLISHED.name()))
                .thenReturn(
                        asList(countryHealthIndicatorForInd1, countryHealthIndicatorForInd2, countryHealthIndicatorForInd3
                        ));

        CountryPhaseId countryPhaseId1 = new CountryPhaseId("PAK", year);
        CountryPhase countryPhase1 = CountryPhase.builder().countryPhaseId(countryPhaseId1).countryOverallPhase(2).build();
        CountryPhaseId countryPhaseId2 = new CountryPhaseId("PAK", year);
        CountryPhase countryPhase2 = CountryPhase.builder().countryPhaseId(countryPhaseId2).countryOverallPhase(2).build();
        CountryPhaseId countryPhaseId3 = new CountryPhaseId("IND", year);
        CountryPhase countryPhase3 = CountryPhase.builder().countryPhaseId(countryPhaseId3).countryOverallPhase(2).build();
        CountryPhaseId countryPhaseId4 = new CountryPhaseId("IND", year);
        CountryPhase countryPhase4 = CountryPhase.builder().countryPhaseId(countryPhaseId4).countryOverallPhase(2).build();
        CountryPhaseId countryPhaseId5 = new CountryPhaseId("IND", year);
        CountryPhase countryPhase5 = CountryPhase.builder().countryPhaseId(countryPhaseId5).countryOverallPhase(2).build();

        when(iCountryPhaseRepository.findByCountryPhaseIdCountryIdAndCountryPhaseIdYear("PAK", year)).
                thenReturn(countryPhase1).thenReturn(countryPhase2);

        when(iCountryPhaseRepository.findByCountryPhaseIdCountryIdAndCountryPhaseIdYear(countryId, year)).
                thenReturn(countryPhase3).thenReturn(countryPhase4).thenReturn(countryPhase5);


        Map<Integer, BenchmarkDto> expectedBenchMark = new HashMap<>();
        expectedBenchMark.put(indicatorId1, new BenchmarkDto(2));
        expectedBenchMark.put(indicatorId2, new BenchmarkDto(1));
        expectedBenchMark.put(indicatorId3, new BenchmarkDto(2));

        Map<Integer, BenchmarkDto> benchmarkFor = benchMarkService.getBenchmarkFor(countryId, benchmarkType, year);

        assertThat(expectedBenchMark.get(indicatorId1)).usingRecursiveComparison().isEqualTo(benchmarkFor.get(indicatorId1));
        assertThat(expectedBenchMark.get(indicatorId2)).usingRecursiveComparison().isEqualTo(benchmarkFor.get(indicatorId2));
        assertThat(expectedBenchMark.get(indicatorId3)).usingRecursiveComparison().isEqualTo(benchmarkFor.get(indicatorId3));
    }


    private CountryHealthIndicator buildCountryHealthIndicator(Integer indicatorId, String countryId, Integer score) {
        return CountryHealthIndicator.builder()
                .indicator(new Indicator(indicatorId, "indicator name", "some definition", 1))
                .country(new Country(countryId, "Some Country", UUID.randomUUID(), "SC"))
                .score(score).build();
    }

}