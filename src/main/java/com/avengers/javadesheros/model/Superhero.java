package com.avengers.javadesheros.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "super_heros")
@Getter @Setter @NoArgsConstructor
public class Superhero {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_super_heros")
    private Long id;

    @NotBlank(message = "Le nom du héros est obligatoire")
    @Column(name = "nom_hero", nullable = false, unique = true, length = 150)
    private String nom;

    @Column(columnDefinition = "TEXT")
    private String pouvoir;

    @Column(name = "point_faible", columnDefinition = "TEXT")
    private String pointFaible;

    /**
     * Score moyen calculé depuis les enquêtes de satisfaction.
     * Mis à jour automatiquement par SatisfactionService.
     */
    @Column(columnDefinition = "DECIMAL(5,2) DEFAULT 0.00")
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
