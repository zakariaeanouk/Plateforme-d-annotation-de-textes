package com.AnnotationPlatform.Core.dao;

import com.AnnotationPlatform.Core.bo.ClassePossible;
import com.AnnotationPlatform.Core.bo.Dataset;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import org.springframework.stereotype.Repository;
@Repository
public interface IClassePossibleRepository extends JpaRepository<ClassePossible, Long> {
    List<ClassePossible> findByTextClasse(String textClasse);

    List<ClassePossible> findByDataset(Dataset dataset);

    List<ClassePossible> getClassePossibleByDataset(Dataset dataset);
}
