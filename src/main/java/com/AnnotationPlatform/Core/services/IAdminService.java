package com.AnnotationPlatform.Core.services;


import com.AnnotationPlatform.Core.bo.Administrateur;
import com.AnnotationPlatform.Core.bo.Annotateur;
import com.AnnotationPlatform.Core.bo.Annotation;
import com.AnnotationPlatform.Core.bo.Dataset;
import com.AnnotationPlatform.Core.dto.AnnotateurDTO;
import com.AnnotationPlatform.Core.dto.annDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


import java.util.List;
import java.util.Optional;


public interface IAdminService {
    Dataset createDataset(Dataset dataset);

    List<Dataset> findAllDataset();

    Optional<Dataset> readDataset(Long id);

    annDTO createAnnotateur(Annotateur annotateur);

    List<Annotateur> findAllAnnotateurs();

    Optional<Annotateur> readAnnotateur(Long id);

    Annotateur updateAnnotateur(Annotateur annotateur);

    void deleteAnnotateur(Long id);

    void affecterAnnotateurADataset(Long annotateurId, Long datasetId);

    long countAllDatasets();

    long countAllAnnotators();


    String generateSecurePassword();


    List<Annotation> findAnnotationsByDataset(Long datasetId);

    String exportAnnotationsToCSV(Long datasetId);

}
