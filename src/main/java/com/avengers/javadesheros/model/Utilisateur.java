package com.avengers.javadesheros.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "utilisateur")
@Getter @Setter @NoArgsConstructor
public class Utilisateur {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_utilisateur")
    private Long id;

    @Column(name = "login", nullable = false, unique = true, length = 100)
    private String username;

    @Column(name = "mot_de_passe", nullable = false)
    private String password;

    @Column(nullable = false, unique = true, length = 255)
    private String email;

    @Column(name = "date_creation", updatable = false)
    private LocalDateTime dateCreation = LocalDateTime.now();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "utilisateur_role",
        joinColumns = @JoinColumn(name = "id_utilisateur"),
        inverseJoinColumns = @JoinColumn(name = "id_role")
    )
    private Set<Role> roles = new HashSet<>();
}
