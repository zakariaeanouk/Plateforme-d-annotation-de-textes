package com.AnnotationPlatform.Core.bo;

import jakarta.persistence.*;
import lombok.*;


/**
 * class for user Account
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Utilisateur {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "idRole")
    private Role role;

    private String nom;

    private String prenom;

    private String login;

    private String password;

    private boolean isActive = true;
}
