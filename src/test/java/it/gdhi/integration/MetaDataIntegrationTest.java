package it.gdhi.integration;

import io.restassured.response.Response;
import it.gdhi.GdhiServiceApplication;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;
import java.util.ArrayList;

import static io.restassured.RestAssured.given;
import static it.gdhi.utils.LanguageCode.USER_LANGUAGE;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = GdhiServiceApplication.class)
@ActiveProfiles("test")
public class MetaDataIntegrationTest extends BaseIntegrationTest{

    @Value("${local.server.port}")
    private int port;

    @Test
    public void shouldReturnAllPhases() throws IOException {

        Response response = given()
                .contentType("application/json")
                .when()
                .get("http://localhost:" + port + "/phases");

        assertEquals(200, response.getStatusCode());
        assertResponse(response.asString(), "phases.json");
    }

    @Test
    public void shouldReturnHealthIndicatorOptions() throws IOException {
        Response response = given()
                .contentType("application/json")
                .header(USER_LANGUAGE, "en")
                .when()
                .get("http://localhost:" + port + "/health_indicator_options");

        assertEquals(200, response.getStatusCode());
        assertResponse(response.asString(), "health_indicator_options_en.json");
    }

    @Test
    public void shouldReturnHealthIndicatorOptionsInFrench() throws IOException {
        Response response = given()
                .contentType("application/json")
                .header(USER_LANGUAGE, "fr")
                .when()
                .get("http://localhost:" + port + "/health_indicator_options");

        assertEquals(200, response.getStatusCode());
        assertResponse(response.asString(), "health_indicator_options_fr.json");
    }
    @Test
    public void shouldReturnHealthIndicatorOptionsInArabic() throws IOException {
        Response response = given()
                .contentType("application/json")
                .header(USER_LANGUAGE, "ar")
                .when()
                .get("http://localhost:" + port + "/health_indicator_options");

        assertEquals(200, response.getStatusCode());
        assertResponse(response.asString(), "health_indicator_options_ar.json");
    }

    @Override
    void assertResponse(String responseJSON, String expectedJsonFileName) throws IOException {
        String expectedJSON = expectedResponseJson(expectedJsonFileName);
        ArrayList actualList = getMapper().readValue(responseJSON, ArrayList.class);
        ArrayList expectedList = getMapper().readValue(expectedJSON, ArrayList.class);
        assertEquals(expectedList, actualList);
    }
}
