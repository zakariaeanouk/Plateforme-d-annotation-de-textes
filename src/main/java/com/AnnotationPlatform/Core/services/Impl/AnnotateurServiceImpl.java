package com.AnnotationPlatform.Core.services.Impl;

import com.AnnotationPlatform.Core.bo.*;
import com.AnnotationPlatform.Core.dao.IAnnotateurRepository;
import com.AnnotationPlatform.Core.dao.IAnnotationRepository;
import com.AnnotationPlatform.Core.dao.IClassePossibleRepository;
import com.AnnotationPlatform.Core.dao.ITacheRepository;
import com.AnnotationPlatform.Core.dto.AnnotateurDTO;
import com.AnnotationPlatform.Core.services.IAnnotateurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AnnotateurServiceImpl implements IAnnotateurService {


    @Autowired
    private IAnnotateurRepository annotateurRepository;
    @Autowired
    private ITacheRepository tacheRepository;
    @Autowired
    private IAnnotationRepository annotationRepository;

    @Override
    public List<Tache> findAllTaches(Annotateur annotateur) {
        return tacheRepository.findTachesByAnnotateur(annotateur);
    }

    @Override
    public Annotation annotateCoupleText(Annotateur annotateur,CoupleText coupleText,String classe) {
        Annotation annotation = new Annotation();
        if(annotationRepository.existsByCoupleTextAndAnnotateur(coupleText, annotateur)){
            annotation=annotationRepository.getAnnotationByAnnotateurAndCoupleText(annotateur,coupleText);
            annotation.setClasse(classe);
        }else {
            annotation.setAnnotateur(annotateur);
            annotation.setCoupleText(coupleText);
            annotation.setClasse(classe);
        }

        return annotationRepository.save(annotation);
    }


    public Page<AnnotateurDTO> findAllAnnotator(Pageable pageable) {
        return annotateurRepository.findAllAnnotator(pageable);
    }

}
