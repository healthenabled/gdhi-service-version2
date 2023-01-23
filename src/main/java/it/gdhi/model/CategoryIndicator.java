package it.gdhi.model;

import it.gdhi.model.id.CategoryIndicatorId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(schema ="master", name="categories_indicators")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class CategoryIndicator implements Serializable {

    @EmbeddedId
    private CategoryIndicatorId categoryIndicatorId;

}
