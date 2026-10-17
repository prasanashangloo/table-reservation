// Author/Owner: Prasana Shangloo (23102A0076) | Copyright (c) 2026
package com.example.reservation.web;

import com.example.reservation.model.AppUser;
import com.example.reservation.service.AuthService;
import jakarta.validation.Valid;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final AuthService auth;

    public UserController(AuthService auth) {
        this.auth = auth;
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(@Valid @RequestBody Dtos.RegisterRequest req) {
        AppUser u = auth.register(req.name(), req.email(), req.password());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of("id", u.getId(), "name", u.getName(), "email", u.getEmail()));
    }

    @PostMapping("/login")
    public AuthService.Session login(@Valid @RequestBody Dtos.LoginRequest req) {
        return auth.login(req.email(), req.password());
    }
}
