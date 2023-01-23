package it.gdhi.internationalization.model;

import it.gdhi.model.Indicator;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(schema = "i18n", name = "health_indicator")
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class IndicatorTranslation {

    @EmbeddedId
    private HealthIndicatorTranslationId id;
    private String name;
    private String definition;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "indicator_id", referencedColumnName = "indicator_id", insertable = false, updatable = false)
    private Indicator indicator;

    public Integer getIndicatorId() {
        return this.id.getIndicatorId();
    }
}
