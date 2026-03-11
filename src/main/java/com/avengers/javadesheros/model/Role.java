package com.avengers.javadesheros.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "roles")
@Getter @Setter @NoArgsConstructor
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String nom; // ex: ROLE_ADMIN, ROLE_CIVIL, ROLE_MAITRE_SUPREME

    @Column(length = 255)
    private String description;

    public Role(String nom, String description) {
        this.nom = nom;
        this.description = description;
    }
}
