package com.example.oauthApp.controller;

import com.example.oauthApp.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private UserService userService;



    @Test
    void testUserPage() throws Exception {
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("name", "John Doe");
        attributes.put("email", "johndoe@example.com");
        attributes.put("picture", "profile-pic-url");

        OAuth2User oauth2User = new OAuth2User() {
            @Override
            public Map<String, Object> getAttributes() {
                return attributes;
            }

            @Override
            public Collection<? extends GrantedAuthority> getAuthorities() {
                return Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
            }

            @Override
            public String getName() {
                return "John Doe";
            }
        };

        OAuth2AuthenticationToken oauth2AuthenticationToken = new OAuth2AuthenticationToken(
                oauth2User,
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")),
                "john.doe"
        );


        mockMvc.perform(MockMvcRequestBuilders.get("/user")
                        .principal(oauth2AuthenticationToken))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("John Doe"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("johndoe@example.com"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.role").value("USER"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.picture").value("profile-pic-url"));
    }

}