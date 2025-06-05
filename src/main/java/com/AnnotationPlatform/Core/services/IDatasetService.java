package com.AnnotationPlatform.Core.services;

import com.AnnotationPlatform.Core.bo.Dataset;
import com.AnnotationPlatform.Core.dao.IDatasetRepository;
import com.AnnotationPlatform.Core.dto.DatasetDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IDatasetService {

    Page<DatasetDTO> getAllDatasets(Pageable pageable);

    void deleteDataset(Long id);

    Dataset findById(Long id);

    Dataset saveDataset(Dataset dataset);
    Page<DatasetDTO> getCompletedDatasets(Pageable pageable);

}
