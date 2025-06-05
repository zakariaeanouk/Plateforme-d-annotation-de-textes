package com.AnnotationPlatform.Core.dao;

import com.AnnotationPlatform.Core.bo.Annotateur;
import com.AnnotationPlatform.Core.bo.Annotation;
import com.AnnotationPlatform.Core.bo.CoupleText;
import com.AnnotationPlatform.Core.bo.Tache;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
@Repository
public interface IAnnotationRepository extends JpaRepository<Annotation, Long> {
    Optional<Annotation> findByClasse(String classe);

    List<Annotation> findByAnnotateur(Annotateur annotateur);

    List<Annotation> findByCoupleText(CoupleText coupleText);
    @Query("SELECT a FROM Annotation a WHERE a.coupleText.dataset.id = :datasetId")
    List<Annotation> findByDatasetId(@Param("datasetId") Long datasetId);


    boolean existsByCoupleTextAndAnnotateur(CoupleText ct, Annotateur annotateur);

    Annotation getAnnotationByAnnotateurAndCoupleText(Annotateur annotateur, CoupleText coupleText);

    boolean existsByCoupleText(CoupleText c);

    boolean getAnnotationsByCoupleText(CoupleText coupleText);

    long countByCoupleText_Dataset_Id(long coupleTextDatasetId);
}
