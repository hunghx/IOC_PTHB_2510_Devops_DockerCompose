package com.example.authservice.controller;

import com.example.authservice.dto.UserDto;
import com.example.authservice.model.User;
import com.example.authservice.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) { this.userService = userService; }

    @GetMapping("/health")
    public String health() { return "auth-service: OK"; }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUser(@PathVariable("id") Long id) {
        Optional<User> u = userService.getById(id);
        return u.map(user -> {
            UserDto dto = new UserDto();
            dto.id = user.getId();
            dto.username = user.getUsername();
            dto.email = user.getEmail();
            dto.role = user.getRole();
            return ResponseEntity.ok(dto);
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<UserDto> createUser(@RequestBody UserDto body) {
        User u = new User();
        u.setUsername(body.username);
        u.setEmail(body.email);
        u.setPassword(body.role == null ? "" : ""); // password handling omitted per requirements
        u.setRole(body.role);
        User saved = userService.create(u);
        UserDto dto = new UserDto();
        dto.id = saved.getId();
        dto.username = saved.getUsername();
        dto.email = saved.getEmail();
        dto.role = saved.getRole();
        return ResponseEntity.created(URI.create("/users/" + dto.id)).body(dto);
    }
}
