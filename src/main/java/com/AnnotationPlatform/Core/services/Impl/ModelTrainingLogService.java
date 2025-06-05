package com.AnnotationPlatform.Core.services.Impl;

import com.AnnotationPlatform.Core.bo.ModelTrainingLog;
import com.AnnotationPlatform.Core.dao.IModelRepository;
import com.AnnotationPlatform.Core.services.IModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ModelTrainingLogService implements IModel {

    @Autowired
    private IModelRepository modelRepository;

    public void saveLog(String utilisateur, String hyperparametres, String score) {
        ModelTrainingLog log = new ModelTrainingLog();
        log.setDateExecution(LocalDateTime.now().toString());
        log.setUtilisateur(utilisateur);
        log.setHyperparametres(hyperparametres);
        log.setScore(score);
        modelRepository.save(log);
    }



}
