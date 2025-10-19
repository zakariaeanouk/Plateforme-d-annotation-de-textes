package com.AnnotationPlatform;

import com.AnnotationPlatform.Core.bo.Tache;
import com.AnnotationPlatform.Core.dao.ITacheRepository;
import com.AnnotationPlatform.Core.services.ITacheService;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class AnnotationPlatformApplicationTests {

    @Autowired
    private ITacheService tacheService;



    @Test
    void testFinddataset(){
        List<Tache> techesOfDataset = tacheService.getTachesByDatasetId(1L);
        assertNotNull(techesOfDataset, "The list of tasks should not be null");
        assertFalse(techesOfDataset.isEmpty(), "The list of tasks should not be empty"); // Optional

    }

}
