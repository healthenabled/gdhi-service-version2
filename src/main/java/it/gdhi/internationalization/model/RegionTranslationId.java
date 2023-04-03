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
public class RegionTranslationId implements Serializable {

    @Column(name = "region_id")
    private String regionId;

    @Column(name = "language_id")
    private String languageId;

}
