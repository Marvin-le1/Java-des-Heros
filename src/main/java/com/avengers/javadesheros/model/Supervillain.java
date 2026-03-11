package com.avengers.javadesheros.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "supervilains")
@Getter @Setter @NoArgsConstructor
public class Supervillain {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Le nom du vilain est obligatoire")
    @Column(nullable = false, unique = true, length = 100)
    private String nom;

    @Column(columnDefinition = "TEXT")
    private String pouvoir;

    @Column(name = "point_faible", columnDefinition = "TEXT")
    private String pointFaible;

    /**
     * Degré de malveillance (calculé selon méfaits, dégâts, nombre de missions).
     * Mis à jour lors des rapports de mission.
     */
    @Column(name = "degre_malveillance", columnDefinition = "INT DEFAULT 0")
    private Integer degreMalveillance = 0;

    @Column(columnDefinition = "TEXT")
    private String commentaire;

    /**
     * Identité secrète — optionnelle (pas toujours connue).
     * Peut être découverte lors d'une mission et ajoutée dans le rapport.
     */
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_civil", unique = true)
    private Civil identiteSecrete;

    @Override
    public String toString() {
        return nom;
    }
}
