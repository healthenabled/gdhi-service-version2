package it.gdhi.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class GdhiQuestionnaire {

    private String countryId;

    private String status;

    private CountrySummaryDto countrySummary;

    private List<HealthIndicatorDto> healthIndicators;

    @JsonIgnore
    public String getDataFeederName() {
        return countrySummary.getDataFeederName();
    }

    @JsonIgnore
    public String getDataFeederRole() {
        return countrySummary.getDataFeederRole();
    }

    @JsonIgnore
    public String getContactEmail() {
        return countrySummary.getContactEmail();
    }

    public void translateCountryName(String translatedCountryName) {
        this.countrySummary.translateCountryName(translatedCountryName);
    }

}
