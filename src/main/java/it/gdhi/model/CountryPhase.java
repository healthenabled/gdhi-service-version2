package it.gdhi.model;

import it.gdhi.model.id.CountryPhaseId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(schema = "country_health_data", name = "country_phase")
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CountryPhase {

    @EmbeddedId
    CountryPhaseId countryPhaseId;

    @Column(name = "country_overall_phase")
    private Integer countryOverallPhase;

    public CountryPhase(String countryId, Integer countryOverallPhase, String year) {
        this.countryPhaseId = new CountryPhaseId(countryId, year);
        this.countryOverallPhase = countryOverallPhase;
    }

    public String getYear() {
        return this.countryPhaseId.getYear();
    }
}
