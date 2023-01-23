package it.gdhi.dto;

import it.gdhi.model.IndicatorScore;
import lombok.*;

import static it.gdhi.utils.GDHIStringUtil.isNonNullAndNonEmpty;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
public class ScoreDto {
    private Long scoreId;
    private Integer score;
    private String scoreDefinition;

    ScoreDto(IndicatorScore indicatorScore) {
        this.scoreId = indicatorScore.getId();
        this.score = indicatorScore.getScore();
        this.scoreDefinition = indicatorScore.getDefinition();
    }

    public void translateDefinition(String translatedScoreDefinition) {
        if(isNonNullAndNonEmpty(translatedScoreDefinition)) this.scoreDefinition = translatedScoreDefinition;
    }
}
