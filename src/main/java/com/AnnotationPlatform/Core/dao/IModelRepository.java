package com.AnnotationPlatform.Core.dao;

import com.AnnotationPlatform.Core.bo.ModelTrainingLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IModelRepository extends JpaRepository<ModelTrainingLog , Long> {

}
