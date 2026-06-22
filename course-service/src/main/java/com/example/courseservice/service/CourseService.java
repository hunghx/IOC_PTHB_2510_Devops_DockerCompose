package com.example.courseservice.service;

import com.example.courseservice.model.Course;
import com.example.courseservice.repository.CourseRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CourseService {
    private final CourseRepository repo;

    public CourseService(CourseRepository repo) { this.repo = repo; }

    public List<Course> listAll() { return repo.findAll(); }
    public Optional<Course> getById(Long id) { return repo.findById(id); }
    public Course create(Course c) { return repo.save(c); }
}
