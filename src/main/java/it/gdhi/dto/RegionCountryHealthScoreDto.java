package it.gdhi.dto;

import java.util.List;
import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
@Builder
public class RegionCountryHealthScoreDto {

    private List<RegionCategoryHealthScoreDto> categories;

    private Integer countryPhase;

}
