package com.AnnotationPlatform.Core.services.Impl;

import com.AnnotationPlatform.Core.bo.ClassePossible;
import com.AnnotationPlatform.Core.bo.Dataset;
import com.AnnotationPlatform.Core.dao.IClassePossibleRepository;
import com.AnnotationPlatform.Core.dao.IDatasetRepository;
import com.AnnotationPlatform.Core.services.IClassePossibleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClassePossibleImpl implements IClassePossibleService {


    @Autowired
    IClassePossibleRepository classePossibleRepository;

    @Autowired
    IDatasetRepository datasetRepository;


    @Override
    public void saveListOfClassePossible(List<String> classePossibleList, long idDataset) {
        Dataset dataset = datasetRepository.findById(idDataset).orElseThrow(() -> new RuntimeException("Dataset with id :"+idDataset+" was not found"));
        for(String it : classePossibleList){
            ClassePossible cp = new ClassePossible();
            cp.setTextClasse(it.trim());
            cp.setDataset(dataset);

            classePossibleRepository.save(cp);
        }
    }
}
