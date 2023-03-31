package it.gdhi.repository;


import it.gdhi.model.CountryPhase;
import it.gdhi.model.id.CountryPhaseId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.transaction.Transactional;

import java.util.List;

import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@Transactional
@ActiveProfiles("test")
public class ICountryPhaseRepositoryTest {

    @Autowired
    private ICountryPhaseRepository iCountryPhaseRepository;

    private void addCountryPhase(String countryId , Integer overAllPhase , String year) {
        CountryPhase countryPhase = CountryPhase.builder().countryPhaseId(new CountryPhaseId(countryId , year)).countryOverallPhase(overAllPhase).build();
        iCountryPhaseRepository.save(countryPhase);
    }

    @Test
    public void shouldReturnPublishedYearsForACountry() {
        String countryId = "IND";
        addCountryPhase(countryId , 2 , "2022");
        addCountryPhase(countryId, 4 , "2023");

        List<String> actual =  iCountryPhaseRepository.findByCountryPhaseIdOrderByYearDesc(countryId , 2);
        List<String> expected = asList("2023" , "2022");

        assertEquals(actual , expected);
    }
}
