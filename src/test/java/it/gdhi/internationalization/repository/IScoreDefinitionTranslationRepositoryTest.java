package it.gdhi.internationalization.repository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.transaction.Transactional;

import static it.gdhi.utils.LanguageCode.ar;
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
        String expectedScoreDefinition = "La structure de gouvernance est entièrement fonctionnelle, dirigée par le gouvernement, consulte d'autres ministères et surveille la mise en œuvre de la santé numérique et de la gouvernance des données, y compris l'intelligence artificielle, sur la base d'un plan de travail.";

        String scoreDefinitionFR = repository.findTranslationForLanguage(fr, 1, 4);

        assertEquals(expectedScoreDefinition, scoreDefinitionFR);
    }
    @Test
    public void shouldReturnScoreDefinitionInArabicForGivenHealthIndicator() {
        String expectedScoreDefinition = "يعمل هيكل الحوكمة بكامل طاقته، وتقوده الحكومة، ويتشاور مع الوزارات الأخرى، ويراقب تنفيذ الصحة الرقمية وحوكمة البيانات، بما في ذلك الذكاء الاصطناعي، ويتم كل ذلك بناءا على خطة عمل.";

        String scoreDefinitionFR = repository.findTranslationForLanguage(ar, 1, 4);

        assertEquals(expectedScoreDefinition, scoreDefinitionFR);
    }
}