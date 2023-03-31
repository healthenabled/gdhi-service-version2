package it.gdhi.dto;

import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
@ToString
public class YearScoreDto {
    String year;
    YearHealthScoreDto data;
}
