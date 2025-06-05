package com.AnnotationPlatform.Core.dao;

import com.AnnotationPlatform.Core.bo.Annotateur;
import com.AnnotationPlatform.Core.bo.Role;
import com.AnnotationPlatform.Core.dto.AnnotateurDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
@Repository

public interface IAnnotateurRepository extends JpaRepository<Annotateur, Long> {

    List<Annotateur> findByNom(String nom);
    List<Annotateur> findByPrenom(String prenom);
    Optional<Annotateur> findByLogin(String login);
    List<Annotateur> findByRole(Role role);
    void deleteById(Long id);
    @Query("""
    SELECT new com.AnnotationPlatform.Core.dto.AnnotateurDTO
    (
        d.id,
        d.nom,
        d.prenom
            )
        FROM Annotateur d
        WHERE d.isActive = true
    """)
    Page<AnnotateurDTO> findAllAnnotator(Pageable pageable);
}
