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
public class CategoryTranslationId implements Serializable {

    @Column(name = "category_id")
    private Integer categoryId;

    @Column(name = "language_id")
    @Enumerated(STRING)
    private LanguageCode languageId;
}
