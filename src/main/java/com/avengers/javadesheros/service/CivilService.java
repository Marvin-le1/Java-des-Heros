package com.avengers.javadesheros.service;

import com.avengers.javadesheros.model.Civil;
import com.avengers.javadesheros.repository.CivilRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CivilService {

    private final CivilRepository civilRepository;

    public CivilService(CivilRepository civilRepository) {
        this.civilRepository = civilRepository;
    }

    // ---- CRUD ----

    public List<Civil> findAll() {
        return civilRepository.findAll();
    }

    public Civil findById(Long id) {
        return civilRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Civil introuvable : " + id));
    }

    public List<Civil> search(String query) {
        if (query == null || query.isBlank()) return findAll();
        return civilRepository.search(query.trim());
    }

    public Civil save(Civil civil) {
        return civilRepository.save(civil);
    }

    public void deleteById(Long id) {
        if (!civilRepository.existsById(id)) {
            throw new EntityNotFoundException("Civil introuvable : " + id);
        }
        civilRepository.deleteById(id);
    }
}
