package com.avengers.javadesheros.repository;

import com.avengers.javadesheros.model.Mission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MissionRepository extends JpaRepository<Mission, Long> {

    List<Mission> findByStatut(Mission.Statut statut);

    List<Mission> findByNiveauUrgence(Mission.NiveauUrgence urgence);

    // Missions urgentes en cours (pour les alertes)
    @Query("SELECT m FROM Mission m WHERE m.statut = 'en_cours' AND m.niveauUrgence IN ('eleve','critique')")
    List<Mission> findMissionsUrgentesEnCours();

    // Missions d'un héros spécifique
    @Query("SELECT m FROM Mission m JOIN m.superheros h WHERE h.id = :heroId")
    List<Mission> findBySuperheroId(@Param("heroId") Long heroId);

    // Recherche par titre
    List<Mission> findByTitreContainingIgnoreCase(String titre);
}
