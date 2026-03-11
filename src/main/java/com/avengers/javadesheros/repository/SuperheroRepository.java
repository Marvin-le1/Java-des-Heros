package com.avengers.javadesheros.repository;

import com.avengers.javadesheros.model.Superhero;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SuperheroRepository extends JpaRepository<Superhero, Long> {

    Optional<Superhero> findByNom(String nom);

    @Query("SELECT h FROM Superhero h WHERE LOWER(h.nom) LIKE LOWER(CONCAT('%',:q,'%')) OR LOWER(h.pouvoir) LIKE LOWER(CONCAT('%',:q,'%'))")
    List<Superhero> search(@Param("q") String query);

    // Top héros par score (pour le tableau de classement)
    List<Superhero> findTop10ByOrderByScoreDesc();

    boolean existsByIdentiteSecreteId(Long civilId);
}
