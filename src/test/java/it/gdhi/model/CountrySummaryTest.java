package it.gdhi.model;

import it.gdhi.dto.CountrySummaryDto;
import it.gdhi.model.id.CountrySummaryId;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

import java.util.stream.Collectors;

import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class CountrySummaryTest {
    @Test
    public void shouldTransformIntoCountryResourceLinks() throws Exception {

        CountrySummary countrySummary = new CountrySummary(new CountrySummaryId("IND", "PUBLISHED", "Version1"), CountrySummaryDto.builder().resources(asList("res1", "res2")).build());
        assertEquals(2, countrySummary.getCountryResourceLinks().size());
        assertThat(countrySummary.getCountryResourceLinks().stream().map(CountryResourceLink::getLink).collect(Collectors.toList()),
                Matchers.containsInAnyOrder("res1", "res2"));
    }

    @Test
    public void shouldHandleEmptyResource() throws Exception {
        CountrySummary countrySummary = new CountrySummary(new CountrySummaryId("IND", "PUBLISHED", "Version1"), CountrySummaryDto.builder().build());
        assertNull(countrySummary.getCountryResourceLinks());
    }
}