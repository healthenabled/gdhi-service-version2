package it.gdhi.repository;

import java.util.List;

import it.gdhi.model.RegionalCategoryData;
import it.gdhi.model.id.RegionalCategoryId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

@Repository
public interface IRegionalCategoryDataRepository extends JpaRepository<RegionalCategoryData, RegionalCategoryId> {

    @Modifying
    void deleteByRegionalCategoryIdRegionIdAndRegionalCategoryIdYear(String regionId, String currentYear);

    List<RegionalCategoryData> findByRegionalCategoryIdRegionIdAndRegionalCategoryIdYearOrderByRegionalCategoryIdCategoryId(String regionId,
                                                                                                                            String year);

    List<RegionalCategoryData> findDistinctByRegionalCategoryIdRegionIdOrderByRegionalCategoryIdCategoryId(String regionId);

    RegionalCategoryData findByRegionalCategoryIdRegionIdAndRegionalCategoryIdYearAndRegionalCategoryIdCategoryIdOrderByRegionalCategoryIdCategoryId(String regionId, String year, Integer categoryId);
}
