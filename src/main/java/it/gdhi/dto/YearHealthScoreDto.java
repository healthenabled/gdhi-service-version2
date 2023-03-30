package it.gdhi.dto;

import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class YearHealthScoreDto {
    CountryHealthScoreDto country;
    GlobalHealthScoreDto average;
}
