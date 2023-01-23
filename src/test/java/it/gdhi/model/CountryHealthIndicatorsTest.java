package it.gdhi.model;

import it.gdhi.model.id.CountryHealthIndicatorId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class CountryHealthIndicatorsTest {

    private List<CountryHealthIndicator> countryHealthIndicators;
    String countryName = "india";
    String countryAlpha2Code = "IN";

    @BeforeEach
    public void setUp() throws Exception {
        String india = "IND";
        String status = "PUBLISHED";
        Country country = new Country(india, countryName, UUID.randomUUID(), "IN");

        CountryHealthIndicator countryHealthIndicator = CountryHealthIndicator.builder()
                .countryHealthIndicatorId(new CountryHealthIndicatorId(india, 1, 1, status))
                .country(country)
                .category(Category.builder().id(1).build())
                .indicator(Indicator.builder().indicatorId(1).build())
                .score(3)
                .build();

        CountryHealthIndicator countryHealthIndicator1 = CountryHealthIndicator.builder()
                .country(country)
                .countryHealthIndicatorId(new CountryHealthIndicatorId(india, 1, 2, status))
                .category(Category.builder().id(1).build())
                .indicator(Indicator.builder().indicatorId(2).build())
                .score(3)
                .build();


        CountryHealthIndicator countryHealthIndicator3 = CountryHealthIndicator.builder()
                .countryHealthIndicatorId(new CountryHealthIndicatorId(india, 1, 1, status))
                .country(country)
                .category(Category.builder().id(1).build())
                .indicator(Indicator.builder().indicatorId(3).build())
                .score(2)
                .build();

        CountryHealthIndicator countryHealthIndicator2 = CountryHealthIndicator.builder()
                .countryHealthIndicatorId(new CountryHealthIndicatorId(india, 2, 2, status))
                .country(country)
                .category(Category.builder().id(2).build())
                .indicator(Indicator.builder().indicatorId(4).build())
                .score(null)
                .build();

        CountryHealthIndicator countryHealthIndicator4 = CountryHealthIndicator.builder()
                .countryHealthIndicatorId(new CountryHealthIndicatorId(india, 1, 21, status))
                .country(country)
                .category(Category.builder().id(1).build())
                .indicator(Indicator.builder().indicatorId(5).parentId(9).build())
                .score(2)
                .build();

        CountryHealthIndicator countryHealthIndicator5 = CountryHealthIndicator.builder()
                .country(country)
                .countryHealthIndicatorId(new CountryHealthIndicatorId(india, 2, 2, status))
                .category(Category.builder().id(2).build())
                .indicator(Indicator.builder().indicatorId(6).build())
                .score(null)
                .build();

        this.countryHealthIndicators = asList(countryHealthIndicator, countryHealthIndicator1, countryHealthIndicator2,
                countryHealthIndicator3, countryHealthIndicator4, countryHealthIndicator5);

    }

    @Test
    public void shouldGetCountryName() throws Exception {
        assertEquals(countryName, new CountryHealthIndicators(countryHealthIndicators).getCountryName());
    }

    @Test
    public void shouldGetCountryAlpha2Code() throws Exception {
        assertEquals(countryAlpha2Code, new CountryHealthIndicators(countryHealthIndicators).getCountryAlpha2Code());
    }

    @Test
    public void ShouldgetOverallScoreForMainIndicators() throws Exception{
        assertEquals(2.0, new CountryHealthIndicators(countryHealthIndicators).getOverallScore(),0.5);
    }
    
}