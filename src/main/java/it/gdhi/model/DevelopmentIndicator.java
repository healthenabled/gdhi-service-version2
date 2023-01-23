package it.gdhi.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import it.gdhi.view.DevelopmentIndicatorView;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(schema = "country_health_data", name="development_indicators")
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class DevelopmentIndicator {

    //Todo: Remove jsonView at all attributes and make it at class level
    @Id
    @Column(name = "country_id")
    @JsonView(DevelopmentIndicatorView.class)
    private String countryId;

    @OneToOne
    @JsonIgnore
    @JoinColumn(name = "country_id", referencedColumnName = "id", insertable = false, updatable = false)
    private Country country;

    @JsonView(DevelopmentIndicatorView.class)
    private Long totalPopulation;

    @JsonView(DevelopmentIndicatorView.class)
    private BigDecimal gniPerCapita;

    @JsonView(DevelopmentIndicatorView.class)
    private BigDecimal lifeExpectancy;

    @JsonView(DevelopmentIndicatorView.class)
    private BigDecimal healthExpenditure;

    @Column(name = "ncd_deaths_per_capita_total")
    @JsonView(DevelopmentIndicatorView.class)
    private BigDecimal totalNcdDeathsPerCapita;

    @Column(name = "under_5_mortality")
    @JsonView(DevelopmentIndicatorView.class)
    private BigDecimal under5Mortality;

    @JsonView(DevelopmentIndicatorView.class)
    private BigDecimal doingBusinessIndex;

    @JsonView(DevelopmentIndicatorView.class)
    private BigDecimal adultLiteracy;

}
