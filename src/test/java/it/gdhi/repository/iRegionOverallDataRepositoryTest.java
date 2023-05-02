package it.gdhi.repository;

import java.util.Collections;
import java.util.List;

import javax.transaction.Transactional;

import it.gdhi.model.RegionalOverallData;
import it.gdhi.model.id.RegionalOverallId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@Transactional
@ActiveProfiles("test")
public class iRegionOverallDataRepositoryTest {

    @Autowired
    private IRegionalOverallDataRepository iRegionalOverallDataRepository;

    private void addRegionalOverallData(String regionId, Integer overAllPhase, String year) {
        RegionalOverallId regionalOverallId = RegionalOverallId.builder().year(year).regionId(regionId).build();
        RegionalOverallData regionalOverallData =
                RegionalOverallData.builder().regionalOverallId(regionalOverallId).overAllScore(overAllPhase).build();
        iRegionalOverallDataRepository.save(regionalOverallData);
    }

    @Test
    void shouldReturnPublishedCountriesYearsForARegion() {
        String regionId = "PAHO";
        addRegionalOverallData(regionId, 1, "2016");
        addRegionalOverallData(regionId, 1, "2018");
        addRegionalOverallData(regionId, 2, "2022");
        addRegionalOverallData(regionId, 3, "2023");

        List<String> expectedYears = asList("2023", "2022", "2018");
        List<String> actualYears = iRegionalOverallDataRepository.findByRegionIdOrderByUpdatedAtDesc(regionId, 3);

        assertEquals(expectedYears, actualYears);
    }

    @Test
    void shouldReturnEmptyListForARegionWhenRegionDoesNotExist() {
        String regionId = "PAHO";
        addRegionalOverallData(regionId, 1, "2016");

        List<String> expectedYears = Collections.emptyList();
        List<String> actualYears = iRegionalOverallDataRepository.findByRegionIdOrderByUpdatedAtDesc("ABCD", 3);

        assertEquals(expectedYears, actualYears);
    }
}
