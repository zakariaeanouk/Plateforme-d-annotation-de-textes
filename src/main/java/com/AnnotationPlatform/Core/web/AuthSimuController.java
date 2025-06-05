package com.AnnotationPlatform.Core.web;

import com.AnnotationPlatform.Core.bo.Annotateur;
import com.AnnotationPlatform.Core.dao.IAnnotateurRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthSimuController {

    @Autowired
    private IAnnotateurRepository annotateurRepository;

    @GetMapping("/simulate-login")
    public String simulateLogin(HttpSession session) {
        // Simuler la récupération de l'utilisateur avec l'ID = 4
        Annotateur user = annotateurRepository.findById(15L).orElse(null);
        if (user != null) {
            System.out.println("user found");
            session.setAttribute("user", user); // mettre l'utilisateur dans la session
            return "redirect:/user/"; // rediriger vers une page protégée
        } else {
            System.out.println("user is null");
            return "redirect:/error"; // ou une page d'erreur
        }
    }
}
