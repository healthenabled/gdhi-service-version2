package it.gdhi.model;

import it.gdhi.model.id.CountryPhaseId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

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

    private boolean latest;

    private Date updatedAt;

    public CountryPhase(String countryId, Integer countryOverallPhase, String year) {
        this(countryId, countryOverallPhase, year, false);
    }

    public CountryPhase(String countryId, Integer countryOverallPhase, String year, boolean latest) {
        this.countryPhaseId = new CountryPhaseId(countryId, year);
        this.countryOverallPhase = countryOverallPhase;
        this.latest = latest;
    }

    public String getYear() {
        return this.countryPhaseId.getYear();
    }

    @PreUpdate
    @PrePersist
    public void updateTimeStamps() {
        updatedAt = new Date();
    }
}
