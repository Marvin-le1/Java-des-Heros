package com.avengers.javadesheros.controller;

import com.avengers.javadesheros.model.Incident;
import com.avengers.javadesheros.repository.CivilRepository;
import com.avengers.javadesheros.repository.OrganisationRepository;
import com.avengers.javadesheros.repository.SuperheroRepository;
import com.avengers.javadesheros.service.IncidentService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/incidents")
public class IncidentController {

    private final IncidentService incidentService;
    private final CivilRepository civilRepository;
    private final OrganisationRepository organisationRepository;
    private final SuperheroRepository superheroRepository;

    public IncidentController(IncidentService incidentService,
                              CivilRepository civilRepository,
                              OrganisationRepository organisationRepository,
                              SuperheroRepository superheroRepository) {
        this.incidentService = incidentService;
        this.civilRepository = civilRepository;
        this.organisationRepository = organisationRepository;
        this.superheroRepository = superheroRepository;
    }

    // ---- Liste ----
    @GetMapping
    public String list(@RequestParam(required = false) String q, Model model) {
        model.addAttribute("incidents", incidentService.search(q));
        model.addAttribute("q", q);
        return "incidents/list";
    }

    // ---- Détail ----
    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model) {
        model.addAttribute("incident", incidentService.findById(id));
        return "incidents/detail";
    }

    // ---- Formulaire ajout ----
    @GetMapping("/nouveau")
    @PreAuthorize("hasAnyRole('ROLE_INCIDENT','ROLE_MAITRE_SUPREME')")
    public String nouveauForm(Model model) {
        model.addAttribute("incident", new Incident());
        populateFormModel(model);
        model.addAttribute("isNew", true);
        return "incidents/form";
    }

    // ---- Formulaire édition ----
    @GetMapping("/{id}/modifier")
    @PreAuthorize("hasAnyRole('ROLE_INCIDENT','ROLE_MAITRE_SUPREME')")
    public String modifierForm(@PathVariable Long id, Model model) {
        model.addAttribute("incident", incidentService.findById(id));
        populateFormModel(model);
        model.addAttribute("isNew", false);
        return "incidents/form";
    }

    // ---- Sauvegarder ----
    @PostMapping("/sauvegarder")
    @PreAuthorize("hasAnyRole('ROLE_INCIDENT','ROLE_MAITRE_SUPREME')")
    public String sauvegarder(@Valid @ModelAttribute("incident") Incident incident,
                              BindingResult result,
                              @RequestParam(required = false) Long declarantCivilId,
                              @RequestParam(required = false) Long declarantOrganisationId,
                              @RequestParam(required = false) Long declarantHeroId,
                              RedirectAttributes flash,
                              Model model) {
        if (result.hasErrors()) {
            populateFormModel(model);
            model.addAttribute("isNew", incident.getId() == null);
            return "incidents/form";
        }
        try {
            incidentService.save(incident, declarantCivilId, declarantOrganisationId, declarantHeroId);
            flash.addFlashAttribute("success", "Incident enregistré avec succès !");
        } catch (Exception e) {
            flash.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/incidents";
    }

    // ---- Supprimer ----
    @PostMapping("/{id}/supprimer")
    @PreAuthorize("hasRole('ROLE_MAITRE_SUPREME')")
    public String supprimer(@PathVariable Long id, RedirectAttributes flash) {
        try {
            incidentService.deleteById(id);
            flash.addFlashAttribute("success", "Incident supprimé.");
        } catch (Exception e) {
            flash.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/incidents";
    }

    private void populateFormModel(Model model) {
        model.addAttribute("civils", civilRepository.findAll());
        model.addAttribute("organisations", organisationRepository.findAll());
        model.addAttribute("superheros", superheroRepository.findAll());
        model.addAttribute("statuts", Incident.Statut.values());
    }
}
