package it.gdhi.internationalization.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class CountryTranslationId implements Serializable {

    @Column(name = "country_id")
    private String countryId;

    @Column(name = "language_id")
    private String languageId;

}
