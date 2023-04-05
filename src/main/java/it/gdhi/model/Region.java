package it.gdhi.model;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(schema ="regions", name="region")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Slf4j
@Builder
@EqualsAndHashCode
public class Region {

    @Id
    String region_id;

    @Column(name="region_name")
    String regionName;

    public Region makeWithName(String name) {
        return new Region(this.region_id, name);
    }
}
