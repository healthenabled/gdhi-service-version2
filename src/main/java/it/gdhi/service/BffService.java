package it.gdhi.service;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import it.gdhi.dto.CountrySummaryDto;
import it.gdhi.dto.CountryUrlGenerationStatusDto;
import it.gdhi.dto.GdhiQuestionnaire;
import it.gdhi.dto.GdhiQuestionnaires;
import it.gdhi.dto.GlobalHealthScoreDto;
import it.gdhi.dto.YearDto;
import it.gdhi.dto.YearHealthScoreDto;
import it.gdhi.dto.YearOnYearDto;
import it.gdhi.dto.YearScoreDto;
import it.gdhi.model.Country;
import it.gdhi.model.CountrySummary;
import it.gdhi.model.id.CountrySummaryId;
import it.gdhi.model.response.CountryStatuses;
import it.gdhi.repository.ICountryPhaseRepository;
import it.gdhi.repository.ICountryRepository;
import it.gdhi.utils.FormStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    private RegionService regionService;

    @Autowired
    public BffService(CountryService countryService, DefaultYearDataService defaultYearDataService,
                      ICountryPhaseRepository iCountryPhaseRepository,
                      CountryHealthIndicatorService countryHealthIndicatorService,
                      ICountryRepository iCountryRepository, CountryHealthDataService countryHealthDataService,
                      RegionService regionService) {
        this.countryService = countryService;
        this.defaultYearDataService = defaultYearDataService;
        this.iCountryPhaseRepository = iCountryPhaseRepository;
        this.countryHealthIndicatorService = countryHealthIndicatorService;
        this.iCountryRepository = iCountryRepository;
        this.countryHealthDataService = countryHealthDataService;
        this.regionService = regionService;
    }

    public YearDto fetchDistinctYears(Integer limit) {
        List<String> years = countryService.fetchPublishCountriesDistinctYears(limit);
        String defaultYear = defaultYearDataService.fetchDefaultYear();
        YearDto yearDto = YearDto.builder().years(years).defaultYear(defaultYear).build();
        return yearDto;
    }

    public List<String> fetchPublishedYearsForACountry(String countryId, Integer limit) {
        return iCountryPhaseRepository.findByCountryPhaseIdOrderByYearDesc(countryId, limit);
    }

    public YearOnYearDto fetchYearOnYearData(List<String> years, String countryId, String regionId) {
        List<YearScoreDto> yearScoreDtos =
                years.stream().map(year -> getYearScoreDto(countryId, year, regionId)).toList();
        return YearOnYearDto.builder().currentYear(getCurrentYear()).yearOnYearData(yearScoreDtos)
                .defaultYear(defaultYearDataService.fetchDefaultYear()).build();
    }

    private YearScoreDto getYearScoreDto(String countryId, String year, String regionId) {
        return YearScoreDto.builder().year(year).data(getYearHealthScoreDto(countryId, year, regionId)).build();
    }

    private YearHealthScoreDto getYearHealthScoreDto(String countryId, String year, String regionId) {
        return YearHealthScoreDto.builder().country(countryHealthIndicatorService.fetchCountryHealthScore(countryId,
                        en, year))
                .average(fetchHealthScore(year, regionId)).build();
    }

    private GlobalHealthScoreDto fetchHealthScore(String year, String regionId) {
        if (regionId == null) {
            return countryHealthIndicatorService.getGlobalHealthIndicator(null, null, en, year);
        }
        else {
            return regionService.isRegionalCategoryDataPresent(regionId, year) ? regionService.fetchRegionalHealthScores(null, regionId, en,
                    year)
                    : new GlobalHealthScoreDto(null, null);
        }
    }

    @Transactional
    public CountryStatuses submitCountryCSVData(GdhiQuestionnaires gdhiQuestionnaires) {
        CountryStatuses countryStatuses = new CountryStatuses();
        List<GdhiQuestionnaire> gdhiQuestionnaireList = gdhiQuestionnaires.getGdhiQuestionnaires();
        gdhiQuestionnaireList.forEach(gdhiQuestionnaire -> {
            String countryName = gdhiQuestionnaire.getCountrySummary().getCountryName();
            Country country = iCountryRepository.findByNameIgnoreCase(countryName);
            if (!isValidCountry(country)) {
                countryStatuses.add(countryName, false, null, "Invalid Country Name");
            }
            else {
                CountryUrlGenerationStatusDto countryUrlGenerationStatusDto =
                        countryHealthDataService.saveNewCountrySummary(country.getUniqueId());
                if (canSubmitDataForCountry(countryUrlGenerationStatusDto)) {
                    GdhiQuestionnaire gdhiQuestionnaire1 = constructGdhiQuestionnaire(gdhiQuestionnaire,
                            countryUrlGenerationStatusDto, country);
                    boolean isValidQuestionnaire = countryHealthDataService.validateRequiredFields(gdhiQuestionnaire1);
                    if (isValidQuestionnaire) {
                        countryHealthDataService.submit(gdhiQuestionnaire1);
                        countryStatuses.add(countryName, true, FormStatus.REVIEW_PENDING, "");
                    }
                    else {
                        countryStatuses.add(countryName, false, countryUrlGenerationStatusDto.getExistingStatus(),
                                "Invalid Questionnaire Data");
                    }
                }
                else {
                    countryStatuses.add(countryName, false, countryUrlGenerationStatusDto.getExistingStatus(),
                            "Country is already in " + countryUrlGenerationStatusDto.getExistingStatus() + " state");
                }
            }
        });
        return countryStatuses;
    }

    public boolean isValidCountry(Country country) {
        return country == null ? false : true;
    }

    public boolean canSubmitDataForCountry(CountryUrlGenerationStatusDto countryUrlGenerationStatusDto) {
        return countryUrlGenerationStatusDto.isSuccess() || countryUrlGenerationStatusDto.getExistingStatus().equals(FormStatus.DRAFT) || countryUrlGenerationStatusDto.getExistingStatus().equals(FormStatus.NEW);
    }

    public GdhiQuestionnaire constructGdhiQuestionnaire(GdhiQuestionnaire gdhiQuestionnaire,
                                                        CountryUrlGenerationStatusDto countryUrlGenerationStatusDto,
                                                        Country country) {
        String status = countryUrlGenerationStatusDto.getExistingStatus() == null ? "NEW" :
                countryUrlGenerationStatusDto.getExistingStatus().toString();
        String yearToPrefillData = countryService.fetchTheYearToPrefillData(country.getUniqueId());
        CountrySummaryDto countrySummaryDto = gdhiQuestionnaire.getCountrySummary();
        CountrySummaryId countrySummaryId =
                CountrySummaryId.builder().countryId(country.getId()).status(status).year(getCurrentYear()).build();
        CountrySummary countrySummary = new CountrySummary(countrySummaryId, countrySummaryDto, country);

        GdhiQuestionnaire gdhiQuestionnaire1;
        CountrySummaryDto countrySummaryDto1 =
                Optional.ofNullable(countrySummary).map(CountrySummaryDto::new).orElse(null);
        String updatedDateStr = countrySummary != null && countrySummary.getUpdatedAt() != null ?
                new SimpleDateFormat("MMMM yyyy").format(countrySummary.getUpdatedAt()) : "";

        gdhiQuestionnaire1 = new GdhiQuestionnaire(country.getId(), getCurrentYear(), yearToPrefillData,
                countrySummary.getCountrySummaryId().getStatus(), updatedDateStr, countrySummaryDto1,
                gdhiQuestionnaire.getHealthIndicators());
        return gdhiQuestionnaire1;
    }

}

