package com.AnnotationPlatform.Core.dao;

import com.AnnotationPlatform.Core.bo.Administrateur;
import com.AnnotationPlatform.Core.bo.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IAdminRepository extends JpaRepository<Administrateur, Long> {
    List<Administrateur> findByNom(String nom);

    @Query("SELECT COUNT(d) FROM Dataset d")
    long countAllDatasets();


    @Query("SELECT COUNT(A) FROM Annotateur A")
    long countAllAnnotators();

    List<Administrateur> findByPrenom(String prenom);

    Optional<Administrateur> findByLogin(String login);

    List<Administrateur> findByRole(Role role);

}
