package com.avengers.javadesheros.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * Entité Crise — alignée sur la table `crise` du script SQL de P1.
 */
@Entity
@Table(name = "crise")
@Getter @Setter @NoArgsConstructor
public class Crise {

    public enum TypeCrise {
        hero_demasque, nouveau_vilain, proces, dommages_collateraux, autre
    }

    public enum NiveauAlerte {
        faible, modere, eleve, critique
    }

    public enum Statut {
        ouverte, en_cours, resolue
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_crise")
    private Long id;

    @NotBlank(message = "Le titre est obligatoire")
    @Column(nullable = false, length = 255)
    private String titre;

    @Enumerated(EnumType.STRING)
    @Column(name = "type_crise", nullable = false, columnDefinition = "varchar(50)")
    private TypeCrise typeCrise = TypeCrise.autre;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_mission")
    private Mission mission;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "niveau_alerte", nullable = false, columnDefinition = "varchar(50)")
    private NiveauAlerte niveauAlerte = NiveauAlerte.modere;

    @Column(name = "date_declaration", updatable = false)
    private LocalDateTime dateDeclaration = LocalDateTime.now();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "varchar(50)")
    private Statut statut = Statut.ouverte;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "crise_super_heros",
        joinColumns = @JoinColumn(name = "id_crise"),
        inverseJoinColumns = @JoinColumn(name = "id_super_heros")
    )
    private Set<Superhero> herosConcernes = new HashSet<>();

    @Override
    public String toString() {
        return titre;
    }
}
