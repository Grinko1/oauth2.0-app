package com.example.oauthApp.controller;

import com.example.oauthApp.service.SocialAppService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@PreAuthorize("hasRole('ADMIN')")
@RestController
public class AdminController {
    @GetMapping("/admin")

    public String adminPage() {
        System.out.println("admin controller");
        return "Only admins things will be here ";
    }
}


