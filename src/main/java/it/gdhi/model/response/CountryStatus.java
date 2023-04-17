package it.gdhi.model.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import it.gdhi.utils.FormStatus;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@EqualsAndHashCode
public class CountryStatus {
    private String countryName;
    private boolean success;
    private FormStatus currentStatus;
}
