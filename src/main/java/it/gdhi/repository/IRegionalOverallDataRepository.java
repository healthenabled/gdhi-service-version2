package it.gdhi.repository;

import java.util.List;

import it.gdhi.model.RegionalOverallData;
import it.gdhi.model.id.RegionalOverallId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface IRegionalOverallDataRepository extends JpaRepository<RegionalOverallData, RegionalOverallId> {

    @Modifying
    void deleteByRegionalOverallIdRegionIdAndRegionalOverallIdYear(String Region, String currentYear);

    RegionalOverallData findByRegionalOverallIdRegionIdAndRegionalOverallIdYear(String region, String year);

    @Query(value = "SELECT year from regions.regional_overall_data r where" +
            " r.region_id = UPPER(:regionId) " +
            " order by r.updated_at DESC LIMIT :limit", nativeQuery = true)
    List<String> findByRegionIdOrderByUpdatedAtDesc(@Param("regionId") String regionId, @Param("limit") Integer limit);
}