package com.avengers.javadesheros.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Évaluation de satisfaction après une mission.
 * Alignée sur la table `satisfaction` du schéma CRM Avengers.
 */
@Entity
@Table(name = "satisfaction")
@Getter @Setter @NoArgsConstructor
public class Satisfaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_satisfaction")
    private Long id;

    /** Mission évaluée (obligatoire) */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_mission", nullable = false)
    private Mission mission;

    /** Civil ayant soumis l'évaluation (optionnel) */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_civil")
    private Civil civil;

    @DecimalMin(value = "0.0", message = "La note doit être entre 0 et 5")
    @DecimalMax(value = "5.0", message = "La note doit être entre 0 et 5")
    @Column(name = "note", nullable = false, columnDefinition = "DECIMAL(3,1)")
    private Double note = 3.0;

    @Column(columnDefinition = "TEXT")
    private String commentaire;

    @Column(name = "date_evaluation", updatable = false)
    private LocalDateTime dateEvaluation = LocalDateTime.now();

    @Override
    public String toString() {
        return "Satisfaction #" + id + " — " + note + "/5";
    }
}
