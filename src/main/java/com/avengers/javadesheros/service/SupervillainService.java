package com.avengers.javadesheros.service;

import com.avengers.javadesheros.model.Civil;
import com.avengers.javadesheros.model.Supervillain;
import com.avengers.javadesheros.repository.CivilRepository;
import com.avengers.javadesheros.repository.SupervillainRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class SupervillainService {

    private final SupervillainRepository supervillainRepository;
    private final CivilRepository civilRepository;

    public SupervillainService(SupervillainRepository supervillainRepository,
                               CivilRepository civilRepository) {
        this.supervillainRepository = supervillainRepository;
        this.civilRepository = civilRepository;
    }

    // ---- CRUD ----

    public List<Supervillain> findAll() {
        return supervillainRepository.findAll();
    }

    public Supervillain findById(Long id) {
        return supervillainRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Super-vilain introuvable : " + id));
    }

    public List<Supervillain> search(String query) {
        if (query == null || query.isBlank()) return findAll();
        return supervillainRepository.search(query.trim());
    }

    /**
     * Sauvegarde un vilain.
     * @param civilId peut être null si l'identité secrète n'est pas connue.
     */
    public Supervillain save(Supervillain villain, Long civilId) {
        if (civilId != null) {
            Civil civil = civilRepository.findById(civilId)
                .orElseThrow(() -> new EntityNotFoundException("Civil introuvable : " + civilId));

            if (supervillainRepository.existsByIdentiteSecreteId(civilId)
                    && (villain.getId() == null
                        || !villain.getIdentiteSecrete().getId().equals(civilId))) {
                throw new IllegalArgumentException("Ce civil est déjà l'identité secrète d'un autre vilain.");
            }
            villain.setIdentiteSecrete(civil);
        } else {
            villain.setIdentiteSecrete(null);
        }
        return supervillainRepository.save(villain);
    }

    public void deleteById(Long id) {
        if (!supervillainRepository.existsById(id)) {
            throw new EntityNotFoundException("Super-vilain introuvable : " + id);
        }
        supervillainRepository.deleteById(id);
    }

    // ---- Classement ----

    public List<Supervillain> getTopVilains() {
        return supervillainRepository.findTop10ByOrderByDegreMalveillanceDesc();
    }

    /**
     * Incrémente le degré de malveillance (appelé lors d'un rapport de mission).
     */
    public void incrementerMalveillance(Long villainId, int points) {
        Supervillain villain = findById(villainId);
        villain.setDegreMalveillance(villain.getDegreMalveillance() + points);
        supervillainRepository.save(villain);
    }
}
