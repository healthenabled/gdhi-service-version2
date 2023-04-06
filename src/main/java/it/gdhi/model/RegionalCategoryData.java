package it.gdhi.model;

import it.gdhi.model.id.RegionalCategoryId;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(schema = "regions", name = "regional_category_data")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Slf4j
@Builder
@EqualsAndHashCode
public class RegionalCategoryData {

    @EmbeddedId
    RegionalCategoryId regionalCategoryId;

    @Column(name = "score")
    Integer score;

}
