package com.avengers.javadesheros.controller;

import com.avengers.javadesheros.service.AdminService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasAuthority('Maitre Supreme')")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    // ---- Dashboard admin ----
    @GetMapping
    public String dashboard(Model model) {
        model.addAttribute("utilisateurs", adminService.findAll());
        model.addAttribute("roles", adminService.findAllRoles());
        return "admin/users";
    }

    // ---- Formulaire création ----
    @GetMapping("/nouveau")
    public String nouveauForm(Model model) {
        model.addAttribute("roles", adminService.findAllRoles());
        model.addAttribute("isNew", true);
        return "admin/form";
    }

    // ---- Création ----
    @PostMapping("/creer")
    public String creer(@RequestParam String login,
                        @RequestParam String email,
                        @RequestParam String motDePasse,
                        @RequestParam(required = false) List<Long> roleIds,
                        RedirectAttributes flash) {
        try {
            adminService.creer(login, email, motDePasse, roleIds);
            flash.addFlashAttribute("success", "Utilisateur « " + login + " » créé avec succès.");
        } catch (Exception e) {
            flash.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin";
    }

    // ---- Formulaire édition ----
    @GetMapping("/{id}/modifier")
    public String modifierForm(@PathVariable Long id, Model model) {
        model.addAttribute("utilisateur", adminService.findById(id));
        model.addAttribute("roles", adminService.findAllRoles());
        model.addAttribute("isNew", false);
        return "admin/form";
    }

    // ---- Mise à jour ----
    @PostMapping("/{id}/modifier")
    public String modifier(@PathVariable Long id,
                           @RequestParam String email,
                           @RequestParam(required = false) List<Long> roleIds,
                           RedirectAttributes flash) {
        try {
            adminService.modifier(id, email, roleIds);
            flash.addFlashAttribute("success", "Utilisateur mis à jour.");
        } catch (Exception e) {
            flash.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin";
    }

    // ---- Changer mot de passe ----
    @PostMapping("/{id}/password")
    public String changerPassword(@PathVariable Long id,
                                  @RequestParam String nouveauMotDePasse,
                                  RedirectAttributes flash) {
        try {
            adminService.changerMotDePasse(id, nouveauMotDePasse);
            flash.addFlashAttribute("success", "Mot de passe mis à jour.");
        } catch (Exception e) {
            flash.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin";
    }

    // ---- Suppression ----
    @PostMapping("/{id}/supprimer")
    public String supprimer(@PathVariable Long id, RedirectAttributes flash) {
        try {
            adminService.supprimer(id);
            flash.addFlashAttribute("success", "Utilisateur supprimé.");
        } catch (Exception e) {
            flash.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin";
    }
}
