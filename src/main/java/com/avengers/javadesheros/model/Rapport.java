package com.avengers.javadesheros.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * Entité Rapport — alignée sur la table `rapport` du script SQL de P1.
 */
@Entity
@Table(name = "rapport")
@Getter @Setter @NoArgsConstructor
public class Rapport {

    public enum Resultat {
        succes, echec_partiel, echec
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_rapport")
    private Long id;

    /** Une mission ne peut avoir qu'un seul rapport (contrainte SQL uq_rap_mission). */
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_mission", nullable = false, unique = true)
    private Mission mission;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_interlocuteur_civil")
    private Civil interlocuteurCivil;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_interlocuteur_heros")
    private Superhero interlocuteurHero;

    @Column(name = "detail_intervention", columnDefinition = "TEXT")
    private String detailIntervention;

    @Enumerated(EnumType.STRING)
    @Column(name = "resultat", columnDefinition = "varchar(50)")
    private Resultat resultat;

    @Column(columnDefinition = "TEXT")
    private String degats;

    @Column(name = "date_rapport", updatable = false)
    private LocalDateTime dateRapport = LocalDateTime.now();

    @Column(columnDefinition = "TEXT")
    private String commentaire;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "rapport_super_vilain",
        joinColumns = @JoinColumn(name = "id_rapport"),
        inverseJoinColumns = @JoinColumn(name = "id_super_vilain")
    )
    private Set<Supervillain> supervilains = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "rapport_civil",
        joinColumns = @JoinColumn(name = "id_rapport"),
        inverseJoinColumns = @JoinColumn(name = "id_civil")
    )
    private Set<Civil> nouveauxCivils = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "rapport_crise",
        joinColumns = @JoinColumn(name = "id_rapport"),
        inverseJoinColumns = @JoinColumn(name = "id_crise")
    )
    private Set<Crise> crises = new HashSet<>();

    @Override
    public String toString() {
        return mission != null ? "Rapport mission #" + mission.getId() : "Rapport #" + id;
    }
}
