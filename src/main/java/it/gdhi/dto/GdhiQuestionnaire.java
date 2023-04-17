package it.gdhi.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@EqualsAndHashCode
public class GdhiQuestionnaire {

    private String countryId;

    private String currentYear;

    private String dataAvailableForYear;

    private String status;

    private String updatedDate;

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
