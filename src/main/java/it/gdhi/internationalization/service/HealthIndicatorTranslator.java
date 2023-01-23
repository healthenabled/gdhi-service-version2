package it.gdhi.internationalization.service;

import it.gdhi.dto.*;
import it.gdhi.internationalization.model.IndicatorTranslation;
import it.gdhi.internationalization.repository.ICategoryTranslationRepository;
import it.gdhi.internationalization.repository.IIndicatorTranslationRepository;
import it.gdhi.internationalization.repository.IScoreDefinitionTranslationRepository;
import it.gdhi.utils.LanguageCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static it.gdhi.utils.LanguageCode.en;

@Component
public class HealthIndicatorTranslator {

    private final ICategoryTranslationRepository categoryTranslationRepository;
    private final IIndicatorTranslationRepository indicatorTranslationRepo;
    private final IScoreDefinitionTranslationRepository scoreTranslationRepository;
    private final CountryNameTranslator countryNameTranslator;

    @Autowired
    public HealthIndicatorTranslator(ICategoryTranslationRepository categoryTranslationRepository,
                                     IIndicatorTranslationRepository indicatorTranslationRepo,
                                     IScoreDefinitionTranslationRepository scoreTranslationRepository,
                                     CountryNameTranslator countryNameTranslator) {
        this.categoryTranslationRepository = categoryTranslationRepository;
        this.indicatorTranslationRepo = indicatorTranslationRepo;
        this.scoreTranslationRepository = scoreTranslationRepository;
        this.countryNameTranslator = countryNameTranslator;
    }

    public CategoryIndicatorDto translateHealthIndicatorOptions(CategoryIndicatorDto categoryIndicatorDto,
                                                                LanguageCode languageCode) {
        if (isLocalizationNotRequired(languageCode)) return categoryIndicatorDto;

        String translatedCategoryName = getTranslatedCategory(categoryIndicatorDto.getCategoryName(), languageCode);
        categoryIndicatorDto
                .translateCategoryName(translatedCategoryName)
                .getIndicators()
                .forEach(indicator -> {
                    translateIndicatorDto(languageCode, indicator);
                    indicator.getScores()
                            .forEach(score -> translateScoreDto(languageCode, indicator.getIndicatorId(), score));
                });
        return categoryIndicatorDto;
    }

    public String getTranslatedCategory(String categoryName, LanguageCode languageCode) {
        if (isLocalizationNotRequired(languageCode)) return categoryName;

        String translationCategoryName = categoryTranslationRepository.findTranslationForLanguage(  languageCode,
                                                                                                    categoryName);

        return (translationCategoryName == null || translationCategoryName.isEmpty()) ? categoryName
                                                                                        : translationCategoryName;
    }

    private void translateIndicatorDto(LanguageCode languageCode, IndicatorDto indicator) {
        IndicatorTranslation translatedIndicator = getTranslatedIndicator(languageCode, indicator.getIndicatorId());
        indicator.translateName(translatedIndicator.getName());
        indicator.translateDefinition(translatedIndicator.getDefinition());
    }

    private IndicatorTranslation getTranslatedIndicator(LanguageCode languageCode, Integer indicatorId) {
        return indicatorTranslationRepo.findTranslationForLanguage(languageCode, indicatorId);
    }

    private void translateScoreDto(LanguageCode languageCode, Integer indicatorId, ScoreDto scoreDto) {
        String translatedDefinition = getTranslatedScoreDefinition(languageCode, indicatorId, scoreDto.getScore());
        scoreDto.translateDefinition(translatedDefinition);
    }

    private String getTranslatedScoreDefinition(LanguageCode languageCode, Integer indicatorId, Integer score) {
        return scoreTranslationRepository.findTranslationForLanguage(languageCode, indicatorId, score);
    }

    public CountryHealthScoreDto translateCountryHealthScores(LanguageCode languageCode,
                                                              CountryHealthScoreDto countryHealthScore) {
        if(isLocalizationNotRequired(languageCode)) return countryHealthScore;

        translateCountry(languageCode, countryHealthScore);
        countryHealthScore.getCategories()
                .forEach(category -> {
                    translateCategory(languageCode, category);
                    category.getIndicators()
                            .forEach(indicatorScore -> translateIndicatorScore(languageCode, indicatorScore));
                });

        return countryHealthScore;
    }

    private void translateCountry(LanguageCode languageCode, CountryHealthScoreDto countryHealthScore) {
        String translatedCountryName = countryNameTranslator.getCountryTranslationForLanguage(languageCode,
                                                                                    countryHealthScore.getCountryId());
        countryHealthScore.translateCountryName(translatedCountryName);
    }

    private void translateCategory(LanguageCode languageCode, CategoryHealthScoreDto category) {
        String translatedCategoryName = getTranslatedCategory(category.getName(), languageCode);
        category.translateCategoryName(translatedCategoryName);
    }

    private void translateIndicatorScore(LanguageCode languageCode, IndicatorScoreDto indicatorScore) {
        IndicatorTranslation translatedIndicator = getTranslatedIndicator(languageCode, indicatorScore.getId());
        String translatedScore = getTranslatedScoreDefinition(languageCode, indicatorScore.getId(),
                                                                indicatorScore.getScore());
        indicatorScore.translateIndicator(translatedIndicator.getName(), translatedIndicator.getDefinition());
        indicatorScore.translateScoreDefinition(translatedScore);
    }

    private boolean isLocalizationNotRequired(LanguageCode languageCode) {
        return languageCode == en || languageCode == null;
    }
}
