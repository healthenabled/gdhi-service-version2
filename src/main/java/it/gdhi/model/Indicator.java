package it.gdhi.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(schema = "master", name="health_indicators")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class Indicator {

    @Id
    @Column(name = "indicator_id")
    private Integer indicatorId;

    private String name;

    private String code;

    private Integer rank;

    @Column(name = "parent_id")
    private Integer parentId;

    @OneToMany
    @JoinColumn(name = "indicator_id", referencedColumnName = "indicator_id", insertable = false, updatable = false)
    @OrderBy("score")
    private List<IndicatorScore> options;

    private String definition;

    public Indicator(Integer id, String name, String definition, Integer rank) {
        this.indicatorId = id;
        this.name = name;
        this.definition = definition;
        this.rank = rank;
    }
}
