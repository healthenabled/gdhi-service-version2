package it.gdhi.model;

import lombok.Builder;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static java.math.BigDecimal.*;
import static java.math.RoundingMode.CEILING;
import static java.util.Objects.isNull;

@Builder
public class Score {

    private Double value;

    public Score(Double value) {
        this.value = value;
    }

    public Integer convertToPhase() {
        Integer result = null;
        if(!isNull(value)) {
            BigDecimal overallScoreInBigDecimal = new BigDecimal(value);
            BigDecimal ceiledScore = overallScoreInBigDecimal.setScale(1, RoundingMode.HALF_EVEN)
                    .setScale(0, CEILING);
            BigDecimal phase = ceiledScore.equals(ZERO) ? ceiledScore.add(ONE) : ceiledScore;
            result = phase.intValue();
        }
        return result;
    }
}
