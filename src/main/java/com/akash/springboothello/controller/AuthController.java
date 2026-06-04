package com.akash.springboothello.controller;

import com.akash.springboothello.dto.LoginRequest;
import com.akash.springboothello.security.JwtService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final JwtService jwtService;

    public AuthController(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @PostMapping("/login")
    public String login(@RequestBody LoginRequest request) {

        if ("akash".equals(request.getUsername())
                && "password123".equals(request.getPassword())) {

            return jwtService.generateToken(
                    request.getUsername());
        }

        throw new RuntimeException("Invalid credentials");
    }
}