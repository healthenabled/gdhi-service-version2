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
@Table(schema ="regions", name="region")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class Region {

    @Id
    String regionId;

    @Column(name="region_name")
    String regionName;
}
