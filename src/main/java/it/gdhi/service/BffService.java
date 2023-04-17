package it.gdhi.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import it.gdhi.dto.*;
import it.gdhi.model.Country;
import it.gdhi.model.CountrySummary;
import it.gdhi.model.id.CountrySummaryId;
import it.gdhi.model.response.CountryStatus;
import it.gdhi.repository.ICountryPhaseRepository;
import it.gdhi.repository.ICountryRepository;
import it.gdhi.utils.FormStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

import static it.gdhi.utils.LanguageCode.en;
import static it.gdhi.utils.Util.getCurrentYear;

@Service
public class BffService {

    private CountryService countryService;
    private DefaultYearDataService defaultYearDataService;
    private final ICountryPhaseRepository iCountryPhaseRepository;
    private CountryHealthIndicatorService countryHealthIndicatorService;
    private ICountryRepository iCountryRepository;
    private CountryHealthDataService countryHealthDataService;

    @Autowired
    public BffService(CountryService countryService, DefaultYearDataService defaultYearDataService,
                      ICountryPhaseRepository iCountryPhaseRepository, CountryHealthIndicatorService countryHealthIndicatorService,
                      ICountryRepository iCountryRepository, CountryHealthDataService countryHealthDataService) {
        this.countryService = countryService;
        this.defaultYearDataService = defaultYearDataService;
        this.iCountryPhaseRepository = iCountryPhaseRepository;
        this.countryHealthIndicatorService = countryHealthIndicatorService;
        this.iCountryRepository = iCountryRepository;
        this.countryHealthDataService = countryHealthDataService;
    }

    public YearDto fetchDistinctYears() {
        List<String> years = countryService.fetchPublishCountriesDistinctYears();
        String defaultYear = defaultYearDataService.fetchDefaultYear();
        YearDto yearDto = YearDto.builder().years(years).defaultYear(defaultYear).build();
        return yearDto;
    }

    public List<String> fetchPublishedYearsForACountry(String countryId, Integer limit) {
        return iCountryPhaseRepository.findByCountryPhaseIdOrderByYearDesc(countryId, limit);
    }

    public YearOnYearDto fetchYearOnYearData(List<String> years, String countryId, String regionId) {
        List<YearScoreDto> yearScoreDtos = years.stream().map(year -> getYearScoreDto(countryId, year, regionId)).toList();
        return YearOnYearDto.builder().currentYear(getCurrentYear()).yearOnYearData(yearScoreDtos).defaultYear(defaultYearDataService.fetchDefaultYear()).build();
    }

    private YearScoreDto getYearScoreDto(String countryId, String year, String regionId) {
        return YearScoreDto.builder().year(year).data(getYearHealthScoreDto(countryId, year, regionId)).build();
    }

    private YearHealthScoreDto getYearHealthScoreDto(String countryId, String year, String regionId) {
        return YearHealthScoreDto.builder().country(countryHealthIndicatorService.fetchCountryHealthScore(countryId, en, year))
                .average(countryHealthIndicatorService.getGlobalHealthIndicator(null, null, regionId, en, year)).build();
    }

    @Transactional
    public List<CountryStatus> submitCountryCSVData(GdhiQuestionnaires gdhiQuestionnaires) {
        List<CountryStatus> countryStatuses = new ArrayList<>();
        List<GdhiQuestionnaire> gdhiQuestionnaireList = gdhiQuestionnaires.getGdhiQuestionnaires();
        gdhiQuestionnaireList.forEach(gdhiQuestionnaire -> {
            String countryName = gdhiQuestionnaire.getCountrySummary().getCountryName();
            Country country = iCountryRepository.findByName(countryName);
            if (country == null) {
                CountryStatus countryStatus = new CountryStatus(countryName, false, null);
                countryStatuses.add(countryStatus);
            } else {
                CountryUrlGenerationStatusDto countryUrlGenerationStatusDto = countryHealthDataService.saveNewCountrySummary(country.getUniqueId());
                if (canSubmitDataForCountry(countryUrlGenerationStatusDto)) {
                    GdhiQuestionnaire gdhiQuestionnaire1 = constructGdhiQuestionnaire(gdhiQuestionnaire , countryUrlGenerationStatusDto , country);
                    boolean isValid = countryHealthDataService.validateRequiredFields(gdhiQuestionnaire1);
                    if (isValid) {
                        countryHealthDataService.submit(gdhiQuestionnaire1);
                        CountryStatus countryStatus = new CountryStatus(countryName, true, FormStatus.REVIEW_PENDING);
                        countryStatuses.add(countryStatus);
                    } else {
                        CountryStatus countryStatus = new CountryStatus(countryName, false, countryUrlGenerationStatusDto.getExistingStatus());
                        countryStatuses.add(countryStatus);
                    }
                } else {
                    CountryStatus countryStatus = new CountryStatus(countryName, false, countryUrlGenerationStatusDto.getExistingStatus());
                    countryStatuses.add(countryStatus);
                }
            }
        });
        return countryStatuses;
    }

    public boolean canSubmitDataForCountry(CountryUrlGenerationStatusDto countryUrlGenerationStatusDto) {
        return countryUrlGenerationStatusDto.isSuccess() || countryUrlGenerationStatusDto.getExistingStatus().equals(FormStatus.DRAFT) || countryUrlGenerationStatusDto.getExistingStatus().equals(FormStatus.NEW);
    }

    public GdhiQuestionnaire constructGdhiQuestionnaire(GdhiQuestionnaire gdhiQuestionnaire , CountryUrlGenerationStatusDto countryUrlGenerationStatusDto , Country country) {
        String status = countryUrlGenerationStatusDto.getExistingStatus() == null ? "NEW" : countryUrlGenerationStatusDto.getExistingStatus().toString();
        String yearToPrefillData = countryService.fetchTheYearToPrefillData(country.getUniqueId());
        CountrySummaryDto countrySummaryDto = gdhiQuestionnaire.getCountrySummary();
        CountrySummaryId countrySummaryId = CountrySummaryId.builder().countryId(country.getId()).status(status).year(getCurrentYear()).build();
        CountrySummary countrySummary = new CountrySummary(countrySummaryId, countrySummaryDto , country);

        GdhiQuestionnaire gdhiQuestionnaire1;
        CountrySummaryDto countrySummaryDto1 = Optional.ofNullable(countrySummary).map(CountrySummaryDto::new).orElse(null);
        String updatedDateStr = countrySummary != null && countrySummary.getUpdatedAt() != null ?
                new SimpleDateFormat("MMMM yyyy").format(countrySummary.getUpdatedAt()) : "";

        gdhiQuestionnaire1 = new GdhiQuestionnaire(country.getId(), getCurrentYear(), yearToPrefillData, countrySummary.getCountrySummaryId().getStatus(), updatedDateStr, countrySummaryDto1, gdhiQuestionnaire.getHealthIndicators());
        return gdhiQuestionnaire1;
    }

}

