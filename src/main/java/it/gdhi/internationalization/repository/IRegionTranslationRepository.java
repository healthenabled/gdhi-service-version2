package it.gdhi.internationalization.repository;
import it.gdhi.internationalization.model.RegionTranslation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface IRegionTranslationRepository extends JpaRepository<RegionTranslation, Long> {

    @Query("SELECT c.region_name FROM RegionTranslation c " +
            "WHERE c.id.languageId = :languageId and c.id.regionId = :regionId")
    String findTranslationForLanguage(@Param("languageId") String languageId, @Param("regionId") String regionId);

}