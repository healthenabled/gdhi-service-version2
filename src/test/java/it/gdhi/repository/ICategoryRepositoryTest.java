package it.gdhi.repository;

import it.gdhi.model.Category;
import it.gdhi.model.CategoryIndicator;
import it.gdhi.model.Indicator;
import it.gdhi.model.IndicatorScore;
import it.gdhi.model.id.CategoryIndicatorId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@Transactional
@ActiveProfiles("test")
public class ICategoryRepositoryTest {
    @Autowired
    ICategoryRepository iCategoryRepository;
    @Autowired
    private EntityManager entityManager;


    @Test
    public void shouldFetchAllCategoriesWithAssociation() throws Exception {
        Indicator indicator20001 = Indicator.builder().indicatorId(20001).code("21").rank(2).parentId(null)
                .name("indicator 21").definition("this is indicator 21").build();
        Indicator indicator20000 = Indicator.builder().indicatorId(20000).code("20").rank(1).parentId(null)
                .name("indicator 20").definition("this is indicator 20").build();
        entityManager.persist(indicator20001);
        entityManager.persist(indicator20000);
        Category category9 = Category.builder().id(9).name("categort 9").build();
        Category category8 = Category.builder().id(8).name("categort 8").build();
        entityManager.persist(category9);
        entityManager.persist(category8);
        CategoryIndicator categoryIndicator1 = CategoryIndicator.builder()
                .categoryIndicatorId(CategoryIndicatorId.builder().categoryId(category9.getId()).indicatorId(indicator20001.getIndicatorId()).build()).build();
        CategoryIndicator categoryIndicator2 = CategoryIndicator.builder()
                .categoryIndicatorId(CategoryIndicatorId.builder().categoryId(category9.getId()).indicatorId(indicator20000.getIndicatorId()).build()).build();
        entityManager.persist(categoryIndicator1);
        entityManager.persist(categoryIndicator2);
        IndicatorScore indicatorScore = IndicatorScore.builder().id(1800L).indicatorId(indicator20000.getIndicatorId
                ()).score(null).definition("NA").build();
        IndicatorScore indicatorScore1 = IndicatorScore.builder().id(1900L).indicatorId(indicator20000.getIndicatorId
                ()).score(2).definition("score 2").build();
        IndicatorScore indicatorScore2 = IndicatorScore.builder().id(5000L).indicatorId(indicator20000.getIndicatorId
                ()).score(1).definition("score 1").build();
        entityManager.persist(indicatorScore);
        entityManager.persist(indicatorScore1);
        entityManager.persist(indicatorScore2);
        entityManager.flush();
        entityManager.clear();

        List<Category> categories = iCategoryRepository.findAllByOrderById();
        assertEquals(9, categories.size());
        Category category = categories.get(8);
        assertThat(category.getName(), is(category9.getName()));
        assertEquals(2, category.getIndicators().size());
        Indicator indicator = category.getIndicators().get(0);
        assertThat(indicator.getName(), is(indicator20000.getName()));
        assertThat(indicator.getDefinition(), is(indicator20000.getDefinition()));
        List<IndicatorScore> options = indicator.getOptions();
        assertEquals(3, options.size());
        IndicatorScore actualIndicatorScore = options.get(2);
        assertNull(actualIndicatorScore.getScore());
        actualIndicatorScore = options.get(0);
        assertEquals(1, actualIndicatorScore.getScore().intValue());
        actualIndicatorScore = options.get(1);
        assertEquals(2, actualIndicatorScore.getScore().intValue());
    }
}