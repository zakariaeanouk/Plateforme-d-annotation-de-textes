package com.AnnotationPlatform.Core.bo;

import jakarta.persistence.*;
import lombok.*;



@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Annotation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String classe;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_annotateur")
    private Annotateur annotateur;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_coupleText")
    private CoupleText coupleText;


}
