package com.fiap.hackathon.card_authentication_ms.controller;

import com.fiap.hackathon.card_authentication_ms.config.DataTokenJWT;
import com.fiap.hackathon.card_authentication_ms.config.TokenService;
import com.fiap.hackathon.card_authentication_ms.model.AuthenticationRequestDto;
import com.fiap.hackathon.card_authentication_ms.model.Role;
import com.fiap.hackathon.card_authentication_ms.model.User;
import com.fiap.hackathon.card_authentication_ms.model.UserRequestDto;
import com.fiap.hackathon.card_authentication_ms.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthenticationManager manager;

    @Autowired
    private TokenService tokenService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid AuthenticationRequestDto request) {
        System.out.println("senhas ==> " + request.password());
        var authenticationToken = new UsernamePasswordAuthenticationToken(request.username(), request.password());
        var authentication = manager.authenticate(authenticationToken);
        var tokenJWT = tokenService.gerarToken((User) authentication.getPrincipal());

        return ResponseEntity.ok(new DataTokenJWT(tokenJWT));
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserRequestDto request) {
        if (userRepository.existsByUsername(request.username())) {
            return ResponseEntity.badRequest().body("Username is already taken!");
        }
        User user = convertUserRequestDtoToUserEntity(request);
        user.setPassword(new BCryptPasswordEncoder().encode(request.password()));
        user.setRoles(Collections.singleton(new Role(1L, "ROLE_USER")));
        userRepository.save(user);

        return ResponseEntity.ok("User registered successfully");
    }

    private User convertUserRequestDtoToUserEntity(UserRequestDto request) {
        User user = new User();
        user.setUsername(request.username());
        user.setPassword(request.password());
        return user;
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        // Implementação do logout (opcional)
        return ResponseEntity.ok("Logged out successfully");
    }
}
