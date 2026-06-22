package com.example.notificationservice.service;

import com.example.notificationservice.model.NotificationLog;
import com.example.notificationservice.repository.NotificationRepository;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {
    private final NotificationRepository repo;

    public NotificationService(NotificationRepository repo) { this.repo = repo; }

    public NotificationLog save(NotificationLog n) { return repo.save(n); }
}
