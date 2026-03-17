package com.avengers.javadesheros.service;

import com.avengers.javadesheros.model.Litige;
import com.avengers.javadesheros.repository.CivilRepository;
import com.avengers.javadesheros.repository.LitigeRepository;
import com.avengers.javadesheros.repository.MissionRepository;
import com.avengers.javadesheros.repository.SuperheroRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class LitigeService {

    private final LitigeRepository litigeRepository;
    private final MissionRepository missionRepository;
    private final CivilRepository civilRepository;
    private final SuperheroRepository superheroRepository;

    public LitigeService(LitigeRepository litigeRepository,
                         MissionRepository missionRepository,
                         CivilRepository civilRepository,
                         SuperheroRepository superheroRepository) {
        this.litigeRepository = litigeRepository;
        this.missionRepository = missionRepository;
        this.civilRepository = civilRepository;
        this.superheroRepository = superheroRepository;
    }

    public List<Litige> findAll() {
        return litigeRepository.findAll();
    }

    public List<Litige> search(String q) {
        if (q == null || q.isBlank()) return findAll();
        return litigeRepository.search(q.trim());
    }

    public Litige findById(Long id) {
        return litigeRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Litige introuvable : " + id));
    }

    public Litige save(Litige litige, Long missionId, Long plaignantId, Long superheroId) {
        if (missionId != null) {
            missionRepository.findById(missionId).ifPresent(litige::setMission);
        }
        if (plaignantId != null) {
            civilRepository.findById(plaignantId).ifPresent(litige::setPlaignant);
        }
        if (superheroId != null) {
            superheroRepository.findById(superheroId).ifPresent(litige::setSuperhero);
        }
        // Auto-clôture si résolu/classé
        if ((litige.getStatut() == Litige.Statut.resolu || litige.getStatut() == Litige.Statut.classe)
                && litige.getDateCloture() == null) {
            litige.setDateCloture(LocalDateTime.now());
        }
        return litigeRepository.save(litige);
    }

    public void deleteById(Long id) {
        if (!litigeRepository.existsById(id)) {
            throw new EntityNotFoundException("Litige introuvable : " + id);
        }
        litigeRepository.deleteById(id);
    }
}
