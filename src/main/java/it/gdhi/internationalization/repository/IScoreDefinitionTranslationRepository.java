package it.gdhi.internationalization.repository;

import it.gdhi.internationalization.model.ScoreDefinitionTranslation;
import it.gdhi.utils.LanguageCode;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

public interface IScoreDefinitionTranslationRepository extends Repository<ScoreDefinitionTranslation, Long> {

    @Query("SELECT t.definition FROM ScoreDefinitionTranslation t " +
            "WHERE t.id.languageId = :languageId " +
            "and t.id.indicatorId = :indicatorId " +
            "and t.id.score = :score ")
    String findTranslationForLanguage(@Param("languageId") LanguageCode languageId,
                                      @Param("indicatorId") Integer indicatorId,
                                      @Param("score") Integer score);
}
