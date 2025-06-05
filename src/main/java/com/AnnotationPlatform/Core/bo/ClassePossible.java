package com.AnnotationPlatform.Core.bo;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class ClassePossible {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;


    private String textClasse;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_dataset")
    private Dataset dataset;


}