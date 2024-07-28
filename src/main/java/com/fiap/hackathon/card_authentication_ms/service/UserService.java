package com.fiap.hackathon.card_authentication_ms.service;

import com.fiap.hackathon.card_authentication_ms.model.User;

import java.util.List;

public interface UserService {
    List<User> getUsers();
    User getUserByUsername(String username);
    boolean existsByUsername(String username);
    void save(User user);
}
