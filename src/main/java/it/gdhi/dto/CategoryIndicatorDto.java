package it.gdhi.dto;

import it.gdhi.model.Category;
import it.gdhi.model.Indicator;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

import static java.util.Collections.emptyList;
import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toList;

@Getter
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class CategoryIndicatorDto {

    private Integer categoryId;
    private String categoryName;
    private List<IndicatorDto> indicators;

    public CategoryIndicatorDto(Category category) {
        this.categoryId = category.getId();
        this.categoryName = category.getName();
        this.indicators = transformToIndicatorDto(category);
    }

    private List<IndicatorDto> transformToIndicatorDto(Category category) {
        List<Indicator> indicators = category.getIndicators();

        return indicators != null  ? indicators.stream().map(IndicatorDto::new)
                .sorted(comparing(IndicatorDto::getIndicatorRank))
                .collect(toList()) : emptyList();
    }

    public CategoryIndicatorDto translateCategoryName(String translatedCategoryName){
        this.categoryName = translatedCategoryName;
        return this;
    }

}
