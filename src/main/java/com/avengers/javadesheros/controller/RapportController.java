package com.avengers.javadesheros.controller;

import com.avengers.javadesheros.model.Rapport;
import com.avengers.javadesheros.repository.CivilRepository;
import com.avengers.javadesheros.repository.CriseRepository;
import com.avengers.javadesheros.repository.MissionRepository;
import com.avengers.javadesheros.repository.SuperheroRepository;
import com.avengers.javadesheros.repository.SupervillainRepository;
import com.avengers.javadesheros.service.RapportService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/rapports")
public class RapportController {

    private final RapportService rapportService;
    private final MissionRepository missionRepository;
    private final CivilRepository civilRepository;
    private final SuperheroRepository superheroRepository;
    private final SupervillainRepository supervillainRepository;
    private final CriseRepository criseRepository;

    public RapportController(RapportService rapportService,
                             MissionRepository missionRepository,
                             CivilRepository civilRepository,
                             SuperheroRepository superheroRepository,
                             SupervillainRepository supervillainRepository,
                             CriseRepository criseRepository) {
        this.rapportService = rapportService;
        this.missionRepository = missionRepository;
        this.civilRepository = civilRepository;
        this.superheroRepository = superheroRepository;
        this.supervillainRepository = supervillainRepository;
        this.criseRepository = criseRepository;
    }

    // ---- Liste ----
    @GetMapping
    public String list(@RequestParam(required = false) String q,
                       @RequestParam(required = false) String resultat,
                       Model model) {
        List<Rapport> rapports;
        if (resultat != null && !resultat.isBlank()) {
            rapports = rapportService.findByResultat(Rapport.Resultat.valueOf(resultat));
        } else {
            rapports = rapportService.search(q);
        }

        model.addAttribute("rapports", rapports);
        model.addAttribute("q", q);
        model.addAttribute("resultatFiltre", resultat);
        model.addAttribute("resultats", Rapport.Resultat.values());
        return "rapports/list";
    }

    // ---- Détail ----
    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model) {
        model.addAttribute("rapport", rapportService.findById(id));
        return "rapports/detail";
    }

    // ---- Formulaire ajout ----
    @GetMapping({"/nouveau", "/nouvelle"})
    @PreAuthorize("hasAnyAuthority('Gestionnaire Rapports','Maitre Supreme')")
    public String nouveauForm(@RequestParam(required = false) Long missionId, Model model) {
        model.addAttribute("rapport", new Rapport());
        model.addAttribute("missionPreselect", missionId);
        populateFormModel(model);
        model.addAttribute("isNew", true);
        return "rapports/form";
    }

    // ---- Formulaire édition ----
    @GetMapping("/{id}/modifier")
    @PreAuthorize("hasAnyAuthority('Gestionnaire Rapports','Maitre Supreme')")
    public String modifierForm(@PathVariable Long id, Model model) {
        model.addAttribute("rapport", rapportService.findById(id));
        populateFormModel(model);
        model.addAttribute("isNew", false);
        return "rapports/form";
    }

    // ---- Sauvegarder ----
    @PostMapping("/sauvegarder")
    @PreAuthorize("hasAnyAuthority('Gestionnaire Rapports','Maitre Supreme')")
    public String sauvegarder(@Valid @ModelAttribute("rapport") Rapport rapport,
                              BindingResult result,
                              @RequestParam(required = false) Long missionId,
                              @RequestParam(required = false) Long interlocuteurCivilId,
                              @RequestParam(required = false) Long interlocuteurHeroId,
                              @RequestParam(required = false) List<Long> supervillainIds,
                              @RequestParam(required = false) List<Long> nouveauCivilIds,
                              @RequestParam(required = false) List<Long> criseIds,
                              RedirectAttributes flash,
                              Model model) {
        if (result.hasErrors()) {
            populateFormModel(model);
            model.addAttribute("missionPreselect", missionId);
            model.addAttribute("isNew", rapport.getId() == null);
            return "rapports/form";
        }

        try {
            rapportService.save(
                rapport,
                missionId,
                interlocuteurCivilId,
                interlocuteurHeroId,
                supervillainIds,
                nouveauCivilIds,
                criseIds
            );
            flash.addFlashAttribute("success", "Rapport enregistré avec succès !");
        } catch (Exception e) {
            flash.addFlashAttribute("error", e.getMessage());
            return "redirect:" + (rapport.getId() != null ? "/rapports/" + rapport.getId() + "/modifier" : "/rapports/nouveau");
        }

        return "redirect:/rapports";
    }

    // ---- Supprimer ----
    @PostMapping("/{id}/supprimer")
    @PreAuthorize("hasAuthority('Maitre Supreme')")
    public String supprimer(@PathVariable Long id, RedirectAttributes flash) {
        try {
            rapportService.deleteById(id);
            flash.addFlashAttribute("success", "Rapport supprimé.");
        } catch (Exception e) {
            flash.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/rapports";
    }

    private void populateFormModel(Model model) {
        model.addAttribute("missions", missionRepository.findAll());
        model.addAttribute("civils", civilRepository.findAll());
        model.addAttribute("superheros", superheroRepository.findAll());
        model.addAttribute("supervilains", supervillainRepository.findAll());
        model.addAttribute("crises", criseRepository.findAll());
        model.addAttribute("resultats", Rapport.Resultat.values());
    }
}
