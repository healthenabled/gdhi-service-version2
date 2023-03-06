package it.gdhi.service;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import it.gdhi.dto.CategoryHealthScoreDto;
import it.gdhi.dto.CountriesHealthScoreDto;
import it.gdhi.dto.CountryHealthScoreDto;
import it.gdhi.dto.GlobalHealthScoreDto;
import it.gdhi.dto.IndicatorScoreDto;
import it.gdhi.internationalization.service.HealthIndicatorTranslator;
import it.gdhi.model.Category;
import it.gdhi.model.Country;
import it.gdhi.model.CountryHealthIndicator;
import it.gdhi.model.CountryPhase;
import it.gdhi.model.CountrySummary;
import it.gdhi.model.Indicator;
import it.gdhi.model.IndicatorScore;
import it.gdhi.model.id.CountryHealthIndicatorId;
import it.gdhi.model.id.CountryPhaseId;
import it.gdhi.repository.ICountryHealthIndicatorRepository;
import it.gdhi.repository.ICountryPhaseRepository;
import it.gdhi.repository.ICountrySummaryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import static com.google.common.collect.ImmutableList.of;
import static it.gdhi.utils.FormStatus.PUBLISHED;
import static it.gdhi.utils.LanguageCode.USER_LANGUAGE;
import static it.gdhi.utils.LanguageCode.ar;
import static it.gdhi.utils.LanguageCode.en;
import static it.gdhi.utils.LanguageCode.es;
import static it.gdhi.utils.LanguageCode.fr;
import static it.gdhi.utils.ListUtils.findFirst;
import static java.util.Arrays.asList;
import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyList;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class CountryHealthIndicatorServiceTest {

    @InjectMocks
    CountryHealthIndicatorService countryHealthIndicatorService;

    @Mock
    ICountryHealthIndicatorRepository iCountryHealthIndicatorRepository;

    @Mock
    ICountryPhaseRepository iCountryPhaseRepository;

    @Mock
    private ExcelUtilService excelUtilService;

    @Mock
    private ICountrySummaryRepository iCountrySummaryRepository;

    @Mock
    private HealthIndicatorTranslator indicatorTranslator;

    private void dataSet(String countryId1, int categoryId1, int categoryId2,
                         int indicatorId1, int indicatorId2, int indicatorId3, String year) {
        Integer score1 = 3;
        Integer score2 = 4;
        Integer score3 = 1;
        String publishedStatus = "PUBLISHED";
        year = "Version1";

        CountryHealthIndicatorId countryHealthIndicatorId1 = new CountryHealthIndicatorId(countryId1, categoryId1, indicatorId1, publishedStatus, year);
        Country country1 = new Country("IND", "India", UUID.randomUUID(), "IN");
        Category category1 = new Category(categoryId1, "Leadership and Governance");
        Indicator indicator1 = new Indicator(indicatorId1, "Indicator 1", "Definition1", 1);
        IndicatorScore indicatorScore1 = IndicatorScore.builder().id(1L).indicatorId(indicatorId2).score(score2).definition("score 1").build();
        CountryHealthIndicator countryHealthIndicator1 = new CountryHealthIndicator(countryHealthIndicatorId1, country1,
                category1, indicator1, indicatorScore1, score1, "st1", new Date(), null);

        CountryHealthIndicatorId countryHealthIndicatorId2 = new CountryHealthIndicatorId(countryId1, categoryId2, indicatorId2, publishedStatus, year);
        Category category2 = new Category(categoryId2, "Category2");
        Indicator indicator2 = new Indicator(indicatorId2, "Indicator 2", "Definition2", 2);
        Indicator indicator3 = new Indicator(indicatorId3, "Indicator 3", "Definition3", 3);

        IndicatorScore indicatorScore2 = IndicatorScore.builder().id(1L).indicatorId(indicatorId2).score(score2).definition("score 2").build();
        CountryHealthIndicator countryHealthIndicator2 = new CountryHealthIndicator(countryHealthIndicatorId2,
                country1, category2, indicator2, indicatorScore2, score2, "st2", new Date(), null);
        IndicatorScore indicatorScore3 = IndicatorScore.builder().id(2L).indicatorId(indicatorId3).score(score3).definition("score 3").build();
        CountryHealthIndicator countryHealthIndicator3 = new CountryHealthIndicator(countryHealthIndicatorId2,
                country1, category2, indicator3, indicatorScore3, score3, "st3", new Date(), null);

        List<CountryHealthIndicator> countryHealthIndicatorsForCountry = asList(countryHealthIndicator3, countryHealthIndicator2, countryHealthIndicator1);

        when(iCountryHealthIndicatorRepository.findByCountryHealthIndicatorIdCountryIdAndCountryHealthIndicatorIdStatusAndCountryHealthIndicatorIdYear(countryId1, publishedStatus, year)).thenReturn(countryHealthIndicatorsForCountry);
        CountryPhase countryPhaseIND = buildCountryPhase("IND", 3);
        when(iCountryPhaseRepository.findByCountryPhaseIdCountryIdAndCountryPhaseIdYear("IND", year)).thenReturn(countryPhaseIND);
    }

    @Test
    public void shouldReturnCountryHealthScoreGivenCountryIdAndYearForDifferentCategory() {
        String countryId = "IND";
        Integer categoryId1 = 1;
        Integer categoryId2 = 2;
        Integer indicatorId1 = 1;
        Integer indicatorId2 = 2;
        Integer indicatorId3 = 3;
        String countryName = "India";
        String year = "Version1";

        dataSet(countryId, categoryId1, categoryId2, indicatorId1, indicatorId2, indicatorId3, year);
        CategoryHealthScoreDto categoryHealthScoreDto1 = new CategoryHealthScoreDto(1, "Leadership and Governance", 3.0, 3, of(new IndicatorScoreDto(1, null, "Indicator 1", "Definition1", 1, 3, "st1", "score 1")));
        CategoryHealthScoreDto categoryHealthScoreDto2 = new CategoryHealthScoreDto(2, "Category2", 2.5, 3, of(new IndicatorScoreDto(2, null, "Indicator 2", "Definition2", 2, 4, "st2", "score 2"), new IndicatorScoreDto(3, null, "Indicator 3", "Definition3", 3, 1, "st3", "score 3")));
        CountryHealthScoreDto countryHealthScoreDto = new CountryHealthScoreDto("IND", "India", "IN", of(categoryHealthScoreDto1, categoryHealthScoreDto2), 3, "");
        when(indicatorTranslator.translateCountryHealthScores(en, countryHealthScoreDto)).thenReturn(countryHealthScoreDto);

        CountryHealthScoreDto healthScoreForACountry = countryHealthIndicatorService.fetchCountryHealthScore(countryId, en, year);

        assertSet1(healthScoreForACountry, countryId, countryName);
    }

    @Test
    public void shouldReturnCountryHealthScoresInSpanishGivenCountryIdAndYear() {
        String countryId = "IND";
        Integer categoryId1 = 1;
        Integer categoryId2 = 2;
        Integer indicatorId1 = 1;
        Integer indicatorId2 = 2;
        Integer indicatorId3 = 3;
        String year = "Version1";
        dataSet(countryId, categoryId1, categoryId2, indicatorId1, indicatorId2, indicatorId3, year);

        CategoryHealthScoreDto categoryHealthScoreDto1 = new CategoryHealthScoreDto(1, "Leadership and Governance", 3.0, 3, of(new IndicatorScoreDto(1, null, "Indicator 1", "Definition1", 1, 3, "st1", "score 1")));
        CategoryHealthScoreDto categoryHealthScoreDto2 = new CategoryHealthScoreDto(2, "Category2", 2.5, 3, of(
                new IndicatorScoreDto(2, null, "Indicator 2", "Definition2", 2, 4, "st2", "score 2"),
                new IndicatorScoreDto(3, null, "Indicator 3", "Definition3", 3, 1, "st3", "score 3")));
        CountryHealthScoreDto countryHealthScoreDtoEN = new CountryHealthScoreDto("IND", "India", "IN", of(categoryHealthScoreDto1, categoryHealthScoreDto2), 3, "");
        CategoryHealthScoreDto categoryHealthScoreDto3 = new CategoryHealthScoreDto(1, "Leadership and Governance Spanish", 3.0, 3, of(new IndicatorScoreDto(1, null, "Indicator 1 Spanish", "Definition1 Spanish", 1, 3, "st1", "score 1 Spanish")));
        CategoryHealthScoreDto categoryHealthScoreDto4 = new CategoryHealthScoreDto(2, "Category2 Spanish", 2.5, 3, of(
                new IndicatorScoreDto(2, null, "Indicator 2 Spanish", "Definition2 Spanish", 2, 4, "st2", "score 2 Spanish"),
                new IndicatorScoreDto(3, null, "Indicator 3 Spanish", "Definition3 Spanish", 3, 1, "st3", "score 3 Spanish")));
        CountryHealthScoreDto countryHealthScoreDtoES = new CountryHealthScoreDto("IND", "India", "IN", of(categoryHealthScoreDto3, categoryHealthScoreDto4), 3, "");

        when(indicatorTranslator.translateCountryHealthScores(es, countryHealthScoreDtoEN)).thenReturn(countryHealthScoreDtoES);

        CountryHealthScoreDto healthScoreForACountry = countryHealthIndicatorService.fetchCountryHealthScore(countryId, es, year);

        assertEquals(countryHealthScoreDtoES, healthScoreForACountry);
    }

    private void assertSet1(CountryHealthScoreDto healthScoreForACountry, String countryId, String countryName) {
        assertThat(healthScoreForACountry.getCountryId(), is(countryId));
        assertThat(healthScoreForACountry.getCountryName(), is(countryName));
        assertThat(healthScoreForACountry.getCategories().size(), is(2));
        CategoryHealthScoreDto leadership = healthScoreForACountry.getCategories().stream().filter(a -> a.getName().equals("Leadership and Governance")).findFirst().get();
        CategoryHealthScoreDto category2 = healthScoreForACountry.getCategories().stream().filter(a -> a.getName().equals("Category2")).findFirst().get();
        assertThat(leadership.getPhase(), is(3));
        assertThat(leadership.getIndicators().size(), is(1));
        IndicatorScoreDto indicator = leadership.getIndicators().stream().filter(ind -> ind.getId().equals(1)).findFirst().get();
        assertThat(indicator.getName(), is("Indicator 1"));
        assertThat(indicator.getScore(), is(3));
        assertThat(indicator.getIndicatorDescription(), is("Definition1"));
        assertThat(indicator.getScoreDescription(), is("score 1"));
        assertThat(indicator.getSupportingText(), is("st1"));
        assertThat(category2.getPhase(), is(3));
        assertThat(category2.getIndicators().size(), is(2));
        indicator = category2.getIndicators().stream().filter(ind -> ind.getId().equals(2)).findFirst().get();
        assertThat(indicator.getName(), is("Indicator 2"));
        assertThat(indicator.getScore(), is(4));
        assertThat(indicator.getIndicatorDescription(), is("Definition2"));
        assertThat(indicator.getScoreDescription(), is("score 2"));
        assertThat(indicator.getSupportingText(), is("st2"));
        indicator = category2.getIndicators().stream().filter(ind -> ind.getId().equals(3)).findFirst().get();
        assertThat(indicator.getName(), is("Indicator 3"));
        assertThat(indicator.getScore(), is(1));
        assertThat(indicator.getIndicatorDescription(), is("Definition3"));
        assertThat(indicator.getScoreDescription(), is("score 3"));
        assertThat(indicator.getSupportingText(), is("st3"));
    }

    @Test
    public void shouldReturnCountryHealthScoreGivenCountryIdAndYearForDifferentNullIndicatorScoreForSameCategory() {
        Integer categoryId1 = 1;
        Integer indicatorId1 = 1;
        Integer indicatorId2 = 2;
        String categoryName = "Leadership and Governance";
        String publishedStatus = "PUBLISHED";

        String countryId = "IND";
        String countryName = "India";
        String year = "Version1";
        CountryHealthIndicatorId countryHealthIndicatorId1 = new CountryHealthIndicatorId(countryId, categoryId1,
                indicatorId1, publishedStatus, year);
        String countryAlpha2Code = "IN";
        Country country1 = new Country(countryId, countryName, UUID.randomUUID(), countryAlpha2Code);
        Category category1 = new Category(categoryId1, categoryName);
        Indicator indicator1 = new Indicator(indicatorId1, "Indicator 1", "Definition", 1);
        CountryHealthIndicator countryHealthIndicator1 = new CountryHealthIndicator(countryHealthIndicatorId1,
                country1, category1, indicator1, IndicatorScore.builder().build(), null, null, new Date(), null);

        CountryHealthIndicatorId countryHealthIndicatorId2 = new CountryHealthIndicatorId(countryId, categoryId1, indicatorId2, publishedStatus, year);
        Indicator indicator2 = new Indicator(indicatorId2, "Indicator 1", "Definition", 2);
        Integer indicatorScore = 2;
        CountryHealthIndicator countryHealthIndicator2 = new CountryHealthIndicator(countryHealthIndicatorId2,
                country1, category1, indicator2, IndicatorScore.builder().build(), indicatorScore, "st1", new Date(), null);

        List<CountryHealthIndicator> countryHealthIndicatorsForCountry = asList(countryHealthIndicator1, countryHealthIndicator2);

        when(iCountryHealthIndicatorRepository.findByCountryHealthIndicatorIdCountryIdAndCountryHealthIndicatorIdStatusAndCountryHealthIndicatorIdYear(countryId, publishedStatus, year)).thenReturn(countryHealthIndicatorsForCountry);
        CountryPhase countryPhaseIND = buildCountryPhase(countryId, 2);
        when(iCountryPhaseRepository.findByCountryPhaseIdCountryIdAndCountryPhaseIdYear(countryId, year)).thenReturn(countryPhaseIND);

        CategoryHealthScoreDto categoryHealthScoreDto1 = new CategoryHealthScoreDto(1, "Leadership and Governance", 1.5, 2, of(
                new IndicatorScoreDto(1, null, "Indicator 1", "Definition", 1, null, null, "Not Available"),
                new IndicatorScoreDto(2, null, "Indicator 1", "Definition", 2, 2, "st1", "Not Available")));
        CountryHealthScoreDto countryHealthScoreDto = new CountryHealthScoreDto("IND", "India", "IN", of(categoryHealthScoreDto1), 2, "");
        when(indicatorTranslator.translateCountryHealthScores(en, countryHealthScoreDto)).thenReturn(countryHealthScoreDto);

        CountryHealthScoreDto healthScoreForACountry = countryHealthIndicatorService.fetchCountryHealthScore(countryId, en, year);
        assertSet4(healthScoreForACountry, countryId, countryName, countryAlpha2Code);
    }

    private void assertSet4(CountryHealthScoreDto healthScoreForACountry, String countryId, String countryName,
                            String countryAlpha2Code) {
        assertThat(healthScoreForACountry.getCountryId(), is(countryId));
        assertThat(healthScoreForACountry.getCountryName(), is(countryName));
        assertThat(healthScoreForACountry.getCountryAlpha2Code(), is(countryAlpha2Code));
        assertThat(healthScoreForACountry.getCategories().size(), is(1));
        List<CategoryHealthScoreDto> leadership = healthScoreForACountry.getCategories().stream().filter(a -> a.getName().equals("Leadership and Governance")).collect(toList());
        assertThat(leadership.size(), is(1));
        assertThat(leadership.get(0).getPhase(), is(2));
    }

    @Test
    public void shouldReturnCountryHealthScoreGivenCountryIdAndYearForANullCategoryScore() {
        String countryId = "IND";
        Integer categoryId1 = 1;
        Integer categoryId2 = 2;
        Integer indicatorId1 = 1;
        Integer indicatorId2 = 2;
        String categoryName = "Leadership and Governance";
        String categoryName2 = "Category2";

        String countryId1 = "IND";
        String countryName = "India";
        String publishedStatus = "PUBLISHED";
        String year = "Version1";
        CountryHealthIndicatorId countryHealthIndicatorId1 = new CountryHealthIndicatorId(countryId1, categoryId1, indicatorId1, publishedStatus, year);
        Country country1 = new Country(countryId, countryName, UUID.randomUUID(), "IN");
        Category category1 = new Category(categoryId1, categoryName);
        Category category2 = new Category(categoryId2, categoryName2);
        Indicator indicator1 = new Indicator(indicatorId1, "Indicator 1", "Definition", 1);
        CountryHealthIndicator countryHealthIndicator1 = new CountryHealthIndicator(countryHealthIndicatorId1,
                country1, category1, indicator1, IndicatorScore.builder().build(), null, "st1", new Date(), null);

        CountryHealthIndicatorId countryHealthIndicatorId2 = new CountryHealthIndicatorId(countryId1, categoryId1, indicatorId2, publishedStatus, year);
        Indicator indicator2 = new Indicator(indicatorId2, "Indicator 1", "Definition", 2);
        CountryHealthIndicator countryHealthIndicator2 = new CountryHealthIndicator(countryHealthIndicatorId2,
                country1, category2, indicator2, IndicatorScore.builder().build(), 2, "st2", new Date(), null);

        List<CountryHealthIndicator> countryHealthIndicatorsForCountry = asList(countryHealthIndicator1, countryHealthIndicator2);

        when(iCountryHealthIndicatorRepository.findByCountryHealthIndicatorIdCountryIdAndCountryHealthIndicatorIdStatusAndCountryHealthIndicatorIdYear(countryId1, publishedStatus, year)).thenReturn(countryHealthIndicatorsForCountry);
        CountryPhase countryPhaseIND = buildCountryPhase("IND", 2);
        when(iCountryPhaseRepository.findByCountryPhaseIdCountryIdAndCountryPhaseIdYear("IND", year)).thenReturn(countryPhaseIND);

        CategoryHealthScoreDto categoryHealthScoreDto1 = new CategoryHealthScoreDto(1, categoryName, 1.0, 1, of(new IndicatorScoreDto(1, null, "Indicator 1", "Definition", 1, null, "st1", "Not Available")));
        CategoryHealthScoreDto categoryHealthScoreDto2 = new CategoryHealthScoreDto(2, categoryName2, 2.0, 2, of(new IndicatorScoreDto(2, null, "Indicator 1", "Definition", 2, 2, "st2", "Not Available")));
        CountryHealthScoreDto countryHealthScoreDto = new CountryHealthScoreDto("IND", "India", "IN", of(categoryHealthScoreDto1, categoryHealthScoreDto2), 2, "");
        when(indicatorTranslator.translateCountryHealthScores(en, countryHealthScoreDto)).thenReturn(countryHealthScoreDto);

        CountryHealthScoreDto healthScoreForACountry = countryHealthIndicatorService.fetchCountryHealthScore(countryId, en, year);

        assertSet5(healthScoreForACountry, countryId, countryName);
    }

    @Test
    public void shouldReturnCountryHealthScoreWithoutConsideringNegativeScores() {
        String countryId = "IND";
        Integer categoryId1 = 1;
        Integer categoryId2 = 2;
        Integer indicatorId1 = 1;
        Integer indicatorId2 = 2;
        String categoryName = "Leadership and Governance";
        String categoryName2 = "Category2";

        String countryName = "India";
        String publishedStatus = "PUBLISHED";
        String year = "Version1";
        CountryHealthIndicatorId countryHealthIndicatorId1 = new CountryHealthIndicatorId(countryId, categoryId1, indicatorId1, publishedStatus, year);
        Country country1 = new Country(countryId, countryName, UUID.randomUUID(), "IN");
        Category category1 = new Category(categoryId1, categoryName);
        Category category2 = new Category(categoryId2, categoryName2);
        Indicator indicator1 = new Indicator(indicatorId1, "Indicator 1", "Definition", 1);
        CountryHealthIndicator countryHealthIndicator1 = new CountryHealthIndicator(countryHealthIndicatorId1,
                country1, category1, indicator1, IndicatorScore.builder().build(), null, "st1", new Date(), null);

        CountryHealthIndicatorId countryHealthIndicatorId2 = new CountryHealthIndicatorId(countryId, categoryId1, indicatorId2, publishedStatus, year);
        Indicator indicator2 = new Indicator(indicatorId2, "Indicator 1", "Definition", 2);
        CountryHealthIndicator countryHealthIndicator2 = new CountryHealthIndicator(countryHealthIndicatorId2,
                country1, category2, indicator2, IndicatorScore.builder().indicatorId(indicatorId2).score(2).build(), 2, "st2", new Date(), null);

        List<CountryHealthIndicator> countryHealthIndicatorsForCountry = asList(countryHealthIndicator1, countryHealthIndicator2);

        when(iCountryHealthIndicatorRepository.findByCountryHealthIndicatorIdCountryIdAndCountryHealthIndicatorIdStatusAndCountryHealthIndicatorIdYear(countryId, publishedStatus, year)).thenReturn(countryHealthIndicatorsForCountry);
        CountryPhase countryPhaseIND = buildCountryPhase(countryId, 2);
        when(iCountryPhaseRepository.findByCountryPhaseIdCountryIdAndCountryPhaseIdYear(countryId, year)).thenReturn(countryPhaseIND);

        CategoryHealthScoreDto categoryHealthScoreDto1 = new CategoryHealthScoreDto(1, "Leadership and Governance", 1.0, 1, of(new IndicatorScoreDto(1, null, "Indicator 1", "Definition", 1, null, "st1", "Not Available")));
        CategoryHealthScoreDto categoryHealthScoreDto2 = new CategoryHealthScoreDto(2, "Category2", 2.0, 2, of(new IndicatorScoreDto(2, null, "Indicator 1", "Definition", 2, 2, "st2", "Not Available")));
        CountryHealthScoreDto countryHealthScoreDto = new CountryHealthScoreDto("IND", "India", "IN", of(categoryHealthScoreDto1, categoryHealthScoreDto2), 2, "");
        when(indicatorTranslator.translateCountryHealthScores(en, countryHealthScoreDto)).thenReturn(countryHealthScoreDto);

        CountryHealthScoreDto healthScoreForACountry = countryHealthIndicatorService.fetchCountryHealthScore(countryId, en, year);

        assertSet5(healthScoreForACountry, countryId, countryName);
    }

    private void assertSet5(CountryHealthScoreDto healthScoreForACountry, String countryId, String countryName) {
        assertThat(healthScoreForACountry.getCountryId(), is(countryId));
        assertThat(healthScoreForACountry.getCountryName(), is(countryName));
        assertThat(healthScoreForACountry.getCategories().size(), is(2));
        assertThat(healthScoreForACountry.getCountryPhase(), is(2));
        List<CategoryHealthScoreDto> leadership = healthScoreForACountry.getCategories().stream().filter(a -> a.getName().equals("Leadership and Governance")).collect(toList());
        List<CategoryHealthScoreDto> category2 = healthScoreForACountry.getCategories().stream().filter(a -> a.getName().equals("Category2")).collect(toList());
        assertThat(leadership.size(), is(1));
        assertThat(leadership.get(0).getPhase(), is(1));
        assertThat(category2.size(), is(1));
        assertThat(category2.get(0).getPhase(), is(2));
        assertThat(category2.get(0).getOverallScore(), is(2.0));
    }

    @Test
    public void shouldReturnCountryHealthScoreGivenCountryIdAndYearForNullCountryScore() {
        String countryId = "IND";
        Integer categoryId1 = 1;
        Integer indicatorId1 = 1;
        Integer indicatorId2 = 2;
        String categoryName = "Leadership and Governance";

        String countryName = "India";
        String publishedStatus = "PUBLISHED";
        String year = "Version1";
        CountryHealthIndicatorId countryHealthIndicatorId1 = new CountryHealthIndicatorId(countryId, categoryId1, indicatorId1, publishedStatus, year);
        Country country1 = new Country(countryId, countryName, UUID.randomUUID(), "IN");
        Category category1 = new Category(categoryId1, categoryName);
        Indicator indicator1 = new Indicator(indicatorId1, "Indicator 1", "Definition", 1);
        CountryHealthIndicator countryHealthIndicator1 = new CountryHealthIndicator(countryHealthIndicatorId1,
                country1, category1, indicator1, IndicatorScore.builder().build(), null, null, new Date(), null);

        CountryHealthIndicatorId countryHealthIndicatorId2 = new CountryHealthIndicatorId(countryId, categoryId1, indicatorId2, publishedStatus, year);
        Indicator indicator2 = new Indicator(indicatorId2, "Indicator 1", "Definition", 2);
        CountryHealthIndicator countryHealthIndicator2 = new CountryHealthIndicator(countryHealthIndicatorId2,
                country1, category1, indicator2, IndicatorScore.builder().build(), null, null, new Date(), null);

        List<CountryHealthIndicator> countryHealthIndicatorsForCountry = asList(countryHealthIndicator1, countryHealthIndicator2);

        when(iCountryHealthIndicatorRepository.findByCountryHealthIndicatorIdCountryIdAndCountryHealthIndicatorIdStatusAndCountryHealthIndicatorIdYear(countryId, publishedStatus, year)).thenReturn(countryHealthIndicatorsForCountry);
        CountryPhase countryPhaseIND = buildCountryPhase(countryId, null);
        when(iCountryPhaseRepository.findByCountryPhaseIdCountryIdAndCountryPhaseIdYear(countryId, year)).thenReturn(countryPhaseIND);

        CategoryHealthScoreDto categoryHealthScoreDto1 = new CategoryHealthScoreDto(1, "Leadership and Governance", 1.0, 1, of(
                new IndicatorScoreDto(1, null, "Indicator 1", "Definition", 1, null, null, "Not Available"),
                new IndicatorScoreDto(2, null, "Indicator 1", "Definition", 2, null, null, "Not Available")));
        CountryHealthScoreDto countryHealthScoreDto = new CountryHealthScoreDto("IND", "India", "IN", of(categoryHealthScoreDto1), null, "");
        when(indicatorTranslator.translateCountryHealthScores(en, countryHealthScoreDto)).thenReturn(countryHealthScoreDto);

        CountryHealthScoreDto healthScoreForACountry = countryHealthIndicatorService.fetchCountryHealthScore(countryId, en, year);
        assertSet6(healthScoreForACountry, countryId, countryName);
    }

    private void assertSet6(CountryHealthScoreDto healthScoreForACountry, String countryId, String countryName) {
        assertThat(healthScoreForACountry.getCountryId(), is(countryId));
        assertThat(healthScoreForACountry.getCountryName(), is(countryName));
        assertThat(healthScoreForACountry.getCategories().size(), is(1));
        assertNull(healthScoreForACountry.getCountryPhase());
        List<CategoryHealthScoreDto> leadership = healthScoreForACountry.getCategories().stream().filter(a -> a.getName().equals("Leadership and Governance")).collect(toList());
        assertThat(leadership.size(), is(1));
        assertThat(leadership.get(0).getPhase(), is(1));
    }

    @Test
    public void shouldReturnCountryHealthScoreGivenCountryIdAndYearForSameCategory() {
        String countryId = "IND";
        Integer categoryId1 = 1;
        String categoryName = "Leadership and Governance";
        Integer indicatorId1 = 1;
        Integer indicatorId2 = 2;

        Integer indicatorScore1 = 3;
        Integer indicatorScore2 = 4;

        String countryName = "India";
        String publishedStatus = "PUBLISHED";
        String year = "Version1";
        CountryHealthIndicatorId countryHealthIndicatorId1 = new CountryHealthIndicatorId(countryId, categoryId1, indicatorId1, publishedStatus, year);
        Country country1 = new Country(countryId, countryName, UUID.randomUUID(), "IN");
        Category category1 = new Category(categoryId1, categoryName);
        Indicator indicator1 = new Indicator(indicatorId1, "Indicator 1", "Definition", 1);
        CountryHealthIndicator countryHealthIndicator1 = new CountryHealthIndicator(countryHealthIndicatorId1,
                country1, category1, indicator1, IndicatorScore.builder().build(), indicatorScore1, "st1", new Date(), null);

        CountryHealthIndicatorId countryHealthIndicatorId2 = new CountryHealthIndicatorId(countryId, categoryId1, indicatorId2, publishedStatus, year);
        Indicator indicator2 = new Indicator(indicatorId2, "Indicator 1", "Definition", 2);
        CountryHealthIndicator countryHealthIndicator2 = new CountryHealthIndicator(countryHealthIndicatorId2,
                country1, category1, indicator2, IndicatorScore.builder().build(), indicatorScore2, "st2", new Date(), null);

        List<CountryHealthIndicator> countryHealthIndicatorsForCountry = asList(countryHealthIndicator1, countryHealthIndicator2);

        when(iCountryHealthIndicatorRepository.findByCountryHealthIndicatorIdCountryIdAndCountryHealthIndicatorIdStatusAndCountryHealthIndicatorIdYear(countryId, publishedStatus, year)).thenReturn(countryHealthIndicatorsForCountry);
        CountryPhase countryPhaseIND = buildCountryPhase(countryId, 4);
        when(iCountryPhaseRepository.findByCountryPhaseIdCountryIdAndCountryPhaseIdYear(countryId, year)).thenReturn(countryPhaseIND);

        CategoryHealthScoreDto categoryHealthScoreDto1 = new CategoryHealthScoreDto(1, "Leadership and Governance", 3.5, 4, of(
                new IndicatorScoreDto(1, null, "Indicator 1", "Definition", 1, 3, "st1", "Not Available"),
                new IndicatorScoreDto(2, null, "Indicator 1", "Definition", 2, 4, "st2", "Not Available")));
        CountryHealthScoreDto countryHealthScoreDto = new CountryHealthScoreDto("IND", "India", "IN", of(categoryHealthScoreDto1), 4, "");
        when(indicatorTranslator.translateCountryHealthScores(en, countryHealthScoreDto)).thenReturn(countryHealthScoreDto);

        CountryHealthScoreDto healthScoreForACountry = countryHealthIndicatorService.fetchCountryHealthScore(countryId, en, year);

        assertSet2(healthScoreForACountry, countryId, countryName);
    }

    private void assertSet2(CountryHealthScoreDto healthScoreForACountry, String countryId, String countryName) {
        assertThat(healthScoreForACountry.getCountryId(), is(countryId));
        assertThat(healthScoreForACountry.getCountryName(), is(countryName));
        assertThat(healthScoreForACountry.getCategories().size(), is(1));
        assertThat(healthScoreForACountry.getCountryPhase(), is(4));
        List<CategoryHealthScoreDto> leadership = healthScoreForACountry.getCategories().stream().filter(a -> a.getName().equals("Leadership and Governance")).collect(toList());
        assertThat(leadership.size(), is(1));
        assertThat(leadership.get(0).getPhase(), is(4));

    }

    @Test
    public void shouldReturnCountryHealthScoreGivenCountryIdAndYearForSameCategoryCheckingSinglePrecision() {
        String countryId = "IND";
        Integer categoryId1 = 1;
        String categoryName = "Leadership and Governance";
        Integer indicatorId1 = 1;
        Integer indicatorId2 = 2;
        Integer indicatorId3 = 3;

        Integer indicatorScore1 = 5;
        Integer indicatorScore2 = -1;
        Integer indicatorScore3 = -1;

        String countryId1 = "IND";
        String countryName = "India";
        String publishedStatus = "PUBLISHED";
        String year = "Version1";

        CountryHealthIndicatorId countryHealthIndicatorId1 = new CountryHealthIndicatorId(countryId1, categoryId1, indicatorId1, publishedStatus, year);
        Country country1 = new Country(countryId, countryName, UUID.randomUUID(), "IN");
        Category category1 = new Category(categoryId1, categoryName);
        Indicator indicator1 = new Indicator(indicatorId1, "Indicator 1", "Definition", 1);
        CountryHealthIndicator countryHealthIndicator1 = new CountryHealthIndicator(countryHealthIndicatorId1,
                country1, category1, indicator1, IndicatorScore.builder().build(), indicatorScore1, "st1", new Date(), null);

        CountryHealthIndicatorId countryHealthIndicatorId2 = new CountryHealthIndicatorId(countryId1, categoryId1, indicatorId2, publishedStatus, year);
        Indicator indicator2 = new Indicator(indicatorId2, "Indicator 1", "Definition", 2);
        CountryHealthIndicator countryHealthIndicator2 = new CountryHealthIndicator(countryHealthIndicatorId2,
                country1, category1, indicator2, IndicatorScore.builder().build(), indicatorScore2, "st2", new Date(), null);

        CountryHealthIndicatorId countryHealthIndicatorId3 = new CountryHealthIndicatorId(countryId1, categoryId1, indicatorId3, publishedStatus, year);
        Indicator indicator3 = new Indicator(indicatorId3, "Indicator 1", "Definition", 3);
        CountryHealthIndicator countryHealthIndicator3 = new CountryHealthIndicator(countryHealthIndicatorId3,
                country1, category1, indicator3, IndicatorScore.builder().build(), indicatorScore3, "st3", new Date(), null);

        List<CountryHealthIndicator> countryHealthIndicatorsForCountry = asList(countryHealthIndicator1, countryHealthIndicator2, countryHealthIndicator3);


        when(iCountryHealthIndicatorRepository.findByCountryHealthIndicatorIdCountryIdAndCountryHealthIndicatorIdStatusAndCountryHealthIndicatorIdYear(countryId1, publishedStatus, year)).thenReturn(countryHealthIndicatorsForCountry);
        CountryPhase countryPhaseIND = buildCountryPhase("IND", 5);
        when(iCountryPhaseRepository.findByCountryPhaseIdCountryIdAndCountryPhaseIdYear("IND", year)).thenReturn(countryPhaseIND);

        CategoryHealthScoreDto categoryHealthScoreDto1 = new CategoryHealthScoreDto(1, "Leadership and Governance", 2.3333333333333335, 3, of(
                new IndicatorScoreDto(1, null, "Indicator 1", "Definition", 1, 5, "st1", "Not Available"),
                new IndicatorScoreDto(2, null, "Indicator 1", "Definition", 2, -1, "st2", "Not Available"),
                new IndicatorScoreDto(3, null, "Indicator 1", "Definition", 3, -1, "st3", "Not Available")));
        CountryHealthScoreDto countryHealthScoreDto = new CountryHealthScoreDto("IND", "India", "IN", of(categoryHealthScoreDto1), 5, "");
        when(indicatorTranslator.translateCountryHealthScores(en, countryHealthScoreDto)).thenReturn(countryHealthScoreDto);

        CountryHealthScoreDto healthScoreForACountry = countryHealthIndicatorService.fetchCountryHealthScore(countryId, en, year);

        assertSet3(healthScoreForACountry, countryId, countryName);
    }

    private void assertSet3(CountryHealthScoreDto healthScoreForACountry, String countryId, String countryName) {
        assertThat(healthScoreForACountry.getCountryId(), is(countryId));
        assertThat(healthScoreForACountry.getCountryName(), is(countryName));
        assertThat(healthScoreForACountry.getCategories().size(), is(1));
        assertThat(healthScoreForACountry.getCountryPhase(), is(5));
        List<CategoryHealthScoreDto> leadership = healthScoreForACountry.getCategories().stream().filter(a -> a.getName().equals("Leadership and Governance")).collect(toList());
        assertThat(leadership.size(), is(1));
        assertThat(leadership.get(0).getPhase(), is(3));

    }

    @Test
    public void shouldFetchGlobalHealthScores() throws Exception {
        Category category1 = Category.builder().id(9).name("Category 1").build();
        Country country1 = new Country("IND", "India", UUID.randomUUID(), "IN");
        Indicator indicator1 = Indicator.builder().indicatorId(1).rank(1).build();
        String year = "Version1";
        CountryHealthIndicator mock1 = CountryHealthIndicator.builder().country(country1).category(category1).indicator(indicator1).score(1).build();

        Category category2 = Category.builder().id(8).name("Category 2").build();
        Country country2 = new Country("USA", "United States", UUID.randomUUID(), "US");
        Indicator indicator2 = Indicator.builder().indicatorId(2).rank(2).build();
        CountryHealthIndicator mock2 = CountryHealthIndicator.builder().country(country2).category(category2).indicator(indicator2).score(2).build();

        Category category3 = Category.builder().id(7).name("Category 4").build();
        Indicator indicator3 = Indicator.builder().indicatorId(3).rank(3).build();
        CountryHealthIndicator mock3 = CountryHealthIndicator.builder().country(country2).category(category3).indicator(indicator3).score(3).build();

        Indicator indicator4 = Indicator.builder().indicatorId(4).rank(4).build();
        CountryHealthIndicator mock4 = CountryHealthIndicator.builder().country(country2).category(category3).indicator(indicator4).score(4).build();
        when(iCountryHealthIndicatorRepository.findByCountryHealthIndicatorIdStatusAndCountryHealthIndicatorIdYear(PUBLISHED.name(), year)).thenReturn(asList(mock1, mock2, mock3, mock4));
        ;

        CountrySummary countrySummary = CountrySummary.builder().collectedDate(new SimpleDateFormat("dd-MM-yyyy").parse("04-04-2018")).build();
        when(iCountrySummaryRepository.findByCountrySummaryIdCountryIdAndCountrySummaryIdStatusAndCountrySummaryIdYear(anyString(), anyString(), anyString())).thenReturn(countrySummary);
        CountryPhase countryPhaseIND = buildCountryPhase("IND", 1);
        CountryPhase countryPhaseUSA = buildCountryPhase("USA", 3);
        when(iCountryPhaseRepository.findByCountryPhaseIdCountryIdAndCountryPhaseIdYear("IND", year)).thenReturn(countryPhaseIND);
        when(iCountryPhaseRepository.findByCountryPhaseIdCountryIdAndCountryPhaseIdYear("USA", year)).thenReturn(countryPhaseUSA);

        CategoryHealthScoreDto categoryHealthScoreDto1 = new CategoryHealthScoreDto(9, "Category 1", 1.0, 1, of(new IndicatorScoreDto(1, null, null, null, 1, 1, null, "Not Available")));
        CountryHealthScoreDto countryHealthScoreDtoIN = new CountryHealthScoreDto("IND", "India", "IN", of(categoryHealthScoreDto1), 1, "April 2018");

        CategoryHealthScoreDto categoryHealthScoreDto2 = new CategoryHealthScoreDto(7, "Category 4", 3.5, 4,
                of(new IndicatorScoreDto(3, null, null, null, 3, 3, null, "Not Available"),
                        new IndicatorScoreDto(4, null, null, null, 4, 4, null, "Not Available")));
        CategoryHealthScoreDto categoryHealthScoreDto3 = new CategoryHealthScoreDto(8, "Category 2", 2.0, 2, of(
                new IndicatorScoreDto(2, null, null, null, 2, 2, null, "Not Available")));
        CountryHealthScoreDto countryHealthScoreDtoUS = new CountryHealthScoreDto("USA", "United States", "US", of(categoryHealthScoreDto2, categoryHealthScoreDto3), 3, "April 2018");

        when(indicatorTranslator.translateCountryHealthScores(en, countryHealthScoreDtoUS)).thenReturn(countryHealthScoreDtoUS);
        when(indicatorTranslator.translateCountryHealthScores(en, countryHealthScoreDtoIN)).thenReturn(countryHealthScoreDtoIN);

        CountriesHealthScoreDto countriesHealthScoreDto = countryHealthIndicatorService.fetchCountriesHealthScores(null, null, en, year);

        assertThat(countriesHealthScoreDto.getCountryHealthScores().size(), is(2));
        CountryHealthScoreDto actualInd = countriesHealthScoreDto.getCountryHealthScores().stream().filter(c -> c.getCountryId().equals("IND")).findFirst().get();
        CountryHealthScoreDto actualUSA = countriesHealthScoreDto.getCountryHealthScores().stream().filter(c -> c.getCountryId().equals("USA")).findFirst().get();
        assertThat(actualInd.getCountryPhase(), is(1));
        assertThat(actualUSA.getCountryPhase(), is(3));
        assertThat(actualUSA.getCategories().size(), is(2));
        CategoryHealthScoreDto actualCategory3 = actualUSA.getCategories().stream().filter(c -> c.getId().equals(category3.getId())).findFirst().get();
        assertThat(actualCategory3.getOverallScore(), is(3.5));
        assertThat(actualCategory3.getIndicators().size(), is(2));
        CategoryHealthScoreDto actualCategory2 = actualUSA.getCategories().stream().filter(c -> c.getId().equals(category2.getId())).findFirst().get();
        assertThat(actualCategory2.getOverallScore(), is(2.0));
        assertThat(actualInd.getCollectedDate(), is("April 2018"));
    }

    @Test
    public void shouldReturnEmptyListWhenPhaseForGivenCategoryIsNotPresentWhileFetchingGlobalHealthScores() throws Exception {
        String USA = "USA";
        String India = "IND";
        String year = "Version1";

        Category category1 = Category.builder().id(9).name("Category 1").build();
        Country country1 = new Country(India, "India", UUID.randomUUID(), "IN");
        Indicator indicator1 = Indicator.builder().indicatorId(1).build();
        CountryHealthIndicator mock1 = CountryHealthIndicator.builder().country(country1).category(category1).indicator(indicator1).score(1).build();

        Category category2 = Category.builder().id(8).name("Category 2").build();
        Country country2 = new Country(USA, "United States", UUID.randomUUID(), "US");
        Indicator indicator2 = Indicator.builder().indicatorId(2).build();
        CountryHealthIndicator mock2 = CountryHealthIndicator.builder().country(country2).category(category2).indicator(indicator2).score(2).build();

        when(iCountryHealthIndicatorRepository.findByCountryHealthIndicatorIdCategoryIdAndCountryHealthIndicatorIdStatusAndCountryHealthIndicatorIdYear(1, PUBLISHED.name(), year)).thenReturn(asList(mock1, mock2));

        CountrySummary countrySummary = CountrySummary.builder().collectedDate(new SimpleDateFormat("dd-MM-yyyy").parse("04-04-2018")).build();
        when(iCountrySummaryRepository.findByCountrySummaryIdCountryIdAndCountrySummaryIdStatusAndCountrySummaryIdYear(anyString(), anyString(), anyString())).thenReturn(countrySummary);

        CountryPhase countryPhaseIND = buildCountryPhase(India, 1);
        CountryPhase countryPhaseUSA = buildCountryPhase(USA, 2);
        when(iCountryPhaseRepository.findByCountryPhaseIdCountryIdAndCountryPhaseIdYear(India, year)).thenReturn(countryPhaseIND);
        when(iCountryPhaseRepository.findByCountryPhaseIdCountryIdAndCountryPhaseIdYear(USA, year)).thenReturn(countryPhaseUSA);

        CountriesHealthScoreDto countriesHealthScoreDto = countryHealthIndicatorService.fetchCountriesHealthScores(1, 5, en, year);

        assertThat(countriesHealthScoreDto.getCountryHealthScores().size(), is(0));
    }

    @Test
    public void shouldFetchIndicatorsBasedOnFilters() throws Exception {
        Category category3 = Category.builder().id(7).name("Category 4").build();
        Country country1 = new Country("IND", "India", UUID.randomUUID(), "IN");
        String year = "Version1";
        Indicator indicator1 = Indicator.builder().indicatorId(1).rank(1).build();
        CountryHealthIndicator mock1 = CountryHealthIndicator.builder().country(country1).category(category3).indicator(indicator1).score(1).build();

        Country country2 = new Country("USA", "United States", UUID.randomUUID(), "US");

        Indicator indicator3 = Indicator.builder().indicatorId(3).rank(3).build();
        CountryHealthIndicator mock3 = CountryHealthIndicator.builder().country(country2).category(category3).indicator(indicator3).score(3).build();

        Indicator indicator4 = Indicator.builder().indicatorId(4).rank(4).build();
        CountryHealthIndicator mock4 = CountryHealthIndicator.builder().country(country2).category(category3).indicator(indicator4).score(4).build();
        when(iCountryHealthIndicatorRepository.findByCountryHealthIndicatorIdCategoryIdAndCountryHealthIndicatorIdStatusAndCountryHealthIndicatorIdYear(3, PUBLISHED.name(), year)).thenReturn(asList(mock1, mock3, mock4));

        CountrySummary countrySummary = CountrySummary.builder().collectedDate(new SimpleDateFormat("dd-MM-yyyy").parse("04-04-2018")).build();
        when(iCountrySummaryRepository.findByCountrySummaryIdCountryIdAndCountrySummaryIdStatusAndCountrySummaryIdYear(anyString(), anyString(), anyString())).thenReturn(countrySummary);

        CountryPhase countryPhaseIND = buildCountryPhase("IND", 1);
        CountryPhase countryPhaseUSA = buildCountryPhase("USA", 4);
        when(iCountryPhaseRepository.findByCountryPhaseIdCountryIdAndCountryPhaseIdYear("IND" , year)).thenReturn(countryPhaseIND);
        when(iCountryPhaseRepository.findByCountryPhaseIdCountryIdAndCountryPhaseIdYear( "USA", year)).thenReturn(countryPhaseUSA);

        CategoryHealthScoreDto categoryHealthScoreDto2 = new CategoryHealthScoreDto(7, "Category 4", 3.5, 4,
                of(new IndicatorScoreDto(3, null, null, null, 3, 3, null, "Not Available"),
                        new IndicatorScoreDto(4, null, null, null, 4, 4, null, "Not Available")));
        CountryHealthScoreDto countryHealthScoreDtoUS = new CountryHealthScoreDto("USA", "United States", "US", of(categoryHealthScoreDto2), 4, "April 2018");
        when(indicatorTranslator.translateCountryHealthScores(en, countryHealthScoreDtoUS)).thenReturn(countryHealthScoreDtoUS);

        CountriesHealthScoreDto countriesHealthScoreDto = countryHealthIndicatorService.fetchCountriesHealthScores(3, 4, en, year);

        assertThat(countriesHealthScoreDto.getCountryHealthScores().size(), is(1));
        assertThat(countriesHealthScoreDto.getCountryHealthScores().get(0).getCountryPhase(), is(4));
        assertThat(countriesHealthScoreDto.getCountryHealthScores().get(0).getCategories().size(), is(1));
        assertThat(countriesHealthScoreDto.getCountryHealthScores().get(0).getCategories().get(0).getOverallScore(), is(3.5));
        assertThat(countriesHealthScoreDto.getCountryHealthScores().get(0).getCategories().get(0).getPhase(), is(4));
    }

    @Test
    public void shouldFilterAtCountryLevelIfCategoryIsNull() throws Exception {
        Country country1 = new Country("IND", "India", UUID.randomUUID(), "IN");
        Country country2 = new Country("USA", "United States", UUID.randomUUID(), "US");
        String year = "Version1";
        Category category2 = Category.builder().id(6).name("Category 4").build();
        Category category3 = Category.builder().id(7).name("Category 4").build();
        Indicator indicator1 = Indicator.builder().indicatorId(1).rank(1).build();
        Indicator indicator3 = Indicator.builder().indicatorId(3).rank(3).build();
        Indicator indicator4 = Indicator.builder().indicatorId(4).rank(4).build();
        Indicator indicator5 = Indicator.builder().indicatorId(5).rank(5).build();

        CountryHealthIndicator mock1 = CountryHealthIndicator.builder().country(country1).category(category3).indicator(indicator1).score(1).build();
        CountryHealthIndicator mock2 = CountryHealthIndicator.builder().country(country2).category(category3).indicator(indicator4).score(2).build();
        CountryHealthIndicator mock3 = CountryHealthIndicator.builder().country(country2).category(category3).indicator(indicator3).score(3).build();
        CountryHealthIndicator mock4 = CountryHealthIndicator.builder().country(country2).category(category2).indicator(indicator5).score(5).build();
        when(iCountryHealthIndicatorRepository.findByCountryHealthIndicatorIdStatusAndCountryHealthIndicatorIdYear(PUBLISHED.name(), year)).thenReturn(asList(mock1, mock2, mock3, mock4));

        CategoryHealthScoreDto categoryHealthScoreDto1 = new CategoryHealthScoreDto(6, "Category 4", 5.0, 5, of(new IndicatorScoreDto(5, null, null, null, 5, 5, null, "Not Available")));
        CategoryHealthScoreDto categoryHealthScoreDto2 = new CategoryHealthScoreDto(7, "Category 4", 2.5, 3,
                of(new IndicatorScoreDto(3, null, null, null, 3, 3, null, "Not Available"),
                        new IndicatorScoreDto(4, null, null, null, 4, 2, null, "Not Available")));
        CountryHealthScoreDto countryHealthScoreDtoUS = new CountryHealthScoreDto("USA", "United States", "US", of(categoryHealthScoreDto1, categoryHealthScoreDto2), 4, "April 2018");

        when(indicatorTranslator.translateCountryHealthScores(en, countryHealthScoreDtoUS)).thenReturn(countryHealthScoreDtoUS);

        CountrySummary countrySummary = CountrySummary.builder().collectedDate(new SimpleDateFormat("dd-MM-yyyy").parse("04-04-2018")).build();
        when(iCountrySummaryRepository.findByCountrySummaryIdCountryIdAndCountrySummaryIdStatusAndCountrySummaryIdYear(anyString(), anyString(), anyString())).thenReturn(countrySummary);
        CountryPhase countryPhaseIND = buildCountryPhase("IND", 1);
        CountryPhase countryPhaseUSA = buildCountryPhase("USA", 4);
        when(iCountryPhaseRepository.findByCountryPhaseIdCountryIdAndCountryPhaseIdYear( "IND", year)).thenReturn(countryPhaseIND);
        when(iCountryPhaseRepository.findByCountryPhaseIdCountryIdAndCountryPhaseIdYear("USA", year)).thenReturn(countryPhaseUSA);

        CountriesHealthScoreDto countriesHealthScoreDto = countryHealthIndicatorService.fetchCountriesHealthScores(null, 4, en, year);

        assertThat(countriesHealthScoreDto.getCountryHealthScores().size(), is(1));
        assertThat(countriesHealthScoreDto.getCountryHealthScores().get(0).getCountryPhase(), is(4));
        List<CategoryHealthScoreDto> categories = countriesHealthScoreDto.getCountryHealthScores().get(0).getCategories();
        assertThat(categories.size(), is(2));
        assertThat(findFirst(categories, c -> c.getId().equals(category3.getId())).getOverallScore(), is(2.5));
        assertThat(findFirst(categories, c -> c.getId().equals(category3.getId())).getPhase(), is(3));
        assertThat(findFirst(categories, c -> c.getId().equals(category2.getId())).getOverallScore(), is(5.0));
        assertThat(findFirst(categories, c -> c.getId().equals(category2.getId())).getPhase(), is(5));
    }

    @Test
    public void shouldFetchOverAllCategoriesWithScore() {
        Category category = Category.builder().id(9).name("Category 1").build();
        Category category1 = Category.builder().id(3).name("Category 2").build();
        String year = "Version1";
        CountryHealthIndicator countryHealthIndicator = CountryHealthIndicator.builder()
                .country(new Country("IND", "India", UUID.randomUUID(), "IN"))
                .indicator(Indicator.builder().indicatorId(1).build())
                .category(category).score(5).build();

        CountryHealthIndicator countryHealthIndicator1 = CountryHealthIndicator.builder()
                .country(new Country("IND", "India", UUID.randomUUID(), "IN"))
                .indicator(Indicator.builder().indicatorId(1).build())
                .category(category1).score(2).build();

        CountryHealthIndicator countryHealthIndicator2 = CountryHealthIndicator.builder()
                .country(new Country("USA", "USA", UUID.randomUUID(), "US"))
                .indicator(Indicator.builder().indicatorId(1).build())
                .category(category).score(2).build();

        CountryHealthIndicator countryHealthIndicator3 = CountryHealthIndicator.builder()
                .country(new Country("USA", "USA", UUID.randomUUID(), "US"))
                .indicator(Indicator.builder().indicatorId(1).build())
                .category(category1).score(null).build();

        CountryHealthIndicator countryHealthIndicator4 = CountryHealthIndicator.builder()
                .country(new Country("UK", "UK", UUID.randomUUID(), "UK"))
                .indicator(Indicator.builder().indicatorId(1).build())
                .category(category1).score(-1).build();

        List<CountryHealthIndicator> countryHealthIndicators = asList(countryHealthIndicator, countryHealthIndicator1, countryHealthIndicator2, countryHealthIndicator3, countryHealthIndicator4);
        when(iCountryHealthIndicatorRepository.findByCountryHealthIndicatorIdStatusAndCountryHealthIndicatorIdYear(PUBLISHED.name(), year)).thenReturn(countryHealthIndicators);
        CountryPhase countryPhaseIND = buildCountryPhase("IND", 4);
        CountryPhase countryPhaseUSA = buildCountryPhase("USA", 2);
        CountryPhase countryPhaseUK = buildCountryPhase("UK", null);
        when(iCountryPhaseRepository.findByCountryPhaseIdCountryIdAndCountryPhaseIdYear( "IND", year)).thenReturn(countryPhaseIND);
        when(iCountryPhaseRepository.findByCountryPhaseIdCountryIdAndCountryPhaseIdYear("USA", year)).thenReturn(countryPhaseUSA);
        when(iCountryPhaseRepository.findByCountryPhaseIdCountryIdAndCountryPhaseIdYear( "UK", year)).thenReturn(countryPhaseUK);

        CategoryHealthScoreDto categoryHealthScoreDto1 = new CategoryHealthScoreDto(3, "Category 2", 2.0, 2, of(new IndicatorScoreDto(1, null, null, null, null, 2, null, "Not Available")));
        CategoryHealthScoreDto categoryHealthScoreDto2 = new CategoryHealthScoreDto(9, "Category 1", 5.0, 5, of(new IndicatorScoreDto(1, null, null, null, null, 5, null, "Not Available")));
        CountryHealthScoreDto countryHealthScoreDtoIN = new CountryHealthScoreDto("IND", "India", "IN", of(categoryHealthScoreDto1, categoryHealthScoreDto2), 4, "");
        CategoryHealthScoreDto categoryHealthScoreDto3 = new CategoryHealthScoreDto(3, "Category 2", 1.0, 1, of(new IndicatorScoreDto(1, null, null, null, null, -1, null, "Not Available")));
        CountryHealthScoreDto countryHealthScoreDtoUK = new CountryHealthScoreDto("UK", "UK", "UK", of(categoryHealthScoreDto3), null, "");
        CategoryHealthScoreDto categoryHealthScoreDto4 = new CategoryHealthScoreDto(3, "Category 2", 1.0, 1, of(new IndicatorScoreDto(1, null, null, null, null, null, null, "Not Available")));
        CategoryHealthScoreDto categoryHealthScoreDto5 = new CategoryHealthScoreDto(9, "Category 1", 2.0, 2, of(new IndicatorScoreDto(1, null, null, null, null, 2, null, "Not Available")));
        CountryHealthScoreDto countryHealthScoreDtoUS = new CountryHealthScoreDto("USA", "USA", "US", of(categoryHealthScoreDto4, categoryHealthScoreDto5), 2, "");

        when(indicatorTranslator.translateCountryHealthScores(null, countryHealthScoreDtoIN)).thenReturn(countryHealthScoreDtoIN);
        when(indicatorTranslator.translateCountryHealthScores(null, countryHealthScoreDtoUK)).thenReturn(countryHealthScoreDtoUK);
        when(indicatorTranslator.translateCountryHealthScores(null, countryHealthScoreDtoUS)).thenReturn(countryHealthScoreDtoUS);

        GlobalHealthScoreDto globalHealthIndicator = countryHealthIndicatorService.getGlobalHealthIndicator(null, null, en, year);

        assertEquals(2, globalHealthIndicator.getCategories().size());
        CategoryHealthScoreDto actualCategory = globalHealthIndicator.getCategories().stream().filter(cat -> cat.getId().equals(category.getId())).findFirst().get();
        assertEquals(4, actualCategory.getPhase().intValue());
        actualCategory = globalHealthIndicator.getCategories().stream().filter(cat -> cat.getId().equals(category1.getId())).findFirst().get();
        assertEquals(2, actualCategory.getPhase().intValue());
        assertThat(globalHealthIndicator.getOverAllScore(), is(3));
    }

    @Test
    public void fetchGlobalHealthScoresShouldIgnoreCountryWithAllIndicatorsMissing() {
        CountryHealthIndicator mock1 = mock(CountryHealthIndicator.class);
        when(mock1.getScore()).thenReturn(2);
        Country mockCountry1 = mock(Country.class);
        Category mock = mock(Category.class);
        Indicator indicatorMock = mock(Indicator.class);
        String year = "Version1";

        when(mock1.getCountryId()).thenReturn("IND");
        when(mock1.getCountry()).thenReturn(mockCountry1);
        when(mockCountry1.getId()).thenReturn("IND");
        when(mock1.getCategory()).thenReturn(mock);
        when(mock1.getCategory().getId()).thenReturn(9);
        when(mock1.getCategory().getName()).thenReturn("Category 1");
        when(mock1.getIndicator()).thenReturn(indicatorMock);
        when(mock1.getIndicator().getParentId()).thenReturn(null);
        when(mock1.isScoreValid()).thenReturn(true);

        CountryHealthIndicator mock2 = mock(CountryHealthIndicator.class);
        when(mock2.getScore()).thenReturn(null);
        Country mockCountry2 = mock(Country.class);

        when(mock2.getCountry()).thenReturn(mockCountry2);
        when(mock2.getCountryId()).thenReturn("USA");
        when(mockCountry2.getId()).thenReturn("USA");
        when(mock2.getCategory()).thenReturn(mock);
        when(mock2.getCategory().getId()).thenReturn(9);
        when(mock2.getCategory().getName()).thenReturn("Category 1");
        when(mock2.getIndicator()).thenReturn(indicatorMock);
        when(mock2.getIndicator().getParentId()).thenReturn(null);
        when(mock2.isScoreValid()).thenReturn(false);

        CountryHealthIndicator mock3 = mock(CountryHealthIndicator.class);
        when(mock3.getScore()).thenReturn(null);

        when(mock3.getCountry()).thenReturn(mockCountry2);
        when(mock3.getCountryId()).thenReturn("USA");
        when(mockCountry2.getId()).thenReturn("USA");
        when(mock3.getCategory()).thenReturn(mock);
        when(mock3.getCategory().getId()).thenReturn(8);
        when(mock3.getCategory().getName()).thenReturn("Category 2");
        when(mock3.getIndicator()).thenReturn(indicatorMock);
        when(mock3.getIndicator().getParentId()).thenReturn(null);
        when(mock3.isScoreValid()).thenReturn(false);


        List<CountryHealthIndicator> countryHealthIndicators = new ArrayList<>();
        countryHealthIndicators.add(mock1);
        countryHealthIndicators.add(mock2);

        when(iCountryHealthIndicatorRepository.findByCountryHealthIndicatorIdCategoryIdAndCountryHealthIndicatorIdStatusAndCountryHealthIndicatorIdYear(1, PUBLISHED.name(), "Version1")).thenReturn(countryHealthIndicators);

        CountryPhase countryPhaseIND = buildCountryPhase("IND", 2);
        CountryPhase countryPhaseUSA = buildCountryPhase("USA", null);
        when(iCountryPhaseRepository.findByCountryPhaseIdCountryIdAndCountryPhaseIdYear( "IND", year)).thenReturn(countryPhaseIND);
        when(iCountryPhaseRepository.findByCountryPhaseIdCountryIdAndCountryPhaseIdYear( "USA", year)).thenReturn(countryPhaseUSA);

        CategoryHealthScoreDto categoryHealthScoreDto1 = new CategoryHealthScoreDto(8, "Category 2", 2.0, 2, of(new IndicatorScoreDto(0, null, null, null, 0, 2, null, null)));
        CountryHealthScoreDto countryHealthScoreDtoIN = new CountryHealthScoreDto("IND", null, null, of(categoryHealthScoreDto1), 2, "");
        when(indicatorTranslator.translateCountryHealthScores(null, countryHealthScoreDtoIN)).thenReturn(countryHealthScoreDtoIN);

        GlobalHealthScoreDto globalHealthIndicator = countryHealthIndicatorService.getGlobalHealthIndicator(1, 2, en, "Version1");

        assertEquals(1, globalHealthIndicator.getCategories().size());
        assertThat(globalHealthIndicator.getOverAllScore(), is(2));
    }

    @Test
    public void shouldConsiderOnlyCategoriesWithGivenPhase() throws Exception {
        Category category = Category.builder().id(9).name("Category 1").build();
        String year = "Version1";

        CountryHealthIndicator countryHealthIndicator = CountryHealthIndicator.builder()
                .country(new Country("Ind", "India", UUID.randomUUID(), "IN"))
                .countryHealthIndicatorId(
                        new CountryHealthIndicatorId("Ind", category.getId(), 1, PUBLISHED.name(), "Version1"))
                .indicator(Indicator.builder().indicatorId(1).rank(1).build())
                .category(category).score(5).build();

        CountryHealthIndicator countryHealthIndicator1 = CountryHealthIndicator.builder()
                .country(new Country("Ind", "India", UUID.randomUUID(), "IN"))
                .countryHealthIndicatorId(
                        new CountryHealthIndicatorId("Ind", category.getId(), 1, PUBLISHED.name(), "Version1"))
                .indicator(Indicator.builder().indicatorId(2).rank(2).build())
                .category(category).score(2).build();

        CountryHealthIndicator countryHealthIndicator2 = CountryHealthIndicator.builder()
                .country(new Country("USA", "USA", UUID.randomUUID(), "US"))
                .countryHealthIndicatorId(
                        new CountryHealthIndicatorId("USA", category.getId(), 1, PUBLISHED.name(), "Version1"))
                .indicator(Indicator.builder().indicatorId(1).rank(3).build())
                .category(category).score(2).build();

        CountryHealthIndicator countryHealthIndicator3 = CountryHealthIndicator.builder()
                .country(new Country("USA", "USA", UUID.randomUUID(), "US"))
                .countryHealthIndicatorId(
                        new CountryHealthIndicatorId("USA", category.getId(), 1, PUBLISHED.name(), "Version1"))
                .indicator(Indicator.builder().indicatorId(2).rank(4).build())
                .category(category).score(1).build();

        CountryHealthIndicator countryHealthIndicator4 = CountryHealthIndicator.builder()
                .country(new Country("USA", "USA", UUID.randomUUID(), "US"))
                .countryHealthIndicatorId(
                        new CountryHealthIndicatorId("USA", category.getId(), 1, PUBLISHED.name(), "Version1"))
                .indicator(Indicator.builder().indicatorId(3).rank(5).build())
                .category(category).score(null).build();
        when(iCountryHealthIndicatorRepository.findByCountryHealthIndicatorIdCategoryIdAndCountryHealthIndicatorIdStatusAndCountryHealthIndicatorIdYear(category.getId(), PUBLISHED.name(), year)).thenReturn(asList(countryHealthIndicator, countryHealthIndicator1, countryHealthIndicator2, countryHealthIndicator3, countryHealthIndicator4));

        CountryPhase countryPhaseUSA = buildCountryPhase("USA", 2);
        CountryPhase countryPhaseIND = buildCountryPhase("Ind", 4);
        when(iCountryPhaseRepository.findByCountryPhaseIdCountryIdAndCountryPhaseIdYear( "USA", year)).thenReturn(countryPhaseUSA);
        when(iCountryPhaseRepository.findByCountryPhaseIdCountryIdAndCountryPhaseIdYear("Ind", year)).thenReturn(countryPhaseIND);

        CategoryHealthScoreDto categoryHealthScoreDto = new CategoryHealthScoreDto(9, "Category 1", 1.3333333333333333, 2,
                of(new IndicatorScoreDto(1, null, null, null, 3, 2, null, "Not Available"),
                        new IndicatorScoreDto(2, null, null, null, 4, 1, null, "Not Available"),
                        new IndicatorScoreDto(3, null, null, null, 5, null, null, "Not Available")));
        CountryHealthScoreDto countryHealthScoreDtoUS = new CountryHealthScoreDto("USA", "USA", "US", of(categoryHealthScoreDto), 2, "");

        when(indicatorTranslator.translateCountryHealthScores(null, countryHealthScoreDtoUS)).thenReturn(countryHealthScoreDtoUS);

        GlobalHealthScoreDto globalHealthIndicator = countryHealthIndicatorService.getGlobalHealthIndicator(category.getId(), 2, null, year);

        assertEquals(2, globalHealthIndicator.getOverAllScore().intValue());
        assertEquals(1, globalHealthIndicator.getCategories().size());
        assertEquals(1.333, globalHealthIndicator.getCategories().get(0).getOverallScore(), 0.01);
        assertEquals(2, globalHealthIndicator.getCategories().get(0).getPhase().intValue());
    }

    private CountryPhase buildCountryPhase(String countryId, Integer countryPhase) {
        CountryPhaseId countryPhaseId = new CountryPhaseId(countryId, "Version1");
        return CountryPhase.builder().countryPhaseId(countryPhaseId).countryOverallPhase(countryPhase).build();
    }

    @Test
    public void shouldInvokeConvertExcelOnGlobalExportForAGivenYear() throws IOException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        String year = "Version1";
        countryHealthIndicatorService.createGlobalHealthIndicatorInExcel(request, response, year);

        verify(excelUtilService).convertListToExcel(anyList(), any());
        verify(excelUtilService).downloadFile(request, response);
    }

    @Test
    public void shouldInvokeConvertExcelOnGlobalExportForFrenchForAGivenYear() throws IOException, ParseException {
        Category category1 = Category.builder().id(9).name("Category 1").build();
        Country country1 = new Country("IND", "India", UUID.randomUUID(), "IN");
        Indicator indicator1 = Indicator.builder().indicatorId(1).rank(1).build();
        String year = "Version1";
        CountryHealthIndicator mock1 = CountryHealthIndicator.builder().country(country1).category(category1).indicator(indicator1).score(1).build();

        when(iCountryHealthIndicatorRepository.findByCountryHealthIndicatorIdStatusAndCountryHealthIndicatorIdYear(PUBLISHED.name(), year)).thenReturn(asList(mock1));

        CountrySummary countrySummary = CountrySummary.builder().collectedDate(new SimpleDateFormat("dd-MM-yyyy").parse("04-04-2018")).build();
        when(iCountrySummaryRepository.findByCountrySummaryIdCountryIdAndCountrySummaryIdStatusAndCountrySummaryIdYear(anyString(), anyString(), anyString())).thenReturn(countrySummary);
        CountryPhase countryPhaseIND = buildCountryPhase("IND", 1);
        when(iCountryPhaseRepository.findByCountryPhaseIdCountryIdAndCountryPhaseIdYear( "IND", year)).thenReturn(countryPhaseIND);

        IndicatorScoreDto indicatorScore = new IndicatorScoreDto(1, null, null, null, 1, 1, null, "Not Available");
        CategoryHealthScoreDto categoryScore = new CategoryHealthScoreDto(9, "Category 1", 1.0, 1, of(indicatorScore));
        CountryHealthScoreDto countryHealthScore = new CountryHealthScoreDto("IND", "India", "IN", of(categoryScore), 1, "April 2018");
        IndicatorScoreDto indicatorScoreFR = new IndicatorScoreDto(1, null, null, null, 1, 1, null, "French desc");
        CategoryHealthScoreDto categoryScoreFR = new CategoryHealthScoreDto(9, "Category 1", 1.0, 1, of(indicatorScoreFR));
        CountryHealthScoreDto countryHealthScoreFR = new CountryHealthScoreDto("IND", "Inde", "IN", of(categoryScoreFR), 1, "April 2018");
        when(indicatorTranslator.translateCountryHealthScores(fr, countryHealthScore)).thenReturn(countryHealthScoreFR);

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        when(request.getParameter(USER_LANGUAGE)).thenReturn("fr");

        countryHealthIndicatorService.createGlobalHealthIndicatorInExcel(request, response, year);

        verify(indicatorTranslator).translateCountryHealthScores(fr, countryHealthScore);
    }

    @Test
    public void shouldInvokeConvertExcelOnExportOfSingleCountryForAGivenYear() throws IOException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        Country country = new Country("IND", "India", UUID.randomUUID(), "IN");
        Category category = new Category(1, "Leadership and Governance");
        Indicator indicator = new Indicator(1, "Indicator 1", "Definition1", 1);
        String year = "Version1";
        IndicatorScore indicatorScore = IndicatorScore.builder().id(1L).indicatorId(1).score(1).definition("score 1").build();
        CountryHealthIndicatorId countryHealthIndicatorId = new CountryHealthIndicatorId(country.getId(),
                category.getId(), indicator.getIndicatorId(), "PUBLISHED", year);

        CountryHealthIndicator countryHealthIndicator = new CountryHealthIndicator(countryHealthIndicatorId,
                country, category, indicator, indicatorScore, 1, "st3", new Date(), null);

        List<CountryHealthIndicator> countryHealthIndicatorsForCountry = asList(countryHealthIndicator);
        when(iCountryHealthIndicatorRepository.findByCountryHealthIndicatorIdCountryIdAndCountryHealthIndicatorIdStatusAndCountryHealthIndicatorIdYear("IND", "PUBLISHED", "Version1"))
                .thenReturn(countryHealthIndicatorsForCountry);

        CountryPhase countryPhase = buildCountryPhase(country.getId(), 1);
        when(iCountryPhaseRepository.findByCountryPhaseIdCountryIdAndCountryPhaseIdYear( country.getId(), year)).thenReturn(countryPhase);

        countryHealthIndicatorService.createHealthIndicatorInExcelFor("IND", request, response, year);
        verify(excelUtilService).convertListToExcel(anyList(), any());
        verify(excelUtilService).downloadFile(request, response);
    }

    @Test
    public void shouldInvokeHealthIndicatorTranslatorWithLanguageAsFrench() throws IOException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        Country country = new Country("IND", "India", UUID.randomUUID(), "IN");
        Category category = new Category(1, "Leadership and Governance");
        Indicator indicator = new Indicator(1, "Indicator 1", "Definition1", 1);
        IndicatorScore indicatorScore = IndicatorScore.builder().id(1L).indicatorId(1).score(1).definition("score 1").build();
        String year = "Version1";
        CountryHealthIndicatorId countryHealthIndicatorId = new CountryHealthIndicatorId(country.getId(), category.getId(), indicator.getIndicatorId(), "PUBLISHED", year);

        CountryHealthIndicator countryHealthIndicator = new CountryHealthIndicator(countryHealthIndicatorId, country, category, indicator, indicatorScore, 1, "st3", new Date(), null);

        List<CountryHealthIndicator> countryHealthIndicatorsForCountry = asList(countryHealthIndicator);
        when(iCountryHealthIndicatorRepository.findByCountryHealthIndicatorIdCountryIdAndCountryHealthIndicatorIdStatusAndCountryHealthIndicatorIdYear("IND", "PUBLISHED", "Version1"))
                .thenReturn(countryHealthIndicatorsForCountry);

        IndicatorScoreDto indicatorScoreDto = new IndicatorScoreDto(1, null, "Indicator 1", "Definition1", 1, 1, "st3", "score 1");
        CategoryHealthScoreDto categoryScore = new CategoryHealthScoreDto(1, "Leadership and Governance", 1.0, 1, of(indicatorScoreDto));
        CountryHealthScoreDto countryHealthScoreDto = new CountryHealthScoreDto("IND", "India", "IN", of(categoryScore), 1, "");

        when(indicatorTranslator.translateCountryHealthScores(fr, countryHealthScoreDto)).thenReturn(countryHealthScoreDto);
        when(request.getParameter(USER_LANGUAGE)).thenReturn("fr");

        CountryPhase countryPhase = buildCountryPhase(country.getId(), 1);
        when(iCountryPhaseRepository.findByCountryPhaseIdCountryIdAndCountryPhaseIdYear( country.getId(), year)).thenReturn(countryPhase);

        countryHealthIndicatorService.createHealthIndicatorInExcelFor("IND", request, response, year);

        verify(indicatorTranslator).translateCountryHealthScores(fr, countryHealthScoreDto);
    }

    @Test
    public void shouldTranslateCategoryNameToGivenUserLanguageForGlobalHealthIndicators() {
        Category category = Category.builder().id(9).name("Legislation, Policy, and Compliance").build();
        String year = "Version1";
        CountryHealthIndicator countryHealthIndicator = CountryHealthIndicator.builder()
                .country(new Country("IND", "India", UUID.randomUUID(), "IN"))
                .indicator(Indicator.builder().indicatorId(1).build())
                .category(category).score(5).build();
        Category category1 = Category.builder().id(3).name("Workforce").build();
        CountryHealthIndicator countryHealthIndicator1 = CountryHealthIndicator.builder()
                .country(new Country("IND", "India", UUID.randomUUID(), "IN"))
                .indicator(Indicator.builder().indicatorId(1).build())
                .category(category1).score(2).build();

        List<CountryHealthIndicator> countryHealthIndicators = asList(countryHealthIndicator, countryHealthIndicator1);
        CountryPhase countryPhaseIND = buildCountryPhase("IND", 4);

        when(iCountryHealthIndicatorRepository.findByCountryHealthIndicatorIdStatusAndCountryHealthIndicatorIdYear(PUBLISHED.name(), year))
                .thenReturn(countryHealthIndicators);
        when(iCountryPhaseRepository.findByCountryPhaseIdCountryIdAndCountryPhaseIdYear( "IND", year)).thenReturn(countryPhaseIND);
        when(indicatorTranslator.getTranslatedCategory("Legislation, Policy, and Compliance", ar))
                .thenReturn("التشريعات والسياسات والامتثال");
        when(indicatorTranslator.getTranslatedCategory("Workforce", ar)).thenReturn("الأيدي العاملة");

        CategoryHealthScoreDto categoryHealthScoreDto1 = new CategoryHealthScoreDto(3, "Workforce", 2.0, 2, of(new IndicatorScoreDto(1, null, null, null, null, 2, null, "Not Available")));
        CategoryHealthScoreDto categoryHealthScoreDto2 = new CategoryHealthScoreDto(9, "Legislation, Policy, and Compliance", 5.0, 5, of(new IndicatorScoreDto(1, null, null, null, null, 5, null, "Not Available")));
        CountryHealthScoreDto countryHealthScoreDto = new CountryHealthScoreDto("IND", "India", "IN", of(categoryHealthScoreDto1, categoryHealthScoreDto2), 4, "");
        when(indicatorTranslator.translateCountryHealthScores(null, countryHealthScoreDto)).thenReturn(countryHealthScoreDto);

        GlobalHealthScoreDto globalHealthIndicator = countryHealthIndicatorService
                .getGlobalHealthIndicator(null, null, ar, year);
        globalHealthIndicator.getCategories().sort(comparing(CategoryHealthScoreDto::getId));

        assertEquals("الأيدي العاملة", globalHealthIndicator.getCategories().get(0).getName());
        assertEquals("التشريعات والسياسات والامتثال", globalHealthIndicator.getCategories().get(1).getName());
    }

    @Test
    public void shouldTranslateCountriesHealthIndicatorsToFrench() throws Exception {
        Category category1 = Category.builder().id(9).name("Category 1").build();
        Country country1 = new Country("IND", "India", UUID.randomUUID(), "IN");
        Indicator indicator1 = Indicator.builder().indicatorId(1).rank(1).build();
        String year = "Version1";
        CountryHealthIndicator mock1 = CountryHealthIndicator.builder().country(country1).category(category1).indicator(indicator1).score(1).build();

        when(iCountryHealthIndicatorRepository.findByCountryHealthIndicatorIdStatusAndCountryHealthIndicatorIdYear(PUBLISHED.name(), year)).thenReturn(asList(mock1));

        CountrySummary countrySummary = CountrySummary.builder().collectedDate(new SimpleDateFormat("dd-MM-yyyy").parse("04-04-2018")).build();
        when(iCountrySummaryRepository.findByCountrySummaryIdCountryIdAndCountrySummaryIdStatusAndCountrySummaryIdYear(anyString(), anyString(), anyString())).thenReturn(countrySummary);
        CountryPhase countryPhaseIND = buildCountryPhase("IND", 1);
        when(iCountryPhaseRepository.findByCountryPhaseIdCountryIdAndCountryPhaseIdYear( "IND", year)).thenReturn(countryPhaseIND);

        IndicatorScoreDto indicatorScore = new IndicatorScoreDto(1, null, null, null,
                1, 1, null, "Not Available");
        CategoryHealthScoreDto categoryScore = new CategoryHealthScoreDto(9, "Category 1", 1.0,
                1, of(indicatorScore));
        CountryHealthScoreDto countryHealthScore = new CountryHealthScoreDto("IND", "India",
                "IN", of(categoryScore), 1, "April 2018");
        IndicatorScoreDto indicatorScoreFR = new IndicatorScoreDto(1, null, null, null,
                1, 1, null, "French desc");
        CategoryHealthScoreDto categoryScoreFR = new CategoryHealthScoreDto(9, "Category 1", 1.0,
                1, of(indicatorScoreFR));
        CountryHealthScoreDto countryHealthScoreFR = new CountryHealthScoreDto("IND", "Inde",
                "IN", of(categoryScoreFR), 1, "April 2018");
        when(indicatorTranslator.translateCountryHealthScores(fr, countryHealthScore)).thenReturn(countryHealthScoreFR);

        CountriesHealthScoreDto countriesHealthScoreDto = countryHealthIndicatorService.fetchCountriesHealthScores(null, null, fr, year);
        String indicatorDescription = countriesHealthScoreDto.getCountryHealthScores().get(0).getCategories().get(0)
                .getIndicators().get(0).getScoreDescription();

        verify(indicatorTranslator).translateCountryHealthScores(fr, countryHealthScore);
        assertEquals(indicatorDescription, "French desc");
    }

}