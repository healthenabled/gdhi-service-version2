package it.gdhi.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

@Entity
@Table(schema = "master", name = "countries")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Slf4j
@EqualsAndHashCode
public class Country {

    @Id
    private String id;
    private String name;
    @Column(name = "unique_id")
    private UUID uniqueId;
    @Column(name = "alpha_2_code")
    private String alpha2Code;

    public Country makeWithName(String name) {
        return new Country(this.id, name, this.getUniqueId(), this.alpha2Code);
    }
}
