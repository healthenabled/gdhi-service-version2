package it.gdhi.internationalization.model;

import it.gdhi.utils.LanguageCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Enumerated;
import java.io.Serializable;

import static javax.persistence.EnumType.STRING;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ScoreDefinitionTranslationId implements Serializable {

    @Column(name = "indicator_id")
    private Integer indicatorId;

    @Column(name = "score")
    private Integer score;

    @Column(name = "language_id")
    @Enumerated(STRING)
    private LanguageCode languageId;
}
