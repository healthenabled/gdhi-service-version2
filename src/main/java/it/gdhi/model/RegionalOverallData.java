package it.gdhi.model;

import it.gdhi.model.id.RegionalOverallId;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(schema = "regions", name = "regional_overall_data")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Slf4j
@Builder
@EqualsAndHashCode
public class RegionalOverallData {

    @EmbeddedId
    RegionalOverallId regionalOverallId;

    @Column(name = "overall_score")
    Integer overAllScore;
}
