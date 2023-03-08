package it.gdhi.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.Map;

@Getter
@Builder
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CountrySummaryStatusYearDto {

    String currentYear;
    @JsonProperty("NEW")
    List<CountrySummaryStatusDto> newStatus;

    @JsonProperty("DRAFT")
    List<CountrySummaryStatusDto> draftStatus;

    @JsonProperty("PUBLISHED")
    List<CountrySummaryStatusDto> publishedStatus;

    @JsonProperty("REVIEW_PENDING")
    List<CountrySummaryStatusDto> reviewPendingStatus;

}
