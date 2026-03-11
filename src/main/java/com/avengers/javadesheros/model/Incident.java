package com.avengers.javadesheros.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Entité Incident — alignée sur la table `incident` du script SQL de P1.
 */
@Entity
@Table(name = "incident")
@Getter @Setter @NoArgsConstructor
public class Incident {

    public enum Statut {
        en_attente, classe_sans_suite, transforme_en_mission
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_incident")
    private Long id;

    @NotBlank(message = "Le titre est obligatoire")
    @Column(nullable = false, length = 255)
    private String titre;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(length = 255)
    private String lieu;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Statut statut = Statut.en_attente;

    // Déclarant civil (optionnel)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_declarant_civil")
    private Civil declarantCivil;

    // Déclarant organisation (optionnel)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_declarant_organisation")
    private Organisation declarantOrganisation;

    // Déclarant super-héros (optionnel)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_declarant_hero")
    private Superhero declarantHero;

    @Column(name = "date_declaration", updatable = false)
    private LocalDateTime dateDeclaration = LocalDateTime.now();

    @Column(columnDefinition = "TEXT")
    private String commentaire;

    @Override
    public String toString() {
        return titre;
    }
}
