package com.avengers.javadesheros.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Litige juridique lié à une mission ou un incident.
 * Aligné sur la table `litige` du schéma CRM Avengers.
 */
@Entity
@Table(name = "litige")
@Getter @Setter @NoArgsConstructor
public class Litige {

    public enum TypeLitige {
        degats_materiels, atteinte_physique, diffamation, propriete, autre
    }

    public enum Statut {
        ouvert, en_instruction, resolu, classe
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_litige")
    private Long id;

    @NotBlank(message = "Le titre est obligatoire")
    @Column(nullable = false, length = 255)
    private String titre;

    @Enumerated(EnumType.STRING)
    @Column(name = "type_litige", nullable = false)
    private TypeLitige typeLitige = TypeLitige.autre;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Statut statut = Statut.ouvert;

    /** Mission à l'origine du litige (optionnel) */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_mission")
    private Mission mission;

    /** Civil plaignant (optionnel) */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_plaignant")
    private Civil plaignant;

    /** Super-héros mis en cause (optionnel) */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_super_heros")
    private Superhero superhero;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(columnDefinition = "TEXT")
    private String resolution;

    @Column(name = "date_ouverture", updatable = false)
    private LocalDateTime dateOuverture = LocalDateTime.now();

    @Column(name = "date_cloture")
    private LocalDateTime dateCloture;

    @Override
    public String toString() {
        return titre;
    }
}
