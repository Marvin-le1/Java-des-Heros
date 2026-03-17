package com.avengers.javadesheros.config;

import com.avengers.javadesheros.model.Role;
import com.avengers.javadesheros.model.Utilisateur;
import com.avengers.javadesheros.repository.RoleRepository;
import com.avengers.javadesheros.repository.UtilisateurRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * Initialise les données de base au démarrage (rôles, compte admin).
 * S'exécute uniquement si la BDD est vide.
 */
@Component
@Order(1)
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
        // Création des rôles si absents (noms alignés sur script SQL de P1)
        Role roleMaitre = creerRoleSiAbsent("Maitre Supreme",             "all");
        creerRoleSiAbsent("Gestionnaire Civils",        "civils");
        creerRoleSiAbsent("Gestionnaire Organisations", "organisations");
        creerRoleSiAbsent("Gestionnaire Super-Heros",   "super_heros");
        creerRoleSiAbsent("Gestionnaire Super-Vilains", "super_vilains");
        creerRoleSiAbsent("Gestionnaire Incidents",     "incidents");
        creerRoleSiAbsent("Gestionnaire Missions",      "missions");
        creerRoleSiAbsent("Gestionnaire Rapports",      "rapports");
        creerRoleSiAbsent("Gestionnaire Satisfaction",  "satisfaction");
        creerRoleSiAbsent("Gestionnaire Crises",        "crises");
        creerRoleSiAbsent("Gestionnaire Litiges",       "litiges");

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

    private Role creerRoleSiAbsent(String nom, String module) {
        return roleRepository.findByNom(nom).orElseGet(() -> {
            Role role = new Role(nom, module);
            return roleRepository.save(role);
        });
    }
}
