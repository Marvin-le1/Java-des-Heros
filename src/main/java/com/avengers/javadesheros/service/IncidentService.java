package com.avengers.javadesheros.service;

import com.avengers.javadesheros.model.Civil;
import com.avengers.javadesheros.model.Incident;
import com.avengers.javadesheros.model.Organisation;
import com.avengers.javadesheros.model.Superhero;
import com.avengers.javadesheros.repository.CivilRepository;
import com.avengers.javadesheros.repository.IncidentRepository;
import com.avengers.javadesheros.repository.OrganisationRepository;
import com.avengers.javadesheros.repository.SuperheroRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class IncidentService {

    private final IncidentRepository incidentRepository;
    private final CivilRepository civilRepository;
    private final OrganisationRepository organisationRepository;
    private final SuperheroRepository superheroRepository;

    public IncidentService(IncidentRepository incidentRepository,
                           CivilRepository civilRepository,
                           OrganisationRepository organisationRepository,
                           SuperheroRepository superheroRepository) {
        this.incidentRepository = incidentRepository;
        this.civilRepository = civilRepository;
        this.organisationRepository = organisationRepository;
        this.superheroRepository = superheroRepository;
    }

    // ---- CRUD ----

    public List<Incident> findAll() {
        return incidentRepository.findAll();
    }

    public Incident findById(Long id) {
        return incidentRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Incident introuvable : " + id));
    }

    public List<Incident> search(String query) {
        if (query == null || query.isBlank()) return findAll();
        return incidentRepository.search(query.trim());
    }

    public Incident save(Incident incident, Long declarantCivilId,
                         Long declarantOrganisationId, Long declarantHeroId) {
        if (declarantCivilId != null) {
            Civil civil = civilRepository.findById(declarantCivilId)
                .orElseThrow(() -> new EntityNotFoundException("Civil introuvable : " + declarantCivilId));
            incident.setDeclarantCivil(civil);
        } else {
            incident.setDeclarantCivil(null);
        }

        if (declarantOrganisationId != null) {
            Organisation org = organisationRepository.findById(declarantOrganisationId)
                .orElseThrow(() -> new EntityNotFoundException("Organisation introuvable : " + declarantOrganisationId));
            incident.setDeclarantOrganisation(org);
        } else {
            incident.setDeclarantOrganisation(null);
        }

        if (declarantHeroId != null) {
            Superhero hero = superheroRepository.findById(declarantHeroId)
                .orElseThrow(() -> new EntityNotFoundException("Super-héros introuvable : " + declarantHeroId));
            incident.setDeclarantHero(hero);
        } else {
            incident.setDeclarantHero(null);
        }

        return incidentRepository.save(incident);
    }

    public void deleteById(Long id) {
        if (!incidentRepository.existsById(id)) {
            throw new EntityNotFoundException("Incident introuvable : " + id);
        }
        incidentRepository.deleteById(id);
    }
}
