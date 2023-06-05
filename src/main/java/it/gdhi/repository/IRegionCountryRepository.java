package it.gdhi.repository;
import it.gdhi.model.RegionCountry;
import it.gdhi.model.RegionCountryId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IRegionCountryRepository extends JpaRepository<RegionCountry, RegionCountryId> {

    RegionCountry findByRegionCountryIdCountryId(String countryId);

    @Query(value = "SELECT country_id from regions.regions_countries  where" +
            " region_id = UPPER(:regionId) ", nativeQuery = true)
    List<String> findByRegionCountryIdRegionId(@Param("regionId")String regionId);
}
