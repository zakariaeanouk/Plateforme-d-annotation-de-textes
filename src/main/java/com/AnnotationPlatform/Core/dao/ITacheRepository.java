package com.AnnotationPlatform.Core.dao;

import com.AnnotationPlatform.Core.bo.Annotateur;
import com.AnnotationPlatform.Core.bo.Dataset;
import com.AnnotationPlatform.Core.bo.Tache;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;
@Repository
public interface ITacheRepository extends JpaRepository<Tache, Long> {
    Optional<Tache> findByDateLimit(String dateLimit);
    List<Tache> findByDataset_Id(long datasetId);

    Optional<Tache> findByAnnotateur(Annotateur annotateur);

    List<Tache> findTacheByAnnotateur(Annotateur annotateur);

    List<Tache> findTachesByAnnotateur(Annotateur annotateur, Sort sort);

    List<Tache> findTachesByAnnotateur(Annotateur annotateur);

    Object getTacheById(long id);

    Tache getTacheByDataset(Dataset dataset);

    List<Tache> getTachesByDataset(Dataset dataset);
}
