package it.gdhi.repository;

import it.gdhi.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ICategoryRepository extends JpaRepository<Category, Long> {

    @Query("SELECT c from Category c order by c.id")
    List<Category> findAll();

}

