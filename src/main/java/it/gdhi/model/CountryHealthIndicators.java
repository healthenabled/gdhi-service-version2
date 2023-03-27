package it.gdhi.model;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.averagingInt;
import static java.util.stream.Collectors.groupingBy;

@NoArgsConstructor
@Getter
public class CountryHealthIndicators {

    private Country country;

    private List<CountryHealthIndicator> countryHealthIndicators;

    public CountryHealthIndicators(List<CountryHealthIndicator> countryHealthIndicators) {
        this.countryHealthIndicators = countryHealthIndicators;
        this.country = Optional.ofNullable(countryHealthIndicators)
                .filter(indicators -> indicators.size() > 0)
                .map(indicators -> indicators.get(0).getCountry())
                .orElse(null);
    }

    public Map<Integer, Double> groupByCategoryIdWithoutNullAndNegativeScores() {

        return convertingNullScoresToNotAvailable(countryHealthIndicators).filter(countryHealthIndicator
                -> countryHealthIndicator.getIndicator() != null
                && countryHealthIndicator.getIndicator().getParentId() == null && countryHealthIndicator.getScore() != -1 )
                .collect(groupingBy(h -> h.getCategory().getId(),
                        averagingInt(CountryHealthIndicator::getScore)));
    }

    public Double getOverallScore() {
        OptionalDouble optionalDouble = this.groupByCategoryIdWithoutNullAndNegativeScores()
                .values()
                .stream()
                .mapToDouble(Double::doubleValue)
                .average();
        return optionalDouble.isPresent() ? optionalDouble.getAsDouble() : null;
    }

    public String getCountryName() {
        return Optional.ofNullable(country)
                .map(Country::getName)
                .orElse(null);
    }

    public String getCountryAlpha2Code() {
        return Optional.ofNullable(country)
                .map(Country::getAlpha2Code)
                .orElse(null);
    }

    public Map<Category, List<CountryHealthIndicator>> groupByCategory() {
        return this.countryHealthIndicators.stream().collect(groupingBy(CountryHealthIndicator::getCategory));
    }

    private Stream<CountryHealthIndicator> excludingNullAndNegativeScores() {
        return countryHealthIndicators.stream()
                .filter(healthIndicator -> healthIndicator.isScoreValid());
    }

    private Stream<CountryHealthIndicator> convertingNullScoresToNotAvailable(
            List<CountryHealthIndicator> newCountryHealthIndicators) {

        return countryHealthIndicators.stream().map(row -> {
            return new CountryHealthIndicator(row.getCountryHealthIndicatorId(),
                    (row.getScore()== null) ? -1 : row.getScore(),
                    row.getIndicator(),row.getCategory());

        }).collect(Collectors.toList()).stream();

    }

}
