package com.avengers.javadesheros.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "role")
@Getter @Setter @NoArgsConstructor
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_role")
    private Long id;

    @Column(name = "nom_role", nullable = false, unique = true, length = 100)
    private String nom; // ex: Gestionnaire Civils, Maitre Supreme

    @Column(length = 100)
    private String module;

    public Role(String nom, String module) {
        this.nom = nom;
        this.module = module;
    }
}
