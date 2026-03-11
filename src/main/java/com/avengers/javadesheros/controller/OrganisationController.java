package com.avengers.javadesheros.controller;

import com.avengers.javadesheros.model.Organisation;
import com.avengers.javadesheros.repository.CivilRepository;
import com.avengers.javadesheros.service.OrganisationService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/organisations")
public class OrganisationController {

    private final OrganisationService organisationService;
    private final CivilRepository civilRepository;

    public OrganisationController(OrganisationService organisationService,
                                  CivilRepository civilRepository) {
        this.organisationService = organisationService;
        this.civilRepository = civilRepository;
    }

    // ---- Liste ----
    @GetMapping
    public String list(@RequestParam(required = false) String q, Model model) {
        model.addAttribute("organisations", organisationService.search(q));
        model.addAttribute("q", q);
        return "organisations/list";
    }

    // ---- Détail ----
    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model) {
        model.addAttribute("organisation", organisationService.findById(id));
        return "organisations/detail";
    }

    // ---- Formulaire ajout ----
    @GetMapping("/nouveau")
    @PreAuthorize("hasAnyRole('ROLE_ORGANISATION','ROLE_MAITRE_SUPREME')")
    public String nouveauForm(Model model) {
        model.addAttribute("organisation", new Organisation());
        model.addAttribute("civils", civilRepository.findAll());
        model.addAttribute("isNew", true);
        return "organisations/form";
    }

    // ---- Formulaire édition ----
    @GetMapping("/{id}/modifier")
    @PreAuthorize("hasAnyRole('ROLE_ORGANISATION','ROLE_MAITRE_SUPREME')")
    public String modifierForm(@PathVariable Long id, Model model) {
        model.addAttribute("organisation", organisationService.findById(id));
        model.addAttribute("civils", civilRepository.findAll());
        model.addAttribute("isNew", false);
        return "organisations/form";
    }

    // ---- Sauvegarder ----
    @PostMapping("/sauvegarder")
    @PreAuthorize("hasAnyRole('ROLE_ORGANISATION','ROLE_MAITRE_SUPREME')")
    public String sauvegarder(@Valid @ModelAttribute("organisation") Organisation organisation,
                              BindingResult result,
                              @RequestParam(required = false) Long dirigeantId,
                              RedirectAttributes flash,
                              Model model) {
        if (result.hasErrors()) {
            model.addAttribute("civils", civilRepository.findAll());
            model.addAttribute("isNew", organisation.getId() == null);
            return "organisations/form";
        }
        try {
            organisationService.save(organisation, dirigeantId);
            flash.addFlashAttribute("success", "Organisation enregistrée avec succès !");
        } catch (Exception e) {
            flash.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/organisations";
    }

    // ---- Supprimer ----
    @PostMapping("/{id}/supprimer")
    @PreAuthorize("hasRole('ROLE_MAITRE_SUPREME')")
    public String supprimer(@PathVariable Long id, RedirectAttributes flash) {
        try {
            organisationService.deleteById(id);
            flash.addFlashAttribute("success", "Organisation supprimée.");
        } catch (Exception e) {
            flash.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/organisations";
    }
}
