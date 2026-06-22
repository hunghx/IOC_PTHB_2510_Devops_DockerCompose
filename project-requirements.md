# Yêu cầu dự án: Hệ thống Education (Spring Boot, Microservices)

## Tổng quan
Tài liệu này mô tả yêu cầu tối giản cho một hệ thống education theo kiến trúc microservice, bao gồm cấu trúc thư mục, phiên bản công nghệ, dependencies, build system và phân tách nghiệp vụ theo module. Không yêu cầu validate dữ liệu và xử lý ngoại lệ tùy chỉnh.

---

## 1. Cấu trúc thư mục chuẩn tối giản (mỗi service)
Mỗi microservice là một project Spring Boot độc lập (Maven/Gradle). Cấu trúc tối giản cho mỗi service:

- service-name/
  - src/
    - main/
      - java/com/example/servicename/
        - ServiceNameApplication.java
        - controller/
        - service/
        - repository/
        - model/
        - dto/
      - resources/
        - application.yml
        - static/ (nếu cần)
    - test/
  - pom.xml (hoặc build.gradle)
  - Dockerfile (tùy chọn)
  - README.md

Các module đặc thù (ví dụ gateway, config) giữ cấu trúc tương tự, nhưng không nhất thiết chứa repository nếu chỉ làm cầu nối.

---

## 2. Phiên bản, JDK, dependencies, build system
- JDK: tối thiểu Java 17 (Hỗ trợ), Khuyến nghị Java 21 (LTS)
- Spring Boot: 3.2.x (hoặc phiên bản 3.x mới nhất tương thích với JDK)
- Build system: Maven (pom.xml) hoặc Gradle (Gradle Kotlin DSL) — chọn 1 cho toàn bộ mono-repo/quốc tế

Dependencies cơ bản (mỗi service chọn theo trách nhiệm):
- spring-boot-starter-web (REST API)
- spring-boot-starter-actuator
- spring-boot-starter-data-jpa (nếu cần persistence)
- driver DB: postgresql (hoặc mysql)
- spring-boot-starter-security (nếu có auth cơ bản)
- spring-cloud-starter-openfeign (inter-service REST client)
- spring-cloud-starter-bootstrap / config client (nếu dùng Spring Cloud Config)
- spring-boot-starter-amqp hoặc spring-kafka (nếu dùng messaging)
- resilience4j-spring-boot2 (circuit breaker, retries) hoặc spring-retry
- lombok (tùy chọn)
- test: spring-boot-starter-test

Phiên bản Spring Cloud (nếu dùng): Spring Cloud 2023.x hoặc tương đương theo BOM tương thích với Spring Boot 3.2.x.

Ví dụ quản lý dependency: sử dụng BOM của Spring Boot và Spring Cloud trong pom/gradle.

---

## 3. Nghiệp vụ cơ bản — phân tách module (microservices)
Gợi ý các service chính cho hệ thống Education (mô tả trách nhiệm, API chính, datastore):

1) api-gateway (Gateway)
- Chức năng: Route request tới các service, có thể làm authentication/authorization trung gian.
- Không lưu dữ liệu.

2) auth-service / user-service
- Trách nhiệm: Quản lý tài khoản người dùng (student, instructor, admin), đăng ký, đăng nhập, profile, role.
- API chính: /users, /users/{id}, /auth/login, /auth/register
- Datastore: users DB (Postgres)

3) course-service
- Trách nhiệm: Quản lý khoá học, chương, bài học, metadata (title, description, instructorId, category, price)
- API chính: /courses, /courses/{id}, /courses/{id}/lessons
- Datastore: courses DB

4) enrollment-service (hoặc subscription-service)
- Trách nhiệm: Quản lý ghi danh học viên vào khoá học, trạng thái enrollment, lịch sử học
- API chính: /enrollments, /enrollments/{id}, /users/{id}/enrollments
- Datastore: enrollments DB

5) notification-service
- Trách nhiệm: Gửi email/notification cho các event (enrollment, payment success, course updates). Có thể dùng queue để xử lý async.
- Integration: subscribe event từ message broker hoặc nhận HTTP callbacks từ các service khác
- Datastore: logs/queue metadata (tùy chọn)


Giao tiếp giữa services:
- Đồng bộ: REST over HTTPS (OpenFeign) cho các yêu cầu cần phản hồi ngay
- Bất đồng bộ: Message broker (RabbitMQ / Kafka) để phát event (UserRegistered, CoursePublished, EnrollmentCreated, PaymentCompleted) và giảm coupling

Data ownership:
- Mỗi service sở hữu schema riêng và chỉ truy vấn dữ liệu của mình. Để lấy dữ liệu cần thiết từ service khác, gọi API chuyên biệt hoặc subscribe events

---


## 4. Các lưu ý triển khai tối thiểu
- Config: sử dụng application.yml riêng cho mỗi service; secrets/credentials giữ ngoài (env vars hoặc secret manager).  
- Containerization: mỗi service đóng gói thành jar và Docker image (Dockerfile tối giản).  
- Observability: bật actuator endpoints cơ bản, logs đủ để debug.
- Testing: unit test/service-level tests (không bắt buộc e2e trong yêu cầu).

---

## 5. Ví dụ ngắn pom.xml (Maven) - phần phụ thuộc chính
```xml
<properties>
  <java.version>17</java.version>
  <spring-boot.version>3.2.0</spring-boot.version>
</properties>
<dependencyManagement>
  <dependencies>
    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-dependencies</artifactId>
      <version>${spring-boot.version}</version>
      <type>pom</type>
      <scope>import</scope>
    </dependency>
  </dependencies>
</dependencyManagement>
<dependencies>
  <dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
  </dependency>
  <dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa</artifactId>
  </dependency>
  <dependency>
    <groupId>org.postgresql</groupId>
    <artifactId>postgresql</artifactId>
  </dependency>
  <dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-openfeign</artifactId>
  </dependency>
</dependencies>
```

---

## Kết
Tài liệu này cung cấp yêu cầu tối giản để bắt đầu triển khai hệ thống Education theo microservices. Nếu muốn, có thể mở rộng thêm chi tiết từng service (API contract, ERD, sequence diagrams) ở bước tiếp theo.
