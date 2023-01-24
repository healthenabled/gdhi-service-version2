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
public class CountryResourceLinkId implements Serializable {

    @Column(name = "country_id")
    private String countryId;

    @Column(name = "link")
    private String link;

    @Column(name = "status")
    private String status;

}