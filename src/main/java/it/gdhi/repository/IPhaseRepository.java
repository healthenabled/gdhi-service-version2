package it.gdhi.repository;

import it.gdhi.model.Phase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface IPhaseRepository extends JpaRepository<Phase, Integer> {

    List<Phase> findAllByOrderByPhaseIdAsc();
}