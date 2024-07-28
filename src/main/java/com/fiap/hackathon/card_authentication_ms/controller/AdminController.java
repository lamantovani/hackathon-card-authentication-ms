package com.fiap.hackathon.card_authentication_ms.controller;

import com.fiap.hackathon.card_authentication_ms.model.Role;
import com.fiap.hackathon.card_authentication_ms.model.User;
import com.fiap.hackathon.card_authentication_ms.model.UserRequestDto;
import com.fiap.hackathon.card_authentication_ms.model.UserResponseDto;
import com.fiap.hackathon.card_authentication_ms.repository.UserRepository;
import com.fiap.hackathon.card_authentication_ms.service.UserService;
import jakarta.validation.constraints.NotNull;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private UserService userService;

    private static final Log logger = LogFactory.getLog(AdminController.class);

    @GetMapping("/users")
    public ResponseEntity<?> getUsers() {
        logger.info("GET_USERS-ADMIN...");
        return ResponseEntity.ok(userService.getUsers().stream().map(UserResponseDto::new));
    }

    @GetMapping("/users/{username}")
    public ResponseEntity<?> getUserByUsername(@PathVariable @NotNull String username) {
        logger.info("GET_USER_BY_USERNAME-ADMIN...");
        return ResponseEntity.ok(userService.getUserByUsername(username));
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserRequestDto request) {
        if (userService.existsByUsername(request.username())) {
            return ResponseEntity.badRequest().body("Username is already taken!");
        }
        User user = convertUserRequestDtoToUserEntity(request);
        user.setPassword(new BCryptPasswordEncoder().encode(request.password()));
        user.setRoles(Collections.singleton(new Role(null, "ROLE_ADMIN")));
        userService.save(user);

        return ResponseEntity.ok("User registered successfully");
    }

    private User convertUserRequestDtoToUserEntity(UserRequestDto request) {
        User user = new User();
        user.setUsername(request.username());
        user.setPassword(request.password());
        return user;
    }
}
