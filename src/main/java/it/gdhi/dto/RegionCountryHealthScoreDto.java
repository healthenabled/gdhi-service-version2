package it.gdhi.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
@Builder
public class RegionCountryHealthScoreDto {

    @JsonIgnore
    private String countryId;

    @JsonIgnore
    private String countryName;

    @JsonIgnore
    private String countryAlpha2Code;

    private List<CategoryHealthScoreDto> categories;

    private Integer countryPhase;

    @JsonIgnore
    private String updatedDate;

    public RegionCountryHealthScoreDto(CountryHealthScoreDto countryHealthScoreDto) {
        this.countryId = countryHealthScoreDto.getCountryId();
        this.countryName = countryHealthScoreDto.getCountryName();
        this.countryPhase = countryHealthScoreDto.getCountryPhase();
        this.countryAlpha2Code = countryHealthScoreDto.getCountryAlpha2Code();
        this.categories = countryHealthScoreDto.getCategories();
        this.updatedDate = countryHealthScoreDto.getUpdatedDate();
    }

}
