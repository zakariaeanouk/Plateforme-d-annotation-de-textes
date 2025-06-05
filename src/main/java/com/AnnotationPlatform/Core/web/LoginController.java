package com.AnnotationPlatform.Core.web;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

    @Autowired
    private HttpSession httpSession;

    @GetMapping("/showMyLoginPage")
    public String showLoginForm(
//            @RequestParam(name = "error", required = false) String error,
            @RequestParam(name = "logout", required = false) String logout,
            HttpSession session,
            Model model) {

//        if (error != null) {
//            System.out.println("error");
//            handleLoginError(error, model);
//        }

        if (logout != null) {
            model.addAttribute("messageType", "success");
            model.addAttribute("message", "You have been successfully logged out.");
        }

        // Nettoyage session
        if (session.getAttribute("message") != null) {
            session.removeAttribute("message");
        }

        model.addAttribute("content", "USER/Login");
        model.addAttribute("title", "Login Page");
        return "USER/Login";
    }

    @GetMapping("/logout")
    public String logout() {
        httpSession.invalidate();
        return "redirect:/showMyLoginPage?logout";
    }

    // 🔹 Cette méthode reste privée car elle est utilitaire, non mappée
    private void handleLoginError(String error, Model model) {
        String message;
        switch (error) {
            case "disabled":
                message = "Your account is disabled. Please contact admin.";
                break;
            case "locked":
                message = "Your account is locked. Please try again later.";
                break;
            case "expired":
                message = "Your credentials have expired. Please reset your password.";
                break;
            case "bad_credentials":
                message = "Invalid username or password.";
                break;
            default:
                message = "Invalid username or password.";
        }
        model.addAttribute("messageType", "error");
        model.addAttribute("message", message);
        System.out.println("je suis dans l'erreur du controlleur ");
    }

    @GetMapping("/login-error")
    public String loginError(
            @RequestParam(name = "error", required = false) String error,
            Model model) {

        handleLoginError(error, model);

        model.addAttribute("content", "USER/Login");
        model.addAttribute("title", "Login Page");

        return "USER/Login";
    }

}
