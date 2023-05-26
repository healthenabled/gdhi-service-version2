package it.gdhi.model;

import it.gdhi.dto.CountrySummaryDto;
import it.gdhi.model.id.CountryResourceLinkId;
import it.gdhi.model.id.CountrySummaryId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.springframework.util.ObjectUtils;

import javax.persistence.*;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Entity
@Table(schema = "country_health_data", name = "country_summary")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class CountrySummary implements Serializable {

    @EmbeddedId
    CountrySummaryId countrySummaryId;

    @OneToOne
    @JoinColumn(name = "country_id", referencedColumnName = "id", insertable = false, updatable = false)
    private Country country;
    private String summary;
    private String contactName;
    private String contactDesignation;
    private String contactOrganization;
    private String contactEmail;
    private String dataFeederName;
    private String dataFeederRole;
    private String dataFeederEmail;
    private String dataApproverName;
    private String dataApproverRole;
    private String dataApproverEmail;
    private String status;
    @Column(name = "govt_approved")
    private Boolean govtApproved;
    @Column(insertable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    private Date updatedAt;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name = "country_id", referencedColumnName = "country_id", insertable = false, updatable =
                    false),
            @JoinColumn(name = "year", referencedColumnName = "year", insertable = false, updatable = false)
    })
    private List<CountryResourceLink> countryResourceLinks;

    public CountrySummary(CountrySummaryId countrySummaryId, CountrySummaryDto countrySummaryDetailDto, String status) {
        this.countrySummaryId = countrySummaryId;
        this.summary = countrySummaryDetailDto.getSummary();
        this.contactName = countrySummaryDetailDto.getContactName();
        this.contactDesignation = countrySummaryDetailDto.getContactDesignation();
        this.contactOrganization = countrySummaryDetailDto.getContactOrganization();
        this.contactEmail = countrySummaryDetailDto.getContactEmail();
        this.dataFeederName = countrySummaryDetailDto.getDataFeederName();
        this.dataFeederRole = countrySummaryDetailDto.getDataFeederRole();
        this.dataFeederEmail = countrySummaryDetailDto.getDataFeederEmail();
        this.dataApproverName = countrySummaryDetailDto.getDataApproverName();
        this.dataApproverRole = countrySummaryDetailDto.getDataApproverRole();
        this.dataApproverEmail = countrySummaryDetailDto.getDataApproverEmail();
        this.govtApproved = countrySummaryDetailDto.getGovtApproved();
        this.countryResourceLinks = transformToResourceLinks(countrySummaryId.getCountryId(),
                status, countrySummaryDetailDto, countrySummaryId.getYear());
        this.status = status;
    }


    public CountrySummary(CountrySummaryId countrySummaryId, CountrySummaryDto countrySummaryDetailDto,
                          Country country, String status) {
        this.countrySummaryId = countrySummaryId;
        this.summary = countrySummaryDetailDto.getSummary();
        this.contactName = countrySummaryDetailDto.getContactName();
        this.contactDesignation = countrySummaryDetailDto.getContactDesignation();
        this.contactOrganization = countrySummaryDetailDto.getContactOrganization();
        this.contactEmail = countrySummaryDetailDto.getContactEmail();
        this.dataFeederName = countrySummaryDetailDto.getDataFeederName();
        this.dataFeederRole = countrySummaryDetailDto.getDataFeederRole();
        this.dataFeederEmail = countrySummaryDetailDto.getDataFeederEmail();
        this.dataApproverName = countrySummaryDetailDto.getDataApproverName();
        this.dataApproverRole = countrySummaryDetailDto.getDataApproverRole();
        this.dataApproverEmail = countrySummaryDetailDto.getDataApproverEmail();
        this.govtApproved = countrySummaryDetailDto.getGovtApproved();
        this.countryResourceLinks = transformToResourceLinks(countrySummaryId.getCountryId(),
                status, countrySummaryDetailDto, countrySummaryId.getYear());
        this.country = country;
        this.status = status;
    }


    private List<CountryResourceLink> transformToResourceLinks(String countryId,
                                                               String status,
                                                               CountrySummaryDto countrySummaryDetailDto, String year) {
        List<String> resourceLinks = countrySummaryDetailDto.getResources();
        return ObjectUtils.isEmpty(resourceLinks) ? null : resourceLinks.stream().map(
                        link ->
                                new CountryResourceLink(new CountryResourceLinkId(countryId, link, year), new Date(),
                                        null, status))
                .collect(toList());
    }

    @PreUpdate
    @PrePersist
    public void updateTimeStamps() {
        updatedAt = new Date();
    }
}