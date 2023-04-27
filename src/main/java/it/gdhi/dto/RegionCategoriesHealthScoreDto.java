package it.gdhi.dto;

import java.util.List;
import java.util.stream.Collectors;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@EqualsAndHashCode
@ToString
public class RegionCategoriesHealthScoreDto {

    List<RegionCategoryHealthScoreDto> regionCategoryHealthScoreDtos;

    public RegionCategoriesHealthScoreDto(List<CategoryHealthScoreDto> categoryHealthScoreDtos) {
        this.regionCategoryHealthScoreDtos =
                categoryHealthScoreDtos.stream().map(RegionCategoryHealthScoreDto::new).collect(Collectors.toList());
    }
}
