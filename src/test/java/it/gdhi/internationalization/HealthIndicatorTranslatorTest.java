package it.gdhi.internationalization;

import it.gdhi.dto.CategoryHealthScoreDto;
import it.gdhi.dto.CategoryIndicatorDto;
import it.gdhi.dto.CountryHealthScoreDto;
import it.gdhi.dto.IndicatorScoreDto;
import it.gdhi.internationalization.model.HealthIndicatorTranslationId;
import it.gdhi.internationalization.model.IndicatorTranslation;
import it.gdhi.internationalization.repository.ICategoryTranslationRepository;
import it.gdhi.internationalization.repository.IIndicatorTranslationRepository;
import it.gdhi.internationalization.repository.IScoreDefinitionTranslationRepository;
import it.gdhi.internationalization.service.CountryNameTranslator;
import it.gdhi.internationalization.service.HealthIndicatorTranslator;
import it.gdhi.model.Category;
import it.gdhi.model.Indicator;
import it.gdhi.model.IndicatorScore;
import it.gdhi.utils.LanguageCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.google.common.collect.ImmutableList.of;
import static it.gdhi.utils.LanguageCode.en;
import static it.gdhi.utils.LanguageCode.fr;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class HealthIndicatorTranslatorTest {

    @InjectMocks
    private HealthIndicatorTranslator translator;
    @Mock
    private ICategoryTranslationRepository categoryTranslationRepo;
    @Mock
    private IIndicatorTranslationRepository indicatorTranslationRepo;
    @Mock
    private IScoreDefinitionTranslationRepository scoreTranslationRepo;
    @Mock
    private CountryNameTranslator countryNameTranslator;

    private String categoryNameEN;
    private String categoryNameFR;

    private String indicatorNameEN;
    private String indicatorDefEN;
    private Indicator indicatorEN;
    private String indicatorNameFR;
    private String indicatorDefFR;
    private Indicator indicatorFR;
    private IndicatorTranslation indicatorTranslationFR;

    private String scoreDefEN;
    private IndicatorScore scoreEN;
    private String scoreDefFR;
    private IndicatorScore scoreFR;

    private CategoryHealthScoreDto categoryScoreEN;
    private IndicatorScoreDto indicatorScoreEN;
    private CountryHealthScoreDto countryHealthScoreEN;

    @BeforeEach
    public void setUp() {
        categoryNameEN = "Strategy and Investment";
        categoryNameFR = "Stratégie et investissement";

        scoreDefEN = "Governance structure is fully-functional, government-led, consults with other " +
                            "ministries, and monitors implementation of digital health based on a work plan.";
        scoreEN = new IndicatorScore(22L, 1, 4, scoreDefEN);
        indicatorNameEN = "Digital health prioritized at the national level through dedicated bodies / " +
                        "mechanisms for governance";
        indicatorDefEN = "Does the country have a separate department / agency / national working group for digital health?";
        indicatorEN = new Indicator(1, indicatorNameEN, "1", 4, 1,
                                            of(scoreEN), indicatorDefEN);

        HealthIndicatorTranslationId indicatorId = new HealthIndicatorTranslationId(1, fr);
        indicatorNameFR = "Priorité accordée à la santé numérique au niveau national par l''intermédiaire " +
                                "d''organes et de mécanismes de gouvernance dédiés/ mechanisms for governance";
        indicatorDefFR = "Le pays dispose-t-il d''un ministère, d''un organisme ou d''un groupe de " +
                                        "travail national distinct pour la santé numérique ?";
        indicatorTranslationFR = new IndicatorTranslation(indicatorId, indicatorNameFR,
                indicatorDefFR, indicatorEN);
        scoreDefFR = "La structure de gouvernance est completement fonctionnelle, dirigée par le " +
                                    "gouvernement, consulte les autres ministères et surveille la mise en œuvre de " +
                                    "la santé numérique en fonction d'un plan de travail.";
        scoreFR = new IndicatorScore(22L, 1, 4, scoreDefFR);
        indicatorFR = new Indicator(1, indicatorNameFR, "1", 4, 1,
                                            of(scoreFR), indicatorDefFR);

        indicatorScoreEN = new IndicatorScoreDto(3, "3", indicatorNameEN, indicatorDefEN,
                3, 4, "Supporting text", scoreDefEN);
        categoryScoreEN = new CategoryHealthScoreDto(1, categoryNameEN, 3.0,
                3, of(indicatorScoreEN));
        countryHealthScoreEN = new CountryHealthScoreDto("IND", "India",
                "IN", of(categoryScoreEN), 4, "Date");
    }

    @Test
    public void shouldReturnCategoryNamesInEnglishGivenUserLanguageIsNull() {
        String workforceEN = "Workforce";

        String actualCategory = translator.getTranslatedCategory(workforceEN, null);

        assertEquals(workforceEN, actualCategory);
    }

    @Test
    public void shouldReturnCategoryNamesInEnglishGivenUserLanguageIsEnglish() {
        String workforceEN = "Workforce";

        String actualCategory = translator.getTranslatedCategory(workforceEN, en);

        assertEquals(workforceEN, actualCategory);
    }

    @Test
    public void shouldNotInvokeCategoryTranslationRepositoryGivenUserLanguageIsNull() {
        String workforceEN = "Workforce";

        translator.getTranslatedCategory(workforceEN, en);

        verify(categoryTranslationRepo, never()).findTranslationForLanguage(any(), anyString());
    }

    @Test
    public void shouldReturnCategoryNamesInFrenchGivenUserLanguageIsFrench() {
        String workforceEN = "Workforce";
        String workforceFR = "Lois, politiques et conformité";

        when(categoryTranslationRepo.findTranslationForLanguage(fr, workforceEN))
                                            .thenReturn("Lois, politiques et conformité");

        String actualCategory = translator.getTranslatedCategory(workforceEN, fr);

        assertEquals(workforceFR, actualCategory);
    }

    @Test
    public void shouldReturnCategoryNameInEnglishGivenCategoryNameInUserLanguageIsNull() {
        String workforceEN = "Workforce";

        when(categoryTranslationRepo.findTranslationForLanguage(fr, workforceEN)).thenReturn(null);

        String actualCategory = translator.getTranslatedCategory(workforceEN, fr);

        assertEquals(workforceEN, actualCategory);
    }

    @Test
    public void shouldReturnCategoryNameInEnglishGivenCategoryNameInUserLanguageIsEmptyString() {
        String workforceEN = "Workforce";

        when(categoryTranslationRepo.findTranslationForLanguage(fr, workforceEN)).thenReturn("");

        String actualCategory = translator.getTranslatedCategory(workforceEN, fr);

        assertEquals(workforceEN, actualCategory);
    }

    @Test
    public void shouldNotTranslateCategoryIndicatorGivenLanguageCodeIsEN() {
        Category categoryEN = new Category(2, categoryNameEN, of(new Indicator(1,
        "Digital health prioritized at the national level through dedicated bodies / mechanisms for governance",
        "Does the country have a separate department / agency / national working group for digital health?",
                4)));
        CategoryIndicatorDto categoryIndicatorEN = new CategoryIndicatorDto(categoryEN);

        translator.translateHealthIndicatorOptions(categoryIndicatorEN, en);

        verify(categoryTranslationRepo, never()).findTranslationForLanguage(any(), anyString());
        verify(indicatorTranslationRepo, never()).findTranslationForLanguage(any(), anyInt());
        verify(scoreTranslationRepo, never()).findTranslationForLanguage(any(), anyInt(), anyInt());
    }

    @Test
    public void shouldNotTranslateCategoryIndicatorGivenLanguageCodeIsNull() {
        Category categoryEN = new Category(2, categoryNameEN, of(new Indicator(1,
        "Digital health prioritized at the national level through dedicated bodies / mechanisms for governance",
        "Does the country have a separate department / agency / national working group for digital health?",
                4)));
        CategoryIndicatorDto categoryIndicatorEN = new CategoryIndicatorDto(categoryEN);

        translator.translateHealthIndicatorOptions(categoryIndicatorEN, null);

        verify(categoryTranslationRepo, never()).findTranslationForLanguage(any(), anyString());
        verify(indicatorTranslationRepo, never()).findTranslationForLanguage(any(), anyInt());
        verify(scoreTranslationRepo, never()).findTranslationForLanguage(any(), anyInt(), anyInt());
    }

    @Test
    public void shouldTranslateCategoryIndicatorToFrench() {
        Category categoryEN = new Category(2, categoryNameEN, of(indicatorEN));
        CategoryIndicatorDto categoryIndicatorEN = new CategoryIndicatorDto(categoryEN);
        Category categoryFR = new Category(2, "Stratégie et investissement", of(indicatorFR));
        CategoryIndicatorDto categoryIndicatorFR = new CategoryIndicatorDto(categoryFR);

        when(categoryTranslationRepo
                .findTranslationForLanguage(fr, categoryNameEN))
                .thenReturn("Stratégie et investissement");
        when(indicatorTranslationRepo.findTranslationForLanguage(fr, 1))
                .thenReturn(indicatorTranslationFR);
        when(scoreTranslationRepo.findTranslationForLanguage(fr, 1, 4))
                .thenReturn(scoreDefFR);

        CategoryIndicatorDto translatedIndicator = translator.translateHealthIndicatorOptions(categoryIndicatorEN, LanguageCode.fr);

        assertEquals(categoryIndicatorFR, translatedIndicator);
    }

    @Test
    public void shouldTranslateCountryHealthScoreToFrench() {
        IndicatorScoreDto indicatorScoreFR = new IndicatorScoreDto(3, "3", indicatorNameFR, indicatorDefFR,
                                                3, 4, "Supporting text", scoreDefFR);
        CategoryHealthScoreDto categoryScoreFR = new CategoryHealthScoreDto(1, categoryNameFR, 3.0,
                                                                        3, of(indicatorScoreFR));
        CountryHealthScoreDto countryHealthScoreFR = new CountryHealthScoreDto("IND", "Inde",
                                    "IN", of(categoryScoreFR), 4, "Date");

        when(countryNameTranslator.getCountryTranslationForLanguage(fr, "IND"))
                .thenReturn("Inde");
        when(categoryTranslationRepo.findTranslationForLanguage(fr, categoryNameEN))
                .thenReturn("Stratégie et investissement");
        when(indicatorTranslationRepo.findTranslationForLanguage(fr, 3))
                .thenReturn(indicatorTranslationFR);
        when(scoreTranslationRepo.findTranslationForLanguage(fr, 3, 4))
                .thenReturn(scoreDefFR);

        CountryHealthScoreDto translatedHealthScore = translator.translateCountryHealthScores(fr, countryHealthScoreEN);

        assertEquals(countryHealthScoreFR, translatedHealthScore);
    }

    @Test
    public void shouldNotTranslateCountryHealthIndicatorGivenLanguageCodeIsEN() {
        translator.translateCountryHealthScores(en, countryHealthScoreEN);

        verify(countryNameTranslator, never()).getCountryTranslationForLanguage(any(), anyString());
        verify(categoryTranslationRepo, never()).findTranslationForLanguage(any(), anyString());
        verify(indicatorTranslationRepo, never()).findTranslationForLanguage(any(), anyInt());
        verify(scoreTranslationRepo, never()).findTranslationForLanguage(any(), anyInt(), anyInt());
    }

    @Test
    public void shouldNotTranslateCountryHealthIndicatorGivenLanguageCodeIsNull() {
        translator.translateCountryHealthScores(null, countryHealthScoreEN);

        verify(countryNameTranslator, never()).getCountryTranslationForLanguage(any(), anyString());
        verify(categoryTranslationRepo, never()).findTranslationForLanguage(any(), anyString());
        verify(indicatorTranslationRepo, never()).findTranslationForLanguage(any(), anyInt());
        verify(scoreTranslationRepo, never()).findTranslationForLanguage(any(), anyInt(), anyInt());
    }
}