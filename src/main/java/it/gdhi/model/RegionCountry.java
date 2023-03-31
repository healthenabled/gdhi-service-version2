package it.gdhi.model;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(schema = "region", name="regions_countries")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class RegionCountry implements Serializable {

    @EmbeddedId
    private RegionCountryId regionCountryId;
}
