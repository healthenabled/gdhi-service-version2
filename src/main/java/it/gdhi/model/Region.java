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
@Table(schema = "region", name="region")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class Region implements Serializable {

    @Id
    @Column(name = "region_id")
    private String regionId;

    @Column(name = "region_name")
    private String regionName;
}
