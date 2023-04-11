package it.gdhi.repository;

import it.gdhi.model.RegionalOverallData;
import it.gdhi.model.id.RegionalOverallId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

@Repository
public interface IRegionalOverallDataRepository extends JpaRepository<RegionalOverallData, RegionalOverallId> {

    @Modifying
    void deleteByRegionalOverallIdRegionIdAndRegionalOverallIdYear(String Region, String currentYear);

}