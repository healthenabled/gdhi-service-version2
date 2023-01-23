package it.gdhi.internationalization.repository;

import it.gdhi.internationalization.model.CategoryTranslation;
import it.gdhi.utils.LanguageCode;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ICategoryTranslationRepository extends Repository<CategoryTranslation, Long> {

    @Query("SELECT c.name FROM CategoryTranslation c " +
            "WHERE c.id.languageId = :languageId and c.category.name = :categoryName")
    String findTranslationForLanguage(@Param("languageId") LanguageCode languageId,
                                      @Param("categoryName") String categoryName);

    @Query("SELECT c FROM CategoryTranslation c WHERE c.id.languageId = :languageId")
    List<CategoryTranslation> findByLanguageId(@Param("languageId") LanguageCode languageId);
}
