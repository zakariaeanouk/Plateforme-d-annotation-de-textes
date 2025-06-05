package com.AnnotationPlatform.Security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomLoginFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
                                        HttpServletResponse response,
                                        AuthenticationException exception) throws IOException {
        // For debugging
        exception.printStackTrace();

        String contextPath = request.getContextPath();
        String error;
        if (exception instanceof DisabledException) {
            error="disabled";
        } else if (exception instanceof LockedException) {
            error="locked";
        } else if (exception instanceof CredentialsExpiredException) {
            error="expired";
        } else if (exception instanceof org.springframework.security.authentication.BadCredentialsException) {
            error="bad credentials";
        } else {
            error="other";
        }
        response.sendRedirect("/login-error?error=" + error);
        System.out.println("Login failed due to: " + exception.getClass().getName());

    }
}
