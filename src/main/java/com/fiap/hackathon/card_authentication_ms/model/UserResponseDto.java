package com.fiap.hackathon.card_authentication_ms.model;

import java.util.Set;

public record UserResponseDto(String username, Set<Role> roles) {
    public UserResponseDto(User user) {
        this(user.getUsername(), user.getRoles());
    }

    private static String convertRoleToString(Role role) {
        return role.getName();
    }
}
