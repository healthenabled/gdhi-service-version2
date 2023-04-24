package it.gdhi.model;

import it.gdhi.model.id.RegionalIndicatorId;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(schema = "regions", name = "regional_indicator_data")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Slf4j
@Builder
@EqualsAndHashCode
public class RegionalIndicatorData {

    @EmbeddedId
    RegionalIndicatorId regionalIndicatorId;

    @Column(name = "score")
    Integer score;

    public Integer getRegionalIndicatorId() {
        return regionalIndicatorId.getIndicatorId();
    }
}
