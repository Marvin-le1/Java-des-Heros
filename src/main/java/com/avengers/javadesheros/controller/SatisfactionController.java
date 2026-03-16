package com.avengers.javadesheros.controller;

import com.avengers.javadesheros.model.Satisfaction;
import com.avengers.javadesheros.repository.CivilRepository;
import com.avengers.javadesheros.repository.MissionRepository;
import com.avengers.javadesheros.service.SatisfactionService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/satisfactions")
public class SatisfactionController {

    private final SatisfactionService satisfactionService;
    private final MissionRepository missionRepository;
    private final CivilRepository civilRepository;

    public SatisfactionController(SatisfactionService satisfactionService,
                                  MissionRepository missionRepository,
                                  CivilRepository civilRepository) {
        this.satisfactionService = satisfactionService;
        this.missionRepository = missionRepository;
        this.civilRepository = civilRepository;
    }

    // ---- Liste générale ----
    @GetMapping
    public String list(Model model) {
        model.addAttribute("satisfactions", satisfactionService.findAll());
        return "satisfactions/list";
    }

    // ---- Détail ----
    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model) {
        model.addAttribute("satisfaction", satisfactionService.findById(id));
        return "satisfactions/detail";
    }

    // ---- Formulaire nouvelle évaluation ----
    @GetMapping("/nouveau")
    @PreAuthorize("hasAnyAuthority('Gestionnaire Satisfaction','Maitre Supreme')")
    public String nouveauForm(@RequestParam(required = false) Long missionId, Model model) {
        model.addAttribute("satisfaction", new Satisfaction());
        model.addAttribute("missions", missionRepository.findAll());
        model.addAttribute("civils", civilRepository.findAll());
        model.addAttribute("missionPreselect", missionId);
        model.addAttribute("isNew", true);
        return "satisfactions/form";
    }

    // ---- Sauvegarder ----
    @PostMapping("/sauvegarder")
    @PreAuthorize("hasAnyAuthority('Gestionnaire Satisfaction','Maitre Supreme')")
    public String sauvegarder(@Valid @ModelAttribute("satisfaction") Satisfaction satisfaction,
                              BindingResult result,
                              @RequestParam Long missionId,
                              @RequestParam(required = false) Long civilId,
                              RedirectAttributes flash,
                              Model model) {
        if (result.hasErrors()) {
            model.addAttribute("missions", missionRepository.findAll());
            model.addAttribute("civils", civilRepository.findAll());
            model.addAttribute("isNew", true);
            return "satisfactions/form";
        }
        try {
            satisfactionService.save(satisfaction, missionId, civilId);
            flash.addFlashAttribute("success", "Évaluation enregistrée !");
        } catch (Exception e) {
            flash.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/satisfactions";
    }

    // ---- Supprimer ----
    @PostMapping("/{id}/supprimer")
    @PreAuthorize("hasAuthority('Maitre Supreme')")
    public String supprimer(@PathVariable Long id, RedirectAttributes flash) {
        try {
            satisfactionService.deleteById(id);
            flash.addFlashAttribute("success", "Évaluation supprimée.");
        } catch (Exception e) {
            flash.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/satisfactions";
    }
}
