package com.avengers.javadesheros.repository;

import com.avengers.javadesheros.model.Rapport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RapportRepository extends JpaRepository<Rapport, Long> {

    List<Rapport> findByResultat(Rapport.Resultat resultat);

    Optional<Rapport> findByMissionId(Long missionId);

    List<Rapport> findAllByOrderByDateRapportDesc();

    @Query("""
        SELECT r
        FROM Rapport r
        LEFT JOIN r.mission m
        WHERE LOWER(m.titre) LIKE LOWER(CONCAT('%', :q, '%'))
           OR LOWER(COALESCE(r.detailIntervention, '')) LIKE LOWER(CONCAT('%', :q, '%'))
           OR LOWER(COALESCE(r.degats, '')) LIKE LOWER(CONCAT('%', :q, '%'))
        ORDER BY r.dateRapport DESC
    """)
    List<Rapport> search(@Param("q") String query);
}
