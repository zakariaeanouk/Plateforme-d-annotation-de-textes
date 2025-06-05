package com.AnnotationPlatform.Core.services;

import com.AnnotationPlatform.Core.bo.Tache;

import java.util.List;

public interface ITacheService {

    void divideDatasetIntoTasks(long datasetId, String dateLimit);

    List<Tache> getTachesByDatasetId(long datasetId);

    void assignAnnotatorsToDataset(List<Tache> tacheList, List<Long> annotatorListId);
}
