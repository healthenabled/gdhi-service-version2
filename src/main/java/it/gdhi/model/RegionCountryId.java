package it.gdhi.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(schema ="region", name="regions_countries")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class RegionCountryId implements Serializable {

    @Id
    @Column(name = "region_id")
    private String regionId;

    @Column(name = "country_id")
    private String countryId;

}
