package com.avengers.javadesheros.controller;

import com.avengers.javadesheros.model.Supervillain;
import com.avengers.javadesheros.repository.CivilRepository;
import com.avengers.javadesheros.service.SupervillainService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/supervilains")
public class SupervillainController {

    private final SupervillainService supervillainService;
    private final CivilRepository civilRepository;

    public SupervillainController(SupervillainService supervillainService,
                                  CivilRepository civilRepository) {
        this.supervillainService = supervillainService;
        this.civilRepository = civilRepository;
    }

    // ---- Liste ----
    @GetMapping
    public String list(@RequestParam(required = false) String q, Model model) {
        model.addAttribute("vilains", supervillainService.search(q));
        model.addAttribute("q", q);
        model.addAttribute("topVilains", supervillainService.getTopVilains());
        return "supervilains/list";
    }

    // ---- Détail ----
    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model) {
        model.addAttribute("vilain", supervillainService.findById(id));
        return "supervilains/detail";
    }

    // ---- Formulaire ajout ----
    @GetMapping("/nouveau")
    @PreAuthorize("hasAnyRole('ROLE_SUPERVILLAIN','ROLE_MAITRE_SUPREME')")
    public String nouveauForm(Model model) {
        model.addAttribute("vilain", new Supervillain());
        model.addAttribute("civils", civilRepository.findAll());
        model.addAttribute("isNew", true);
        return "supervilains/form";
    }

    // ---- Formulaire édition ----
    @GetMapping("/{id}/modifier")
    @PreAuthorize("hasAnyRole('ROLE_SUPERVILLAIN','ROLE_MAITRE_SUPREME')")
    public String modifierForm(@PathVariable Long id, Model model) {
        model.addAttribute("vilain", supervillainService.findById(id));
        model.addAttribute("civils", civilRepository.findAll());
        model.addAttribute("isNew", false);
        return "supervilains/form";
    }

    // ---- Sauvegarder ----
    @PostMapping("/sauvegarder")
    @PreAuthorize("hasAnyRole('ROLE_SUPERVILLAIN','ROLE_MAITRE_SUPREME')")
    public String sauvegarder(@Valid @ModelAttribute("vilain") Supervillain vilain,
                              BindingResult result,
                              @RequestParam(required = false) Long civilId,
                              RedirectAttributes flash,
                              Model model) {
        if (result.hasErrors()) {
            model.addAttribute("civils", civilRepository.findAll());
            model.addAttribute("isNew", vilain.getId() == null);
            return "supervilains/form";
        }
        try {
            supervillainService.save(vilain, civilId);
            flash.addFlashAttribute("success", "Super-vilain enregistré avec succès !");
        } catch (Exception e) {
            flash.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/supervilains";
    }

    // ---- Supprimer ----
    @PostMapping("/{id}/supprimer")
    @PreAuthorize("hasRole('ROLE_MAITRE_SUPREME')")
    public String supprimer(@PathVariable Long id, RedirectAttributes flash) {
        try {
            supervillainService.deleteById(id);
            flash.addFlashAttribute("success", "Super-vilain supprimé.");
        } catch (Exception e) {
            flash.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/supervilains";
    }
}
