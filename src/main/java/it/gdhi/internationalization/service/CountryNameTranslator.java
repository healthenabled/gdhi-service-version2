package it.gdhi.internationalization.service;

import it.gdhi.internationalization.repository.ICountryTranslationRepository;
import it.gdhi.model.Country;
import it.gdhi.utils.LanguageCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

import static it.gdhi.utils.LanguageCode.en;
import static java.util.stream.Collectors.toList;

@Component
public class CountryNameTranslator {

    private final ICountryTranslationRepository translationRepository;

    @Autowired
    public CountryNameTranslator(ICountryTranslationRepository translationRepository) {
        this.translationRepository = translationRepository;
    }

    public List<Country> translate(List<Country> countries, LanguageCode languageCode) {
        if (languageCode == en || languageCode == null) return countries;

        return countries.stream()
                        .map(country -> country.makeWithName(getCountryTranslationForLanguage(languageCode, country)))
                        .collect(toList());
    }

    public String getCountryTranslationForLanguage(LanguageCode languageCode, String countryId) {
        return translationRepository.findTranslationForLanguage(languageCode.toString(), countryId);
    }

    private String getCountryTranslationForLanguage(LanguageCode languageCode, Country country) {
        String newName = translationRepository.findTranslationForLanguage(languageCode.toString(), country.getId());
        return (newName == null || newName.isEmpty()) ? country.getName() : newName ;
    }

}
