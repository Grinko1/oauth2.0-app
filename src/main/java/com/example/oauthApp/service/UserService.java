package com.example.oauthApp.service;

import com.example.oauthApp.model.User;
import com.example.oauthApp.repository.UserRepository;
import org.springframework.stereotype.Service;


@Service
public class UserService {
    private final UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    public User findByEmail(String email){
        return repository.findByEmail(email);
    }
    public void createUser(User user){
        repository.save(user);
    }
}
