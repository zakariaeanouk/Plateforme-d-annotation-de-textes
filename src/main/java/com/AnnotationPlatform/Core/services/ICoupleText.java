package com.AnnotationPlatform.Core.services;

import com.AnnotationPlatform.Core.bo.CoupleText;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ICoupleText {


    void parseFileAndSaveCoupleText(MultipartFile file, long idDataset) throws IOException;

    Page<CoupleText> getCoupleTextsByDatasetId(long datasetId, Pageable pageable);

}
