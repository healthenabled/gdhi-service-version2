package it.gdhi.model;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class RegionCountryId implements Serializable {

    @Column(name = "region_id")
    private String regionId;

    @Column(name = "country_id")
    private String countryId;

}
