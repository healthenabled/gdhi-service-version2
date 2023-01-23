package it.gdhi.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import it.gdhi.model.CountryResourceLink;
import it.gdhi.model.CountrySummary;
import it.gdhi.utils.JsonDateDeserializer;
import it.gdhi.utils.JsonDateSerializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static it.gdhi.utils.GDHIStringUtil.isNonNullAndNonEmpty;
import static java.util.Objects.isNull;
import static java.util.stream.Collectors.toList;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CountrySummaryDto {

    private String countryId;
    private String countryName;
    private String countryAlpha2Code;
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

    @JsonSerialize(using = JsonDateSerializer.class)
    @JsonDeserialize(using = JsonDateDeserializer.class)
    private Date collectedDate;

    private List<String> resources;


    public CountrySummaryDto(CountrySummary countrySummary) {
        this.countryId = countrySummary.getCountrySummaryId().getCountryId();
        this.countryName = countrySummary.getCountry().getName();
        this.countryAlpha2Code = countrySummary.getCountry().getAlpha2Code();
        this.summary = countrySummary.getSummary();
        this.contactName = countrySummary.getContactName();
        this.contactDesignation = countrySummary.getContactDesignation();
        this.contactOrganization = countrySummary.getContactOrganization();
        this.contactEmail = countrySummary.getContactEmail();
        this.dataFeederName = countrySummary.getDataFeederName();
        this.dataFeederRole = countrySummary.getDataFeederRole();
        this.dataFeederEmail = countrySummary.getDataFeederEmail();
        this.dataApproverName = countrySummary.getDataApproverName();
        this.dataApproverRole = countrySummary.getDataApproverRole();
        this.dataApproverEmail = countrySummary.getDataApproverEmail();
        this.collectedDate = countrySummary.getCollectedDate();
        this.resources = transformResourceLinks(countrySummary);
    }

    private List<String> transformResourceLinks(CountrySummary countrySummary) {
        return Optional.ofNullable(countrySummary.getCountryResourceLinks())
                .map(list -> list.stream().map(CountryResourceLink::getLink).collect(toList()))
                .orElse(null);

    }

    public List<String> getResources() {
        return Optional.ofNullable(resources)
                .map(list -> list.stream().filter(item -> !isNull(item) && !item.isEmpty()).collect(toList()))
                .orElse(null);
    }

    public void translateCountryName(String translatedCountryName) {
        if (isNonNullAndNonEmpty(translatedCountryName)) this.countryName = translatedCountryName;
    }
}
