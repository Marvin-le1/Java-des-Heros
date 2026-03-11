package com.avengers.javadesheros.service;

import com.avengers.javadesheros.model.Civil;
import com.avengers.javadesheros.model.Organisation;
import com.avengers.javadesheros.repository.CivilRepository;
import com.avengers.javadesheros.repository.OrganisationRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class OrganisationService {

    private final OrganisationRepository organisationRepository;
    private final CivilRepository civilRepository;

    public OrganisationService(OrganisationRepository organisationRepository,
                               CivilRepository civilRepository) {
        this.organisationRepository = organisationRepository;
        this.civilRepository = civilRepository;
    }

    // ---- CRUD ----

    public List<Organisation> findAll() {
        return organisationRepository.findAll();
    }

    public Organisation findById(Long id) {
        return organisationRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Organisation introuvable : " + id));
    }

    public List<Organisation> search(String query) {
        if (query == null || query.isBlank()) return findAll();
        return organisationRepository.search(query.trim());
    }

    public Organisation save(Organisation organisation, Long dirigeantId) {
        if (dirigeantId != null) {
            Civil dirigeant = civilRepository.findById(dirigeantId)
                .orElseThrow(() -> new EntityNotFoundException("Civil introuvable : " + dirigeantId));
            organisation.setDirigeant(dirigeant);
        } else {
            organisation.setDirigeant(null);
        }
        return organisationRepository.save(organisation);
    }

    public void deleteById(Long id) {
        if (!organisationRepository.existsById(id)) {
            throw new EntityNotFoundException("Organisation introuvable : " + id);
        }
        organisationRepository.deleteById(id);
    }
}
