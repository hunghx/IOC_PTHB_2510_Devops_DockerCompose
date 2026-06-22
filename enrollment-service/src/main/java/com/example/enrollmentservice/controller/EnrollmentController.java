package com.example.enrollmentservice.controller;

import com.example.enrollmentservice.client.CourseClient;
import com.example.enrollmentservice.client.UserClient;
import com.example.enrollmentservice.dto.EnrollmentDto;
import com.example.enrollmentservice.model.Enrollment;
import com.example.enrollmentservice.service.EnrollmentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/enrollments")
public class EnrollmentController {
    private final EnrollmentService enrollmentService;
    private final UserClient userClient;
    private final CourseClient courseClient;

    public EnrollmentController(EnrollmentService enrollmentService, UserClient userClient, CourseClient courseClient) {
        this.enrollmentService = enrollmentService;
        this.userClient = userClient;
        this.courseClient = courseClient;
    }

    @GetMapping("/health")
    public String health() { return "enrollment-service: OK"; }

    @GetMapping
    public List<EnrollmentDto> listAll() {
        return enrollmentService.findByUserId(null).stream().map(e -> {
            EnrollmentDto d = new EnrollmentDto(); d.id = e.getId(); d.userId = e.getUserId(); d.courseId = e.getCourseId(); d.status = e.getStatus(); return d;
        }).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EnrollmentDto> get(@PathVariable("id") Long id) {
        return enrollmentService.getById(id).map(e -> {
            EnrollmentDto d = new EnrollmentDto(); d.id = e.getId(); d.userId = e.getUserId(); d.courseId = e.getCourseId(); d.status = e.getStatus(); return ResponseEntity.ok(d);
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<EnrollmentDto> create(@RequestBody EnrollmentDto body) {
        Enrollment e = new Enrollment(); e.setUserId(body.userId); e.setCourseId(body.courseId); e.setStatus(body.status == null ? "ENROLLED" : body.status);
        Enrollment saved = enrollmentService.create(e);
        EnrollmentDto d = new EnrollmentDto(); d.id = saved.getId(); d.userId = saved.getUserId(); d.courseId = saved.getCourseId(); d.status = saved.getStatus();
        return ResponseEntity.created(URI.create("/enrollments/" + d.id)).body(d);
    }

    @GetMapping("/users/{userId}")
    public List<EnrollmentDto> getByUser(@PathVariable("userId") Long userId) {
        return enrollmentService.findByUserId(userId).stream().map(e -> { EnrollmentDto d = new EnrollmentDto(); d.id = e.getId(); d.userId = e.getUserId(); d.courseId = e.getCourseId(); d.status = e.getStatus(); return d; }).collect(Collectors.toList());
    }
}
