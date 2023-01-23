package it.gdhi.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(schema = "country_health_data", name="country_phase")
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CountryPhase {

    @Id
    @Column(name = "country_id")
    private String countryId;

    @Column(name = "country_overall_phase")
    private Integer countryOverallPhase;

}
