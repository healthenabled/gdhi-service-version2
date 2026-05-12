package it.gdhi.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

public final class ScoreRoundingUtil {

    private ScoreRoundingUtil() {
    }

    public static Double roundToTwoDecimals(Double value) {
        if (value == null) {
            return null;
        }
        return BigDecimal.valueOf(value)
                .setScale(2, RoundingMode.HALF_UP)
                .doubleValue();
    }
}
