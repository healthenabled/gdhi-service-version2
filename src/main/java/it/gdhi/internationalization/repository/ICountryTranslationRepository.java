package it.gdhi.internationalization.repository;

import it.gdhi.internationalization.model.CountryTranslation;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

public interface ICountryTranslationRepository extends Repository<CountryTranslation, Long> {

    @Query("SELECT c.name FROM CountryTranslation c " +
            "WHERE c.id.languageId = :languageId and c.id.countryId = :countryId")
    String findTranslationForLanguage(@Param("languageId") String languageId,
                                                         @Param("countryId") String countryId);
}
