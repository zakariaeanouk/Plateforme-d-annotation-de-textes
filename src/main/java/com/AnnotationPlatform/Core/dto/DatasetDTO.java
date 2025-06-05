package com.AnnotationPlatform.Core.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @AllArgsConstructor
@NoArgsConstructor
public class DatasetDTO {

    private Long id;
    private String nomDataset;
    private String descriptionDataset;
    private int classeCount;
    private int coupleCount;

}
