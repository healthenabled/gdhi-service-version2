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

    private List<CategoryHealthScoreDto> categories;

    private Integer countryPhase;

}
