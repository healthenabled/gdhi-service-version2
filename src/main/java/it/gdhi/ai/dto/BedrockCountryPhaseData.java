package it.gdhi.ai.dto;

public record BedrockCountryPhaseData(
        String countryId,
        String countryName,
        String countryAlpha2Code,
        String year,
        Integer countryPhase,
        String phaseLabel
) {
    public static String phaseLabelFor(Integer phase) {
        if (phase == null) {
            return null;
        }
        return switch (phase) {
            case 1 -> "Nascent";
            case 2 -> "Developing";
            case 3 -> "Initial Scale";
            case 4 -> "Established";
            case 5 -> "Optimized";
            default -> null;
        };
    }
}
