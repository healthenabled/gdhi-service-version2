package it.gdhi.ai.dto;

import java.util.List;

public record BedrockCountryPhaseTrendData(
        String countryId,
        String countryName,
        String regionId,
        String scoreType,
        Integer scoreId,
        String scoreName,
        String startYear,
        Integer startPhase,
        String endYear,
        Integer endPhase,
        Integer phaseChange,
        List<String> submissionYears,
        List<YearPhase> trajectory
) {
    public record YearPhase(String year, Integer phase) {
    }
}
