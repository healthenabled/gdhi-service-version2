package it.gdhi.integration;

import io.restassured.response.Response;
import it.gdhi.GdhiServiceApplication;
import it.gdhi.dto.CountriesHealthScoreDto;
import it.gdhi.dto.HealthIndicatorDto;
import it.gdhi.model.Country;
import it.gdhi.model.CountryPhase;
import it.gdhi.model.CountrySummary;
import it.gdhi.model.id.CountryPhaseId;
import it.gdhi.model.id.CountrySummaryId;
import it.gdhi.repository.ICountryPhaseRepository;
import it.gdhi.repository.ICountrySummaryRepository;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static it.gdhi.utils.LanguageCode.USER_LANGUAGE;
import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = GdhiServiceApplication.class)
@ActiveProfiles("test")
public class ScoreAggregationIntegrationTest extends BaseIntegrationTest {
    @Value("${local.server.port}")
    private int port;

    @Autowired
    private ICountrySummaryRepository countrySummaryRepository;

    @Autowired
    private ICountryPhaseRepository iCountryPhaseRepository;

    private void addCountrySummary(String countryId, String countryName, String alpha2code, String year) throws Exception {
        String status = "PUBLISHED";
        CountrySummary countrySummary = CountrySummary.builder()
                .countrySummaryId(new CountrySummaryId(countryId, status, year))
                .summary("summary")
                .country(new Country(countryId, countryName, UUID.randomUUID(), alpha2code))
                .contactName("contactName")
                .contactDesignation("contactDesignation")
                .contactOrganization("contactOrganization")
                .contactEmail("email")
                .dataFeederName("feeder name")
                .dataFeederRole("feeder role")
                .dataFeederEmail("email")
                .dataApproverName("coll name")
                .dataApproverRole("coll role")
                .dataFeederRole("coll role")
                .dataApproverEmail("coll email")
                .countryResourceLinks(new ArrayList<>())
                .build();
        countrySummaryRepository.save(countrySummary);
    }

    private void addCountryPhase(String countryId, Integer phase, String year) {
        CountryPhaseId countryPhaseId = new CountryPhaseId(countryId, year);
        CountryPhase countryPhase = CountryPhase.builder().countryPhaseId(countryPhaseId).countryOverallPhase(phase).build();
        iCountryPhaseRepository.save(countryPhase);
    }

    @Test
    public void shouldGetOverAllGlobalScore() throws Exception {
        String india = "IND";
        String uk = "GBR";
        String pakistan = "PAK";
        String year = "2023";
        Integer categoryId1 = 1;
        Integer categoryId2 = 2;
        Integer categoryId3 = 3;
        Integer categoryId4 = 4;
        Integer indicatorId1_1 = 1;
        Integer indicatorId1_2 = 2;
        Integer indicatorId2_1 = 3;
        Integer indicatorId2_2 = 4;
        Integer indicatorId3_1 = 5;
        Integer indicatorId3_2 = 6;
        Integer indicatorId4_1 = 7;

        String status = "PUBLISHED";
        addCountrySummary(india, status, "IN", year);
        addCountrySummary(uk, status, "UK", year);
        addCountrySummary(pakistan, status, "PK", year);

        List<HealthIndicatorDto> healthIndicatorDtos = asList(
                HealthIndicatorDto.builder().categoryId(categoryId1).indicatorId(indicatorId1_1).status(status).score(3).supportingText("sp1").build(),
                HealthIndicatorDto.builder().categoryId(categoryId1).indicatorId(indicatorId1_2).status(status).score(4).supportingText("sp2").build(),
                HealthIndicatorDto.builder().categoryId(categoryId2).indicatorId(indicatorId2_1).status(status).score(3).supportingText("sp3").build(),
                HealthIndicatorDto.builder().categoryId(categoryId2).indicatorId(indicatorId2_2).status(status).score(4).supportingText("sp4").build(),
                HealthIndicatorDto.builder().categoryId(categoryId3).indicatorId(indicatorId3_1).status(status).score(null).supportingText("sp5").build(),
                HealthIndicatorDto.builder().categoryId(categoryId3).indicatorId(indicatorId3_2).status(status).score(null).supportingText("sp6").build(),
                HealthIndicatorDto.builder().categoryId(categoryId4).indicatorId(indicatorId4_1).status(status).score(null).supportingText("sp19").build());

        setupHealthIndicatorsForCountry(india, healthIndicatorDtos, year);
        addCountryPhase(india, 2, year);

        healthIndicatorDtos = asList(
                HealthIndicatorDto.builder().categoryId(categoryId1).indicatorId(indicatorId1_1).status(status).score(4).supportingText("sp7").build(),
                HealthIndicatorDto.builder().categoryId(categoryId1).indicatorId(indicatorId1_2).status(status).score(5).supportingText("sp8").build(),
                HealthIndicatorDto.builder().categoryId(categoryId2).indicatorId(indicatorId2_1).status(status).score(3).supportingText("sp9").build(),
                HealthIndicatorDto.builder().categoryId(categoryId2).indicatorId(indicatorId2_2).status(status).score(4).supportingText("sp10").build(),
                HealthIndicatorDto.builder().categoryId(categoryId3).indicatorId(indicatorId3_1).status(status).score(null).supportingText("sp11").build(),
                HealthIndicatorDto.builder().categoryId(categoryId3).indicatorId(indicatorId3_2).status(status).score(4).supportingText("sp12").build(),
                HealthIndicatorDto.builder().categoryId(categoryId4).indicatorId(indicatorId4_1).status(status).score(null).supportingText("sp20").build());

        setupHealthIndicatorsForCountry(uk, healthIndicatorDtos, year);
        addCountryPhase(uk, 4, year);


        healthIndicatorDtos = asList(
                HealthIndicatorDto.builder().categoryId(categoryId1).indicatorId(indicatorId1_1).status(status).score(null).supportingText("sp13").build(),
                HealthIndicatorDto.builder().categoryId(categoryId1).indicatorId(indicatorId1_2).status(status).score(null).supportingText("sp14").build(),
                HealthIndicatorDto.builder().categoryId(categoryId2).indicatorId(indicatorId2_1).status(status).score(2).supportingText("sp15").build(),
                HealthIndicatorDto.builder().categoryId(categoryId2).indicatorId(indicatorId2_2).status(status).score(2).supportingText("sp16").build(),
                HealthIndicatorDto.builder().categoryId(categoryId3).indicatorId(indicatorId3_1).status(status).score(null).supportingText("sp17").build(),
                HealthIndicatorDto.builder().categoryId(categoryId3).indicatorId(indicatorId3_2).status(status).score(null).supportingText("sp18").build(),
                HealthIndicatorDto.builder().categoryId(categoryId4).indicatorId(indicatorId4_1).status(status).score(null).supportingText("sp21").build());

        setupHealthIndicatorsForCountry(pakistan, healthIndicatorDtos, year);
        addCountryPhase(pakistan, null, year);

        Response response = given()
                .contentType("application/json")
                .header(USER_LANGUAGE, "en")
                .when()
                .get("http://localhost:" + port + "/global_health_indicators?year=" + year);

        assertResponse(response.asString(), "global_indicators.json");

    }

    @Test
    public void shouldFilterOverAllGlobalScoreByCategoryAndPhase() throws Exception {
        String india = "IND";
        String uk = "GBR";
        String pakistan = "PAK";
        String year = "2023";
        Integer categoryId1 = 1;
        Integer categoryId2 = 2;
        Integer categoryId3 = 3;
        Integer categoryId4 = 4;
        Integer indicatorId1_1 = 1;
        Integer indicatorId1_2 = 2;
        Integer indicatorId2_1 = 3;
        Integer indicatorId2_2 = 4;
        Integer indicatorId3_1 = 5;
        Integer indicatorId3_2 = 6;
        Integer indicatorId4_1 = 7;

        String status = "PUBLISHED";
        addCountrySummary(india, status, "IN", year);
        addCountrySummary(uk, status, "UK", year);
        addCountrySummary(pakistan, status, "PK", year);

        List<HealthIndicatorDto> healthIndicatorDtos = asList(
                HealthIndicatorDto.builder().categoryId(categoryId1).indicatorId(indicatorId1_1).score(1).status(status).supportingText("sp1").build(),
                HealthIndicatorDto.builder().categoryId(categoryId1).indicatorId(indicatorId1_2).status(status).score(1).supportingText("sp2").build(),
                HealthIndicatorDto.builder().categoryId(categoryId2).indicatorId(indicatorId2_1).status(status).score(3).supportingText("sp3").build(),
                HealthIndicatorDto.builder().categoryId(categoryId2).indicatorId(indicatorId2_2).status(status).score(null).supportingText("sp4").build(),
                HealthIndicatorDto.builder().categoryId(categoryId3).indicatorId(indicatorId3_1).status(status).score(null).supportingText("sp5").build(),
                HealthIndicatorDto.builder().categoryId(categoryId3).indicatorId(indicatorId3_2).status(status).score(null).supportingText("sp6").build(),
                HealthIndicatorDto.builder().categoryId(categoryId4).indicatorId(indicatorId4_1).status(status).score(null).supportingText("sp19").build());

        setupHealthIndicatorsForCountry(india, healthIndicatorDtos, year);
        addCountryPhase(india, 2, year);

        healthIndicatorDtos = asList(
                HealthIndicatorDto.builder().categoryId(categoryId1).indicatorId(indicatorId1_1).status(status).score(1).supportingText("sp7").build(),
                HealthIndicatorDto.builder().categoryId(categoryId1).indicatorId(indicatorId1_2).status(status).score(5).supportingText("sp8").build(),
                HealthIndicatorDto.builder().categoryId(categoryId2).indicatorId(indicatorId2_1).status(status).score(3).supportingText("sp9").build(),
                HealthIndicatorDto.builder().categoryId(categoryId2).indicatorId(indicatorId2_2).status(status).score(null).supportingText("sp10").build(),
                HealthIndicatorDto.builder().categoryId(categoryId3).indicatorId(indicatorId3_1).status(status).score(null).supportingText("sp11").build(),
                HealthIndicatorDto.builder().categoryId(categoryId3).indicatorId(indicatorId3_2).status(status).score(4).supportingText("sp12").build(),
                HealthIndicatorDto.builder().categoryId(categoryId4).indicatorId(indicatorId4_1).status(status).score(null).supportingText("sp20").build());

        setupHealthIndicatorsForCountry(uk, healthIndicatorDtos, year);
        addCountryPhase(uk, 4, year);

        healthIndicatorDtos = asList(
                HealthIndicatorDto.builder().categoryId(categoryId1).indicatorId(indicatorId1_1).status(status).score(null).supportingText("sp13").build(),
                HealthIndicatorDto.builder().categoryId(categoryId1).indicatorId(indicatorId1_2).status(status).score(null).supportingText("sp14").build(),
                HealthIndicatorDto.builder().categoryId(categoryId2).indicatorId(indicatorId2_1).status(status).score(null).supportingText("sp15").build(),
                HealthIndicatorDto.builder().categoryId(categoryId2).indicatorId(indicatorId2_2).status(status).score(null).supportingText("sp16").build(),
                HealthIndicatorDto.builder().categoryId(categoryId3).indicatorId(indicatorId3_1).status(status).score(null).supportingText("sp17").build(),
                HealthIndicatorDto.builder().categoryId(categoryId3).indicatorId(indicatorId3_2).status(status).score(null).supportingText("sp18").build(),
                HealthIndicatorDto.builder().categoryId(categoryId4).indicatorId(indicatorId4_1).status(status).score(null).supportingText("sp21").build());

        setupHealthIndicatorsForCountry(pakistan, healthIndicatorDtos, year);
        addCountryPhase(pakistan, null, year);

        Response response = given()
                .contentType("application/json")
                .header(USER_LANGUAGE, "en")
                .when()
                .get("http://localhost:" + port + "/global_health_indicators?categoryId=" + categoryId1 + "&phase=1" + "&year=" + year);

        assertResponse(response.asString(), "filtered_global_indicators.json");

    }

    @Test
    public void shouldFilterOverAllGlobalScoreByCategory() throws Exception {
        String india = "IND";
        String uk = "GBR";
        String pakistan = "PAK";
        String year = "2023";
        Integer categoryId1 = 1;
        Integer categoryId2 = 2;
        Integer categoryId3 = 3;
        Integer categoryId4 = 4;
        Integer indicatorId1_1 = 1;
        Integer indicatorId1_2 = 2;
        Integer indicatorId2_1 = 3;
        Integer indicatorId2_2 = 4;
        Integer indicatorId3_1 = 5;
        Integer indicatorId3_2 = 6;
        Integer indicatorId4_1 = 7;

        String status = "PUBLISHED";
        addCountrySummary(india, status, "IN", year);
        addCountrySummary(uk, status, "UK", year);
        addCountrySummary(pakistan, status, "PK", year);

        List<HealthIndicatorDto> healthIndicatorDtos = asList(
                HealthIndicatorDto.builder().categoryId(categoryId1).indicatorId(indicatorId1_1).status(status).score(1).supportingText("sp1").build(),
                HealthIndicatorDto.builder().categoryId(categoryId1).indicatorId(indicatorId1_2).status(status).score(1).supportingText("sp2").build(),
                HealthIndicatorDto.builder().categoryId(categoryId2).indicatorId(indicatorId2_1).status(status).score(3).supportingText("sp3").build(),
                HealthIndicatorDto.builder().categoryId(categoryId2).indicatorId(indicatorId2_2).status(status).score(null).supportingText("sp4").build(),
                HealthIndicatorDto.builder().categoryId(categoryId3).indicatorId(indicatorId3_1).status(status).score(null).supportingText("sp5").build(),
                HealthIndicatorDto.builder().categoryId(categoryId3).indicatorId(indicatorId3_2).status(status).score(null).supportingText("sp6").build(),
                HealthIndicatorDto.builder().categoryId(categoryId4).indicatorId(indicatorId4_1).status(status).score(null).supportingText("sp19").build());

        setupHealthIndicatorsForCountry(india, healthIndicatorDtos, year);
        addCountryPhase(india, 2, year);

        healthIndicatorDtos = asList(
                HealthIndicatorDto.builder().categoryId(categoryId1).indicatorId(indicatorId1_1).status(status).score(1).supportingText("sp7").build(),
                HealthIndicatorDto.builder().categoryId(categoryId1).indicatorId(indicatorId1_2).status(status).score(5).supportingText("sp8").build(),
                HealthIndicatorDto.builder().categoryId(categoryId2).indicatorId(indicatorId2_1).status(status).score(3).supportingText("sp9").build(),
                HealthIndicatorDto.builder().categoryId(categoryId2).indicatorId(indicatorId2_2).status(status).score(null).supportingText("sp10").build(),
                HealthIndicatorDto.builder().categoryId(categoryId3).indicatorId(indicatorId3_1).status(status).score(null).supportingText("sp11").build(),
                HealthIndicatorDto.builder().categoryId(categoryId3).indicatorId(indicatorId3_2).status(status).score(4).supportingText("sp12").build(),
                HealthIndicatorDto.builder().categoryId(categoryId4).indicatorId(indicatorId4_1).status(status).score(null).supportingText("sp20").build());

        setupHealthIndicatorsForCountry(uk, healthIndicatorDtos, year);
        addCountryPhase(uk, 4, year);

        healthIndicatorDtos = asList(
                HealthIndicatorDto.builder().categoryId(categoryId1).indicatorId(indicatorId1_1).status(status).score(1).supportingText("sp13").build(),
                HealthIndicatorDto.builder().categoryId(categoryId1).indicatorId(indicatorId1_2).status(status).score(3).supportingText("sp14").build(),
                HealthIndicatorDto.builder().categoryId(categoryId2).indicatorId(indicatorId2_1).status(status).score(null).supportingText("sp15").build(),
                HealthIndicatorDto.builder().categoryId(categoryId2).indicatorId(indicatorId2_2).status(status).score(null).supportingText("sp16").build(),
                HealthIndicatorDto.builder().categoryId(categoryId3).indicatorId(indicatorId3_1).status(status).score(null).supportingText("sp17").build(),
                HealthIndicatorDto.builder().categoryId(categoryId3).indicatorId(indicatorId3_2).status(status).score(null).supportingText("sp18").build(),
                HealthIndicatorDto.builder().categoryId(categoryId4).indicatorId(indicatorId4_1).status(status).score(null).supportingText("sp21").build());

        setupHealthIndicatorsForCountry(pakistan, healthIndicatorDtos, year);
        addCountryPhase(pakistan, null, year);

        Response response = given()
                .contentType("application/json")
                .header(USER_LANGUAGE, "en")
                .when()
                .get("http://localhost:" + port + "/global_health_indicators?categoryId=" + categoryId1 + "&year=" + year);

        assertResponse(response.asString(), "global_indicators_filtered_by_category.json");

    }

    @Test
    public void shouldFilterOverAllGlobalScoreByPhase() throws Exception {
        String india = "IND";
        String uk = "GBR";
        String pakistan = "PAK";
        String year = "2023";
        Integer categoryId1 = 1;
        Integer categoryId2 = 2;
        Integer categoryId3 = 3;
        Integer categoryId4 = 4;
        Integer indicatorId1_1 = 1;
        Integer indicatorId1_2 = 2;
        Integer indicatorId2_1 = 3;
        Integer indicatorId2_2 = 4;
        Integer indicatorId3_1 = 5;
        Integer indicatorId3_2 = 6;
        Integer indicatorId4_1 = 7;

        String status = "PUBLISHED";
        addCountrySummary(india, status, "IN", year);
        addCountrySummary(uk, status, "UK", year);
        addCountrySummary(pakistan, status, "PK", year);

        List<HealthIndicatorDto> healthIndicatorDtos = asList(
                HealthIndicatorDto.builder().categoryId(categoryId1).indicatorId(indicatorId1_1).status(status).score(1).supportingText("sp1").build(),
                HealthIndicatorDto.builder().categoryId(categoryId1).indicatorId(indicatorId1_2).status(status).score(1).supportingText("sp2").build(),
                HealthIndicatorDto.builder().categoryId(categoryId2).indicatorId(indicatorId2_1).status(status).score(3).supportingText("sp3").build(),
                HealthIndicatorDto.builder().categoryId(categoryId2).indicatorId(indicatorId2_2).status(status).score(null).supportingText("sp4").build(),
                HealthIndicatorDto.builder().categoryId(categoryId3).indicatorId(indicatorId3_1).status(status).score(null).supportingText("sp5").build(),
                HealthIndicatorDto.builder().categoryId(categoryId3).indicatorId(indicatorId3_2).status(status).score(null).supportingText("sp6").build(),
                HealthIndicatorDto.builder().categoryId(categoryId4).indicatorId(indicatorId4_1).status(status).score(null).supportingText("sp19").build());

        setupHealthIndicatorsForCountry(india, healthIndicatorDtos, year);
        addCountryPhase(india, 2, year);

        healthIndicatorDtos = asList(
                HealthIndicatorDto.builder().categoryId(categoryId1).indicatorId(indicatorId1_1).status(status).score(1).supportingText("sp7").build(),
                HealthIndicatorDto.builder().categoryId(categoryId1).indicatorId(indicatorId1_2).status(status).score(5).supportingText("sp8").build(),
                HealthIndicatorDto.builder().categoryId(categoryId2).indicatorId(indicatorId2_1).status(status).score(3).supportingText("sp9").build(),
                HealthIndicatorDto.builder().categoryId(categoryId2).indicatorId(indicatorId2_2).status(status).score(null).supportingText("sp10").build(),
                HealthIndicatorDto.builder().categoryId(categoryId3).indicatorId(indicatorId3_1).status(status).score(null).supportingText("sp11").build(),
                HealthIndicatorDto.builder().categoryId(categoryId3).indicatorId(indicatorId3_2).status(status).score(4).supportingText("sp12").build(),
                HealthIndicatorDto.builder().categoryId(categoryId4).indicatorId(indicatorId4_1).status(status).score(null).supportingText("sp20").build());

        setupHealthIndicatorsForCountry(uk, healthIndicatorDtos, year);
        addCountryPhase(uk, 4, year);

        healthIndicatorDtos = asList(
                HealthIndicatorDto.builder().categoryId(categoryId1).indicatorId(indicatorId1_1).status(status).score(null).supportingText("sp13").build(),
                HealthIndicatorDto.builder().categoryId(categoryId1).indicatorId(indicatorId1_2).status(status).score(null).supportingText("sp14").build(),
                HealthIndicatorDto.builder().categoryId(categoryId2).indicatorId(indicatorId2_1).status(status).score(null).supportingText("sp15").build(),
                HealthIndicatorDto.builder().categoryId(categoryId2).indicatorId(indicatorId2_2).status(status).score(null).supportingText("sp16").build(),
                HealthIndicatorDto.builder().categoryId(categoryId3).indicatorId(indicatorId3_1).status(status).score(null).supportingText("sp17").build(),
                HealthIndicatorDto.builder().categoryId(categoryId3).indicatorId(indicatorId3_2).status(status).score(null).supportingText("sp18").build(),
                HealthIndicatorDto.builder().categoryId(categoryId4).indicatorId(indicatorId4_1).status(status).score(null).supportingText("sp21").build());

        setupHealthIndicatorsForCountry(pakistan, healthIndicatorDtos, year);
        addCountryPhase(pakistan, null, year);

        Response response = given()
                .contentType("application/json")
                .header(USER_LANGUAGE, "en")
                .when()
                .get("http://localhost:" + port + "/global_health_indicators?phase=4" + "&year=" + year);

        assertResponse(response.asString(), "global_indicators_filtered_by_phase.json");

    }

    @Test
    public void shouldGetOverAllScoreForAllCountries() throws Exception {
        String india = "IND";
        String uk = "GBR";
        String pakistan = "PAK";
        String year = "2023";
        Integer categoryId1 = 1;
        Integer categoryId2 = 2;
        Integer categoryId3 = 3;
        Integer indicatorId1_1 = 1;
        Integer indicatorId1_2 = 2;
        Integer indicatorId2_1 = 3;
        Integer indicatorId2_2 = 4;
        Integer indicatorId3_1 = 5;
        Integer indicatorId3_2 = 6;

        String status = "PUBLISHED";
        addCountrySummary(india, status, "IN", year);
        addCountrySummary(uk, status, "UK", year);
        addCountrySummary(pakistan, status, "PK", year);

        List<HealthIndicatorDto> healthIndicatorDtos = asList(
                HealthIndicatorDto.builder().categoryId(categoryId1).indicatorId(indicatorId1_1).status(status).score(1).supportingText("sp1").build(),
                HealthIndicatorDto.builder().categoryId(categoryId1).indicatorId(indicatorId1_2).status(status).score(2).supportingText("sp2").build(),
                HealthIndicatorDto.builder().categoryId(categoryId2).indicatorId(indicatorId2_1).status(status).score(3).supportingText("sp3").build(),
                HealthIndicatorDto.builder().categoryId(categoryId2).indicatorId(indicatorId2_2).status(status).score(null).supportingText("sp4").build(),
                HealthIndicatorDto.builder().categoryId(categoryId3).indicatorId(indicatorId3_1).status(status).score(null).supportingText("sp5").build(),
                HealthIndicatorDto.builder().categoryId(categoryId3).indicatorId(indicatorId3_2).status(status).score(null).supportingText("sp6").build());

        setupHealthIndicatorsForCountry(india, healthIndicatorDtos, year);
        addCountryPhase(india, 2, year);

        healthIndicatorDtos = asList(
                HealthIndicatorDto.builder().categoryId(categoryId1).indicatorId(indicatorId1_1).status(status).score(4).supportingText("sp7").build(),
                HealthIndicatorDto.builder().categoryId(categoryId1).indicatorId(indicatorId1_2).status(status).score(5).supportingText("sp8").build(),
                HealthIndicatorDto.builder().categoryId(categoryId2).indicatorId(indicatorId2_1).status(status).score(3).supportingText("sp9").build(),
                HealthIndicatorDto.builder().categoryId(categoryId2).indicatorId(indicatorId2_2).status(status).score(null).supportingText("sp10").build(),
                HealthIndicatorDto.builder().categoryId(categoryId3).indicatorId(indicatorId3_1).status(status).score(null).supportingText("sp11").build(),
                HealthIndicatorDto.builder().categoryId(categoryId3).indicatorId(indicatorId3_2).status(status).score(null).supportingText("sp12").build());

        setupHealthIndicatorsForCountry(uk, healthIndicatorDtos, year);
        addCountryPhase(uk, 4, year);

        healthIndicatorDtos = asList(
                HealthIndicatorDto.builder().categoryId(categoryId1).indicatorId(indicatorId1_1).status(status).score(null).supportingText("sp13").build(),
                HealthIndicatorDto.builder().categoryId(categoryId1).indicatorId(indicatorId1_2).status(status).score(null).supportingText("sp14").build(),
                HealthIndicatorDto.builder().categoryId(categoryId2).indicatorId(indicatorId2_1).status(status).score(null).supportingText("sp15").build(),
                HealthIndicatorDto.builder().categoryId(categoryId2).indicatorId(indicatorId2_2).status(status).score(null).supportingText("sp16").build(),
                HealthIndicatorDto.builder().categoryId(categoryId3).indicatorId(indicatorId3_1).status(status).score(null).supportingText("sp17").build(),
                HealthIndicatorDto.builder().categoryId(categoryId3).indicatorId(indicatorId3_2).status(status).score(null).supportingText("sp18").build());

        setupHealthIndicatorsForCountry(pakistan, healthIndicatorDtos, year);
        addCountryPhase(pakistan, null, year);

        SimpleDateFormat DateFor = new SimpleDateFormat("MMMM yyyy");
        String expectedUpdatedDate = DateFor.format(new Date());

        Response response = given()
                .contentType("application/json")
                .header(USER_LANGUAGE, "en")
                .when()
                .get("http://localhost:" + port + "/countries_health_indicator_scores?year=" + year);

        CountriesHealthScoreDto countryHealthScoreDto = response.getBody().as(CountriesHealthScoreDto.class);
        assertEquals(countryHealthScoreDto.getCountryHealthScores().get(0).getUpdatedDate(), expectedUpdatedDate);

        assertCountryHealthScoreResponse(response.asString(), "countries_health_indicators.json");
    }

    @Test
    public void shouldGetFilteredOverAllScoreForAllCountriesGivenCategoryAndPhase() throws Exception {
        String india = "IND";
        String uk = "GBR";
        String pakistan = "PAK";
        String year = "2023";
        Integer categoryId1 = 1;
        Integer categoryId2 = 2;
        Integer categoryId3 = 3;
        Integer indicatorId1_1 = 1;
        Integer indicatorId1_2 = 2;
        Integer indicatorId2_1 = 3;
        Integer indicatorId2_2 = 4;
        Integer indicatorId3_1 = 5;
        Integer indicatorId3_2 = 6;

        String status = "PUBLISHED";
        addCountrySummary(india, status, "IN", year);
        addCountrySummary(uk, status, "UK", year);
        addCountrySummary(pakistan, status, "PK", year);

        List<HealthIndicatorDto> healthIndicatorDtos = asList(
                HealthIndicatorDto.builder().categoryId(categoryId1).indicatorId(indicatorId1_1).status(status).score(3).supportingText("sp1").build(),
                HealthIndicatorDto.builder().categoryId(categoryId1).indicatorId(indicatorId1_2).status(status).score(1).supportingText("sp2").build(),
                HealthIndicatorDto.builder().categoryId(categoryId2).indicatorId(indicatorId2_1).status(status).score(3).supportingText("sp3").build(),
                HealthIndicatorDto.builder().categoryId(categoryId2).indicatorId(indicatorId2_2).status(status).score(null).supportingText("sp4").build(),
                HealthIndicatorDto.builder().categoryId(categoryId3).indicatorId(indicatorId3_1).status(status).score(null).supportingText("sp5").build(),
                HealthIndicatorDto.builder().categoryId(categoryId3).indicatorId(indicatorId3_2).status(status).score(null).supportingText("sp6").build());

        setupHealthIndicatorsForCountry(india, healthIndicatorDtos, year);
        addCountryPhase(india, 3, year);

        healthIndicatorDtos = asList(
                HealthIndicatorDto.builder().categoryId(categoryId1).indicatorId(indicatorId1_1).status(status).score(1).supportingText("sp7").build(),
                HealthIndicatorDto.builder().categoryId(categoryId1).indicatorId(indicatorId1_2).status(status).score(5).supportingText("sp8").build(),
                HealthIndicatorDto.builder().categoryId(categoryId2).indicatorId(indicatorId2_1).status(status).score(3).supportingText("sp9").build(),
                HealthIndicatorDto.builder().categoryId(categoryId2).indicatorId(indicatorId2_2).status(status).score(null).supportingText("sp10").build(),
                HealthIndicatorDto.builder().categoryId(categoryId3).indicatorId(indicatorId3_1).status(status).score(null).supportingText("sp11").build(),
                HealthIndicatorDto.builder().categoryId(categoryId3).indicatorId(indicatorId3_2).status(status).score(null).supportingText("sp12").build());

        setupHealthIndicatorsForCountry(uk, healthIndicatorDtos, year);
        addCountryPhase(uk, 3, year);

        healthIndicatorDtos = asList(
                HealthIndicatorDto.builder().categoryId(categoryId1).indicatorId(indicatorId1_1).status(status).score(null).supportingText("sp13").build(),
                HealthIndicatorDto.builder().categoryId(categoryId1).indicatorId(indicatorId1_2).status(status).score(null).supportingText("sp14").build(),
                HealthIndicatorDto.builder().categoryId(categoryId2).indicatorId(indicatorId2_1).status(status).score(null).supportingText("sp15").build(),
                HealthIndicatorDto.builder().categoryId(categoryId2).indicatorId(indicatorId2_2).status(status).score(null).supportingText("sp16").build(),
                HealthIndicatorDto.builder().categoryId(categoryId3).indicatorId(indicatorId3_1).status(status).score(null).supportingText("sp17").build(),
                HealthIndicatorDto.builder().categoryId(categoryId3).indicatorId(indicatorId3_2).status(status).score(null).supportingText("sp18").build());

        setupHealthIndicatorsForCountry(pakistan, healthIndicatorDtos, year);
        addCountryPhase(pakistan, null, year);

        SimpleDateFormat DateFor = new SimpleDateFormat("MMMM yyyy");
        String expectedUpdatedDate = DateFor.format(new Date());

        Response response = given()
                .contentType("application/json")
                .header(USER_LANGUAGE, "en")
                .when()
                .get("http://localhost:" + port + "/countries_health_indicator_scores?categoryId=" + categoryId1 + "&phase=2" + "&year=" + year);

        CountriesHealthScoreDto countryHealthScoreDto = response.getBody().as(CountriesHealthScoreDto.class);
        assertEquals(countryHealthScoreDto.getCountryHealthScores().get(0).getUpdatedDate(), expectedUpdatedDate);

        assertCountryHealthScoreResponse(response.asString(), "filtered_countries_health_indicators.json");
    }

    @Test
    public void shouldGetFilteredOverAllScoreForAllCountriesGivenCategory() throws Exception {
        String india = "IND";
        String uk = "GBR";
        String pakistan = "PAK";
        String year = "2023";
        Integer categoryId1 = 1;
        Integer categoryId2 = 2;
        Integer categoryId3 = 3;
        Integer indicatorId1_1 = 1;
        Integer indicatorId1_2 = 2;
        Integer indicatorId2_1 = 3;
        Integer indicatorId2_2 = 4;
        Integer indicatorId3_1 = 5;
        Integer indicatorId3_2 = 6;

        String status = "PUBLISHED";
        addCountrySummary(india, status, "IN", year);
        addCountrySummary(uk, status, "UK", year);
        addCountrySummary(pakistan, status, "PK", year);

        List<HealthIndicatorDto> healthIndicatorDtos = asList(
                HealthIndicatorDto.builder().categoryId(categoryId1).indicatorId(indicatorId1_1).status(status).score(3).supportingText("sp1").build(),
                HealthIndicatorDto.builder().categoryId(categoryId1).indicatorId(indicatorId1_2).status(status).score(1).supportingText("sp2").build(),
                HealthIndicatorDto.builder().categoryId(categoryId2).indicatorId(indicatorId2_1).status(status).score(3).supportingText("sp3").build(),
                HealthIndicatorDto.builder().categoryId(categoryId2).indicatorId(indicatorId2_2).status(status).score(null).supportingText("sp4").build(),
                HealthIndicatorDto.builder().categoryId(categoryId3).indicatorId(indicatorId3_1).status(status).score(null).supportingText("sp5").build(),
                HealthIndicatorDto.builder().categoryId(categoryId3).indicatorId(indicatorId3_2).status(status).score(null).supportingText("sp6").build());

        setupHealthIndicatorsForCountry(india, healthIndicatorDtos, year);
        addCountryPhase(india, 3, year);

        healthIndicatorDtos = asList(
                HealthIndicatorDto.builder().categoryId(categoryId1).indicatorId(indicatorId1_1).status(status).score(1).supportingText("sp7").build(),
                HealthIndicatorDto.builder().categoryId(categoryId1).indicatorId(indicatorId1_2).status(status).score(5).supportingText("sp8").build(),
                HealthIndicatorDto.builder().categoryId(categoryId2).indicatorId(indicatorId2_1).status(status).score(3).supportingText("sp9").build(),
                HealthIndicatorDto.builder().categoryId(categoryId2).indicatorId(indicatorId2_2).status(status).score(null).supportingText("sp10").build(),
                HealthIndicatorDto.builder().categoryId(categoryId3).indicatorId(indicatorId3_1).status(status).score(null).supportingText("sp11").build(),
                HealthIndicatorDto.builder().categoryId(categoryId3).indicatorId(indicatorId3_2).status(status).score(null).supportingText("sp12").build());

        setupHealthIndicatorsForCountry(uk, healthIndicatorDtos, year);
        addCountryPhase(uk, 3, year);

        healthIndicatorDtos = asList(
                HealthIndicatorDto.builder().categoryId(categoryId1).indicatorId(indicatorId1_1).status(status).score(null).supportingText("sp13").build(),
                HealthIndicatorDto.builder().categoryId(categoryId1).indicatorId(indicatorId1_2).status(status).score(null).supportingText("sp14").build(),
                HealthIndicatorDto.builder().categoryId(categoryId2).indicatorId(indicatorId2_1).status(status).score(null).supportingText("sp15").build(),
                HealthIndicatorDto.builder().categoryId(categoryId2).indicatorId(indicatorId2_2).status(status).score(null).supportingText("sp16").build(),
                HealthIndicatorDto.builder().categoryId(categoryId3).indicatorId(indicatorId3_1).status(status).score(null).supportingText("sp17").build(),
                HealthIndicatorDto.builder().categoryId(categoryId3).indicatorId(indicatorId3_2).status(status).score(null).supportingText("sp18").build());

        setupHealthIndicatorsForCountry(pakistan, healthIndicatorDtos, year);
        addCountryPhase(pakistan, null, year);

        SimpleDateFormat DateFor = new SimpleDateFormat("MMMM yyyy");
        String expectedUpdatedDate = DateFor.format(new Date());

        Response response = given()
                .contentType("application/json")
                .header(USER_LANGUAGE, "en")
                .when()
                .get("http://localhost:" + port + "/countries_health_indicator_scores?categoryId=" + categoryId1 + "&year=" + year);

        CountriesHealthScoreDto countryHealthScoreDto = response.getBody().as(CountriesHealthScoreDto.class);
        assertEquals(countryHealthScoreDto.getCountryHealthScores().get(0).getUpdatedDate(), expectedUpdatedDate);

        assertCountryHealthScoreResponse(response.asString(), "countries_health_indicators_filter_by_category.json");
    }

    @Test
    public void shouldGetFilteredOverAllScoreForAllCountriesGivenPhase() throws Exception {
        String india = "IND";
        String uk = "GBR";
        String pakistan = "PAK";
        String year = "2023";
        Integer categoryId1 = 1;
        Integer categoryId2 = 2;
        Integer categoryId3 = 3;
        Integer indicatorId1_1 = 1;
        Integer indicatorId1_2 = 2;
        Integer indicatorId2_1 = 3;
        Integer indicatorId2_2 = 4;
        Integer indicatorId3_1 = 5;
        Integer indicatorId3_2 = 6;

        String status = "PUBLISHED";
        addCountrySummary(india, status, "IN", year);
        addCountrySummary(uk, status, "UK", year);
        addCountrySummary(pakistan, status, "PK", year);

        List<HealthIndicatorDto> healthIndicatorDtos = asList(
                HealthIndicatorDto.builder().categoryId(categoryId1).indicatorId(indicatorId1_1).status(status).score(3).supportingText("sp1").build(),
                HealthIndicatorDto.builder().categoryId(categoryId1).indicatorId(indicatorId1_2).status(status).score(1).supportingText("sp2").build(),
                HealthIndicatorDto.builder().categoryId(categoryId2).indicatorId(indicatorId2_1).status(status).score(3).supportingText("sp3").build(),
                HealthIndicatorDto.builder().categoryId(categoryId2).indicatorId(indicatorId2_2).status(status).score(null).supportingText("sp4").build(),
                HealthIndicatorDto.builder().categoryId(categoryId3).indicatorId(indicatorId3_1).status(status).score(null).supportingText("sp5").build(),
                HealthIndicatorDto.builder().categoryId(categoryId3).indicatorId(indicatorId3_2).status(status).score(null).supportingText("sp6").build());

        setupHealthIndicatorsForCountry(india, healthIndicatorDtos, year);
        addCountryPhase(india, 3, year);

        healthIndicatorDtos = asList(
                HealthIndicatorDto.builder().categoryId(categoryId1).indicatorId(indicatorId1_1).status(status).score(1).supportingText("sp7").build(),
                HealthIndicatorDto.builder().categoryId(categoryId1).indicatorId(indicatorId1_2).status(status).score(null)
                        .supportingText("sp8").build(),
                HealthIndicatorDto.builder().categoryId(categoryId2).indicatorId(indicatorId2_1).status(status).score(5).supportingText("sp9").build(),
                HealthIndicatorDto.builder().categoryId(categoryId2).indicatorId(indicatorId2_2).status(status).score(null).supportingText("sp10").build(),
                HealthIndicatorDto.builder().categoryId(categoryId3).indicatorId(indicatorId3_1).status(status).score(null).supportingText("sp11").build(),
                HealthIndicatorDto.builder().categoryId(categoryId3).indicatorId(indicatorId3_2).status(status).score(null).supportingText("sp12").build());
        setupHealthIndicatorsForCountry(uk, healthIndicatorDtos, year);
        addCountryPhase(uk, 3, year);

        healthIndicatorDtos = asList(
                HealthIndicatorDto.builder().categoryId(categoryId1).indicatorId(indicatorId1_1).status(status).score(null).supportingText("sp13").build(),
                HealthIndicatorDto.builder().categoryId(categoryId1).indicatorId(indicatorId1_2).status(status).score(null).supportingText("sp14").build(),
                HealthIndicatorDto.builder().categoryId(categoryId2).indicatorId(indicatorId2_1).status(status).score(null).supportingText("sp15").build(),
                HealthIndicatorDto.builder().categoryId(categoryId2).indicatorId(indicatorId2_2).status(status).score(null).supportingText("sp16").build(),
                HealthIndicatorDto.builder().categoryId(categoryId3).indicatorId(indicatorId3_1).status(status).score(null).supportingText("sp17").build(),
                HealthIndicatorDto.builder().categoryId(categoryId3).indicatorId(indicatorId3_2).status(status).score(null).supportingText("sp18").build());

        setupHealthIndicatorsForCountry(pakistan, healthIndicatorDtos, year);
        addCountryPhase(pakistan, null, year);

        SimpleDateFormat DateFor = new SimpleDateFormat("MMMM yyyy");
        String expectedUpdatedDate = DateFor.format(new Date());

        Response response = given()
                .contentType("application/json")
                .header(USER_LANGUAGE, "en")
                .when()
                .get("http://localhost:" + port + "/countries_health_indicator_scores?phase=3" + "&year=" + year);

        CountriesHealthScoreDto countryHealthScoreDto = response.getBody().as(CountriesHealthScoreDto.class);
        assertEquals(countryHealthScoreDto.getCountryHealthScores().get(0).getUpdatedDate(), expectedUpdatedDate);

        assertCountryHealthScoreResponse(response.asString(), "countries_health_indicators_filter_by_phase.json");
    }

    @Test
    public void shouldGetOverAllScoreForAllCountriesInFrench() throws Exception {
        String india = "IND";
        String uk = "GBR";
        String pakistan = "PAK";
        String year = "2023";
        Integer categoryId1 = 1;
        Integer categoryId2 = 2;
        Integer categoryId3 = 3;
        Integer indicatorId1_1 = 1;
        Integer indicatorId1_2 = 2;
        Integer indicatorId2_1 = 3;
        Integer indicatorId2_2 = 4;
        Integer indicatorId3_1 = 5;
        Integer indicatorId3_2 = 6;

        String status = "PUBLISHED";
        addCountrySummary(india, status, "IN", year);
        addCountrySummary(uk, status, "UK", year);
        addCountrySummary(pakistan, status, "PK", year);

        List<HealthIndicatorDto> healthIndicatorDtos = asList(
                HealthIndicatorDto.builder().categoryId(categoryId1).indicatorId(indicatorId1_1).status(status).score(1).supportingText("sp1").build(),
                HealthIndicatorDto.builder().categoryId(categoryId1).indicatorId(indicatorId1_2).status(status).score(2).supportingText("sp2").build(),
                HealthIndicatorDto.builder().categoryId(categoryId2).indicatorId(indicatorId2_1).status(status).score(3).supportingText("sp3").build(),
                HealthIndicatorDto.builder().categoryId(categoryId2).indicatorId(indicatorId2_2).status(status).score(null).supportingText("sp4").build(),
                HealthIndicatorDto.builder().categoryId(categoryId3).indicatorId(indicatorId3_1).status(status).score(null).supportingText("sp5").build(),
                HealthIndicatorDto.builder().categoryId(categoryId3).indicatorId(indicatorId3_2).status(status).score(null).supportingText("sp6").build());

        setupHealthIndicatorsForCountry(india, healthIndicatorDtos, year);
        addCountryPhase(india, 2, year);

        healthIndicatorDtos = asList(
                HealthIndicatorDto.builder().categoryId(categoryId1).indicatorId(indicatorId1_1).status(status).score(4).supportingText("sp7").build(),
                HealthIndicatorDto.builder().categoryId(categoryId1).indicatorId(indicatorId1_2).status(status).score(5).supportingText("sp8").build(),
                HealthIndicatorDto.builder().categoryId(categoryId2).indicatorId(indicatorId2_1).status(status).score(3).supportingText("sp9").build(),
                HealthIndicatorDto.builder().categoryId(categoryId2).indicatorId(indicatorId2_2).status(status).score(null).supportingText("sp10").build(),
                HealthIndicatorDto.builder().categoryId(categoryId3).indicatorId(indicatorId3_1).status(status).score(null).supportingText("sp11").build(),
                HealthIndicatorDto.builder().categoryId(categoryId3).indicatorId(indicatorId3_2).status(status).score(null).supportingText("sp12").build());

        setupHealthIndicatorsForCountry(uk, healthIndicatorDtos, year);
        addCountryPhase(uk, 4, year);

        healthIndicatorDtos = asList(
                HealthIndicatorDto.builder().categoryId(categoryId1).indicatorId(indicatorId1_1).status(status).score(null).supportingText("sp13").build(),
                HealthIndicatorDto.builder().categoryId(categoryId1).indicatorId(indicatorId1_2).status(status).score(null).supportingText("sp14").build(),
                HealthIndicatorDto.builder().categoryId(categoryId2).indicatorId(indicatorId2_1).status(status).score(null).supportingText("sp15").build(),
                HealthIndicatorDto.builder().categoryId(categoryId2).indicatorId(indicatorId2_2).status(status).score(null).supportingText("sp16").build(),
                HealthIndicatorDto.builder().categoryId(categoryId3).indicatorId(indicatorId3_1).status(status).score(null).supportingText("sp17").build(),
                HealthIndicatorDto.builder().categoryId(categoryId3).indicatorId(indicatorId3_2).status(status).score(null).supportingText("sp18").build());

        setupHealthIndicatorsForCountry(pakistan, healthIndicatorDtos, year);
        addCountryPhase(pakistan, null, year);

        SimpleDateFormat DateFor = new SimpleDateFormat("MMMM yyyy");
        String expectedUpdatedDate = DateFor.format(new Date());

        Response response = given()
                .contentType("application/json")
                .header(USER_LANGUAGE, "fr")
                .when()
                .get("http://localhost:" + port + "/countries_health_indicator_scores?year=" + year);

        CountriesHealthScoreDto countryHealthScoreDto = response.getBody().as(CountriesHealthScoreDto.class);
        assertEquals(countryHealthScoreDto.getCountryHealthScores().get(0).getUpdatedDate(), expectedUpdatedDate);

        assertCountryHealthScoreResponse(response.asString(), "countries_health_indicators_fr.json");
    }

    @Test
    public void shouldNotCalculateSubIndicatorsScoreForOverallScore() throws Exception {
        String india = "IND";
        String pakistan = "PAK";
        String year = "2023";
        Integer categoryId1 = 1;
        Integer categoryId2 = 2;
        Integer categoryId7 = 7;
        Integer indicatorId1_1 = 1;
        Integer indicatorId1_2 = 2;
        Integer indicatorId2_1 = 3;
        Integer indicatorId7_1 = 27;
        Integer indicatorId7_2 = 28;
        Integer indicatorId7_3 = 29;

        String status = "PUBLISHED";
        addCountrySummary(india, status, "IN", year);
        addCountrySummary(pakistan, status, "PK", year);
        List<HealthIndicatorDto> healthIndicatorDtos = asList(
                HealthIndicatorDto.builder().categoryId(categoryId1).indicatorId(indicatorId1_1).status(status).score(1).supportingText("sp1").build(),
                HealthIndicatorDto.builder().categoryId(categoryId1).indicatorId(indicatorId1_2).status(status).score(1).supportingText("sp2").build(),
                HealthIndicatorDto.builder().categoryId(categoryId2).indicatorId(indicatorId2_1).status(status).score(1).supportingText("sp3").build(),
                HealthIndicatorDto.builder().categoryId(categoryId7).indicatorId(indicatorId7_1).status(status).score(null).supportingText("sp4").build(),
                HealthIndicatorDto.builder().categoryId(categoryId7).indicatorId(indicatorId7_2).status(status).score(null).supportingText("sp5").build(),
                HealthIndicatorDto.builder().categoryId(categoryId7).indicatorId(indicatorId7_3).status(status).score(null).supportingText("sp6").build());

        setupHealthIndicatorsForCountry(india, healthIndicatorDtos, year);
        healthIndicatorDtos = asList(
                HealthIndicatorDto.builder().categoryId(categoryId1).indicatorId(indicatorId1_1).status(status).score(1).supportingText("sp1").build(),
                HealthIndicatorDto.builder().categoryId(categoryId1).indicatorId(indicatorId1_2).status(status).score(1).supportingText("sp2").build(),
                HealthIndicatorDto.builder().categoryId(categoryId2).indicatorId(indicatorId2_1).status(status).score(1).supportingText("sp3").build(),
                HealthIndicatorDto.builder().categoryId(categoryId7).indicatorId(indicatorId7_1).status(status).score(5).supportingText("sp4").build(),
                HealthIndicatorDto.builder().categoryId(categoryId7).indicatorId(indicatorId7_2).status(status).score(5).supportingText("sp5").build(),
                HealthIndicatorDto.builder().categoryId(categoryId7).indicatorId(indicatorId7_3).status(status).score(5).supportingText("sp6").build());

        setupHealthIndicatorsForCountry(pakistan, healthIndicatorDtos, year);

        Response calculatePhase = given()
                .contentType("application/json")
                .header(USER_LANGUAGE, "en")
                .when()
                .get("http://localhost:" + port + "/admin/countries/calculate_phase?year=" + year);

        SimpleDateFormat DateFor = new SimpleDateFormat("MMMM yyyy");
        String expectedUpdatedDate = DateFor.format(new Date());

        Response response = given()
                .contentType("application/json")
                .header(USER_LANGUAGE, "en")
                .when()
                .get("http://localhost:" + port + "/countries_health_indicator_scores?year=" + year);

        CountriesHealthScoreDto countryHealthScoreDto = response.getBody().as(CountriesHealthScoreDto.class);
        assertEquals(countryHealthScoreDto.getCountryHealthScores().get(0).getUpdatedDate(), expectedUpdatedDate);

        assertCountryHealthScoreResponse(response.asString(), "countries_health_indicators_sub_indicator_score.json");

    }
}
