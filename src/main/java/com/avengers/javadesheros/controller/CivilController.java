package com.avengers.javadesheros.controller;

import com.avengers.javadesheros.model.Civil;
import com.avengers.javadesheros.service.CivilService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/civils")
public class CivilController {

    private final CivilService civilService;

    public CivilController(CivilService civilService) {
        this.civilService = civilService;
    }

    // ---- Liste ----
    @GetMapping
    public String list(@RequestParam(required = false) String q, Model model) {
        model.addAttribute("civils", civilService.search(q));
        model.addAttribute("q", q);
        return "civils/list";
    }

    // ---- Détail ----
    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model) {
        model.addAttribute("civil", civilService.findById(id));
        return "civils/detail";
    }

    // ---- Formulaire ajout ----
    @GetMapping("/nouveau")
    @PreAuthorize("hasAnyRole('ROLE_CIVIL','ROLE_MAITRE_SUPREME')")
    public String nouveauForm(Model model) {
        model.addAttribute("civil", new Civil());
        model.addAttribute("isNew", true);
        return "civils/form";
    }

    // ---- Formulaire édition ----
    @GetMapping("/{id}/modifier")
    @PreAuthorize("hasAnyRole('ROLE_CIVIL','ROLE_MAITRE_SUPREME')")
    public String modifierForm(@PathVariable Long id, Model model) {
        model.addAttribute("civil", civilService.findById(id));
        model.addAttribute("isNew", false);
        return "civils/form";
    }

    // ---- Sauvegarder ----
    @PostMapping("/sauvegarder")
    @PreAuthorize("hasAnyRole('ROLE_CIVIL','ROLE_MAITRE_SUPREME')")
    public String sauvegarder(@Valid @ModelAttribute("civil") Civil civil,
                              BindingResult result,
                              RedirectAttributes flash,
                              Model model) {
        if (result.hasErrors()) {
            model.addAttribute("isNew", civil.getId() == null);
            return "civils/form";
        }
        try {
            civilService.save(civil);
            flash.addFlashAttribute("success", "Civil enregistré avec succès !");
        } catch (Exception e) {
            flash.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/civils";
    }

    // ---- Supprimer ----
    @PostMapping("/{id}/supprimer")
    @PreAuthorize("hasRole('ROLE_MAITRE_SUPREME')")
    public String supprimer(@PathVariable Long id, RedirectAttributes flash) {
        try {
            civilService.deleteById(id);
            flash.addFlashAttribute("success", "Civil supprimé.");
        } catch (Exception e) {
            flash.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/civils";
    }
}
