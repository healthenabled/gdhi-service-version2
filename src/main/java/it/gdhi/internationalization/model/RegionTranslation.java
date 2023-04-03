package it.gdhi.internationalization.model;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(schema = "i18n", name = "region")
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class RegionTranslation {

    @EmbeddedId
    private RegionTranslationId id;
    private String region_name;
}
