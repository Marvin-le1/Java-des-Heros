package com.avengers.javadesheros.controller;

import com.avengers.javadesheros.model.Litige;
import com.avengers.javadesheros.repository.CivilRepository;
import com.avengers.javadesheros.repository.MissionRepository;
import com.avengers.javadesheros.repository.SuperheroRepository;
import com.avengers.javadesheros.service.LitigeService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/litiges")
public class LitigeController {

    private final LitigeService litigeService;
    private final MissionRepository missionRepository;
    private final CivilRepository civilRepository;
    private final SuperheroRepository superheroRepository;

    public LitigeController(LitigeService litigeService,
                            MissionRepository missionRepository,
                            CivilRepository civilRepository,
                            SuperheroRepository superheroRepository) {
        this.litigeService = litigeService;
        this.missionRepository = missionRepository;
        this.civilRepository = civilRepository;
        this.superheroRepository = superheroRepository;
    }

    // ---- Liste ----
    @GetMapping
    public String list(@RequestParam(required = false) String q, Model model) {
        model.addAttribute("litiges", litigeService.search(q));
        model.addAttribute("q", q);
        return "litiges/list";
    }

    // ---- Détail ----
    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model) {
        model.addAttribute("litige", litigeService.findById(id));
        return "litiges/detail";
    }

    // ---- Formulaire ajout ----
    @GetMapping("/nouveau")
    @PreAuthorize("hasAnyAuthority('Gestionnaire Litiges','Maitre Supreme')")
    public String nouveauForm(@RequestParam(required = false) Long missionId, Model model) {
        model.addAttribute("litige", new Litige());
        populateFormModel(model);
        model.addAttribute("missionPreselect", missionId);
        model.addAttribute("isNew", true);
        return "litiges/form";
    }

    // ---- Formulaire édition ----
    @GetMapping("/{id}/modifier")
    @PreAuthorize("hasAnyAuthority('Gestionnaire Litiges','Maitre Supreme')")
    public String modifierForm(@PathVariable Long id, Model model) {
        model.addAttribute("litige", litigeService.findById(id));
        populateFormModel(model);
        model.addAttribute("isNew", false);
        return "litiges/form";
    }

    // ---- Sauvegarder ----
    @PostMapping("/sauvegarder")
    @PreAuthorize("hasAnyAuthority('Gestionnaire Litiges','Maitre Supreme')")
    public String sauvegarder(@Valid @ModelAttribute("litige") Litige litige,
                              BindingResult result,
                              @RequestParam(required = false) Long missionId,
                              @RequestParam(required = false) Long plaignantId,
                              @RequestParam(required = false) Long superheroId,
                              RedirectAttributes flash,
                              Model model) {
        if (result.hasErrors()) {
            populateFormModel(model);
            model.addAttribute("isNew", litige.getId() == null);
            return "litiges/form";
        }
        try {
            litigeService.save(litige, missionId, plaignantId, superheroId);
            flash.addFlashAttribute("success", "Litige enregistré avec succès !");
        } catch (Exception e) {
            flash.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/litiges";
    }

    // ---- Supprimer ----
    @PostMapping("/{id}/supprimer")
    @PreAuthorize("hasAuthority('Maitre Supreme')")
    public String supprimer(@PathVariable Long id, RedirectAttributes flash) {
        try {
            litigeService.deleteById(id);
            flash.addFlashAttribute("success", "Litige supprimé.");
        } catch (Exception e) {
            flash.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/litiges";
    }

    private void populateFormModel(Model model) {
        model.addAttribute("missions", missionRepository.findAll());
        model.addAttribute("civils", civilRepository.findAll());
        model.addAttribute("superheros", superheroRepository.findAll());
        model.addAttribute("types", Litige.TypeLitige.values());
        model.addAttribute("statuts", Litige.Statut.values());
    }
}
