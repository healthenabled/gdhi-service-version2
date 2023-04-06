package it.gdhi.repository;

import it.gdhi.model.RegionalIndicatorData;
import it.gdhi.model.id.RegionalIndicatorId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

@Repository
public interface IRegionalIndicatorDataRepository extends JpaRepository<RegionalIndicatorData, RegionalIndicatorId> {
    @Modifying
    void deleteByRegionalIndicatorIdRegionIdAndRegionalIndicatorIdYear(String regionId, String currentYear);
}
