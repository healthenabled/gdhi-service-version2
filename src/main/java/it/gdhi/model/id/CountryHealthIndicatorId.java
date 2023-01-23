package it.gdhi.model.id;

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
public class CountryHealthIndicatorId implements Serializable {

    @Column(name = "country_id")
    private String countryId;

    @Column(name = "category_id")
    private Integer categoryId;

    @Column(name = "indicator_id")
    private Integer indicatorId;

    @Column(name = "status")
    private String status;

}
