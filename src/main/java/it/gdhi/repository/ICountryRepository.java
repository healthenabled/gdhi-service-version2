package it.gdhi.repository;

import it.gdhi.model.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ICountryRepository extends JpaRepository<Country, Long> {

    List<Country> findAll();

    @Query("SELECT  c FROM Country c WHERE c.id = ?1")
    Country find(String id);

    Country findById(String id);

    Country findByUniqueId(UUID countryUUID);
}