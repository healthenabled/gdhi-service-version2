package it.gdhi.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(schema ="regions", name="regions_countries")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class RegionCountry {

    @EmbeddedId
    RegionCountryId regionCountryId;
}
