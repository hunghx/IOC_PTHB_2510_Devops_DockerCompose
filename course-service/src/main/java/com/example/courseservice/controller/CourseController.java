package com.example.courseservice.controller;

import com.example.courseservice.client.UserClient;
import com.example.courseservice.dto.CourseDto;
import com.example.courseservice.model.Course;
import com.example.courseservice.service.CourseService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/courses")
public class CourseController {
    private final CourseService courseService;
    private final UserClient userClient;

    public CourseController(CourseService courseService, UserClient userClient) {
        this.courseService = courseService;
        this.userClient = userClient;
    }

    @GetMapping("/health")
    public String health() { return "course-service: OK"; }

    @GetMapping
    public List<CourseDto> list() {
        return courseService.listAll().stream().map(c -> {
            CourseDto d = new CourseDto();
            d.id = c.getId(); d.title = c.getTitle(); d.description = c.getDescription(); d.instructorId = c.getInstructorId(); d.price = c.getPrice();
            return d;
        }).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CourseDto> get(@PathVariable("id") Long id) {
        Optional<Course> c = courseService.getById(id);
        return c.map(course -> {
            CourseDto d = new CourseDto();
            d.id = course.getId(); d.title = course.getTitle(); d.description = course.getDescription(); d.instructorId = course.getInstructorId(); d.price = course.getPrice();
            return ResponseEntity.ok(d);
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<CourseDto> create(@RequestBody CourseDto body) {
        Course c = new Course();
        c.setTitle(body.title);
        c.setDescription(body.description);
        c.setInstructorId(body.instructorId);
        c.setPrice(body.price);
        Course saved = courseService.create(c);
        CourseDto d = new CourseDto();
        d.id = saved.getId(); d.title = saved.getTitle(); d.description = saved.getDescription(); d.instructorId = saved.getInstructorId(); d.price = saved.getPrice();
        return ResponseEntity.created(URI.create("/courses/" + d.id)).body(d);
    }

    @GetMapping("/{id}/with-instructor")
    public ResponseEntity<Map<String, Object>> getWithInstructor(@PathVariable("id") Long id) {
        Optional<Course> c = courseService.getById(id);
        return c.map(course -> {
            String instructor = "";
            try {
                instructor = userClient.getUser(String.valueOf(course.getInstructorId()));
            } catch (Exception ex) {
                instructor = "(unavailable)";
            }
            CourseDto d = new CourseDto();
            d.id = course.getId(); d.title = course.getTitle(); d.description = course.getDescription(); d.instructorId = course.getInstructorId(); d.price = course.getPrice();
            return ResponseEntity.ok(Map.of("course", d, "instructor", instructor));
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }
}
