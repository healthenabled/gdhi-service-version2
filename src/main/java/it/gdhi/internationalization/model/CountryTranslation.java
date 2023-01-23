package it.gdhi.internationalization.model;

import it.gdhi.model.Country;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(schema = "i18n", name = "country")
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class CountryTranslation {

    @EmbeddedId
    private CountryTranslationId id;
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "country_id", referencedColumnName = "id", insertable = false, updatable = false)
    private Country country;
}
