package it.gdhi.internationalization.repository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.transaction.Transactional;

import static it.gdhi.utils.LanguageCode.fr;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@Transactional
@ActiveProfiles("test")
public class IScoreDefinitionTranslationRepositoryTest {

    @Autowired
    private IScoreDefinitionTranslationRepository repository;

    @Test
    public void shouldReturnScoreDefinitionInFrenchForGivenHealthIndicator() {
        String expectedScoreDefinition = "La structure de gouvernance est completement fonctionnelle, dirigée par le " +
                "gouvernement, consulte les autres ministères et surveille la mise en œuvre de la santé numérique en " +
                "fonction d'un plan de travail.";

        String scoreDefinitionFR = repository.findTranslationForLanguage(fr, 1, 4);

        assertEquals(expectedScoreDefinition, scoreDefinitionFR);
    }
}