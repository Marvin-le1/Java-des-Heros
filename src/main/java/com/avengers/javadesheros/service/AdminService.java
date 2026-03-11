package com.avengers.javadesheros.service;

import com.avengers.javadesheros.model.Role;
import com.avengers.javadesheros.model.Utilisateur;
import com.avengers.javadesheros.repository.RoleRepository;
import com.avengers.javadesheros.repository.UtilisateurRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class AdminService {

    private final UtilisateurRepository utilisateurRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminService(UtilisateurRepository utilisateurRepository,
                        RoleRepository roleRepository,
                        PasswordEncoder passwordEncoder) {
        this.utilisateurRepository = utilisateurRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // ---- Lecture ----

    public List<Utilisateur> findAll() {
        return utilisateurRepository.findAll();
    }

    public Utilisateur findById(Long id) {
        return utilisateurRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Utilisateur introuvable : " + id));
    }

    public List<Role> findAllRoles() {
        return roleRepository.findAll();
    }

    // ---- Création ----

    public Utilisateur creer(String login, String email, String motDePasse, List<Long> roleIds) {
        if (utilisateurRepository.existsByUsername(login)) {
            throw new IllegalArgumentException("Ce login est déjà utilisé : " + login);
        }
        if (utilisateurRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Cet email est déjà utilisé : " + email);
        }
        Utilisateur u = new Utilisateur();
        u.setUsername(login);
        u.setEmail(email);
        u.setPassword(passwordEncoder.encode(motDePasse));
        u.setRoles(resolveRoles(roleIds));
        return utilisateurRepository.save(u);
    }

    // ---- Mise à jour ----

    public Utilisateur modifier(Long id, String email, List<Long> roleIds) {
        Utilisateur u = findById(id);
        // Email : autoriser si c'est le même utilisateur
        if (!u.getEmail().equals(email) && utilisateurRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Cet email est déjà utilisé : " + email);
        }
        u.setEmail(email);
        u.setRoles(resolveRoles(roleIds));
        return utilisateurRepository.save(u);
    }

    public void changerMotDePasse(Long id, String nouveauMotDePasse) {
        if (nouveauMotDePasse == null || nouveauMotDePasse.length() < 6) {
            throw new IllegalArgumentException("Le mot de passe doit contenir au moins 6 caractères.");
        }
        Utilisateur u = findById(id);
        u.setPassword(passwordEncoder.encode(nouveauMotDePasse));
        utilisateurRepository.save(u);
    }

    // ---- Suppression ----

    public void supprimer(Long id) {
        Utilisateur u = findById(id);
        // Sécurité : ne pas supprimer le seul Maitre Supreme
        boolean estMaitre = u.getRoles().stream()
            .anyMatch(r -> r.getNom().equals("Maitre Supreme"));
        if (estMaitre) {
            long nbMaitres = utilisateurRepository.findAll().stream()
                .filter(usr -> usr.getRoles().stream()
                    .anyMatch(r -> r.getNom().equals("Maitre Supreme")))
                .count();
            if (nbMaitres <= 1) {
                throw new IllegalStateException(
                    "Impossible de supprimer le seul Maître Suprême du système.");
            }
        }
        utilisateurRepository.delete(u);
    }

    // ---- Helpers ----

    private Set<Role> resolveRoles(List<Long> roleIds) {
        Set<Role> roles = new HashSet<>();
        if (roleIds != null) {
            for (Long rid : roleIds) {
                roleRepository.findById(rid).ifPresent(roles::add);
            }
        }
        return roles;
    }
}
