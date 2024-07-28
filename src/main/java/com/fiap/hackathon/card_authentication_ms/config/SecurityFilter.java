package com.fiap.hackathon.card_authentication_ms.config;

import com.fiap.hackathon.card_authentication_ms.repository.UserRepository;
import com.google.gson.Gson;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        var tokenJWT = getToken(request);
        logger.info("JWT ==> " + new Gson().toJson(tokenJWT));

        try {
            if (tokenJWT != null) {
                var subject = tokenService.getSubject(tokenJWT);
                var user = userRepository.findByUsername(subject);
                logger.info("USER ==> " + new Gson().toJson(user));
                logger.info("SUBJECT ==> " + new Gson().toJson(subject));
                var authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                logger.info("AUTHENTICATION ==> " + new Gson().toJson(authentication));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception e) {
            SecurityContextHolder.clearContext();
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());
            return;
        }

        filterChain.doFilter(request, response);

    }

    private String getToken(HttpServletRequest request) {
        var authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader != null) {
            return authorizationHeader.replace("Bearer ", "");
        }

        return null;
    }
}
