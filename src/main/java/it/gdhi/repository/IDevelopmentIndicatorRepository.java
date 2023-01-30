package it.gdhi.repository;

import it.gdhi.model.DevelopmentIndicator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface IDevelopmentIndicatorRepository extends JpaRepository<DevelopmentIndicator, Long> {

    Optional<DevelopmentIndicator> findByCountryId(String countryCode);
}