package com.example.authservice.controller;

import com.example.authservice.dto.UserDto;
import com.example.authservice.model.User;
import com.example.authservice.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final UserService userService;

    public AuthController(UserService userService) { this.userService = userService; }

    @PostMapping("/register")
    public ResponseEntity<UserDto> register(@RequestBody UserDto body) {
        User u = new User();
        u.setUsername(body.username);
        u.setEmail(body.email);
        u.setPassword("");
        u.setRole(body.role);
        User saved = userService.create(u);
        UserDto dto = new UserDto();
        dto.id = saved.getId();
        dto.username = saved.getUsername();
        dto.email = saved.getEmail();
        dto.role = saved.getRole();
        return ResponseEntity.created(URI.create("/users/" + dto.id)).body(dto);
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody Map<String, String> body) {
        // minimal placeholder: returns ok with username, no real auth per requirements
        return ResponseEntity.ok(Map.of("status", "ok", "username", body.getOrDefault("username", "")));
    }
}
