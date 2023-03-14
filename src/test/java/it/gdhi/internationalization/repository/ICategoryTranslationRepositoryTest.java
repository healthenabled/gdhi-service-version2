package it.gdhi.internationalization.repository;

import it.gdhi.internationalization.model.CategoryTranslation;
import it.gdhi.utils.LanguageCode;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.transaction.Transactional;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static com.google.common.collect.ImmutableList.of;
import static it.gdhi.utils.LanguageCode.ar;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@Transactional
@ActiveProfiles("test")
public class ICategoryTranslationRepositoryTest {

    @Autowired
    private ICategoryTranslationRepository repository;

    @Test
    public void shouldReturnCategoryNameInArabic() {
        String category = repository.findTranslationForLanguage(ar, "Services & Applications");

        assertEquals(category, "الخدمات والتطبيقات");
    }

    @Test
    public void shouldReturnAllCategoryNamesInFrench() {
        List<String> expectedCategories = of("Leadership & Gouvernance", "Stratégie & investissement", "Législation, politique & conformité",
                "Ressource Humaine / Main d’Oeuvre", "Standards/ Normes & Interoperabilité", "Infrastructure", "Services & Applications");

        List<CategoryTranslation> categories = repository.findByLanguageId(LanguageCode.fr);
        List<String> categoryNames = categories.stream().map(c -> c.getName()).sorted(Comparator.naturalOrder()).collect(Collectors.toList());

        assertTrue(expectedCategories.containsAll(categoryNames));
    }
}