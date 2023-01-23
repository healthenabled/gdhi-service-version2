package it.gdhi.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class GlobalHealthScoreDto {
    private Integer overAllScore;
    private List<CategoryHealthScoreDto> categories;
}
