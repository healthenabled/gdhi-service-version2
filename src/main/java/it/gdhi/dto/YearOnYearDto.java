package it.gdhi.dto;

import lombok.*;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
@ToString
public class YearOnYearDto {
    String currentYear;
    String defaultYear;
    List<YearScoreDto> yearOnYearData;
}
