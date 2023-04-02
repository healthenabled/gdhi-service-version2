package it.gdhi.repository;

import it.gdhi.model.Country;
import it.gdhi.model.Region;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IRegionRepository extends JpaRepository<Region, String> {

    List<Region> findAll();
}

