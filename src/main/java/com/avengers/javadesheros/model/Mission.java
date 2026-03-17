package com.avengers.javadesheros.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "mission")
@Getter @Setter @NoArgsConstructor
public class Mission {

    public enum Nature {
        sauvetage, projet, voyage, prospection, autre
    }

    public enum NiveauGravite {
        insignifiant, dangereux_population, dangereux_environnement,
        dangereux_infrastructures, extinction_planete
    }

    public enum NiveauUrgence {
        faible, normal, eleve, critique
    }

    public enum Statut {
        planifiee, en_cours, terminee, annulee
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_mission")
    private Long id;

    @NotBlank(message = "Le titre est obligatoire")
    @Column(nullable = false, length = 255)
    private String titre;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "varchar(50)")
    private Nature nature = Nature.sauvetage;

    /** Incident à l'origine de la mission (optionnel) */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_incident")
    private Incident incident;

    @Column(name = "date_debut")
    private LocalDateTime dateDebut;

    @Column(name = "date_fin")
    private LocalDateTime dateFin;

    @Column(columnDefinition = "TEXT")
    private String itineraire;

    @Enumerated(EnumType.STRING)
    @Column(name = "niveau_gravite", nullable = false, columnDefinition = "varchar(50)")
    private NiveauGravite niveauGravite = NiveauGravite.insignifiant;

    @Enumerated(EnumType.STRING)
    @Column(name = "niveau_urgence", nullable = false, columnDefinition = "varchar(50)")
    private NiveauUrgence niveauUrgence = NiveauUrgence.normal;

    @Column(name = "infos_complementaires", columnDefinition = "TEXT")
    private String infosComplementaires;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "varchar(50)")
    private Statut statut = Statut.planifiee;

    /** Super-héros affectés à la mission */
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "mission_super_heros",
        joinColumns = @JoinColumn(name = "id_mission"),
        inverseJoinColumns = @JoinColumn(name = "id_super_heros")
    )
    private Set<Superhero> superheros = new HashSet<>();

    /** Civils impliqués (victimes) */
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "mission_civil",
        joinColumns = @JoinColumn(name = "id_mission"),
        inverseJoinColumns = @JoinColumn(name = "id_civil")
    )
    private Set<Civil> civils = new HashSet<>();

    /** Génère un titre par défaut si absent */
    public String getTitreOuDefaut() {
        if (titre != null && !titre.isBlank()) return titre;
        return "Mission " + id + " – " + (nature != null ? nature.name().toUpperCase() : "");
    }

    @Override
    public String toString() {
        return getTitreOuDefaut();
    }
}
