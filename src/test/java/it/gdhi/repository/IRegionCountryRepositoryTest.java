package it.gdhi.repository;

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
public class IRegionCountryRepositoryTest {

    @Autowired
    IRegionCountryRepository iRegionCountryRepository;

    @Test
    public void shouldReturnCountriesGivenRegionId() {
        List<String> countries = iRegionCountryRepository.findByRegionCountryIdRegionId("SEARO");
        List<String> expectedCountries = asList("BGD", "BTN", "IDN", "IND", "LKA", "MDV", "MMR", "NPL", "THA", "TLS");

        assertEquals(expectedCountries, countries);
    }

}
