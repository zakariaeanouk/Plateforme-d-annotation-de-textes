package com.AnnotationPlatform.Core.bo;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Tache {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String dateLimit;

    @ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinTable(name = "CoupleText_Tache", joinColumns = @JoinColumn(name = "id_tache"), inverseJoinColumns = @JoinColumn(name = "id_coupleTexte"))
    private Set<CoupleText> coupleTexts = new HashSet<>();


    @ManyToOne
    @JoinColumn(name = "id_annotateur")
    private Annotateur annotateur;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_dataset")
    private Dataset dataset;

}
