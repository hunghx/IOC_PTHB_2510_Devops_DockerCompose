package com.example.courseservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "auth-service", url = "${user.service.url:http://auth-service:8081}")
public interface UserClient {

    @GetMapping("/users/{id}")
    String getUser(@PathVariable("id") String id);
}
