package com.AnnotationPlatform.Core.dao;

import com.AnnotationPlatform.Core.bo.Dataset;
import com.AnnotationPlatform.Core.dto.DatasetDTO;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
@Repository
public interface IDatasetRepository extends JpaRepository<Dataset, Long> {
    Optional<Dataset> findByNomDataset(String nomDataset);

    @Query("""
    SELECT new com.AnnotationPlatform.Core.dto.DatasetDTO
    (
        d.id,
        d.nomDataset,
        d.descriptionDataset,
        SIZE(d.classePossibles),
        SIZE(d.coupleTexts)
    )
        FROM Dataset d
    """)
    Page<DatasetDTO> getAllDatasets(Pageable pageable);

}
