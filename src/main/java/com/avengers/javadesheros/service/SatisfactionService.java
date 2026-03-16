package com.avengers.javadesheros.service;

import com.avengers.javadesheros.model.Mission;
import com.avengers.javadesheros.model.Satisfaction;
import com.avengers.javadesheros.repository.CivilRepository;
import com.avengers.javadesheros.repository.MissionRepository;
import com.avengers.javadesheros.repository.SatisfactionRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class SatisfactionService {

    private final SatisfactionRepository satisfactionRepository;
    private final MissionRepository missionRepository;
    private final CivilRepository civilRepository;
    private final SuperheroService superheroService;

    public SatisfactionService(SatisfactionRepository satisfactionRepository,
                               MissionRepository missionRepository,
                               CivilRepository civilRepository,
                               SuperheroService superheroService) {
        this.satisfactionRepository = satisfactionRepository;
        this.missionRepository = missionRepository;
        this.civilRepository = civilRepository;
        this.superheroService = superheroService;
    }

    public List<Satisfaction> findAll() {
        return satisfactionRepository.findAll();
    }

    public Satisfaction findById(Long id) {
        return satisfactionRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Satisfaction introuvable : " + id));
    }

    public List<Satisfaction> findByMission(Long missionId) {
        return satisfactionRepository.findByMissionIdOrderByDate(missionId);
    }

    public Satisfaction save(Satisfaction satisfaction, Long missionId, Long civilId) {
        Mission mission = missionRepository.findById(missionId)
            .orElseThrow(() -> new IllegalArgumentException("Mission introuvable : " + missionId));

        if (mission.getStatut() != Mission.Statut.terminee) {
            throw new IllegalStateException("On ne peut évaluer qu'une mission terminée.");
        }

        satisfaction.setMission(mission);

        if (civilId != null) {
            civilRepository.findById(civilId).ifPresent(satisfaction::setCivil);
        }

        Satisfaction saved = satisfactionRepository.save(satisfaction);

        // Recalculer le score moyen du/des héros de la mission
        mission.getSuperheros().forEach(hero ->
            superheroService.updateScore(hero.getId())
        );

        return saved;
    }

    public void deleteById(Long id) {
        Satisfaction s = findById(id);
        Long missionId = s.getMission().getId();
        satisfactionRepository.deleteById(id);
        // Recalculer les scores
        missionRepository.findById(missionId).ifPresent(mission ->
            mission.getSuperheros().forEach(hero ->
                superheroService.updateScore(hero.getId())
            )
        );
    }

    public Double avgNoteByMission(Long missionId) {
        return satisfactionRepository.avgNoteByMission(missionId);
    }
}
