package it.gdhi.service;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import it.gdhi.dto.CountrySummaryDto;
import it.gdhi.dto.GdhiQuestionnaire;
import it.gdhi.dto.HealthIndicatorDto;
import it.gdhi.internationalization.service.CountryNameTranslator;
import it.gdhi.model.Country;
import it.gdhi.model.CountryHealthIndicator;
import it.gdhi.model.CountryPhase;
import it.gdhi.model.CountrySummary;
import it.gdhi.repository.ICountryHealthIndicatorRepository;
import it.gdhi.repository.ICountryPhaseRepository;
import it.gdhi.repository.ICountryRepository;
import it.gdhi.repository.ICountrySummaryRepository;
import it.gdhi.utils.LanguageCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static it.gdhi.utils.FormStatus.PUBLISHED;
import static java.util.stream.Collectors.toList;
import static it.gdhi.utils.Util.*;


@Service
public class CountryService {

    private final ICountryRepository iCountryRepository;
    private final ICountrySummaryRepository iCountrySummaryRepository;
    private final ICountryPhaseRepository iCountryPhaseRepository;
    private final ICountryHealthIndicatorRepository iCountryHealthIndicatorRepository;
    private final CountryNameTranslator translator;

    @Autowired
    public CountryService(ICountryRepository iCountryRepository, ICountrySummaryRepository iCountrySummaryRepository, ICountryPhaseRepository iCountryPhaseRepository, ICountryHealthIndicatorRepository iCountryHealthIndicatorRepository, CountryNameTranslator translator) {
        this.iCountryRepository = iCountryRepository;
        this.iCountrySummaryRepository = iCountrySummaryRepository;
        this.iCountryPhaseRepository = iCountryPhaseRepository;
        this.iCountryHealthIndicatorRepository = iCountryHealthIndicatorRepository;
        this.translator = translator;
    }

    public List<Country> fetchCountries(LanguageCode languageCode) {
        List<Country> countries = iCountryRepository.findAll();
        return translator.translate(countries, languageCode);
    }

    public CountrySummaryDto fetchCountrySummary(String countryId, String year) {
        CountrySummary countrySummary = iCountrySummaryRepository.findByCountrySummaryIdCountryIdAndCountrySummaryIdYearAndCountrySummaryIdStatus(countryId, year, PUBLISHED.name());
        return Optional.ofNullable(countrySummary).map(CountrySummaryDto::new).orElse(new CountrySummaryDto());
    }

    public GdhiQuestionnaire getDetails(UUID countryUUID, LanguageCode languageCode, boolean publishedOnly, String year) {
        String countryId = iCountryRepository.findByUniqueId(countryUUID).getId();

        GdhiQuestionnaire gdhiQuestionnaire = null;

        List<CountrySummary> countrySummaries = (!publishedOnly) ? getCountrySummaries(year, countryId) : Collections.singletonList(iCountrySummaryRepository.findByCountrySummaryIdCountryIdAndCountrySummaryIdYearAndCountrySummaryIdStatus(countryId, year, PUBLISHED.name()));

        if (countrySummaries != null) {
            CountrySummary countrySummary = countrySummaries.size() > 1 ? getUnPublishedCountrySummary(countrySummaries) : Optional.ofNullable(countrySummaries.get(0)).get();

            List<CountryHealthIndicator> sortedIndicators = getCountryHealthIndicators(countryId, countrySummary, year);
            gdhiQuestionnaire = constructGdhiQuestionnaire(countryId, countrySummary, sortedIndicators, year);
            String translatedCountryName = translator.getCountryTranslationForLanguage(languageCode, gdhiQuestionnaire.getCountryId());
            gdhiQuestionnaire.translateCountryName(translatedCountryName);
        }

        return gdhiQuestionnaire;
    }

    private List<CountrySummary> getCountrySummaries(String year, String countryId) {
        return iCountrySummaryRepository.findByCountrySummaryIdCountryIdAndCountrySummaryIdYear(countryId, year);
    }

    private List<CountryHealthIndicator> getCountryHealthIndicators(String countryId, CountrySummary countrySummary, String year) {
        List<CountryHealthIndicator> countryHealthIndicators = iCountryHealthIndicatorRepository.findByCountryHealthIndicatorIdCountryIdAndCountryHealthIndicatorIdYearAndCountryHealthIndicatorIdStatus(countryId, year, countrySummary.getCountrySummaryId().getStatus());

        return countryHealthIndicators.stream().sorted(Comparator.comparing(o -> o.getIndicator().getRank())).collect(Collectors.toList());
    }

    private CountrySummary getUnPublishedCountrySummary(List<CountrySummary> countrySummaries) {
        return countrySummaries.stream().filter(countrySummaryTmp -> !countrySummaryTmp.getCountrySummaryId().getStatus().equalsIgnoreCase(PUBLISHED.name())).findFirst().get();
    }

    private GdhiQuestionnaire constructGdhiQuestionnaire(String countryId, CountrySummary countrySummary, List<CountryHealthIndicator> sortedIndicators, String year) {
        GdhiQuestionnaire gdhiQuestionnaire;
        CountrySummaryDto countrySummaryDto = Optional.ofNullable(countrySummary).map(CountrySummaryDto::new).orElse(null);
        List<HealthIndicatorDto> healthIndicatorDtos = sortedIndicators.stream().map(HealthIndicatorDto::new).collect(toList());
        String updatedDateStr = countrySummary != null && countrySummary.getUpdatedAt() != null ?
                new SimpleDateFormat("MMMM yyyy").format(countrySummary.getUpdatedAt()) : "";
        gdhiQuestionnaire = new GdhiQuestionnaire(countryId, getCurrentYear(), year, countrySummary.getCountrySummaryId().getStatus(), updatedDateStr, countrySummaryDto, healthIndicatorDtos);
        return gdhiQuestionnaire;
    }

    public List<String> fetchPublishCountriesDistinctYears() {
        List<CountryPhase> countryPhases = iCountryPhaseRepository.findAll();
        return countryPhases.stream().map(CountryPhase::getYear).distinct().sorted(Comparator.reverseOrder()).collect(Collectors.toList());
    }

    public Boolean validateDefaultYear(String year) {
        List<String> distinctYears = this.fetchPublishCountriesDistinctYears();
        return distinctYears.contains(year);
    }
}
