package it.gdhi.integration;

import io.restassured.response.Response;
import it.gdhi.GdhiServiceApplication;
import it.gdhi.dto.CountryHealthScoreDto;
import it.gdhi.dto.GdhiQuestionnaire;
import it.gdhi.dto.HealthIndicatorDto;
import it.gdhi.model.Country;
import it.gdhi.model.CountryPhase;
import it.gdhi.model.CountryResourceLink;
import it.gdhi.model.CountrySummary;
import it.gdhi.model.id.CountryPhaseId;
import it.gdhi.model.id.CountryResourceLinkId;
import it.gdhi.model.id.CountrySummaryId;
import it.gdhi.repository.ICountryPhaseRepository;
import it.gdhi.repository.ICountryRepository;
import it.gdhi.repository.ICountrySummaryRepository;
import it.gdhi.service.CountryHealthDataService;
import it.gdhi.service.MailerService;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.text.SimpleDateFormat;
import java.time.Year;
import java.util.*;
import java.util.stream.Collectors;

import static io.restassured.RestAssured.given;
import static it.gdhi.utils.LanguageCode.USER_LANGUAGE;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = GdhiServiceApplication.class)
@ActiveProfiles("test")
public class CountryIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private ICountryRepository iCountryRepository;

    @Value("${local.server.port}")
    private int port;

    @Autowired
    private ICountrySummaryRepository countrySummaryRepository;

    @Autowired
    private ICountryPhaseRepository countryPhaseRepository;

    @Autowired
    private MailerService mailerService;

    @Autowired
    CountryHealthDataService countryHealthDataService;

    private UUID INDIA_UUID = null;
    private String INDIA_ID = "IND";

    @BeforeEach
    public void setup() {
        INDIA_UUID = iCountryRepository.findById(INDIA_ID).getUniqueId();
    }

    @Test
    public void shouldGetCountryListInGivenUserLanguage() throws Exception {
        Response response = given()
                .contentType("application/json")
                .header("USER_LANGUAGE", "fr")
                .when()
                .get("http://localhost:" + port + "/countries");

        String expectedJSON = expectedResponseJson("countries_in_french.json");
        ArrayList expectedCountries = getMapper().readValue(expectedJSON, ArrayList.class);

        ArrayList countryResponseList = getMapper().readValue(response.asString(), ArrayList.class);
        List countriesWithIdAndName = mapToCountryIdAndName(countryResponseList);
        String translatedCountriesJson = getMapper().writeValueAsString(countriesWithIdAndName);
        ArrayList actualCountries = getMapper().readValue(translatedCountriesJson, ArrayList.class);

        assertTrue(expectedCountries.containsAll(actualCountries));
    }

    @Test
    public void shouldNotReturnErrorIfUserLanguageCodeNotFound() {
        Response response = given()
                .contentType("application/json")
                .header(USER_LANGUAGE, "ls")
                .get("http://localhost:" + port + "/countries");

        assertEquals(response.statusCode(), 200);
    }

    @Test
    public void shouldGetHealthIndicatorForACountry() throws Exception {
        String countryId = INDIA_ID;
        String status = "PUBLISHED";
        String alpha2Code = "IN";
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

        addCountrySummary(countryId, "India", status, alpha2Code, INDIA_UUID, new ArrayList<>(), year);

        List<HealthIndicatorDto> healthIndicatorDtos = asList(
                HealthIndicatorDto.builder().categoryId(categoryId1).indicatorId(indicatorId1_1).status(status).score(1).supportingText("sp1").build(),
                HealthIndicatorDto.builder().categoryId(categoryId1).indicatorId(indicatorId1_2).status(status).score(2).supportingText("sp1").build(),
                HealthIndicatorDto.builder().categoryId(categoryId2).indicatorId(indicatorId2_1).status(status).score(3).supportingText("sp1").build(),
                HealthIndicatorDto.builder().categoryId(categoryId2).indicatorId(indicatorId2_2).status(status).score(null).supportingText("sp1").build(),
                HealthIndicatorDto.builder().categoryId(categoryId3).indicatorId(indicatorId3_1).status(status).score(null).supportingText("sp1").build(),
                HealthIndicatorDto.builder().categoryId(categoryId3).indicatorId(indicatorId3_2).status(status).score(null).supportingText("sp1").build());

        setupHealthIndicatorsForCountry(countryId, healthIndicatorDtos, year);
        setUpCountryPhase(countryId, 2, year);

        SimpleDateFormat DateFor = new SimpleDateFormat("MMMM yyyy");
        String expectedUpdatedDate = DateFor.format(new Date());

        Response response = given()
                .contentType("application/json")
                .header(USER_LANGUAGE, "en")
                .when()
                .get("http://localhost:" + port + "/countries/IND/health_indicators?year=" + year);

        CountryHealthScoreDto countryHealthScoreDto = response.getBody().as(CountryHealthScoreDto.class);
        assertEquals(countryHealthScoreDto.getUpdatedDate(), expectedUpdatedDate);

        assertResponse(response.asString(), "health_indicators.json");
    }

    @Test
    public void shouldGetHealthIndicatorForACountryInPortuguese() throws Exception {
        String countryId = INDIA_ID;
        String status = "PUBLISHED";
        String alpha2Code = "IN";
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

        addCountrySummary(countryId, "India", status, alpha2Code, INDIA_UUID, new ArrayList<>(), year);

        List<HealthIndicatorDto> healthIndicatorDtos = asList(
                HealthIndicatorDto.builder().categoryId(categoryId1).indicatorId(indicatorId1_1).status(status).score(1).supportingText("sp1").build(),
                HealthIndicatorDto.builder().categoryId(categoryId1).indicatorId(indicatorId1_2).status(status).score(2).supportingText("sp1").build(),
                HealthIndicatorDto.builder().categoryId(categoryId2).indicatorId(indicatorId2_1).status(status).score(3).supportingText("sp1").build(),
                HealthIndicatorDto.builder().categoryId(categoryId2).indicatorId(indicatorId2_2).status(status).score(null).supportingText("sp1").build(),
                HealthIndicatorDto.builder().categoryId(categoryId3).indicatorId(indicatorId3_1).status(status).score(null).supportingText("sp1").build(),
                HealthIndicatorDto.builder().categoryId(categoryId3).indicatorId(indicatorId3_2).status(status).score(null).supportingText("sp1").build());

        setupHealthIndicatorsForCountry(countryId, healthIndicatorDtos, year);
        setUpCountryPhase(countryId, 2, year);

        SimpleDateFormat DateFor = new SimpleDateFormat("MMMM yyyy");
        String expectedUpdatedDate = DateFor.format(new Date());

        Response response = given()
                .contentType("application/json")
                .header(USER_LANGUAGE, "pt")
                .when()
                .get("http://localhost:" + port + "/countries/IND/health_indicators?year=" + year);

        CountryHealthScoreDto countryHealthScoreDto = response.getBody().as(CountryHealthScoreDto.class);
        assertEquals(countryHealthScoreDto.getUpdatedDate(), expectedUpdatedDate);

        assertResponse(response.asString(), "health_indicators_pt.json");
    }

    @Test
    public void shouldGetHealthIndicatorForACountryInSpanish() throws Exception {
        String countryId = INDIA_ID;
        String status = "PUBLISHED";
        String alpha2Code = "IN";
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

        addCountrySummary(countryId, "India", status, alpha2Code, INDIA_UUID, new ArrayList<>(), year);

        List<HealthIndicatorDto> healthIndicatorDtos = asList(
                HealthIndicatorDto.builder().categoryId(categoryId1).indicatorId(indicatorId1_1).status(status).score(1).supportingText("sp1").build(),
                HealthIndicatorDto.builder().categoryId(categoryId1).indicatorId(indicatorId1_2).status(status).score(2).supportingText("sp1").build(),
                HealthIndicatorDto.builder().categoryId(categoryId2).indicatorId(indicatorId2_1).status(status).score(3).supportingText("sp1").build(),
                HealthIndicatorDto.builder().categoryId(categoryId2).indicatorId(indicatorId2_2).status(status).score(4).supportingText("sp1").build(),
                HealthIndicatorDto.builder().categoryId(categoryId3).indicatorId(indicatorId3_1).status(status).score(null).supportingText("sp1").build(),
                HealthIndicatorDto.builder().categoryId(categoryId3).indicatorId(indicatorId3_2).status(status).score(null).supportingText("sp1").build());

        setupHealthIndicatorsForCountry(countryId, healthIndicatorDtos, year);
        setUpCountryPhase(countryId, 2, year);

        SimpleDateFormat DateFor = new SimpleDateFormat("MMMM yyyy");
        String expectedUpdatedDate = DateFor.format(new Date());

        Response response = given()
                .contentType("application/json")
                .header(USER_LANGUAGE, "es")
                .when()
                .get("http://localhost:" + port + "/countries/IND/health_indicators?year=" + year);

        CountryHealthScoreDto countryHealthScoreDto = response.getBody().as(CountryHealthScoreDto.class);
        assertEquals(countryHealthScoreDto.getUpdatedDate(), expectedUpdatedDate);

        assertResponse(response.asString(), "health_indicators_es.json");
    }

    @Test
    public void shouldGetCountrySummary() throws Exception {
        String countryId = INDIA_ID;
        String status = "PUBLISHED";
        String alpha2code = "IN";
        String year = "Version1";


        CountryResourceLink countryResourceLink1 = new CountryResourceLink(new CountryResourceLinkId(countryId,
                "link1", year), new Date(), null,status);
        CountryResourceLink countryResourceLink2 = new CountryResourceLink(new CountryResourceLinkId(countryId,
                "link2", year), new Date(), null, status);

        List<CountryResourceLink> countryResourceLinks = asList(countryResourceLink1, countryResourceLink2);

        addCountrySummary(countryId, "India", status, alpha2code, INDIA_UUID, countryResourceLinks, "Version1");

        Response response = given()
                .contentType("application/json")
                .when()
                .get("http://localhost:" + port + "/countries/IND/country_summary");

        assertResponse(response.asString(), "country_summary.json");
    }

    @Test
    public void shouldGetCountryDetails() throws Exception {
        String countryId = INDIA_ID;
        String status = "DRAFT";
        String alpha2code = "IN";
        String currentYear = getCurrentYear();
        String dataAvailableForYear = "2023";

        CountryResourceLink countryResourceLink1 = new CountryResourceLink(new CountryResourceLinkId(countryId,
                "www.example.com", currentYear), new Date(), null,status);
        List<CountryResourceLink> countryResourceLinks = asList(countryResourceLink1);

        addCountrySummary(countryId, "India", status, alpha2code, INDIA_UUID, countryResourceLinks, currentYear);

        List<HealthIndicatorDto> healthIndicatorDtos = asList(
                HealthIndicatorDto.builder().categoryId(1).indicatorId(1).status(status).score(-1).supportingText("blah@blah.com").build(),
                HealthIndicatorDto.builder().categoryId(1).indicatorId(2).status(status).score(5).supportingText("blah@blah.com").build(),
                HealthIndicatorDto.builder().categoryId(1).indicatorId(30).status(status).score(5).supportingText("blah@blah.com").build(),
                HealthIndicatorDto.builder().categoryId(1).indicatorId(31).status(status).score(5).supportingText("blah@blah.com").build(),
                HealthIndicatorDto.builder().categoryId(1).indicatorId(32).status(status).score(5).supportingText("blah@blah.com").build(),
                HealthIndicatorDto.builder().categoryId(1).indicatorId(33).status(status).score(5).supportingText("blah@blah.com").build(),
                HealthIndicatorDto.builder().categoryId(2).indicatorId(3).status(status).score(5).supportingText("blah@blah.com").build(),
                HealthIndicatorDto.builder().categoryId(2).indicatorId(37).status(status).score(-1).supportingText("blah@blah.com").build(),
                HealthIndicatorDto.builder().categoryId(2).indicatorId(4).status(status).score(-1).supportingText("blah@blah.com").build(),
                HealthIndicatorDto.builder().categoryId(2).indicatorId(38).status(status).score(-1).supportingText("blah@blah.com").build(),
                HealthIndicatorDto.builder().categoryId(3).indicatorId(5).status(status).score(5).supportingText("blah@blah.com").build(),
                HealthIndicatorDto.builder().categoryId(3).indicatorId(6).status(status).score(1).supportingText("blah@blah.com").build(),
                HealthIndicatorDto.builder().categoryId(3).indicatorId(7).status(status).score(4).supportingText("blah@blah.com").build(),
                HealthIndicatorDto.builder().categoryId(3).indicatorId(36).status(status).score(2).supportingText("blah@blah.com").build(),
                HealthIndicatorDto.builder().categoryId(3).indicatorId(8).status(status).score(2).supportingText("blah@blah.com").build(),
                HealthIndicatorDto.builder().categoryId(4).indicatorId(9).status(status).score(-1).supportingText("blah@blah.com").build(),
                HealthIndicatorDto.builder().categoryId(4).indicatorId(10).status(status).score(-1).supportingText("blah@blah.com").build(),
                HealthIndicatorDto.builder().categoryId(4).indicatorId(11).status(status).score(-1).supportingText("blah@blah.com").build(),
                HealthIndicatorDto.builder().categoryId(4).indicatorId(12).status(status).score(-1).supportingText("blah@blah.com").build(),
                HealthIndicatorDto.builder().categoryId(5).indicatorId(13).status(status).score(-1).supportingText("blah@blah.com").build(),
                HealthIndicatorDto.builder().categoryId(5).indicatorId(14).status(status).score(-1).supportingText("blah@blah.com").build(),
                HealthIndicatorDto.builder().categoryId(6).indicatorId(15).status(status).score(3).supportingText("blah@blah.com").build(),
                HealthIndicatorDto.builder().categoryId(6).indicatorId(16).status(status).score(5).supportingText("blah@blah.com").build(),
                HealthIndicatorDto.builder().categoryId(7).indicatorId(17).status(status).score(-1).supportingText("blah@blah.com").build(),
                HealthIndicatorDto.builder().categoryId(7).indicatorId(18).status(status).score(-1).supportingText("blah@blah.com").build(),
                HealthIndicatorDto.builder().categoryId(7).indicatorId(19).status(status).score(-1).supportingText("blah@blah.com").build(),
                HealthIndicatorDto.builder().categoryId(7).indicatorId(27).status(status).score(-1).supportingText("blah@blah.com").build(),
                HealthIndicatorDto.builder().categoryId(7).indicatorId(28).status(status).score(-1).supportingText("blah@blah.com").build(),
                HealthIndicatorDto.builder().categoryId(7).indicatorId(29).status(status).score(-1).supportingText("blah@blah.com").build(),
                HealthIndicatorDto.builder().categoryId(7).indicatorId(34).status(status).score(-1).supportingText("blah@blah.com").build(),
                HealthIndicatorDto.builder().categoryId(7).indicatorId(35).status(status).score(-1).supportingText("blah@blah.com").build());


        setupHealthIndicatorsForCountry(countryId, healthIndicatorDtos, currentYear);

        SimpleDateFormat DateFor = new SimpleDateFormat("MMMM yyyy");
        String expectedUpdatedDate = DateFor.format(new Date());

        Response response = given()
                .contentType("application/json")
                .when()
                .get("http://localhost:" + port + "/countries/" + INDIA_UUID.toString());
        String responseJson = response.asString();

        GdhiQuestionnaire gdhiQuestionnaire = response.getBody().as(GdhiQuestionnaire.class);
        assertEquals(gdhiQuestionnaire.getUpdatedDate(), expectedUpdatedDate);
        assertEquals(gdhiQuestionnaire.getCurrentYear(), currentYear);

        assertResponse(responseJson, "country_body.json");
    }

    @Test
    public void shouldSaveCountryDetailsWhenNoDateIsProvided() throws Exception {
        String currentYear = getCurrentYear();
        addCountrySummary(INDIA_ID, "India", "NEW", "IN", UUID.randomUUID(), new ArrayList<>(), currentYear);

        Response response = given()
                .contentType("application/json")
                .when()
                .body(expectedResponseJson("country_body_no_date.json"))
                .post("http://localhost:" + port + "/countries/save");

        assertEquals(200, response.getStatusCode());
    }

    @Test
    public void shouldSaveCountryDetailsWhenNullResourceIsProvided() throws Exception {
        String currentYear = getCurrentYear();
        addCountrySummary(INDIA_ID, "India", "NEW", "IN", UUID.randomUUID(), new ArrayList<>(), currentYear);
        mailerService = mock(MailerService.class);
        doNothing().when(mailerService).send(any(Country.class), anyString(), anyString(), anyString());

        Response response = given()
                .contentType("application/json")
                .when()
                .body(expectedResponseJson("country_body_null_resource.json"))
                .post("http://localhost:" + port + "/countries/save");

        assertEquals(200, response.getStatusCode());
    }

    @Test
    public void shouldSaveAndEditCountryDetails() throws Exception {
        String currentYear = getCurrentYear();
        addCountrySummary(INDIA_ID, "India", "NEW", "IN", UUID.randomUUID(), new ArrayList<>(), currentYear);
        mailerService = mock(MailerService.class);
        doNothing().when(mailerService).send(any(Country.class), anyString(), anyString(), anyString());
        String dataAvailableForYear = "2023";

        SimpleDateFormat DateFor = new SimpleDateFormat("MMMM yyyy");
        String expectedUpdatedDate = DateFor.format(new Date());

        Response response = given()
                .contentType("application/json")
                .when()
                .body(expectedResponseJson("country_body.json"))
                .post("http://localhost:" + port + "/countries/save");

        assertEquals(200, response.getStatusCode());

        response = given()
                .contentType("application/json")
                .when()
                .get("http://localhost:" + port + "/countries/" + INDIA_UUID.toString());
        String responseJson = response.asString();
        assertResponse(responseJson, "country_body.json");

        GdhiQuestionnaire gdhiQuestionnaire = response.getBody().as(GdhiQuestionnaire.class);
        assertEquals(gdhiQuestionnaire.getUpdatedDate(), expectedUpdatedDate);
        assertEquals(gdhiQuestionnaire.getCurrentYear(), currentYear);

        response = given()
                .contentType("application/json")
                .when()
                .body(expectedResponseJson("country_body_edit.json"))
                .post("http://localhost:" + port + "/countries/save");

        assertEquals(200, response.getStatusCode());

        response = given()
                .contentType("application/json")
                .when()
                .get("http://localhost:" + port + "/countries/" + INDIA_UUID.toString());
        responseJson = response.asString();
        gdhiQuestionnaire = response.getBody().as(GdhiQuestionnaire.class);
        assertEquals(gdhiQuestionnaire.getUpdatedDate(), expectedUpdatedDate);
        assertEquals(gdhiQuestionnaire.getCurrentYear(), currentYear);

        assertResponse(responseJson, "country_body_edit.json");
    }

    @Test
    public void shouldSubmitCountryDetails() throws Exception {
        String currentYear = getCurrentYear();

        SimpleDateFormat DateFor = new SimpleDateFormat("MMMM yyyy");
        String expectedUpdatedDate = DateFor.format(new Date());

        addCountrySummary(INDIA_ID, "India", "NEW", "IN", UUID.randomUUID(), new ArrayList<>(), currentYear);

        Response response = given()
                .contentType("application/json")
                .when()
                .body(expectedResponseJson("country_body_edit.json"))
                .post("http://localhost:" + port + "/countries/submit");

        assertEquals(201, response.getStatusCode());

        response = given()
                .contentType("application/json")
                .when()
                .get("http://localhost:" + port + "/countries/" + INDIA_UUID.toString());
        String responseJson = response.asString();

        GdhiQuestionnaire gdhiQuestionnaire = response.getBody().as(GdhiQuestionnaire.class);
        assertEquals(gdhiQuestionnaire.getUpdatedDate(), expectedUpdatedDate);
        assertEquals(gdhiQuestionnaire.getCurrentYear(), currentYear);

        assertResponse(responseJson, "country_body_review_pending.json");
    }

    @Test
    public void shouldNotSubmitCountryDetailsForInvalidHealthIndicators() throws Exception {
        addCountrySummary(INDIA_ID, null, "NEW", "IN", UUID.randomUUID(), new ArrayList<>(), "Version1");

        Response response = given()
                .contentType("application/json")
                .body(expectedResponseJson("country_body_no_date.json"))
                .when()
                .post("http://localhost:" + port + "/countries/submit");

        assertEquals(400, response.getStatusCode());
    }

    @Test
    public void shouldDeleteCountryData() throws Exception {
        String countryId = INDIA_ID;
        String status = "REVIEW_PENDING";
        String alpha2code = "IN";
        String year = "2020";

        CountryResourceLink countryResourceLink1 = new CountryResourceLink(new CountryResourceLinkId(countryId,
                "link1", year), new Date(), null,status);
        CountryResourceLink countryResourceLink2 = new CountryResourceLink(new CountryResourceLinkId(countryId,
                "link2", year), new Date(), null, status);
        Integer categoryId1 = 1;
        Integer categoryId2 = 2;
        Integer categoryId3 = 3;
        Integer indicatorId1_1 = 1;
        Integer indicatorId1_2 = 2;
        Integer indicatorId2_1 = 3;
        Integer indicatorId2_2 = 4;
        Integer indicatorId3_1 = 5;
        Integer indicatorId3_2 = 6;
        List<CountryResourceLink> countryResourceLinks = asList(countryResourceLink1, countryResourceLink2);

        addCountrySummary(countryId, "India", status, alpha2code, INDIA_UUID, countryResourceLinks, year);

        List<HealthIndicatorDto> healthIndicatorDtos = asList(
                HealthIndicatorDto.builder().categoryId(categoryId1).indicatorId(indicatorId1_1).status(status).score(1).supportingText("sp1").build(),
                HealthIndicatorDto.builder().categoryId(categoryId1).indicatorId(indicatorId1_2).status(status).score(2).supportingText("sp1").build(),
                HealthIndicatorDto.builder().categoryId(categoryId2).indicatorId(indicatorId2_1).status(status).score(3).supportingText("sp1").build(),
                HealthIndicatorDto.builder().categoryId(categoryId2).indicatorId(indicatorId2_2).status(status).score(null).supportingText("sp1").build(),
                HealthIndicatorDto.builder().categoryId(categoryId3).indicatorId(indicatorId3_1).status(status).score(null).supportingText("sp1").build(),
                HealthIndicatorDto.builder().categoryId(categoryId3).indicatorId(indicatorId3_2).status(status).score(null).supportingText("sp1").build());

        setupHealthIndicatorsForCountry(countryId, healthIndicatorDtos, year);

        Response response = given()
                .contentType("application/json")
                .when()
                .delete("http://localhost:" + port + "/countries/" + INDIA_UUID.toString() + "/delete/year?=" + year);

        assertEquals(200, response.getStatusCode());
    }

    @Test
    public void shouldGetAllCountrySummaries() throws Exception {
        UUID indiaUUID = iCountryRepository.findById(INDIA_ID).getUniqueId();
        ;
        UUID australiaUUID = iCountryRepository.findById("AUS").getUniqueId();
        ;
        String currentYear = this.getCurrentYear();
        addCountrySummary("AUS", "AUSTRALIA", "NEW", "AU", australiaUUID, emptyList(), currentYear);
        addCountrySummary("AUS", "AUSTRALIA", "DRAFT", "AU", australiaUUID, emptyList(), currentYear);
        addCountrySummary("AUS", "AUSTRALIA", "REVIEW_PENDING", "AU", australiaUUID, emptyList(), currentYear);
        addCountrySummary("AUS", "AUSTRALIA", "PUBLISHED", "AU", australiaUUID, emptyList(), currentYear);
        addCountrySummary(INDIA_ID, "INDIA", "NEW", "IN", indiaUUID, emptyList(), currentYear);

        Response response = given()
                .contentType("application/json")
                .when()
                .get("http://localhost:" + port + "/countries/country_status_summaries");

        String expectedJson = "{\"currentYear\": \"2023\",\n" +
                "  \"NEW\": [{\n" +
                "    \"countryName\": \"India\",\n" +
                "    \"countryUUID\": \"" + indiaUUID.toString() + "\",\n" +
                "    \"status\": \"NEW\",\n" +
                "    \"contactName\": \"contactName\",\n" +
                "    \"contactEmail\": \"email\"\n" +
                "  }],\n" +
                "  \"PUBLISHED\": [{\n" +
                "    \"countryName\": \"Australia\",\n" +
                "    \"countryUUID\": \"" + australiaUUID.toString() + "\",\n" +
                "    \"status\": \"PUBLISHED\",\n" +
                "    \"contactName\": \"contactName\",\n" +
                "    \"contactEmail\": \"email\"\n" +
                "  }]\n" +
                "}";
        assertStringResponse(response.asString(), expectedJson);
    }

    @Test
    public void shouldGetGlobalAvgBenchmarkForGivenCountry() throws Exception {
        Integer categoryId1 = 1;
        Integer indicatorId1 = 1;

        String year = "2023";
        addCountrySummary(INDIA_ID, "INDIA", "PUBLISHED", "IN", INDIA_UUID, emptyList(), year);
        addCountrySummary("PAK", "PAKISTAN", "PUBLISHED", "PK", UUID.randomUUID(), emptyList(), year);
        addCountrySummary("ARE", "UNITED ARAB EMIRATES", "PUBLISHED", "UE", UUID.randomUUID(), emptyList(), year);
        addCountrySummary("LKA", "SRI LANKA", "DRAFT", "SL", UUID.randomUUID(), emptyList(), year);
        addCountrySummary(INDIA_ID, "INDIA", "NEW", "IN", UUID.randomUUID(), emptyList(), year);


        List<HealthIndicatorDto> healthIndicatorDto = setUpHealthIndicatorDto(categoryId1, indicatorId1, "PUBLISHED", 1);
        setupHealthIndicatorsForCountry(INDIA_ID, healthIndicatorDto, year);

        List<HealthIndicatorDto> healthIndicatorDto1 = setUpHealthIndicatorDto(categoryId1, indicatorId1, "PUBLISHED", 2);
        setupHealthIndicatorsForCountry("PAK", healthIndicatorDto1, year);

        List<HealthIndicatorDto> healthIndicatorDto2 = setUpHealthIndicatorDto(categoryId1, indicatorId1, "PUBLISHED", -1);
        setupHealthIndicatorsForCountry("ARE", healthIndicatorDto2, year);

        List<HealthIndicatorDto> healthIndicatorDto3 = setUpHealthIndicatorDto(categoryId1, indicatorId1, "DRAFT", 5);
        setupHealthIndicatorsForCountry("LKA", healthIndicatorDto3, year);

        setUpCountryPhase(INDIA_ID, 1, year);
        setUpCountryPhase("PAK", 2, year);
        setUpCountryPhase("ARE", null, year);
        setUpCountryPhase("LKA", 5, year);

        Response response = given()
                .contentType("application/json")
                .when()
                .get("http://localhost:" + port + "/bff/countries/IND/benchmark/-1?year=" + year);

        assertEquals(200, response.getStatusCode());

        assertEquals(response.asString(), "{\"1\":{\"benchmarkScore\":2}}");
    }

    private void setUpCountryPhase(String countryId, Integer countryPhaseValue, String year) {
        CountryPhaseId countryPhaseId = new CountryPhaseId(countryId, year);
        CountryPhase countryPhase = CountryPhase.builder().countryPhaseId(countryPhaseId).countryOverallPhase(countryPhaseValue).build();
        countryPhaseRepository.save(countryPhase);
    }

    private List<HealthIndicatorDto> setUpHealthIndicatorDto(Integer indicatorId, Integer categoryId, String status, Integer score) {
        return asList(HealthIndicatorDto.builder()
                .categoryId(categoryId)
                .indicatorId(indicatorId)
                .status(status)
                .score(score)
                .supportingText("Some text")
                .build());
    }

    private void addCountrySummary(String countryId, String countryName, String status, String alpha2Code, UUID
            countryUUID, List<CountryResourceLink> countryResourceLinks, String year) throws Exception {
        CountrySummary countrySummary = CountrySummary.builder()
                .countrySummaryId(new CountrySummaryId(countryId, year))
                .summary("summary")
                .country(new Country(countryId, countryName, countryUUID, alpha2Code))
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
                .govtApproved(true)
                .countryResourceLinks(countryResourceLinks)
                .status(status)
                .build();
        countrySummaryRepository.save(countrySummary);
    }

    /* Country UUID is auto generated and different in all environments, hence comparing only id and name. */
    private List mapToCountryIdAndName(ArrayList actualList) {
        return (List) actualList.stream().map(c -> {
                    HashMap c1 = (HashMap) c;
                    Pair<Object, Object> pair = Pair.of(c1.get("id"), c1.get("name"));
                    return pair;
                })
                .sorted(Comparator.naturalOrder())
                .collect(Collectors.toList());
    }

    private String getCurrentYear() {
        int currentYear = Year.now().getValue();
        String year = new String(String.valueOf(currentYear));
        return year;
    }

}
