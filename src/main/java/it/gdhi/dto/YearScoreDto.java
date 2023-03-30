package it.gdhi.dto;

import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class YearScoreDto {
    String year;
    YearHealthScoreDto data;
}
