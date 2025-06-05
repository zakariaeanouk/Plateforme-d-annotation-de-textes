package com.AnnotationPlatform.Core.services.Impl;

import com.AnnotationPlatform.Core.bo.Dataset;
import com.AnnotationPlatform.Core.dao.IDatasetRepository;
import com.AnnotationPlatform.Core.dto.DatasetDTO;
import com.AnnotationPlatform.Core.services.IDatasetService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.xml.crypto.Data;
import java.util.List;

@Service
@Transactional
public class DatasetServiceImpl implements IDatasetService {

    @Autowired
    private IDatasetRepository datasetRepo;

    @Override
    public Page<DatasetDTO> getAllDatasets(Pageable pageable) {
        return datasetRepo.getAllDatasets(pageable);
    }

    @Override
    public void deleteDataset(Long id) {
        datasetRepo.deleteById(id);
    }

    @Override
    public Dataset findById(Long id) {
        return datasetRepo.findById(id).isPresent()? datasetRepo.findById(id).get() : null;
    }

    @Override
    public Dataset saveDataset(Dataset dataset) {
        return datasetRepo.save(dataset);
    }
    @Override
    public Page<DatasetDTO> getCompletedDatasets(Pageable pageable) {
        return datasetRepo.getAllDatasets(pageable);
    }
}
