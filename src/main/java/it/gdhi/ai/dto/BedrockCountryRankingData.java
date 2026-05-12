package it.gdhi.ai.dto;

public record BedrockCountryRankingData(
        String countryId,
        String countryName,
        String regionId,
        String year,
        Score primaryScore,
        Score secondaryScore
) {
    public record Score(
            String type,
            Integer id,
            String name,
            Integer phase
    ) {
    }
}
