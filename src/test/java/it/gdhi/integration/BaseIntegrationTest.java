package it.gdhi.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.gdhi.dto.HealthIndicatorDto;
import it.gdhi.model.CountryHealthIndicator;
import it.gdhi.model.id.CountryHealthIndicatorId;
import it.gdhi.repository.ICountryHealthIndicatorRepository;
import org.junit.jupiter.api.AfterEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.jdbc.JdbcTestUtils;

import javax.sql.DataSource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class BaseIntegrationTest {

    @Autowired
    private DataSource dataSource;

    private ObjectMapper mapper = new ObjectMapper();

    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    @Autowired
    ICountryHealthIndicatorRepository healthIndicatorRepository;

    @AfterEach
    public void tearDown() throws Exception {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "country_health_data.health_indicators",
                "country_health_data.country_resource_links", "country_health_data.country_summary", "country_health_data.country_phase");
    }

    ObjectMapper getMapper() {
        return this.mapper;
    }

    String expectedResponseJson(String fileName) throws IOException {
        return new String(Files.readAllBytes(Paths.get(getClass().getClassLoader().getResource("responses/" + fileName).getFile())));
    }

    void setupHealthIndicatorsForCountry(String countryId, List<HealthIndicatorDto> healthIndicatorDtos, String year) {
        healthIndicatorDtos.forEach(healthIndicator -> {
            CountryHealthIndicatorId countryHealthIndicatorId1 = new CountryHealthIndicatorId(countryId, healthIndicator.getCategoryId(), healthIndicator.getIndicatorId(), healthIndicator.getStatus(), year);
            CountryHealthIndicator countryHealthIndicatorSetupData1 = new CountryHealthIndicator(countryHealthIndicatorId1, healthIndicator.getScore(), healthIndicator.getSupportingText());
            healthIndicatorRepository.save(countryHealthIndicatorSetupData1);
        });
    }

    void assertResponse(String responseJSON, String expectedJsonFileName) throws IOException {
        String expectedJSON = expectedResponseJson(expectedJsonFileName);
        HashMap actualMap = getMapper().readValue(responseJSON, HashMap.class);
        HashMap expectedMap = getMapper().readValue(expectedJSON, HashMap.class);
        assertThat(actualMap).usingRecursiveComparison().ignoringFields("updatedDate").isEqualTo(expectedMap);
    }

    void assertCountryHealthScoreResponse(String responseJSON, String expectedJsonFileName) throws IOException {
        String expectedJSON = expectedResponseJson(expectedJsonFileName);
        HashMap actualMap = getMapper().readValue(responseJSON, HashMap.class);
        HashMap expectedMap = getMapper().readValue(expectedJSON, HashMap.class);
        for (Object countryHealthScores : actualMap.keySet()) {
            List<Map<String, Object>> listOfCountriesHealthScores = (List<Map<String, Object>>) actualMap.get(countryHealthScores);
            for (Map<String, Object> countryHealthScore : listOfCountriesHealthScores) {
                countryHealthScore.remove("updatedDate");
            }
        }
        for (Object a : expectedMap.keySet()) {
            List<Map<String, Object>> b = (List<Map<String, Object>>) expectedMap.get(a);
            for (Map<String, Object> c : b) {
                c.remove("updatedDate");
            }
        }
        assertEquals(actualMap, expectedMap);
    }

    void assertStringResponse(String responseJSON, String expectedJSON) throws IOException {
        HashMap actualMap = getMapper().readValue(responseJSON, HashMap.class);
        HashMap expectedMap = getMapper().readValue(expectedJSON, HashMap.class);
        assertEquals(expectedMap, actualMap);
    }
}
