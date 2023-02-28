package it.gdhi.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.Map;

@Getter
@Builder
@AllArgsConstructor
public class CountrySummaryStatusYearDto {

    String year;
    Map<String, List<CountrySummaryStatusDto>> countrySummaryStatusDtos;

}
