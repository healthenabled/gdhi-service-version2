package it.gdhi.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(schema ="regions", name="regions_countries")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class RegionCountry {

    @EmbeddedId
    RegionCountryId regionCountryId;
}
