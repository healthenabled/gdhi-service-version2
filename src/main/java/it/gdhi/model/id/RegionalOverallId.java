package it.gdhi.model.id;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@EqualsAndHashCode
public class RegionalOverallId implements Serializable {

    @Column(name = "region_id")
    private String regionId;

    @Column(name = "year")
    private String year;
}
