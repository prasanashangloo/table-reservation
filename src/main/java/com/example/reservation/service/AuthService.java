// Author/Owner: Prasana Shangloo (23102A0076) | Copyright (c) 2026
package com.example.reservation.service;

import com.example.reservation.model.AppUser;
import com.example.reservation.repo.UserRepository;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UserRepository users;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    private final Map<String, Long> tokens = new ConcurrentHashMap<>();

    public AuthService(UserRepository users) {
        this.users = users;
    }

    public AppUser register(String name, String email, String password) {
        String normalized = email.trim().toLowerCase();
        if (users.existsByEmail(normalized)) {
            throw new ApiException(HttpStatus.CONFLICT, "Email is already registered");
        }
        return users.save(new AppUser(name.trim(), normalized, encoder.encode(password)));
    }

    /** Returns [token, AppUser]-style result via Session. */
    public Session login(String email, String password) {
        AppUser user = users.findByEmail(email.trim().toLowerCase())
                .orElseThrow(() -> new ApiException(HttpStatus.UNAUTHORIZED, "Invalid email or password"));
        if (!encoder.matches(password, user.getPasswordHash())) {
            throw new ApiException(HttpStatus.UNAUTHORIZED, "Invalid email or password");
        }
        String token = UUID.randomUUID().toString();
        tokens.put(token, user.getId());
        return new Session(token, user.getId(), user.getName());
    }

    public Long requireUserId(String token) {
        Long id = token == null ? null : tokens.get(token);
        if (id == null) {
            throw new ApiException(HttpStatus.UNAUTHORIZED, "Please log in first");
        }
        return id;
    }

    public record Session(String token, Long userId, String name) {}
}
