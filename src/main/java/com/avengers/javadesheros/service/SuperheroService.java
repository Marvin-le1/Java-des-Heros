package com.avengers.javadesheros.service;

import com.avengers.javadesheros.model.Superhero;
import com.avengers.javadesheros.model.Civil;
import com.avengers.javadesheros.repository.SuperheroRepository;
import com.avengers.javadesheros.repository.CivilRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class SuperheroService {

    private final SuperheroRepository superheroRepository;
    private final CivilRepository civilRepository;

    public SuperheroService(SuperheroRepository superheroRepository,
                            CivilRepository civilRepository) {
        this.superheroRepository = superheroRepository;
        this.civilRepository = civilRepository;
    }

    // ---- CRUD ----

    public List<Superhero> findAll() {
        return superheroRepository.findAll();
    }

    public Superhero findById(Long id) {
        return superheroRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Super-héros introuvable : " + id));
    }

    public List<Superhero> search(String query) {
        if (query == null || query.isBlank()) return findAll();
        return superheroRepository.search(query.trim());
    }

    public Superhero save(Superhero superhero, Long civilId) {
        Civil civil = civilRepository.findById(civilId)
            .orElseThrow(() -> new EntityNotFoundException("Civil introuvable : " + civilId));

        // Vérifie que ce civil n'est pas déjà l'identité d'un autre héros
        if (superheroRepository.existsByIdentiteSecreteId(civilId)
                && (superhero.getId() == null
                    || !superhero.getIdentiteSecrete().getId().equals(civilId))) {
            throw new IllegalArgumentException("Ce civil est déjà l'identité secrète d'un autre héros.");
        }

        superhero.setIdentiteSecrete(civil);
        return superheroRepository.save(superhero);
    }

    public void deleteById(Long id) {
        if (!superheroRepository.existsById(id)) {
            throw new EntityNotFoundException("Super-héros introuvable : " + id);
        }
        superheroRepository.deleteById(id);
    }

    // ---- Classement ----

    public List<Superhero> getTopHeros() {
        return superheroRepository.findTop10ByOrderByScoreDesc();
    }

    /**
     * Met à jour le score d'un héros (appelé par SatisfactionService).
     */
    public void updateScore(Long heroId, Double nouveauScore) {
        Superhero hero = findById(heroId);
        hero.setScore(nouveauScore);
        superheroRepository.save(hero);
    }
}
