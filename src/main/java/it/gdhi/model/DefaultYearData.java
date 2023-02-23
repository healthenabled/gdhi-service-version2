package it.gdhi.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(schema = "master", name="default_year_data")
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DefaultYearData {

    private String year;

    @Id
    @Column(updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
}
