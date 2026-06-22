package com.example.enrollmentservice.service;

import com.example.enrollmentservice.model.Enrollment;
import com.example.enrollmentservice.repository.EnrollmentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EnrollmentService {
    private final EnrollmentRepository repo;

    public EnrollmentService(EnrollmentRepository repo) { this.repo = repo; }

    public Enrollment create(Enrollment e) { return repo.save(e); }
    public Optional<Enrollment> getById(Long id) { return repo.findById(id); }
    public List<Enrollment> findByUserId(Long userId) { return repo.findByUserId(userId); }
}
