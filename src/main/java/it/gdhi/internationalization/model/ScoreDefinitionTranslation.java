package it.gdhi.internationalization.model;

import it.gdhi.model.IndicatorScore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(schema = "i18n", name = "score_definition")
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ScoreDefinitionTranslation {

    @EmbeddedId
    private ScoreDefinitionTranslationId id;
    private String definition;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumns({
        @JoinColumn(name = "indicator_id", referencedColumnName = "indicator_id", insertable = false,updatable = false),
        @JoinColumn(name = "score", referencedColumnName = "score", insertable = false, updatable = false)})
    private IndicatorScore score;
}
