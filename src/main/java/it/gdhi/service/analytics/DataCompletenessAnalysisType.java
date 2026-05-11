package it.gdhi.service.analytics;

import java.util.Arrays;

public enum DataCompletenessAnalysisType {
    MISSING_COUNTRY_DATA("missing_country_data"),
    PHASE_COUNT_BY_INDICATOR("phase_count_by_indicator"),
    PROXY_ONLY("proxy_only");

    private final String value;

    DataCompletenessAnalysisType(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }

    public static DataCompletenessAnalysisType from(String value) {
        return Arrays.stream(values())
                .filter(DataCompletenessAnalysisType::isSupported)
                .filter(type -> value != null && type.value.equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        "Unknown analysisType '" + value + "'. Supported: " +
                                Arrays.stream(values())
                                        .filter(DataCompletenessAnalysisType::isSupported)
                                        .map(DataCompletenessAnalysisType::value)
                                        .toList()));
    }

    public boolean isSupported() {
        return this != PROXY_ONLY;
    }
}
