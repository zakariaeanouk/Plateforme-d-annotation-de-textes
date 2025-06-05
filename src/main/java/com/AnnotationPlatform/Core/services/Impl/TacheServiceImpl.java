package com.AnnotationPlatform.Core.services.Impl;

import com.AnnotationPlatform.Core.bo.Annotateur;
import com.AnnotationPlatform.Core.bo.CoupleText;
import com.AnnotationPlatform.Core.bo.Dataset;
import com.AnnotationPlatform.Core.bo.Tache;
import com.AnnotationPlatform.Core.dao.IAnnotateurRepository;
import com.AnnotationPlatform.Core.dao.ICoupleTextRepository;
import com.AnnotationPlatform.Core.dao.IDatasetRepository;
import com.AnnotationPlatform.Core.dao.ITacheRepository;
import com.AnnotationPlatform.Core.services.ITacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class TacheServiceImpl implements ITacheService {


    @Autowired
    private ITacheRepository tacheRepository;

    @Autowired
    private IDatasetRepository datasetRepository;

    @Autowired
    private ICoupleTextRepository coupleTextRepository;

    @Autowired
    private IAnnotateurRepository annotateurRepository;


    public void divideDatasetIntoTasks(long datasetId, String dateLimit) {
        Dataset dataset = datasetRepository.findById(datasetId)
                .orElseThrow(() -> new RuntimeException("Dataset not found"));

        List<CoupleText> allCoupleTexts = coupleTextRepository.findByDatasetId(dataset.getId());
        int total = allCoupleTexts.size();
        int perTask = total / 10;

        for (int i = 0; i < 10; i++) {
            Tache tache = new Tache();
            tache.setDataset(dataset);
            tache.setDateLimit(dateLimit);

            // Assign subset
            int start = i * perTask;
            int end = (i == 10 - 1) ? total : start + perTask;
            List<CoupleText> subList = allCoupleTexts.subList(start, end);

            tache.getCoupleTexts().addAll(subList);
            tacheRepository.save(tache);
        }
    }


    public List<Tache> getTachesByDatasetId(long datasetId){
        return tacheRepository.findByDataset_Id(datasetId);
    }


    public void assignAnnotatorsToDataset(List<Tache> tacheList, List<Long> annotatorListId){

        // Shuffle tasks to make the distribution random
        Collections.shuffle(tacheList);

        int annotatorCount = annotatorListId.size();
        if (annotatorCount == 0) {
            throw new IllegalArgumentException("Annotator list is empty");
        }

        // assign tasks to annotators
        for (int i = 0; i < tacheList.size(); i++) {
            Long annotateurId = annotatorListId.get(i % annotatorCount);

            Optional<Annotateur> annotateurOpt = annotateurRepository.findById(annotateurId);
            if (annotateurOpt.isPresent()) {
                Tache tache = tacheList.get(i);
                tache.setAnnotateur(annotateurOpt.get());
                tacheRepository.save(tache);
            } else {
                throw new IllegalArgumentException("Annotator with ID " + annotateurId + " not found.");
            }
        }
    }


}
