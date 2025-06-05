package com.AnnotationPlatform.Core.services.Impl;

import com.AnnotationPlatform.Core.bo.CoupleText;
import com.AnnotationPlatform.Core.bo.Dataset;
import com.AnnotationPlatform.Core.dao.ICoupleTextRepository;
import com.AnnotationPlatform.Core.dao.IDatasetRepository;
import com.AnnotationPlatform.Core.services.ICoupleText;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@Service
public class CoupleTextImpl implements ICoupleText {

    @Autowired
    ICoupleTextRepository coupleTextRepository;

    @Autowired
    IDatasetRepository datasetRepository;


    @Override
    public void parseFileAndSaveCoupleText(MultipartFile datasetFile, long idDataset) throws IOException {

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(datasetFile.getInputStream()))) {
            String line;
            boolean skipHeader = true;
            Dataset dataset = datasetRepository.findById(idDataset).orElseThrow(() -> new RuntimeException("Dataset with id :"+idDataset+" was not found"));


            while ((line = reader.readLine()) != null) {
                if (skipHeader) {
                    skipHeader = false;
                    continue;
                }

                String[] texts = line.split(",");

                if (texts.length != 2) {
                    continue;
                }

                CoupleText coupleText = new CoupleText();
                coupleText.setText1(texts[0].trim());
                coupleText.setText2(texts[1].trim());
                coupleText.setDataset(dataset);

                coupleTextRepository.save(coupleText);
            }
        }

    }

    @Override
    public Page<CoupleText> getCoupleTextsByDatasetId(long idDataset, Pageable pageable) {
        return coupleTextRepository.findByDatasetId(idDataset,pageable);
    }


}
