package com.avengers.javadesheros.controller;

import com.avengers.javadesheros.model.Superhero;
import com.avengers.javadesheros.repository.CivilRepository;
import com.avengers.javadesheros.service.SuperheroService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/superheros")
public class SuperheroController {

    private final SuperheroService superheroService;
    private final CivilRepository civilRepository;

    public SuperheroController(SuperheroService superheroService,
                               CivilRepository civilRepository) {
        this.superheroService = superheroService;
        this.civilRepository = civilRepository;
    }

    // ---- Liste ----
    @GetMapping
    public String list(@RequestParam(required = false) String q, Model model) {
        model.addAttribute("superheros", superheroService.search(q));
        model.addAttribute("q", q);
        model.addAttribute("topHeros", superheroService.getTopHeros());
        return "superheros/list";
    }

    // ---- Détail ----
    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model) {
        model.addAttribute("hero", superheroService.findById(id));
        return "superheros/detail";
    }

    // ---- Formulaire ajout ----
    @GetMapping("/nouveau")
    @PreAuthorize("hasAnyRole('ROLE_SUPERHERO','ROLE_MAITRE_SUPREME')")
    public String nouveauForm(Model model) {
        model.addAttribute("hero", new Superhero());
        model.addAttribute("civils", civilRepository.findAll());
        model.addAttribute("isNew", true);
        return "superheros/form";
    }

    // ---- Formulaire édition ----
    @GetMapping("/{id}/modifier")
    @PreAuthorize("hasAnyRole('ROLE_SUPERHERO','ROLE_MAITRE_SUPREME')")
    public String modifierForm(@PathVariable Long id, Model model) {
        model.addAttribute("hero", superheroService.findById(id));
        model.addAttribute("civils", civilRepository.findAll());
        model.addAttribute("isNew", false);
        return "superheros/form";
    }

    // ---- Sauvegarder ----
    @PostMapping("/sauvegarder")
    @PreAuthorize("hasAnyRole('ROLE_SUPERHERO','ROLE_MAITRE_SUPREME')")
    public String sauvegarder(@Valid @ModelAttribute("hero") Superhero hero,
                              BindingResult result,
                              @RequestParam Long civilId,
                              RedirectAttributes flash,
                              Model model) {
        if (result.hasErrors()) {
            model.addAttribute("civils", civilRepository.findAll());
            model.addAttribute("isNew", hero.getId() == null);
            return "superheros/form";
        }
        try {
            superheroService.save(hero, civilId);
            flash.addFlashAttribute("success", "Super-héros enregistré avec succès !");
        } catch (Exception e) {
            flash.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/superheros";
    }

    // ---- Supprimer ----
    @PostMapping("/{id}/supprimer")
    @PreAuthorize("hasRole('ROLE_MAITRE_SUPREME')")
    public String supprimer(@PathVariable Long id, RedirectAttributes flash) {
        try {
            superheroService.deleteById(id);
            flash.addFlashAttribute("success", "Super-héros supprimé.");
        } catch (Exception e) {
            flash.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/superheros";
    }
}
