package com.example.notificationservice.controller;

import com.example.notificationservice.dto.NotificationDto;
import com.example.notificationservice.model.NotificationLog;
import com.example.notificationservice.service.NotificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/notifications")
public class NotificationController {
    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) { this.notificationService = notificationService; }

    @GetMapping("/health")
    public String health() { return "notification-service: OK"; }

    @PostMapping
    public ResponseEntity<String> sendNotification(@RequestBody NotificationDto body) {
        NotificationLog n = new NotificationLog();
        n.setUserId(body.userId);
        n.setMessage(body.message);
        notificationService.save(n);
        return ResponseEntity.ok("sent");
    }
}
