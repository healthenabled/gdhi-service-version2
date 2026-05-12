package it.gdhi.ai.dto;

public record BedrockDataCompletenessData(
        String analysisType,
        String countryId,
        String countryName,
        String regionId,
        String year,
        Integer missingIndicatorCount,
        Integer totalIndicatorCount,
        Integer indicatorId,
        String indicatorName,
        Integer phase,
        Integer countryCount
) {
}
