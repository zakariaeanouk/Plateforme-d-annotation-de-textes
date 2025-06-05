package com.AnnotationPlatform.Core.services;

import com.AnnotationPlatform.Core.bo.*;
import com.AnnotationPlatform.Core.dto.AnnotateurDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IAnnotateurService {
    public List<Tache> findAllTaches(Annotateur annotateur);
    public Annotation annotateCoupleText(Annotateur annotateur,CoupleText coupleText, String classe);

    Page<AnnotateurDTO> findAllAnnotator(Pageable pageable);


}
