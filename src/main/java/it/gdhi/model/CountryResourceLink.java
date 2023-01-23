package it.gdhi.model;

import it.gdhi.model.id.CountryResourceLinkId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(schema = "country_health_data", name = "country_resource_links")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class CountryResourceLink {

    @EmbeddedId
    private CountryResourceLinkId countryResourceLinkId;

    @Column(insertable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    private Date updatedAt;

    public String getLink() {
        return countryResourceLinkId.getLink();
    }

    @PreUpdate
    @PrePersist
    public void updateTimeStamps() {
        updatedAt = new Date();
    }
}