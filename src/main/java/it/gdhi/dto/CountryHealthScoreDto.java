package it.gdhi.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.AllArgsConstructor;

import java.util.List;

import static it.gdhi.utils.GDHIStringUtil.isNonNullAndNonEmpty;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class CountryHealthScoreDto {

    private String countryId;

    private String countryName;

    private String countryAlpha2Code;

    private List<CategoryHealthScoreDto> categories;

    private Integer countryPhase;

    private String updatedDate;

    @JsonIgnore
    public boolean hasCategories() {
        return this.getCategories().size() > 0;
    }

    public void translateCountryName(String translatedCountryName) {
        if (isNonNullAndNonEmpty(translatedCountryName)) this.countryName = translatedCountryName;
    }
}
