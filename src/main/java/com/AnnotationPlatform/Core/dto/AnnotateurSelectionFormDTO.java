package com.AnnotationPlatform.Core.dto;


import lombok.*;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AnnotateurSelectionFormDTO {

    private List<Long> selectedAnnotatorIds;
}

