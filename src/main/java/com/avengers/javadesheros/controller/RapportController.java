package com.avengers.javadesheros.controller;

import com.avengers.javadesheros.model.*;
import com.avengers.javadesheros.repository.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/rapports")
public class RapportController {

    private final CivilRepository civilRepository;
    private final OrganisationRepository organisationRepository;
    private final SuperheroRepository superheroRepository;
    private final SupervillainRepository supervillainRepository;
    private final MissionRepository missionRepository;
    private final IncidentRepository incidentRepository;
    private final CriseRepository criseRepository;
    private final LitigeRepository litigeRepository;
    private final SatisfactionRepository satisfactionRepository;

    public RapportController(CivilRepository civilRepository,
                             OrganisationRepository organisationRepository,
                             SuperheroRepository superheroRepository,
                             SupervillainRepository supervillainRepository,
                             MissionRepository missionRepository,
                             IncidentRepository incidentRepository,
                             CriseRepository criseRepository,
                             LitigeRepository litigeRepository,
                             SatisfactionRepository satisfactionRepository) {
        this.civilRepository = civilRepository;
        this.organisationRepository = organisationRepository;
        this.superheroRepository = superheroRepository;
        this.supervillainRepository = supervillainRepository;
        this.missionRepository = missionRepository;
        this.incidentRepository = incidentRepository;
        this.criseRepository = criseRepository;
        this.litigeRepository = litigeRepository;
        this.satisfactionRepository = satisfactionRepository;
    }

    @GetMapping
    public String rapport(Model model) {

        // ---- Compteurs généraux ----
        model.addAttribute("nbCivils",         civilRepository.count());
        model.addAttribute("nbOrganisations",  organisationRepository.count());
        model.addAttribute("nbSuperheros",     superheroRepository.count());
        model.addAttribute("nbSupervilains",   supervillainRepository.count());

        // ---- Missions ----
        List<Mission> missions = missionRepository.findAll();
        Map<Mission.Statut, Long> missionsByStatut = missions.stream()
            .collect(Collectors.groupingBy(Mission::getStatut, Collectors.counting()));
        model.addAttribute("nbMissions",          missions.size());
        model.addAttribute("missionsByStatut",    missionsByStatut);
        model.addAttribute("missionsUrgentes",    missionRepository.findMissionsUrgentesEnCours());

        // ---- Incidents ----
        List<Incident> incidents = incidentRepository.findAll();
        Map<Incident.Statut, Long> incidentsByStatut = incidents.stream()
            .collect(Collectors.groupingBy(Incident::getStatut, Collectors.counting()));
        model.addAttribute("nbIncidents",         incidents.size());
        model.addAttribute("incidentsByStatut",   incidentsByStatut);
        model.addAttribute("incidentsEnAttente",  incidentRepository.findByStatut(Incident.Statut.en_attente));

        // ---- Crises ----
        List<Crise> crises = criseRepository.findAll();
        Map<Crise.Statut, Long> crisesByStatut = crises.stream()
            .collect(Collectors.groupingBy(Crise::getStatut, Collectors.counting()));
        model.addAttribute("nbCrises",        crises.size());
        model.addAttribute("crisesByStatut",  crisesByStatut);
        model.addAttribute("crisesOuvertes",  criseRepository.findByStatut(Crise.Statut.ouverte));

        // ---- Litiges ----
        List<Litige> litiges = litigeRepository.findAll();
        Map<Litige.Statut, Long> litigesByStatut = litiges.stream()
            .collect(Collectors.groupingBy(Litige::getStatut, Collectors.counting()));
        model.addAttribute("nbLitiges",       litiges.size());
        model.addAttribute("litigesByStatut", litigesByStatut);
        model.addAttribute("litigesOuverts",  litigeRepository.findByStatut(Litige.Statut.ouvert));

        // ---- Top héros ----
        model.addAttribute("topHeros", superheroRepository.findTop10ByOrderByScoreDesc());

        // ---- Satisfaction ----
        List<Satisfaction> satisfactions = satisfactionRepository.findAll();
        model.addAttribute("nbEvaluations", satisfactions.size());
        double avgGlobale = satisfactions.stream()
            .mapToDouble(Satisfaction::getNote)
            .average()
            .orElse(0.0);
        model.addAttribute("avgSatisfaction", Math.round(avgGlobale * 10.0) / 10.0);
        // Dernières évaluations (5 max)
        model.addAttribute("dernieresEvaluations",
            satisfactions.stream()
                .sorted((a, b) -> b.getDateEvaluation().compareTo(a.getDateEvaluation()))
                .limit(5)
                .collect(Collectors.toList()));

        return "rapports/index";
    }
}
