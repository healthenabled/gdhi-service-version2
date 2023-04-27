package it.gdhi.dto;


import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
@Builder
public class RegionCategoryHealthScoreDto {
    private Integer id;

    private String name;

    private Double overallScore;

    private Integer phase;

    public RegionCategoryHealthScoreDto(CategoryHealthScoreDto categoryHealthScoreDto) {
        this.id = categoryHealthScoreDto.getId();
        this.name = categoryHealthScoreDto.getName();
        this.overallScore = categoryHealthScoreDto.getOverallScore();
        this.phase = categoryHealthScoreDto.getPhase();
    }
}
