package com.avengers.javadesheros.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "superheros")
@Getter @Setter @NoArgsConstructor
public class Superhero {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Le nom du héros est obligatoire")
    @Column(nullable = false, unique = true, length = 100)
    private String nom;

    @NotBlank(message = "Le pouvoir est obligatoire")
    @Column(nullable = false, columnDefinition = "TEXT")
    private String pouvoir;

    @Column(name = "point_faible", columnDefinition = "TEXT")
    private String pointFaible;

    /**
     * Score moyen calculé depuis les enquêtes de satisfaction (0.0 à 5.0).
     * Mis à jour automatiquement par SatisfactionService.
     */
    @Column(columnDefinition = "DOUBLE DEFAULT 0.0")
    private Double score = 0.0;

    @Column(columnDefinition = "TEXT")
    private String commentaire;

    /**
     * Identité secrète — obligatoire depuis les Accords de Sokovie.
     * Un Civil ne peut être l'identité que d'un seul héros.
     */
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_civil", nullable = false, unique = true)
    private Civil identiteSecrete;

    @Override
    public String toString() {
        return nom;
    }
}
