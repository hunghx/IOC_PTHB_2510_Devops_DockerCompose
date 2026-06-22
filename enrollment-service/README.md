Enrollment service. Port: 8083

Endpoints:
- GET /enrollments
- GET /enrollments/{id}
- POST /enrollments
- GET /enrollments/users/{userId}

Uses Feign to call auth-service and course-service as needed.
