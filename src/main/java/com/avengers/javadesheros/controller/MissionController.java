package com.avengers.javadesheros.controller;

import com.avengers.javadesheros.model.Mission;
import com.avengers.javadesheros.repository.IncidentRepository;
import com.avengers.javadesheros.repository.SuperheroRepository;
import com.avengers.javadesheros.service.MissionService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/missions")
public class MissionController {

    private final MissionService missionService;
    private final IncidentRepository incidentRepository;
    private final SuperheroRepository superheroRepository;

    public MissionController(MissionService missionService,
                             IncidentRepository incidentRepository,
                             SuperheroRepository superheroRepository) {
        this.missionService = missionService;
        this.incidentRepository = incidentRepository;
        this.superheroRepository = superheroRepository;
    }

    // ---- Liste ----
    @GetMapping
    public String list(@RequestParam(required = false) String q,
                       @RequestParam(required = false) String statut,
                       Model model) {
        List<Mission> missions;
        if (statut != null && !statut.isBlank()) {
            missions = missionService.findByStatut(Mission.Statut.valueOf(statut));
        } else {
            missions = missionService.search(q);
        }
        model.addAttribute("missions", missions);
        model.addAttribute("missionsUrgentes", missionService.findMissionsUrgentes());
        model.addAttribute("q", q);
        model.addAttribute("statutFiltre", statut);
        model.addAttribute("statuts", Mission.Statut.values());
        return "missions/list";
    }

    // ---- Détail ----
    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model) {
        model.addAttribute("mission", missionService.findById(id));
        return "missions/detail";
    }

    // ---- Formulaire ajout ----
    @GetMapping("/nouvelle")
    @PreAuthorize("hasAnyAuthority('Gestionnaire Missions','Maitre Supreme')")
    public String nouveauForm(@RequestParam(required = false) Long incidentId, Model model) {
        Mission mission = new Mission();
        model.addAttribute("mission", mission);
        model.addAttribute("incidents", incidentRepository.findByStatut(
            com.avengers.javadesheros.model.Incident.Statut.en_attente));
        model.addAttribute("superheros", superheroRepository.findAll());
        model.addAttribute("natures", Mission.Nature.values());
        model.addAttribute("gravites", Mission.NiveauGravite.values());
        model.addAttribute("urgences", Mission.NiveauUrgence.values());
        model.addAttribute("incidentPreselect", incidentId);
        model.addAttribute("isNew", true);
        return "missions/form";
    }

    // ---- Formulaire édition ----
    @GetMapping("/{id}/modifier")
    @PreAuthorize("hasAnyAuthority('Gestionnaire Missions','Maitre Supreme')")
    public String modifierForm(@PathVariable Long id, Model model) {
        Mission mission = missionService.findById(id);
        model.addAttribute("mission", mission);
        model.addAttribute("incidents", incidentRepository.findAll());
        model.addAttribute("superheros", superheroRepository.findAll());
        model.addAttribute("natures", Mission.Nature.values());
        model.addAttribute("gravites", Mission.NiveauGravite.values());
        model.addAttribute("urgences", Mission.NiveauUrgence.values());
        model.addAttribute("isNew", false);
        return "missions/form";
    }

    // ---- Sauvegarder ----
    @PostMapping("/sauvegarder")
    @PreAuthorize("hasAnyAuthority('Gestionnaire Missions','Maitre Supreme')")
    public String sauvegarder(@Valid @ModelAttribute("mission") Mission mission,
                              BindingResult result,
                              @RequestParam(required = false) Long incidentId,
                              @RequestParam(required = false) List<Long> superheroIds,
                              RedirectAttributes flash,
                              Model model) {
        if (result.hasErrors()) {
            model.addAttribute("incidents", incidentRepository.findAll());
            model.addAttribute("superheros", superheroRepository.findAll());
            model.addAttribute("natures", Mission.Nature.values());
            model.addAttribute("gravites", Mission.NiveauGravite.values());
            model.addAttribute("urgences", Mission.NiveauUrgence.values());
            model.addAttribute("isNew", mission.getId() == null);
            return "missions/form";
        }
        try {
            missionService.save(mission, incidentId, superheroIds);
            flash.addFlashAttribute("success", "Mission enregistrée avec succès !");
        } catch (Exception e) {
            flash.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/missions";
    }

    // ---- Changer statut ----
    @PostMapping("/{id}/statut")
    @PreAuthorize("hasAnyAuthority('Gestionnaire Missions','Maitre Supreme')")
    public String changerStatut(@PathVariable Long id,
                                @RequestParam String statut,
                                RedirectAttributes flash) {
        try {
            missionService.changerStatut(id, Mission.Statut.valueOf(statut));
            flash.addFlashAttribute("success", "Statut mis à jour.");
        } catch (Exception e) {
            flash.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/missions/" + id;
    }

    // ---- Supprimer ----
    @PostMapping("/{id}/supprimer")
    @PreAuthorize("hasAuthority('Maitre Supreme')")
    public String supprimer(@PathVariable Long id, RedirectAttributes flash) {
        try {
            missionService.deleteById(id);
            flash.addFlashAttribute("success", "Mission supprimée.");
        } catch (Exception e) {
            flash.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/missions";
    }
}
