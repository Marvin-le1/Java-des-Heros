package com.avengers.javadesheros.repository;

import com.avengers.javadesheros.model.Crise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CriseRepository extends JpaRepository<Crise, Long> {

    List<Crise> findByStatut(Crise.Statut statut);

    @Query("SELECT c FROM Crise c WHERE LOWER(c.titre) LIKE LOWER(CONCAT('%',:q,'%')) OR LOWER(c.description) LIKE LOWER(CONCAT('%',:q,'%'))")
    List<Crise> search(@Param("q") String query);
}
