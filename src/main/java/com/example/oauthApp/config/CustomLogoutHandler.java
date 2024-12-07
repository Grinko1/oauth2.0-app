package com.example.oauthApp.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

@Component
public class CustomLogoutHandler implements LogoutHandler {
    private static final Logger logger = LoggerFactory.getLogger(CustomLogoutHandler.class);

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        if (authentication != null) {
            String username = authentication.getName();
            logger.info("User '{}' just has logged out.", username);
            SecurityContextHolder.clearContext();


            // Проверяем, что authentication стал null
            Authentication authenticationAfterClear = SecurityContextHolder.getContext().getAuthentication();
            if (authenticationAfterClear == null) {
                logger.info("Authentication context has been cleared. Authentication is now null.");
            } else {
                logger.info("Authentication still exists: " + authenticationAfterClear);
            }
        }
    }
}