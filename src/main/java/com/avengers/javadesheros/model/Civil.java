package com.avengers.javadesheros.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Entité Civil — alignée sur la table `civil` du script SQL de P1.
 */
@Entity
@Table(name = "civil")
@Getter @Setter @NoArgsConstructor
public class Civil {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_civil")
    private Long id;

    @Column(nullable = false, length = 100)
    private String nom;

    @Column(nullable = false, length = 100)
    private String prenom;

    @Column(length = 10)
    private String civilite; // M., Mme, Mx

    @Column(name = "adresse_postale", columnDefinition = "TEXT")
    private String adressePostale;

    @Column(length = 150)
    private String email;

    @Column(length = 30)
    private String telephone;

    @Column(name = "date_naissance")
    private LocalDate dateNaissance;

    @Column(length = 100)
    private String nationalite;

    @Column(name = "date_deces")
    private LocalDate dateDeces;

    @Column(columnDefinition = "TEXT")
    private String commentaire;

    @Column(name = "date_ajout", updatable = false)
    private LocalDateTime dateAjout = LocalDateTime.now();

    @Column(name = "date_modification")
    private LocalDateTime dateModification = LocalDateTime.now();

    @PreUpdate
    public void preUpdate() {
        this.dateModification = LocalDateTime.now();
    }

    public String getNomComplet() {
        return civilite != null
            ? civilite + " " + prenom + " " + nom
            : prenom + " " + nom;
    }

    @Override
    public String toString() {
        return getNomComplet();
    }
}
