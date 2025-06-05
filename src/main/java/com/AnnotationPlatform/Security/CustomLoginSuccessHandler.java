package com.AnnotationPlatform.Security;

import com.AnnotationPlatform.Core.bo.Utilisateur;
import com.AnnotationPlatform.Core.bo.UtilisateurPrincipal;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collection;


@Component
public class CustomLoginSuccessHandler implements AuthenticationSuccessHandler {


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {

        Object principal = authentication.getPrincipal();
        String redirectUrl = null;

        HttpSession session = request.getSession();

        if (principal instanceof UtilisateurPrincipal userPrincipal) {
            Utilisateur utilisateur = userPrincipal.getUtilisateur();
            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

            for (GrantedAuthority authority : authorities) {
                String role = authority.getAuthority();
                System.out.println("Authority: " + role);

                if (role.equals("ROLE_ADMIN")) {
                    redirectUrl = "/admin/";
                    session.setAttribute("infoMsg", "Welcome, Admin! You have successfully logged in.");
                    break;
                } else if (role.equals("ROLE_USER")) {
                    redirectUrl = "/user/";
                    session.setAttribute("user", utilisateur);
                    session.setAttribute("infoMsg", "Welcome back M " + utilisateur.getNom().toUpperCase() + " " + utilisateur.getPrenom() + "! Your tasks are ready.");
                    break;
                }
            }
        }

        if (redirectUrl == null) {
            redirectUrl = "/showMyLoginPage?error=unauthorized";
        }

        System.out.println("Redirecting to: " + redirectUrl);
        response.sendRedirect(redirectUrl);
    }

}
