package com.AnnotationPlatform.Core.dao;

import com.AnnotationPlatform.Core.bo.Annotation;
import com.AnnotationPlatform.Core.bo.CoupleText;
import com.AnnotationPlatform.Core.bo.Dataset;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;
@Repository
public interface ICoupleTextRepository extends JpaRepository<CoupleText, Long> {

    Optional<CoupleText> findByDataset(Dataset dataset);

    Page<CoupleText> findByDatasetId(long idDataset, Pageable pageable);

    List<CoupleText> findByDatasetId(long idDataset);

    long countByDataset(Dataset dataset);

    long countByDataset_Id(long datasetId);


}
