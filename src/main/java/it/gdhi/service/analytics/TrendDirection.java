package it.gdhi.service.analytics;

import it.gdhi.ai.dto.BedrockCountryPhaseTrendData;

import java.util.Arrays;
import java.util.Comparator;

public enum TrendDirection {
    ADVANCED("advanced") {
        @Override
        public boolean matches(BedrockCountryPhaseTrendData trend) {
            return trend.phaseChange() > 0;
        }

        @Override
        public Comparator<BedrockCountryPhaseTrendData> comparator() {
            return Comparator.comparing(BedrockCountryPhaseTrendData::phaseChange).reversed();
        }
    },
    REGRESSED("regressed") {
        @Override
        public boolean matches(BedrockCountryPhaseTrendData trend) {
            return trend.phaseChange() < 0;
        }

        @Override
        public Comparator<BedrockCountryPhaseTrendData> comparator() {
            return Comparator.comparing(BedrockCountryPhaseTrendData::phaseChange);
        }
    },
    CHANGED("changed") {
        @Override
        public boolean matches(BedrockCountryPhaseTrendData trend) {
            return trend.phaseChange() != 0;
        }

        @Override
        public Comparator<BedrockCountryPhaseTrendData> comparator() {
            return Comparator.comparing((BedrockCountryPhaseTrendData trend) -> Math.abs(trend.phaseChange()))
                    .reversed();
        }
    },
    SUBMITTED("submitted") {
        @Override
        public boolean matches(BedrockCountryPhaseTrendData trend) {
            return true;
        }

        @Override
        public Comparator<BedrockCountryPhaseTrendData> comparator() {
            return Comparator.comparing((BedrockCountryPhaseTrendData trend) -> trend.submissionYears().size())
                    .reversed();
        }
    };

    private final String value;

    TrendDirection(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }

    public abstract boolean matches(BedrockCountryPhaseTrendData trend);

    public abstract Comparator<BedrockCountryPhaseTrendData> comparator();

    public static TrendDirection from(String value) {
        return Arrays.stream(values())
                .filter(direction -> direction.value.equalsIgnoreCase(value))
                .findFirst()
                .orElse(ADVANCED);
    }
}
