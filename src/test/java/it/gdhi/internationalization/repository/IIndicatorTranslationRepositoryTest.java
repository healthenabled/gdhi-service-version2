package it.gdhi.internationalization.repository;

import it.gdhi.internationalization.model.IndicatorTranslation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.transaction.Transactional;
import java.util.List;

import static it.gdhi.utils.LanguageCode.fr;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@Transactional
@ActiveProfiles("test")
public class IIndicatorTranslationRepositoryTest {

    @Autowired
    private IIndicatorTranslationRepository repository;

    @Test
    public void shouldReturnIndicatorInFrench() {
        String name = "Priorité accordée à la santé numérique au niveau national par l'intermédiaire d'organes et de " +
                "mécanismes de gouvernance dédiés";
        String definition = "Le pays dispose-t-il d'un ministère, d'un organisme ou d'un groupe de travail national " +
                "distinct pour la santé numérique ?";

        IndicatorTranslation translatedIndicator = repository.findTranslationForLanguage(fr, 1);

        assertEquals(translatedIndicator.getName(), name);
        assertEquals(translatedIndicator.getDefinition(), definition);
    }

    @Test
    public void shouldReturnAllIndicatorsInFrench() {
        String name = "Priorité accordée à la santé numérique au niveau national par l'intermédiaire d'organes et de " +
                "mécanismes de gouvernance dédiés";
        String definition = "Le pays dispose-t-il d'un ministère, d'un organisme ou d'un groupe de travail national " +
                "distinct pour la santé numérique ?";

        List<IndicatorTranslation> translatedIndicator = repository.findByLanguageId(fr);

        assertEquals(30, translatedIndicator.size());
        assertEquals(Integer.valueOf(1), translatedIndicator.get(0).getIndicatorId());
        assertEquals(name, translatedIndicator.get(0).getName());
        assertEquals(definition, translatedIndicator.get(0).getDefinition());
    }
}