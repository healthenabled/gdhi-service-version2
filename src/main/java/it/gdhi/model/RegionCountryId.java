package it.gdhi.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class RegionCountryId implements Serializable {

    @Column(name = "region_id")
    private String regionId;

    @Column(name = "country_id")
    private String countryId;

}
