package it.gdhi.internationalization.repository;

import it.gdhi.internationalization.model.ScoreDefinitionTranslation;
import it.gdhi.utils.LanguageCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
@Repository
public interface IScoreDefinitionTranslationRepository extends JpaRepository<ScoreDefinitionTranslation, Long> {

    @Query("SELECT t.definition FROM ScoreDefinitionTranslation t " +
            "WHERE t.id.languageId = :languageId " +
            "and t.id.indicatorId = :indicatorId " +
            "and t.id.score = :score ")
    String findTranslationForLanguage(@Param("languageId") LanguageCode languageId,
                                      @Param("indicatorId") Integer indicatorId,
                                      @Param("score") Integer score);
}
