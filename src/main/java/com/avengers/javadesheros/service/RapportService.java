package com.avengers.javadesheros.service;

import com.avengers.javadesheros.model.*;
import com.avengers.javadesheros.repository.*;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class RapportService {

    private final RapportRepository rapportRepository;
    private final MissionRepository missionRepository;
    private final CivilRepository civilRepository;
    private final SuperheroRepository superheroRepository;
    private final SupervillainRepository supervillainRepository;
    private final CriseRepository criseRepository;

    public RapportService(RapportRepository rapportRepository,
                          MissionRepository missionRepository,
                          CivilRepository civilRepository,
                          SuperheroRepository superheroRepository,
                          SupervillainRepository supervillainRepository,
                          CriseRepository criseRepository) {
        this.rapportRepository = rapportRepository;
        this.missionRepository = missionRepository;
        this.civilRepository = civilRepository;
        this.superheroRepository = superheroRepository;
        this.supervillainRepository = supervillainRepository;
        this.criseRepository = criseRepository;
    }

    // ---- CRUD ----

    public List<Rapport> findAll() {
        return rapportRepository.findAllByOrderByDateRapportDesc();
    }

    public Rapport findById(Long id) {
        return rapportRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Rapport introuvable : " + id));
    }

    public List<Rapport> search(String q) {
        if (q == null || q.isBlank()) return findAll();
        return rapportRepository.search(q.trim());
    }

    public List<Rapport> findByResultat(Rapport.Resultat resultat) {
        return rapportRepository.findByResultat(resultat);
    }

    public Rapport save(Rapport rapport,
                        Long missionId,
                        Long interlocuteurCivilId,
                        Long interlocuteurHeroId,
                        List<Long> supervillainIds,
                        List<Long> nouveauCivilIds,
                        List<Long> criseIds) {
        if (missionId == null) {
            throw new IllegalArgumentException("La mission est obligatoire pour un rapport.");
        }

        Mission mission = missionRepository.findById(missionId)
            .orElseThrow(() -> new EntityNotFoundException("Mission introuvable : " + missionId));

        rapportRepository.findByMissionId(missionId).ifPresent(existant -> {
            if (rapport.getId() == null || !existant.getId().equals(rapport.getId())) {
                throw new IllegalArgumentException("Cette mission a déjà un rapport.");
            }
        });

        rapport.setMission(mission);

        if (interlocuteurCivilId != null) {
            Civil civil = civilRepository.findById(interlocuteurCivilId)
                .orElseThrow(() -> new EntityNotFoundException("Civil introuvable : " + interlocuteurCivilId));
            rapport.setInterlocuteurCivil(civil);
        } else {
            rapport.setInterlocuteurCivil(null);
        }

        if (interlocuteurHeroId != null) {
            Superhero hero = superheroRepository.findById(interlocuteurHeroId)
                .orElseThrow(() -> new EntityNotFoundException("Super-héros introuvable : " + interlocuteurHeroId));
            rapport.setInterlocuteurHero(hero);
        } else {
            rapport.setInterlocuteurHero(null);
        }

        Set<Supervillain> supervilains = supervillainIds != null && !supervillainIds.isEmpty()
            ? new HashSet<>(supervillainRepository.findAllById(supervillainIds))
            : new HashSet<>();
        rapport.setSupervilains(supervilains);

        Set<Civil> nouveauxCivils = nouveauCivilIds != null && !nouveauCivilIds.isEmpty()
            ? new HashSet<>(civilRepository.findAllById(nouveauCivilIds))
            : new HashSet<>();
        rapport.setNouveauxCivils(nouveauxCivils);

        Set<Crise> crises = criseIds != null && !criseIds.isEmpty()
            ? new HashSet<>(criseRepository.findAllById(criseIds))
            : new HashSet<>();
        rapport.setCrises(crises);

        return rapportRepository.save(rapport);
    }

    public void deleteById(Long id) {
        if (!rapportRepository.existsById(id)) {
            throw new EntityNotFoundException("Rapport introuvable : " + id);
        }
        rapportRepository.deleteById(id);
    }
}
