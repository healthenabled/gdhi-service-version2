package it.gdhi.dto;

import it.gdhi.model.Country;
import it.gdhi.model.CountryResourceLink;
import it.gdhi.model.CountrySummary;
import it.gdhi.model.id.CountryResourceLinkId;
import it.gdhi.model.id.CountrySummaryId;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class CountrySummaryDtoTest {
    @Test
    public void shouldTransformIntoCountryResourceLinks() throws Exception {
        CountryResourceLink res1 = CountryResourceLink.builder().countryResourceLinkId
                (CountryResourceLinkId.builder().link("res1").build()).build();
        CountryResourceLink res2 = CountryResourceLink.builder().countryResourceLinkId
                (CountryResourceLinkId.builder().link("res2").build()).build();
        List<CountryResourceLink> countryResourceLinks = asList(res1, res2);
        CountrySummary countrySummary = getCountrySummaryFor(countryResourceLinks);

        CountrySummaryDto countrySummaryDto = new CountrySummaryDto(countrySummary);

        assertEquals(2, countrySummaryDto.getResources().size());
        assertThat(countrySummaryDto.getResources(), Matchers.containsInAnyOrder("res1", "res2"));
    }

    @Test
    public void shouldRemoveEmptyResourceLinks() throws Exception {
        CountryResourceLink res1 = CountryResourceLink.builder().countryResourceLinkId
                (CountryResourceLinkId.builder().link("").build()).build();
        CountryResourceLink res2 = CountryResourceLink.builder().countryResourceLinkId
                (CountryResourceLinkId.builder().link("res2").build()).build();
        CountryResourceLink res3 = CountryResourceLink.builder().countryResourceLinkId
                (CountryResourceLinkId.builder().link(null).build()).build();
        List<CountryResourceLink> countryResourceLinks = asList(res1, res2, res3);
        CountrySummary countrySummary = getCountrySummaryFor(countryResourceLinks);

        CountrySummaryDto countrySummaryDto = new CountrySummaryDto(countrySummary);

        assertEquals(1, countrySummaryDto.getResources().size());
        assertThat(countrySummaryDto.getResources(), Matchers.containsInAnyOrder("res2"));
    }

    private CountrySummary getCountrySummaryFor(List<CountryResourceLink> countryResourceLinks) {
        return CountrySummary.builder()
                    .countrySummaryId(new CountrySummaryId("TUN", "NEW"))
                    .country(new Country("TUN", "TUNISIA", UUID.randomUUID(), "TN"))
                    .countryResourceLinks(countryResourceLinks)
                    .build();
    }
}