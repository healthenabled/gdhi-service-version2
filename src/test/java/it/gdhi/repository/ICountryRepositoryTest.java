package it.gdhi.repository;

import it.gdhi.model.Country;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@Transactional
@ActiveProfiles("test")
public class ICountryRepositoryTest {

    @Autowired
    private ICountryRepository countryRepository;
    @Autowired
    private EntityManager entityManager;

    @Test
    public void shouldGetAllCountries() {
        int size = countryRepository.findAll().size();
        assertThat(size, is(194));
    }

    @Test
    public void shouldGetCountryGivenId() throws Exception {
        Country country = new Country("ABC", "Republic of ABC",UUID.randomUUID(),"AB");
        entityManager.persist(country);
        entityManager.flush();
        entityManager.clear();

        Country actualCountry = countryRepository.findById(country.getId());

        assertEquals(country.getId(), actualCountry.getId());
        assertEquals(country.getName(), actualCountry.getName());
    }
}