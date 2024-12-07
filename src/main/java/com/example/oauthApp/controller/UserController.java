package com.example.oauthApp.controller;

import com.example.oauthApp.dto.UserInfo;
import com.example.oauthApp.model.User;
import com.example.oauthApp.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @GetMapping("/user")
    public UserInfo user(@AuthenticationPrincipal OAuth2User principal, Model model) {
        User user = service.findByEmail( principal.getAttribute("email"));
        return new UserInfo(principal.getAttribute("name"), principal.getAttribute("picture"), principal.getAttribute("email"), user.getRole());
    }
}
