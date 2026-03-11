package com.avengers.javadesheros.service;

import com.avengers.javadesheros.model.Crise;
import com.avengers.javadesheros.model.Mission;
import com.avengers.javadesheros.repository.CriseRepository;
import com.avengers.javadesheros.repository.MissionRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CriseService {

    private final CriseRepository criseRepository;
    private final MissionRepository missionRepository;

    public CriseService(CriseRepository criseRepository,
                        MissionRepository missionRepository) {
        this.criseRepository = criseRepository;
        this.missionRepository = missionRepository;
    }

    // ---- CRUD ----

    public List<Crise> findAll() {
        return criseRepository.findAll();
    }

    public Crise findById(Long id) {
        return criseRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Crise introuvable : " + id));
    }

    public List<Crise> search(String query) {
        if (query == null || query.isBlank()) return findAll();
        return criseRepository.search(query.trim());
    }

    public Crise save(Crise crise, Long missionId) {
        if (missionId != null) {
            Mission mission = missionRepository.findById(missionId)
                .orElseThrow(() -> new EntityNotFoundException("Mission introuvable : " + missionId));
            crise.setMission(mission);
        } else {
            crise.setMission(null);
        }
        return criseRepository.save(crise);
    }

    public void deleteById(Long id) {
        if (!criseRepository.existsById(id)) {
            throw new EntityNotFoundException("Crise introuvable : " + id);
        }
        criseRepository.deleteById(id);
    }
}
