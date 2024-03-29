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
public class RegionalIndicatorId implements Serializable {

    @Column(name = "region_id")
    private String regionId;

    @Column(name = "indicator_id")
    private Integer indicatorId;

    @Column(name = "year")
    private String year;
}
