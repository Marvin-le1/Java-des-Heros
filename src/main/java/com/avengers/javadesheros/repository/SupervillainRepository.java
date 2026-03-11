package com.avengers.javadesheros.repository;

import com.avengers.javadesheros.model.Supervillain;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SupervillainRepository extends JpaRepository<Supervillain, Long> {

    Optional<Supervillain> findByNom(String nom);

    @Query("SELECT v FROM Supervillain v WHERE LOWER(v.nom) LIKE LOWER(CONCAT('%',:q,'%')) OR LOWER(v.pouvoir) LIKE LOWER(CONCAT('%',:q,'%'))")
    List<Supervillain> search(@Param("q") String query);

    // Vilains les plus dangereux (pour les rapports / alertes)
    List<Supervillain> findTop10ByOrderByDegreMalveillanceDesc();

    boolean existsByIdentiteSecreteId(Long civilId);
}
