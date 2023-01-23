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
@Table(schema = "master", name="health_indicator_scores")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class IndicatorScore implements Serializable {

    @Id
    private Long id;

    @Column(name="indicator_id")
    private Integer indicatorId;

    private Integer score;

    private String definition;
}
