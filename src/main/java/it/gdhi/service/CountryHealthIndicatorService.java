package it.gdhi.service;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.OptionalDouble;
import java.util.function.Predicate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import it.gdhi.dto.CategoryHealthScoreDto;
import it.gdhi.dto.CountriesHealthScoreDto;
import it.gdhi.dto.CountryHealthScoreDto;
import it.gdhi.dto.GlobalHealthScoreDto;
import it.gdhi.internationalization.service.HealthIndicatorTranslator;
import it.gdhi.model.*;
import it.gdhi.repository.ICountryHealthIndicatorRepository;
import it.gdhi.repository.ICountryPhaseRepository;
import it.gdhi.repository.ICountrySummaryRepository;
import it.gdhi.utils.LanguageCode;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import static it.gdhi.controller.strategy.FilterStrategy.getCategoryPhaseFilter;
import static it.gdhi.controller.strategy.FilterStrategy.getCountryPhaseFilter;
import static it.gdhi.utils.FormStatus.PUBLISHED;
import static it.gdhi.utils.LanguageCode.USER_LANGUAGE;
import static java.util.Comparator.comparing;
import static java.util.Comparator.nullsLast;
import static java.util.Objects.isNull;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

@Service
public class CountryHealthIndicatorService {

    @Autowired
    private ICountryHealthIndicatorRepository iCountryHealthIndicatorRepository;

    @Autowired
    private ICountrySummaryRepository iCountrySummaryRepository;

    @Autowired
    private ICountryPhaseRepository iCountryPhaseRepository;

    @Autowired
    private ExcelUtilService excelUtilService;

    @Autowired
    private CategoryIndicatorService categoryIndicatorService;

    @Autowired
    private HealthIndicatorTranslator healthIndicatorTranslator;

    @Value("${excelFileLocation}")
    @Getter
    private String fileWithPath;

    public CountryHealthScoreDto fetchCountryHealthScore(String countryId, LanguageCode languageCode, String year) {
        CountryHealthIndicators countryHealthIndicators = new CountryHealthIndicators(iCountryHealthIndicatorRepository
                .findByCountryHealthIndicatorIdCountryIdAndCountryHealthIndicatorIdYearAndStatus(countryId, year, PUBLISHED.name()));
        CountryHealthScoreDto countryHealthScoreDto = constructCountryHealthScore(countryId, countryHealthIndicators,
                getCategoryPhaseFilter(null, null), year);
        return healthIndicatorTranslator.translateCountryHealthScores(languageCode, countryHealthScoreDto);
    }

    public CountriesHealthScoreDto fetchCountriesHealthScores(Integer categoryId, Integer phase, LanguageCode code, String year) {
        List<CountryHealthIndicator> countryHealthIndicators = filterByCategoryAndYear(categoryId, year);

        Map<String, List<CountryHealthIndicator>> countryIdCountryHealthIndicatorsMap = countryHealthIndicators.stream()
                .collect(groupingBy(CountryHealthIndicator::getCountryId));
        List<CountryHealthScoreDto> globalHealthScores = constructCountryHealthScores(categoryId, phase, year, countryIdCountryHealthIndicatorsMap);

        CountriesHealthScoreDto countriesHealthScoreDto = new CountriesHealthScoreDto(globalHealthScores);
        return getTranslatedCountriesHealthScore(countriesHealthScoreDto, code);
    }

    private List<CountryHealthScoreDto> constructCountryHealthScores(Integer categoryId, Integer phase, String year, Map<String,
            List<CountryHealthIndicator>> countryIdCountryHealthIndicatorsMap) {
        return countryIdCountryHealthIndicatorsMap
                .entrySet()
                .stream()
                .map(entry -> constructCountryHealthScore(entry.getKey(),
                        new CountryHealthIndicators(entry.getValue()),
                        getCategoryPhaseFilter(categoryId, phase), year))
                .filter(getCountryPhaseFilter(categoryId, phase))
                .filter(CountryHealthScoreDto::hasCategories)
                .sorted(comparing(CountryHealthScoreDto::getCountryName, nullsLast(Comparator.naturalOrder())))
                .collect(toList());
    }

    private List<CountryHealthIndicator> filterByCategoryAndYear(Integer categoryId, String year) {
        return (categoryId == null)
                ?
                iCountryHealthIndicatorRepository.findByCountryHealthIndicatorIdYearAndStatus(year, PUBLISHED.name())
                : iCountryHealthIndicatorRepository.findByCountryHealthIndicatorIdCategoryIdAndCountryHealthIndicatorIdYearAndStatus(categoryId,
                year, PUBLISHED.name());
    }


    private CountriesHealthScoreDto getTranslatedCountriesHealthScore(CountriesHealthScoreDto countriesHealthScoreDto,
                                                                      LanguageCode code) {
        List<CountryHealthScoreDto> countryHealthScores = countriesHealthScoreDto.getCountryHealthScores().stream()
                .map(dto -> {
                    dto.getCategories().forEach(categoryHealthScoreDto -> categoryHealthScoreDto.setIndicatorToNull(categoryHealthScoreDto.getIndicators()));
                    return healthIndicatorTranslator.translateCountryHealthScoreCountryNameAndCategory(code, dto);
                })
                .collect(toList());
        return new CountriesHealthScoreDto(countryHealthScores);
    }

    public GlobalHealthScoreDto getGlobalHealthIndicator(Integer categoryId, Integer phase, LanguageCode languageCode, String year) {
        CountriesHealthScoreDto countries = this.fetchCountriesHealthScores(categoryId, phase, null, year);
        List<CategoryHealthScoreDto> categories = getCategoriesInCountries(countries);
        Map<Integer, List<CategoryHealthScoreDto>> groupByCategory = categories.stream()
                .collect(groupingBy(CategoryHealthScoreDto::getId));
        List<CategoryHealthScoreDto> categoryHealthScores = groupByCategory.entrySet().stream()
                .map(this::getCategoryHealthScoreDto).collect(toList());
        Score averageCategoryScore = new Score(getAverageCategoryScore(categoryHealthScores));
        Integer globalPhase = averageCategoryScore.convertToPhase();
        GlobalHealthScoreDto globalHealthScoreDto = new GlobalHealthScoreDto(globalPhase, categoryHealthScores);
        return translateCategoryNames(globalHealthScoreDto, languageCode);
    }

    private GlobalHealthScoreDto translateCategoryNames(GlobalHealthScoreDto globalHealthScoreDto, LanguageCode code) {
        globalHealthScoreDto
                .getCategories()
                .forEach((category) -> {
                    String translatedCategory = healthIndicatorTranslator.getTranslatedCategory(category.getName(), code);
                    category.translateCategoryName(translatedCategory);
                });
        return globalHealthScoreDto;
    }

    public void createGlobalHealthIndicatorInExcel(HttpServletRequest request,
                                                   HttpServletResponse response,
                                                   String year) throws IOException {
        LanguageCode languageCode = LanguageCode.getValueFor(request.getParameter(USER_LANGUAGE));
        List<CountryHealthScoreDto> countryHealthScores = fetchCountriesHealthScoresForExcel(languageCode, year)
                .getCountryHealthScores();

        excelUtilService.convertListToExcel(countryHealthScores, languageCode, this.getFileWithPath());
        excelUtilService.downloadFile(request, response, this.getFileWithPath());
    }

    public void createHealthIndicatorInExcelFor(String countryId,
                                                HttpServletRequest request,
                                                HttpServletResponse response, String year) throws IOException {
        LanguageCode languageCode = LanguageCode.getValueFor(request.getParameter(USER_LANGUAGE));
        List countryHealthScoreDtoAsList = new ArrayList<CountryHealthScoreDto>();

        countryHealthScoreDtoAsList.add(fetchCountryHealthScore(countryId, languageCode, year));
        excelUtilService.convertListToExcel(countryHealthScoreDtoAsList, languageCode, this.getFileWithPath());
        excelUtilService.downloadFile(request, response, this.getFileWithPath());
    }

    private CountriesHealthScoreDto fetchCountriesHealthScoresForExcel(LanguageCode languageCode, String year) {
        return this.fetchCountriesHealthScores(null, null, languageCode, year);
    }

    private CategoryHealthScoreDto getCategoryHealthScoreDto(Entry<Integer, List<CategoryHealthScoreDto>> entry) {
        List<CategoryHealthScoreDto> categoriesHealthScore = entry.getValue();
        Double globalScore = getAverageCategoryScore(categoriesHealthScore);
        CategoryHealthScoreDto categoryHealthScoreDto = categoriesHealthScore.stream().findFirst()
                .orElse(new CategoryHealthScoreDto());
        return new CategoryHealthScoreDto(entry.getKey(), categoryHealthScoreDto.getName(), globalScore,
                (new Score(globalScore)).convertToPhase(), null);
    }

    private List<CategoryHealthScoreDto> getCategoriesInCountries(CountriesHealthScoreDto countries) {
        return countries.getCountryHealthScores()
                .stream()
                .map(CountryHealthScoreDto::getCategories)
                .flatMap(Collection::stream)
                .collect(toList());
    }

    private Double getAverageCategoryScore(List<CategoryHealthScoreDto> categoriesHealthScore) {
        OptionalDouble optionalGlobalScore = categoriesHealthScore.stream()
                .filter(c -> (!isNull(c.getOverallScore()) && c.getOverallScore() != -1))
                .mapToDouble(CategoryHealthScoreDto::getOverallScore)
                .average();
        return optionalGlobalScore.isPresent() ? optionalGlobalScore.getAsDouble() : null;
    }

    private CountryHealthScoreDto constructCountryHealthScore(String countryId,
                                                              CountryHealthIndicators countryHealthIndicators,
                                                              Predicate<? super CategoryHealthScoreDto> phaseFilter, String year) {
        List<CategoryHealthScoreDto> categoryDtos = getCategoriesWithIndicators(countryHealthIndicators, phaseFilter);
        CountryPhase countryPhase = iCountryPhaseRepository.findByCountryPhaseIdCountryIdAndCountryPhaseIdYear(countryId, year);
        CountrySummary countrySummary = iCountrySummaryRepository.
                findByCountrySummaryIdCountryIdAndCountrySummaryIdYearAndStatus(countryId, year, PUBLISHED.name());
        String updatedDateStr = countrySummary != null && countrySummary.getUpdatedAt() != null ?
                new SimpleDateFormat("MMMM yyyy").format(countrySummary.getUpdatedAt()) : "";
        return new CountryHealthScoreDto(countryId, countryHealthIndicators.getCountryName(),
                countryHealthIndicators.getCountryAlpha2Code(),
                categoryDtos, countryPhase != null ? countryPhase.getCountryOverallPhase() : null, updatedDateStr);
    }

    public List<CategoryHealthScoreDto> getCategoriesWithIndicators(CountryHealthIndicators countryHealthIndicators,
                                                                    Predicate<? super CategoryHealthScoreDto>
                                                                            phaseFilter) {
        Map<Integer, Double> CategoryScore = countryHealthIndicators
                .groupByCategoryIdWithoutNullAndNegativeScores();
        return countryHealthIndicators.groupByCategory()
                .entrySet()
                .stream()
                .map(entry -> {
                    Category category = entry.getKey();
                    List<CountryHealthIndicator> indicators = entry.getValue();
                    return new CategoryHealthScoreDto(category,
                            CategoryScore.get(category.getId()) == null ? -1 : CategoryScore.get(category.getId()),
                            indicators);
                })
                .filter(phaseFilter)
                .sorted(comparing(CategoryHealthScoreDto::getId))
                .collect(toList());
    }

}