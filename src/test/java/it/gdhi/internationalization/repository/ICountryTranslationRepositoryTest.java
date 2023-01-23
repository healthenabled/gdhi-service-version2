package it.gdhi.internationalization.repository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@Transactional
@ActiveProfiles("test")
public class ICountryTranslationRepositoryTest {

    @Autowired
    private ICountryTranslationRepository repository;

    @Test
    public void shouldReturnCountryNameInSpanish() {
        String country = repository.findTranslationForLanguage("es", "AFG");

        assertEquals(country, "Afganistán");
    }


}