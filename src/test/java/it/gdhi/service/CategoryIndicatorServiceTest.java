package it.gdhi.service;

import it.gdhi.dto.CategoryIndicatorDto;
import it.gdhi.dto.ScoreDto;
import it.gdhi.internationalization.service.HealthIndicatorTranslator;
import it.gdhi.model.Category;
import it.gdhi.model.Indicator;
import it.gdhi.model.IndicatorScore;
import it.gdhi.repository.ICategoryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static com.google.common.collect.ImmutableList.of;
import static it.gdhi.utils.LanguageCode.en;
import static it.gdhi.utils.LanguageCode.pt;
import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CategoryIndicatorServiceTest {

    @InjectMocks
    private CategoryIndicatorService categoryIndicatorService;
    @Mock
    private ICategoryRepository iCategoryRepository;
    @Mock
    private HealthIndicatorTranslator translator;

    @Test
    public void shouldGetTransformedCategoryDtoInSortedByCategoryIdList() {
        Indicator indicator1 = new Indicator(1, "Ind 1", "Ind Def 1", 1);
        Category category1 = Category.builder().id(1).name("Cat 1").indicators(asList(indicator1)).build();
        Indicator indicator2 = new Indicator(1, "Ind 2", "Ind Def 2", 2);
        Category category2 = Category.builder().id(4).name("Cat 4").indicators(asList(indicator2)).build();

        List<Category> categories= asList(category1, category2);
        CategoryIndicatorDto categoryIndicator1 = new CategoryIndicatorDto(category1);
        CategoryIndicatorDto categoryIndicator2 = new CategoryIndicatorDto(category2);

        when(iCategoryRepository.findAllByOrderById()).thenReturn(categories);
        when(translator.translateHealthIndicatorOptions(categoryIndicator1, en)).thenReturn(categoryIndicator1);
        when(translator.translateHealthIndicatorOptions(categoryIndicator2, en)).thenReturn(categoryIndicator2);

        List<CategoryIndicatorDto> categoryIndicatorMapping = categoryIndicatorService.getHealthIndicatorOptions(en);
        assertThat(categoryIndicatorMapping.size(), is(2));
        assertThat(categoryIndicatorMapping.get(0).getCategoryId(), is(1));
        assertThat(categoryIndicatorMapping.get(0).getCategoryName(), is("Cat 1"));
        assertThat(categoryIndicatorMapping.get(0).getIndicators().get(0).getIndicatorId(), is(1));
        assertThat(categoryIndicatorMapping.get(0).getIndicators().get(0).getIndicatorName(), is("Ind 1"));
        assertThat(categoryIndicatorMapping.get(0).getIndicators().get(0).getIndicatorDefinition(), is("Ind Def 1"));
        assertThat(categoryIndicatorMapping.get(1).getCategoryId(), is(4));
        assertThat(categoryIndicatorMapping.get(1).getCategoryName(), is("Cat 4"));
        assertThat(categoryIndicatorMapping.get(1).getIndicators().get(0).getIndicatorId(), is(1));
        assertThat(categoryIndicatorMapping.get(1).getIndicators().get(0).getIndicatorName(), is("Ind 2"));
        assertThat(categoryIndicatorMapping.get(1).getIndicators().get(0).getIndicatorDefinition(), is("Ind Def 2"));

    }

    @Test
    public void shouldGetTransformedCategoryDto() {
        List<Category> categories;

        IndicatorScore option1 = IndicatorScore.builder().indicatorId(1).score(1).definition("Score 1").build();
        IndicatorScore option2 = IndicatorScore.builder().indicatorId(1).score(2).definition("Score 2").build();
        Indicator indicator1 = Indicator.builder().indicatorId(1).name("Ind 1").definition("Ind Def 1").options(asList(option1, option2)).build();
        Category category1 = Category.builder().id(1).name("Cat 1").indicators(asList(indicator1)).build();

        IndicatorScore option3 = IndicatorScore.builder().indicatorId(2).score(3).definition("Score 3").build();
        IndicatorScore option4 = IndicatorScore.builder().indicatorId(2).score(4).definition("Score 4").build();
        Indicator indicator2 = Indicator.builder().indicatorId(2).name("Ind 2").definition("Ind Def 2").options(asList(option3, option4)).build();
        Category category2 = Category.builder().id(4).name("Cat 4").indicators(asList(indicator2)).build();

        categories = asList(category1, category2);
        CategoryIndicatorDto categoryIndicator1 = new CategoryIndicatorDto(category1);
        CategoryIndicatorDto categoryIndicator2 = new CategoryIndicatorDto(category2);

        when(iCategoryRepository.findAllByOrderById()).thenReturn(categories);
        when(translator.translateHealthIndicatorOptions(categoryIndicator1, en)).thenReturn(categoryIndicator1);
        when(translator.translateHealthIndicatorOptions(categoryIndicator2, en)).thenReturn(categoryIndicator2);

        List<CategoryIndicatorDto> categoryIndicatorMapping = categoryIndicatorService.getHealthIndicatorOptions(en);

        assertThat(categoryIndicatorMapping.size(), is(2));
        assertThat(categoryIndicatorMapping.get(0).getCategoryId(), is(1));
        assertThat(categoryIndicatorMapping.get(0).getCategoryName(), is("Cat 1"));
        assertThat(categoryIndicatorMapping.get(0).getIndicators().get(0).getIndicatorId(), is(1));
        assertThat(categoryIndicatorMapping.get(0).getIndicators().get(0).getIndicatorName(), is("Ind 1"));
        assertThat(categoryIndicatorMapping.get(0).getIndicators().get(0).getIndicatorDefinition(), is("Ind Def 1"));
        assertThat(categoryIndicatorMapping.get(0).getIndicators().get(0).getScores().stream().map(ScoreDto::getScoreDefinition)
                .collect(toList()), hasItems("Score 1", "Score 2"));
        assertThat(categoryIndicatorMapping.get(1).getCategoryId(), is(4));
        assertThat(categoryIndicatorMapping.get(1).getCategoryName(), is("Cat 4"));
        assertThat(categoryIndicatorMapping.get(1).getIndicators().get(0).getIndicatorId(), is(2));
        assertThat(categoryIndicatorMapping.get(1).getIndicators().get(0).getIndicatorName(), is("Ind 2"));
        assertThat(categoryIndicatorMapping.get(1).getIndicators().get(0).getIndicatorDefinition(), is("Ind Def 2"));
        assertThat(categoryIndicatorMapping.get(1).getIndicators().get(0).getScores().stream().map(ScoreDto::getScoreDefinition)
                .collect(toList()), hasItems("Score 3", "Score 4"));

    }

    @Test
    public void shouldGetTransformedCategoryDtoInSortedByIndicatorIdListForEachCategory() {

        Indicator indicator1 = new Indicator(5, "Ind 1", "Ind Def 1", 1);
        Indicator indicator2 = new Indicator(1, "Ind 5", "Ind Def 5", 5);
        Category category1 = Category.builder().id(1).name("Cat 1").indicators(asList(indicator1, indicator2)).build();

        Indicator indicator4 = new Indicator(8, "Ind 8", "Ind Def 8", 8);
        Indicator indicator3 = new Indicator(2, "Ind 2", "Ind Def 2", 2);
        Category category2 = Category.builder().id(4).name("Cat 4").indicators(asList(indicator3, indicator4)).build();

        List<Category> categories = asList(category1, category2);
        when(iCategoryRepository.findAllByOrderById()).thenReturn(categories);
        CategoryIndicatorDto categoryIndicator1 = new CategoryIndicatorDto(category1);
        CategoryIndicatorDto categoryIndicator2 = new CategoryIndicatorDto(category2);
        when(translator.translateHealthIndicatorOptions(categoryIndicator1, en)).thenReturn(categoryIndicator1);
        when(translator.translateHealthIndicatorOptions(categoryIndicator2, en)).thenReturn(categoryIndicator2);


        when(iCategoryRepository.findAllByOrderById()).thenReturn(categories);

        List<CategoryIndicatorDto> categoryIndicatorMapping = categoryIndicatorService.getHealthIndicatorOptions(en);
        assertThat(categoryIndicatorMapping.size(), is(2));
        assertThat(categoryIndicatorMapping.get(0).getCategoryId(), is(1));
        assertThat(categoryIndicatorMapping.get(0).getCategoryName(), is("Cat 1"));
        assertThat(categoryIndicatorMapping.get(0).getIndicators().get(0).getIndicatorId(), is(5));
        assertThat(categoryIndicatorMapping.get(0).getIndicators().get(0).getIndicatorName(), is("Ind 1"));
        assertThat(categoryIndicatorMapping.get(0).getIndicators().get(0).getIndicatorDefinition(), is("Ind Def 1"));

        assertThat(categoryIndicatorMapping.get(0).getIndicators().get(1).getIndicatorId(), is(1));
        assertThat(categoryIndicatorMapping.get(0).getIndicators().get(1).getIndicatorName(), is("Ind 5"));
        assertThat(categoryIndicatorMapping.get(0).getIndicators().get(1).getIndicatorDefinition(), is("Ind Def 5"));

        assertThat(categoryIndicatorMapping.get(1).getCategoryId(), is(4));
        assertThat(categoryIndicatorMapping.get(1).getCategoryName(), is("Cat 4"));
        assertThat(categoryIndicatorMapping.get(1).getIndicators().get(0).getIndicatorId(), is(2));
        assertThat(categoryIndicatorMapping.get(1).getIndicators().get(0).getIndicatorName(), is("Ind 2"));
        assertThat(categoryIndicatorMapping.get(1).getIndicators().get(0).getIndicatorDefinition(), is("Ind Def 2"));

        assertThat(categoryIndicatorMapping.get(1).getIndicators().get(1).getIndicatorId(), is(8));
        assertThat(categoryIndicatorMapping.get(1).getIndicators().get(1).getIndicatorName(), is("Ind 8"));
        assertThat(categoryIndicatorMapping.get(1).getIndicators().get(1).getIndicatorDefinition(), is("Ind Def 8"));

    }

    @Test
    public void shouldHandleNullIndicatorsForACategory() {

        Category category1 = Category.builder().id(1).name("Cat 1").indicators(null).build();
        when(iCategoryRepository.findAllByOrderById()).thenReturn(asList(category1));
        CategoryIndicatorDto categoryIndicator1 = new CategoryIndicatorDto(category1);
        when(translator.translateHealthIndicatorOptions(categoryIndicator1, en)).thenReturn(categoryIndicator1);

        List<CategoryIndicatorDto> categoryIndicatorMapping = categoryIndicatorService.getHealthIndicatorOptions(en);
        assertThat(categoryIndicatorMapping.size(), is(1));
        assertThat(categoryIndicatorMapping.get(0).getCategoryId(), is(1));
        assertThat(categoryIndicatorMapping.get(0).getCategoryName(), is("Cat 1"));
        assertTrue(categoryIndicatorMapping.get(0).getIndicators().isEmpty());
    }

    @Test
    public void shouldReturnCategoryIndicatorDTOTranslatedInPortuguese() {
        Category categoryEN = new Category(2, "Strategy and Investment", of(new Indicator(1, "Digital health prioritized at the national level through dedicated bodies / mechanisms for governance", "Does the country have a separate department / agency / national working group for digital health?", 4)));
        CategoryIndicatorDto categoryIndicatorEN = new CategoryIndicatorDto(categoryEN);

        Category categoryPT = new Category(2, "Stratégie et investissement", of(new Indicator(1, "Saúde digital priorizada a nível nacional através de órgãos/mecanismos dedicados à governação", "O país tem um departamento / agência / grupo de trabalho nacional separado para a saúde digital?", 4)));
        CategoryIndicatorDto categoryIndicatorPT = new CategoryIndicatorDto(categoryPT);


        List<Category> categories= asList(categoryEN);
        when(iCategoryRepository.findAllByOrderById()).thenReturn(categories);
        when(translator.translateHealthIndicatorOptions(categoryIndicatorEN, pt)).thenReturn(categoryIndicatorPT);

        List<CategoryIndicatorDto> healthIndicatorOptions = categoryIndicatorService.getHealthIndicatorOptions(pt);

        assertEquals(categoryIndicatorPT, healthIndicatorOptions.get(0));
    }
}