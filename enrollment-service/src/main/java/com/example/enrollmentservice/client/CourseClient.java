package com.example.enrollmentservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "course-service", url = "${course.service.url:http://localhost:8082}")
public interface CourseClient {
    @GetMapping("/courses/{id}")
    String getCourse(@PathVariable("id") String id);
}
