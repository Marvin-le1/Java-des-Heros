package com.avengers.javadesheros.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * Entité Organisation — alignée sur la table `organisation` du script SQL de P1.
 */
@Entity
@Table(name = "organisation")
@Getter @Setter @NoArgsConstructor
public class Organisation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_organisation")
    private Long id;

    @Column(nullable = false, length = 200)
    private String nom;

    @Column(name = "adresse_siege", columnDefinition = "TEXT")
    private String adresseSiege;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_dirigeant")
    private Civil dirigeant;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "civil_organisation",
        joinColumns = @JoinColumn(name = "id_organisation"),
        inverseJoinColumns = @JoinColumn(name = "id_civil")
    )
    private Set<Civil> membres = new HashSet<>();

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

    @Override
    public String toString() {
        return nom;
    }
}
