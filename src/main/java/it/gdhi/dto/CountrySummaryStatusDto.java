package it.gdhi.dto;

import it.gdhi.model.CountrySummary;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
public class CountrySummaryStatusDto {

    private String countryName;
    private UUID countryUUID;
    private String status;
    private String contactName;
    private String contactEmail;

    public CountrySummaryStatusDto(CountrySummary dto) {
        countryName = dto.getCountry().getName();
        countryUUID = dto.getCountry().getUniqueId();
        status = dto.getCountrySummaryId().getStatus();
        contactName = dto.getContactName();
        contactEmail = dto.getContactEmail();
    }
}
