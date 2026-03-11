package com.avengers.javadesheros.config;

import com.avengers.javadesheros.model.Role;
import com.avengers.javadesheros.model.Utilisateur;
import com.avengers.javadesheros.repository.RoleRepository;
import com.avengers.javadesheros.repository.UtilisateurRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * Initialise les données de base au démarrage (rôles, compte admin).
 * S'exécute uniquement si la BDD est vide.
 */
@Component
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UtilisateurRepository utilisateurRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(RoleRepository roleRepository,
                           UtilisateurRepository utilisateurRepository,
                           PasswordEncoder passwordEncoder) {
        this.roleRepository = roleRepository;
        this.utilisateurRepository = utilisateurRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        // Création des rôles si absents
        Role roleMaitre = creerRoleSiAbsent("ROLE_MAITRE_SUPREME", "Accès total à toutes les fonctionnalités");
        creerRoleSiAbsent("ROLE_CIVIL",        "Gestion du module Civils");
        creerRoleSiAbsent("ROLE_ORGANISATION", "Gestion du module Organisations");
        creerRoleSiAbsent("ROLE_SUPERHERO",    "Gestion du module Super-héros");
        creerRoleSiAbsent("ROLE_SUPERVILLAIN", "Gestion du module Super-vilains");
        creerRoleSiAbsent("ROLE_INCIDENT",     "Gestion du module Incidents");
        creerRoleSiAbsent("ROLE_MISSION",      "Gestion du module Missions");
        creerRoleSiAbsent("ROLE_RAPPORT",      "Gestion du module Rapports");
        creerRoleSiAbsent("ROLE_SATISFACTION", "Gestion du module Satisfaction");
        creerRoleSiAbsent("ROLE_CRISE",        "Gestion du module Crise");
        creerRoleSiAbsent("ROLE_LITIGE",       "Gestion du module Litiges");

        // Compte admin par défaut
        if (!utilisateurRepository.existsByUsername("admin")) {
            Utilisateur admin = new Utilisateur();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setEmail("admin@avengers.com");
            admin.setRoles(Set.of(roleMaitre));
            utilisateurRepository.save(admin);
            System.out.println("✔ Compte admin créé (login: admin / mdp: admin123)");
        }

        System.out.println("✔ Initialisation de la base de données terminée.");
    }

    private Role creerRoleSiAbsent(String nom, String description) {
        return roleRepository.findByNom(nom).orElseGet(() -> {
            Role role = new Role(nom, description);
            return roleRepository.save(role);
        });
    }
}
