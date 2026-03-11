package com.avengers.javadesheros.repository;

import com.avengers.javadesheros.model.Incident;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IncidentRepository extends JpaRepository<Incident, Long> {
    List<Incident> findByStatut(Incident.Statut statut);

    @Query("SELECT i FROM Incident i WHERE LOWER(i.titre) LIKE LOWER(CONCAT('%',:q,'%')) OR LOWER(i.lieu) LIKE LOWER(CONCAT('%',:q,'%'))")
    List<Incident> search(@Param("q") String query);
}
