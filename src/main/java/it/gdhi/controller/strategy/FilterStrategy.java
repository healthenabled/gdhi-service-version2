package it.gdhi.controller.strategy;

import it.gdhi.dto.CategoryHealthScoreDto;
import it.gdhi.dto.CountryHealthScoreDto;

import java.util.function.Predicate;

import static java.util.Objects.isNull;

public class FilterStrategy {
    public static Predicate<? super CategoryHealthScoreDto> getCategoryPhaseFilter(Integer categoryId, Integer phase) {
        Predicate<? super CategoryHealthScoreDto> categoryPhaseFilter = categoryHealthScoreDto -> true;
        if (categoryId != null && phase != null) {
            categoryPhaseFilter = categoryHealthScoreDto -> !isNull(categoryHealthScoreDto.getPhase()) &&
                                                            categoryHealthScoreDto.getPhase().equals(phase);
        }
        return categoryPhaseFilter;
    }

    public static Predicate<? super CountryHealthScoreDto> getCountryPhaseFilter(Integer categoryId, Integer phase) {
        Predicate<? super CountryHealthScoreDto> countryPhaseFilter = countryHealthScoreDto -> true;
        if (categoryId == null && phase != null) {
            countryPhaseFilter = (countryHealthScoreDto -> !isNull(countryHealthScoreDto.getCountryPhase()) &&
                                                           countryHealthScoreDto.getCountryPhase().equals(phase));
        }
        return countryPhaseFilter;
    }
}
