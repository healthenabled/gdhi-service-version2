package it.gdhi.dto;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CategoryHealthScoreDtoTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void shouldRoundOverallScoreToTwoDecimalsWhenSerialized() throws Exception {
        CategoryHealthScoreDto dto = new CategoryHealthScoreDto(1, "Infrastructure",
                3.213855421686747, 3, List.of());

        JsonNode json = objectMapper.readTree(objectMapper.writeValueAsString(dto));

        assertEquals(3.21, json.get("overallScore").doubleValue());
        assertEquals(3.213855421686747, dto.getOverallScore());
    }
}
