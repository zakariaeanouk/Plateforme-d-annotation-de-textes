package com.AnnotationPlatform.Core.dto;

import com.AnnotationPlatform.Core.bo.Annotateur;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class annDTO {
    private Annotateur annotateur;
    private String plainPassword;

    public annDTO(Annotateur annotateur, String plainPassword) {
        this.annotateur = annotateur;
        this.plainPassword = plainPassword;
    }

}
