package com.AnnotationPlatform.Core.dao;

import com.AnnotationPlatform.Core.bo.Role;
import com.AnnotationPlatform.Core.bo.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import org.springframework.stereotype.Repository;
@Repository
public interface IUtilisateurRepository extends JpaRepository<Utilisateur, Long> {
    List<Utilisateur> findByNom(String nom);
    List<Utilisateur> findByPrenom(String prenom);
    Utilisateur findByLogin(String login);
    List<Utilisateur> findByNomOrPrenom(String nom, String prenom);
    List<Utilisateur> findByRole(Role role);
}
