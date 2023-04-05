package it.gdhi.internationalization.repository;
import it.gdhi.internationalization.model.RegionTranslation;
import it.gdhi.internationalization.model.RegionTranslationId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IRegionTranslationRepository extends JpaRepository<RegionTranslation, RegionTranslationId> {

    String findByIdRegionIdAndIdLanguageId(String regionId, String languageId);

}