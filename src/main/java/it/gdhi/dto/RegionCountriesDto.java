package it.gdhi.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;


@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class RegionCountriesDto {
    private List<RegionCountryDto> regionCountriesData = new ArrayList<>();

    public void add(String countryId, String countryName,
                    List<RegionCountryHealthScoreYearDto> regionCountryHealthScoreYearDto) {

        RegionCountryDto regionCountryDto =
                RegionCountryDto.builder().countryId(countryId).countryName(countryName).countryYearsData(regionCountryHealthScoreYearDto).build();

        this.regionCountriesData.add(regionCountryDto);
    }
}
