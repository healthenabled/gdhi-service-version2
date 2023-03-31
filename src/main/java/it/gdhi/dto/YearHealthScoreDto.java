package it.gdhi.dto;

import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
@ToString
public class YearHealthScoreDto {
    CountryHealthScoreDto country;
    GlobalHealthScoreDto average;
}
