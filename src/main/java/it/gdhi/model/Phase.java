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
@Table(schema = "master", name="phases")
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Phase {
    @Id
    @Column(name="phase_id")
    private Integer phaseId;

    @Column(name="phase_name")
    private String phaseName;

    @Column(name="phase_value")
    private Integer phaseValue;
}
