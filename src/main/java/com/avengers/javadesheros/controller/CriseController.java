package com.avengers.javadesheros.controller;

import com.avengers.javadesheros.model.Crise;
import com.avengers.javadesheros.repository.MissionRepository;
import com.avengers.javadesheros.service.CriseService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/crises")
public class CriseController {

    private final CriseService criseService;
    private final MissionRepository missionRepository;

    public CriseController(CriseService criseService,
                           MissionRepository missionRepository) {
        this.criseService = criseService;
        this.missionRepository = missionRepository;
    }

    // ---- Liste ----
    @GetMapping
    public String list(@RequestParam(required = false) String q, Model model) {
        model.addAttribute("crises", criseService.search(q));
        model.addAttribute("q", q);
        return "crises/list";
    }

    // ---- Détail ----
    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model) {
        model.addAttribute("crise", criseService.findById(id));
        return "crises/detail";
    }

    // ---- Formulaire ajout ----
    @GetMapping("/nouveau")
    @PreAuthorize("hasAnyAuthority('Gestionnaire Crises','Maitre Supreme')")
    public String nouveauForm(Model model) {
        model.addAttribute("crise", new Crise());
        populateFormModel(model);
        model.addAttribute("isNew", true);
        return "crises/form";
    }

    // ---- Formulaire édition ----
    @GetMapping("/{id}/modifier")
    @PreAuthorize("hasAnyAuthority('Gestionnaire Crises','Maitre Supreme')")
    public String modifierForm(@PathVariable Long id, Model model) {
        model.addAttribute("crise", criseService.findById(id));
        populateFormModel(model);
        model.addAttribute("isNew", false);
        return "crises/form";
    }

    // ---- Sauvegarder ----
    @PostMapping("/sauvegarder")
    @PreAuthorize("hasAnyAuthority('Gestionnaire Crises','Maitre Supreme')")
    public String sauvegarder(@Valid @ModelAttribute("crise") Crise crise,
                              BindingResult result,
                              @RequestParam(required = false) Long missionId,
                              RedirectAttributes flash,
                              Model model) {
        if (result.hasErrors()) {
            populateFormModel(model);
            model.addAttribute("isNew", crise.getId() == null);
            return "crises/form";
        }
        try {
            criseService.save(crise, missionId);
            flash.addFlashAttribute("success", "Crise enregistrée avec succès !");
        } catch (Exception e) {
            flash.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/crises";
    }

    // ---- Supprimer ----
    @PostMapping("/{id}/supprimer")
    @PreAuthorize("hasAuthority('Maitre Supreme')")
    public String supprimer(@PathVariable Long id, RedirectAttributes flash) {
        try {
            criseService.deleteById(id);
            flash.addFlashAttribute("success", "Crise supprimée.");
        } catch (Exception e) {
            flash.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/crises";
    }

    private void populateFormModel(Model model) {
        model.addAttribute("missions", missionRepository.findAll());
        model.addAttribute("typesCrise", Crise.TypeCrise.values());
        model.addAttribute("niveauxAlerte", Crise.NiveauAlerte.values());
        model.addAttribute("statuts", Crise.Statut.values());
    }
}
