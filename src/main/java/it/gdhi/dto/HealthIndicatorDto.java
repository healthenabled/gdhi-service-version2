package it.gdhi.dto;

import it.gdhi.model.CountryHealthIndicator;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class HealthIndicatorDto {

    private Integer categoryId;
    private Integer indicatorId;
    private String  status;

    private Integer score;
    private String supportingText;

    public HealthIndicatorDto(CountryHealthIndicator countryHealthIndicator) {
        this.categoryId = countryHealthIndicator.getCountryHealthIndicatorId().getCategoryId();
        this.indicatorId = countryHealthIndicator.getCountryHealthIndicatorId().getIndicatorId();
        this.score = countryHealthIndicator.getScore();
        this.supportingText = countryHealthIndicator.getSupportingText();
        this.status = countryHealthIndicator.getCountryHealthIndicatorId().getStatus();
    }
}
