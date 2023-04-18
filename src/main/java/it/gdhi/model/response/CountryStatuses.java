package it.gdhi.model.response;

import it.gdhi.utils.FormStatus;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@EqualsAndHashCode
public class CountryStatuses {

    private List<CountryStatus> countryStatuses = new ArrayList<>();

    public void add(String countryName, boolean success, FormStatus currentStatus, String message) {
        CountryStatus countryStatus = new CountryStatus(countryName, success, currentStatus, message);
        this.countryStatuses.add(countryStatus);
    }
}
