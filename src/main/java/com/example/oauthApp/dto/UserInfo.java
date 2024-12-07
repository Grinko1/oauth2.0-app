package com.example.oauthApp.dto;

import com.example.oauthApp.model.Role;

public record UserInfo(String name, String picture, String email, Role role) {
}
