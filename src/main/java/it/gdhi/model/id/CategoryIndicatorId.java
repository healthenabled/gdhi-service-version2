package it.gdhi.model.id;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class CategoryIndicatorId implements Serializable {

    @Column(name = "category_id")
    private Integer categoryId;

    @Column(name = "indicator_id")
    private Integer indicatorId;

}
