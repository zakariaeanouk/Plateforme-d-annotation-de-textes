package com.AnnotationPlatform.Core.services.Impl;

import com.AnnotationPlatform.Core.bo.Utilisateur;
import com.AnnotationPlatform.Core.bo.UtilisateurPrincipal;
import com.AnnotationPlatform.Core.dao.IUtilisateurRepository;
import jakarta.validation.constraints.Email;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailService implements UserDetailsService {

    @Autowired
    IUtilisateurRepository utilisateurRepository;

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {

        Utilisateur utilisateur = utilisateurRepository.findByLogin(login);
        if (utilisateur == null) {
            System.out.println("user not found");
            throw new UsernameNotFoundException("user not found");
        }
        return new UtilisateurPrincipal(utilisateur);
    }

}
