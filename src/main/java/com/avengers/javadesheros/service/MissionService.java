package com.avengers.javadesheros.service;

import com.avengers.javadesheros.model.Incident;
import com.avengers.javadesheros.model.Mission;
import com.avengers.javadesheros.model.Superhero;
import com.avengers.javadesheros.repository.IncidentRepository;
import com.avengers.javadesheros.repository.MissionRepository;
import com.avengers.javadesheros.repository.SuperheroRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class MissionService {

    private final MissionRepository missionRepository;
    private final IncidentRepository incidentRepository;
    private final SuperheroRepository superheroRepository;

    public MissionService(MissionRepository missionRepository,
                          IncidentRepository incidentRepository,
                          SuperheroRepository superheroRepository) {
        this.missionRepository = missionRepository;
        this.incidentRepository = incidentRepository;
        this.superheroRepository = superheroRepository;
    }

    // ---- CRUD ----

    public List<Mission> findAll() {
        return missionRepository.findAll();
    }

    public Mission findById(Long id) {
        return missionRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Mission introuvable : " + id));
    }

    public List<Mission> search(String q) {
        if (q == null || q.isBlank()) return findAll();
        return missionRepository.findByTitreContainingIgnoreCase(q.trim());
    }

    public List<Mission> findByStatut(Mission.Statut statut) {
        return missionRepository.findByStatut(statut);
    }

    public List<Mission> findMissionsUrgentes() {
        return missionRepository.findMissionsUrgentesEnCours();
    }

    /**
     * Crée ou met à jour une mission.
     * @param mission       l'entité mission
     * @param incidentId    id de l'incident d'origine (peut être null)
     * @param superheroIds  ids des héros affectés
     */
    public Mission save(Mission mission, Long incidentId, List<Long> superheroIds) {
        // Lier à l'incident si fourni
        if (incidentId != null) {
            Incident incident = incidentRepository.findById(incidentId)
                .orElseThrow(() -> new EntityNotFoundException("Incident introuvable : " + incidentId));
            mission.setIncident(incident);
            // Marquer l'incident comme transformé en mission
            incident.setStatut(Incident.Statut.transforme_en_mission);
            incidentRepository.save(incident);
        } else {
            mission.setIncident(null);
        }

        // Affecter les héros
        if (superheroIds != null && !superheroIds.isEmpty()) {
            Set<Superhero> heros = new HashSet<>(superheroRepository.findAllById(superheroIds));
            mission.setSuperheros(heros);
        } else {
            mission.setSuperheros(new HashSet<>());
        }

        return missionRepository.save(mission);
    }

    public Mission changerStatut(Long id, Mission.Statut nouveauStatut) {
        Mission mission = findById(id);
        mission.setStatut(nouveauStatut);
        return missionRepository.save(mission);
    }

    public void deleteById(Long id) {
        if (!missionRepository.existsById(id)) {
            throw new EntityNotFoundException("Mission introuvable : " + id);
        }
        missionRepository.deleteById(id);
    }
}
