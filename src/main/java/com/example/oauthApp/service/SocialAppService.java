package com.example.oauthApp.service;
import com.example.oauthApp.model.Role;
import com.example.oauthApp.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class SocialAppService implements OAuth2UserService<OAuth2UserRequest, OAuth2User>{
    private final UserService userService;
    private static final Logger logger = LoggerFactory.getLogger(SocialAppService.class);
    public SocialAppService(UserService userService) {
        this.userService = userService;
    }

    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {
        logger.info("Begin loading user");
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);
        logger.info("User from google: {}", oAuth2User);


        String email = oAuth2User.getAttribute("email");
        User user = userService.findByEmail(email);
                if (user == null) {
            user = new User(email, Role.USER);
            userService.createUser(user);
            logger.info("New user created: {}", email);
        }


        // Преобразуем роль в Spring Security GrantedAuthority
        List<GrantedAuthority> authorities = new ArrayList<>();
        if (user.getRole() == Role.ADMIN) {
            authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        }

        // Возвращаем OAuth2User с заданной ролью
        logger.info("User : {} with role: {} authenticated", email, user.getRole());
        return new DefaultOAuth2User(authorities, oAuth2User.getAttributes(), "email");
    }

    }
