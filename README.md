# 🧩 Ứng Dụng Quiz - Hệ Thống Microservices

Một nền tảng Quiz dựa trên kiến trúc microservices mạnh mẽ, có khả năng mở rộng cao, cho phép người dùng tạo, quản lý và tham gia vào các trò chơi quiz tương tác.

---

## 📋 Tổng Quan Hệ Thống

Ứng dụng Quiz này được xây dựng bằng kiến trúc microservices, cung cấp một nền tảng linh hoạt để tạo các câu đố, quản lý câu hỏi và tạo điều kiện cho các phiên quiz tương tác. Hệ thống triển khai khám phá dịch vụ, mẫu API gateway và containerization để đảm bảo khả năng mở rộng và bảo trì hiệu quả.

Hệ thống cho phép người dùng tạo các quiz đa dạng với nhiều loại câu hỏi khác nhau (trắc nghiệm, đúng/sai, câu đố, thanh trượt, phản hồi văn bản), tham gia vào các phiên quiz trực tiếp, theo dõi hiệu suất cá nhân và thống kê chi tiết. Nền tảng này lý tưởng cho các môi trường giáo dục, đào tạo doanh nghiệp và giải trí tương tác.

---

## 🏗️ Kiến Trúc Hệ Thống

Hệ thống bao gồm các microservices sau:

- **User Service (Dịch vụ Người dùng)**: Quản lý hồ sơ người dùng, mối quan hệ, tùy chọn và cài đặt. Lưu trữ thông tin cá nhân, lịch sử hoạt động, và dữ liệu liên quan đến người dùng. Dịch vụ này cung cấp API để đăng ký, cập nhật thông tin, và quản lý mối quan hệ giữa người dùng.

- **Quiz Service (Dịch vụ Quiz)**: Xử lý việc tạo và quản lý các quiz, bộ sưu tập quiz, và metadata quiz. Chịu trách nhiệm tổ chức nội dung và làm cho nó có thể được khám phá. Hỗ trợ phân loại, gắn thẻ và tìm kiếm quiz dựa trên nhiều tiêu chí.

- **Question Service (Dịch vụ Câu hỏi)**: Quản lý các loại câu hỏi đa dạng (đúng/sai, trắc nghiệm, thanh trượt, câu đố, phản hồi văn bản) và nội dung của chúng. Cung cấp API cho việc tạo, cập nhật và truy xuất câu hỏi. Hỗ trợ đa phương tiện như hình ảnh và âm thanh trong câu hỏi.

- **Gameplay Service (Dịch vụ Gameplay)**: Theo dõi các phiên quiz, phản hồi của người tham gia, điểm số và thống kê. Xử lý tương tác trò chơi theo thời gian thực và kết quả. Hỗ trợ nhiều chế độ chơi như chơi đơn, chơi theo nhóm, và thi đấu trực tiếp.

- **Identity Service (Dịch vụ Định danh)**: Xử lý xác thực, ủy quyền và bảo mật. Cấp token JWT và xác minh danh tính người dùng. Hỗ trợ đăng nhập bằng nhiều phương thức như email/mật khẩu, OAuth, và mạng xã hội.

- **API Gateway (Cổng API)**: Định tuyến yêu cầu từ bên ngoài đến các microservice thích hợp. Đóng vai trò là điểm vào duy nhất cho tất cả các yêu cầu của client. Cung cấp các tính năng như định tuyến, cân bằng tải, giới hạn tốc độ và giám sát.

- **Eureka Server (Máy chủ Eureka)**: Cung cấp khám phá và đăng ký dịch vụ. Giúp các dịch vụ định vị và giao tiếp với nhau một cách động. Cho phép hệ thống tự phục hồi khi các dịch vụ khởi động hoặc tắt.

Các dịch vụ giao tiếp chủ yếu thông qua REST API sử dụng các mẫu sau:

- Giao tiếp bên ngoài thông qua API Gateway
- Giao tiếp nội bộ thông qua Feign Clients, cho phép các lệnh gọi REST được khai báo theo kiểu khai báo
- Khám phá dịch vụ thông qua Eureka Server, cho phép các dịch vụ tìm thấy nhau mà không cần URL cứng
- Luồng xác thực sử dụng JWT tokens, đảm bảo tính bảo mật và toàn vẹn của dữ liệu

## 📁 Cấu Trúc Thư Mục

```
quiz-application/
├── README.md                       # File hướng dẫn này
├── docker-compose.yml              # Cấu hình Docker Compose để chạy tất cả dịch vụ
├── docs/                           # Thư mục tài liệu
│   ├── architecture.md             # Tài liệu thiết kế hệ thống
│   ├── analysis-and-design.md      # Chi tiết phân tích và thiết kế hệ thống
│   ├── asset/                      # Tài nguyên hình ảnh cho tài liệu
│   └── api-specs/                  # Đặc tả API theo chuẩn OpenAPI (YAML)
│       ├── gameplay-service.yaml   # Đặc tả API dịch vụ Gameplay
│       ├── identity-service.yaml   # Đặc tả API dịch vụ Identity
│       ├── question-service.yaml   # Đặc tả API dịch vụ Question
│       ├── quiz-service.yaml       # Đặc tả API dịch vụ Quiz
│       └── user-service.yaml       # Đặc tả API dịch vụ User
├── eureka-server/                  # Máy chủ khám phá dịch vụ
│   ├── Dockerfile                  # File cấu hình Docker cho Eureka Server
│   └── src/                        # Mã nguồn Eureka Server
├── gateway/                        # API Gateway
│   ├── Dockerfile                  # File cấu hình Docker cho API Gateway
│   └── src/                        # Mã nguồn API Gateway
├── services/                       # Các microservice của ứng dụng
│   ├── gameplay-service/           # Dịch vụ Gameplay
│   │   ├── Dockerfile              # File cấu hình Docker
│   │   └── src/                    # Mã nguồn dịch vụ
│   ├── identity-service/           # Dịch vụ Identity
│   │   ├── Dockerfile              # File cấu hình Docker
│   │   └── src/                    # Mã nguồn dịch vụ
│   ├── question-service/           # Dịch vụ Question
│   │   ├── Dockerfile              # File cấu hình Docker
│   │   └── src/                    # Mã nguồn dịch vụ
│   ├── quiz-service/               # Dịch vụ Quiz
│   │   ├── Dockerfile              # File cấu hình Docker
│   │   └── src/                    # Mã nguồn dịch vụ
│   └── user-service/               # Dịch vụ User
│       ├── Dockerfile              # File cấu hình Docker
│       └── src/                    # Mã nguồn dịch vụ
```

---

## 🚀 Bắt Đầu Sử Dụng

### 1. Clone repository này

```bash
git clone https://github.com/your-username/quiz-application.git
cd quiz-application
```

### 2. Chạy với Docker Compose

```bash
docker-compose up --build
```

Lệnh này sẽ tạo và chạy tất cả các container dịch vụ đã được định nghĩa trong file docker-compose.yml, bao gồm:

- Cơ sở dữ liệu MySQL
- Eureka Server để đăng ký và khám phá dịch vụ
- API Gateway
- Các microservices (User, Quiz, Question, Gameplay, Identity)

### 3. Truy cập các dịch vụ

Sau khi các dịch vụ đã được khởi động thành công, bạn có thể truy cập chúng tại:

- **Eureka Server:** http://localhost:8761
ost:8761

  - Giao diện đăng ký và giám sát dịch vụ
  - Xem các dịch vụ đã đăng ký và trạng thái của chúng

- **API Gateway:** http://localhost:8080

  - Điểm vào chính cho tất cả các yêu cầu từ client
  - Tất cả các yêu cầu API nên được định tuyến thông qua endpoint này

### 4. Khởi động lại một dịch vụ riêng lẻ (nếu cần)

```bash
docker-compose restart <tên-dịch-vụ>
```

Ví dụ: `docker-compose restart user-service`

### 5. Kiểm tra logs của dịch vụ

```bash
docker-compose logs -f <tên-dịch-vụ>
```

Ví dụ: `docker-compose logs -f quiz-service`

---

## 🌐 Tài Liệu API

Đặc tả API cho mỗi dịch vụ được cung cấp dưới định dạng OpenAPI và có sẵn tại:

- **User Service:** `/docs/api-specs/user-service.yaml`

  - Quản lý người dùng: đăng ký, cập nhật hồ sơ, tìm kiếm người dùng
  - Quản lý mối quan hệ: theo dõi, kết bạn, nhóm

- **Quiz Service:** `/docs/api-specs/quiz-service.yaml`

  - Tạo và quản lý quiz
  - Phân loại và tìm kiếm quiz
  - Quản lý bộ sưu tập quiz

- **Question Service:** `/docs/api-specs/question-service.yaml`

  - Quản lý các loại câu hỏi khác nhau
  - Tạo và chỉnh sửa câu hỏi
  - Nhập/xuất câu hỏi hàng loạt

- **Gameplay Service:** `/docs/api-specs/gameplay-service.yaml`

  - Tạo và quản lý phiên chơi
  - Theo dõi phản hồi người chơi
  - Quản lý điểm số và thống kê

- **Identity Service:** `/docs/api-specs/identity-service.yaml`
  - Đăng nhập và đăng ký
  - Quản lý token
  - Kiểm tra quyền truy cập

Để xem tài liệu API được trực quan hóa, bạn có thể sử dụng công cụ Swagger UI hoặc Redoc bằng cách import các file YAML này.

---

## 💽 Cơ Sở Dữ Liệu

Ứng dụng sử dụng MySQL để lưu trữ dữ liệu. Cơ sở dữ liệu được khởi tạo với script `init-db.sql` khi chạy với Docker Compose.

### Chi tiết kết nối:

- **Host:** localhost
- **Port:** 3307 (được ánh xạ từ port 3306 trong container)
- **Tên người dùng:** root
- **Mật khẩu:** root
- **Database:** quiz_db

### Cấu trúc cơ sở dữ liệu:

Cơ sở dữ liệu được tổ chức theo mô hình microservices, với mỗi dịch vụ quản lý schema riêng:

- **User Schema:** Lưu trữ thông tin người dùng, mối quan hệ và cài đặt
- **Quiz Schema:** Quản lý các quiz và metadata liên quan
- **Question Schema:** Lưu trữ các loại câu hỏi khác nhau và nội dung
- **Gameplay Schema:** Theo dõi phiên chơi, điểm số và thống kê
- **Identity Schema:** Quản lý thông tin xác thực và phân quyền

Script `init-db.sql` tạo các database và bảng cần thiết, đồng thời thiết lập các mối quan hệ và chèn dữ liệu mẫu để kiểm thử.

---

## ⚙️ Yêu Cầu Hệ Thống

### Để chạy ứng dụng:

- **Docker và Docker Compose:**
  - Docker Engine phiên bản 19.03.0 trở lên
  - Docker Compose phiên bản 1.27.0 trở lên

### Để phát triển cục bộ:

- **JDK 11 trở lên:** Cần thiết để biên dịch và chạy các dịch vụ Java
- **Maven:** Quản lý phụ thuộc và xây dựng dự án
- **IDE đề xuất:**Visual Code
- **Postman hoặc Insomnia:** Để kiểm thử API

### Các yêu cầu bổ sung:

- **Git:** Để quản lý phiên bản mã nguồn
- **MySQL Workbench:** Để quản lý và theo dõi cơ sở dữ liệu (tùy chọn)
- **Ít nhất 4GB RAM:** Để chạy tất cả các dịch vụ cùng lúc

1. **Clients** gửi yêu cầu đến **API Gateway**
2. **API Gateway** định tuyến yêu cầu đến dịch vụ thích hợp, được xác định thông qua **Eureka Server**
3. Các dịch vụ giao tiếp với nhau sử dụng Feign Clients và gọi REST API
4. **Identity Service** xác thực và ủy quyền các yêu cầu trước khi các dịch vụ khác xử lý chúng
5. Mỗi dịch vụ có thể tương tác với dịch vụ khác khi cần, tạo thành một hệ thống phân tán hoàn chỉnh

## 🔌 Tích Hợp Swagger và Mô Tả API

Hệ thống này tích hợp Swagger/OpenAPI để cung cấp tài liệu API trực quan cho tất cả các microservices. Mỗi service có tài liệu API riêng có thể truy cập thông qua giao diện Swagger UI.

### Truy cập Swagger UI

Sau khi khởi động các services, bạn có thể truy cập tài liệu API tại:

- **User Service:** http://localhost:8081/swagger-ui.html
- **Quiz Service:** http://localhost:8082/swagger-ui.html
- **Question Service:** http://localhost:8083/swagger-ui.html
- **Gameplay Service:** http://localhost:8084/swagger-ui.html
- **Identity Service:** http://localhost:8085/swagger-ui.html

### Cấu Hình OpenAPI

Mỗi service đều sử dụng thư viện SpringDoc OpenAPI để tạo tài liệu API:

```properties
# Cấu hình Swagger/OpenAPI (ví dụ từ application.properties)
springdoc.api-docs.path=/api-docs
springdoc.swagger-ui.path=/swagger-ui.html
springdoc.swagger-ui.operationsSorter=method
springdoc.swagger-ui.tagsSorter=alpha
springdoc.swagger-ui.disable-swagger-default-url=true
springdoc.swagger-ui.display-request-duration=true
```

### Chi Tiết Tích Hợp OpenAPI

#### 1. Thêm Phụ Thuộc OpenAPI

Mỗi microservice sử dụng thư viện SpringDoc OpenAPI để tự động tạo tài liệu API. Phụ thuộc Maven được thêm vào `pom.xml` của mỗi dịch vụ:

```xml
<dependency>
    <groupId>org.springdoc</groupId>
    <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
    <version>2.0.2</version>
</dependency>
```

#### 2. Cấu Hình OpenAPI trong Java

Mỗi service định nghĩa một lớp cấu hình OpenAPI để tùy chỉnh tài liệu:

```java
@Configuration
public class OpenApiConfig {
    
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Service Name API")
                        .version("1.0.0")
                        .description("API documentation for Service Name")
                        .contact(new Contact()
                                .name("Development Team")
                                .email("admin@quizapp.com")))
                .externalDocs(new ExternalDocumentation()
                        .description("Wiki Documentation")
                        .url("https://wiki.quizapp.com"));
    }
}
```

#### 3. Annotating Controllers và Models

Để tạo tài liệu chi tiết, các controller và model classes được chú thích với các annotation OpenAPI:

**Controller Example:**
```java
@Tag(name = "Quiz Game Tracking", description = "API endpoints for managing quiz game tracking data")
@RestController
@RequestMapping("/api/quiz-game-tracking")
@RequiredArgsConstructor
public class QuizGameTrackingController {

    @Operation(summary = "Create a new quiz game tracking record", 
               description = "Creates a new tracking record for a quiz game session")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Tracking record created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @PostMapping
    public ResponseEntity<QuizGameTracking> createQuizGameTracking(@RequestBody QuizGameTracking quizGameTracking) {
        // Implementation
    }
    
    // Other methods...
}
```

**Model Example:**
```java
@Schema(description = "Quiz Game Tracking Model")
@Entity
@Table(name = "quiz_game_tracking")
@Data
public class QuizGameTracking {

    @Schema(description = "Unique identifier", example = "1")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Schema(description = "ID of the quiz being played", example = "42")
    private Long quizId;
    
    // Other fields...
}
```

#### 4. API Security Documentation

API Security schema được tài liệu hóa bằng OpenAPI:

```java
@Configuration
public class OpenApiConfig {
    
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
            // Basic Info
            .info(...)
            // Security Schema
            .components(new Components()
                .addSecuritySchemes("bearerAuth", 
                    new SecurityScheme()
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                        .bearerFormat("JWT")
                        .description("JWT Authentication. Enter token without 'Bearer' prefix.")))
            // Apply security globally
            .addSecurityItem(new SecurityRequirement().addList("bearerAuth"));
    }
}
```

#### 5. Phân Nhóm API Endpoints

API endpoints được nhóm theo tính năng sử dụng annotation `@Tag`:

```java
@Tag(name = "User Management", description = "Operations related to user management")
@RestController
@RequestMapping("/api/users")
public class UserController {
    // API methods
}

@Tag(name = "User Follows", description = "Operations related to user follow relationships")
@RestController
@RequestMapping("/api/user-follows")
public class UserFollowController {
    // API methods
}
```

#### 6. Export và Import OpenAPI Specs

Mỗi service tự động tạo file spec OpenAPI ở định dạng JSON và YAML, có thể tải xuống từ:

- JSON: `http://localhost:<port>/v3/api-docs`
- YAML: `http://localhost:<port>/v3/api-docs.yaml`

Các file này được lưu trong thư mục `/docs/api-specs/` để version control và tích hợp với các công cụ khác.

### Mô Tả API Chi Tiết

#### 1. User Service API

| Endpoint            | Phương thức | Mô tả                            | Tham số chính          | Chi tiết |
| ------------------- | ----------- | -------------------------------- | ---------------------- | -------- |
| `/api/users`        | GET         | Lấy danh sách người dùng         | `page`, `size`, `sort` | Truy xuất danh sách người dùng có phân trang, sắp xếp theo trường được chỉ định. Hỗ trợ lọc theo tên hoặc vai trò người dùng. |
| `/api/users/{id}`   | GET         | Lấy thông tin người dùng theo ID | `id`                   | Truy xuất thông tin chi tiết của một người dùng dựa trên ID bao gồm hồ sơ và cài đặt người dùng. |
| `/api/users`        | POST        | Tạo người dùng mới               | `UserDTO` trong body   | Tạo một người dùng mới trong hệ thống với các thông tin cơ bản như tên, email, và mật khẩu. |
| `/api/users/{id}`   | PUT         | Cập nhật thông tin người dùng    | `id`, `UserDTO`        | Cập nhật thông tin cho người dùng hiện có như hồ sơ, thông tin liên hệ, hoặc các tùy chọn cá nhân. |
| `/api/users/{id}`   | DELETE      | Xóa người dùng                   | `id`                   | Xóa thông tin người dùng khỏi hệ thống. Thao tác này có thể là xóa mềm, chỉ đánh dấu người dùng là bị vô hiệu hóa. |
| `/api/user-follows` | POST        | Tạo mối quan hệ theo dõi         | `userId`, `followedId` | Thiết lập mối quan hệ theo dõi giữa hai người dùng, cho phép người dùng nhận thông báo về hoạt động của người được theo dõi. |
| `/api/user-follows` | GET         | Lấy danh sách theo dõi           | `userId`               | Truy xuất danh sách người dùng mà một người dùng cụ thể đang theo dõi hoặc được theo dõi. |
| `/api/user-profile` | GET         | Lấy hồ sơ cá nhân               | `userId`               | Truy xuất thông tin hồ sơ người dùng bao gồm tiểu sử, ảnh đại diện và thông tin công khai khác. |
| `/api/user-settings` | PUT        | Cập nhật cài đặt người dùng      | `userId`, `SettingsDTO` | Cập nhật các tùy chọn và cài đặt cá nhân của người dùng như thông báo, quyền riêng tư và ngôn ngữ. |

#### 2. Quiz Service API

| Endpoint                          | Phương thức | Mô tả                      | Tham số chính                | Chi tiết |
| --------------------------------- | ----------- | -------------------------- | ---------------------------- | -------- |
| `/api/quizzes`                    | GET         | Lấy danh sách quiz         | `page`, `size`, `categoryId` | Truy xuất danh sách các bài quiz có phân trang, với khả năng lọc theo danh mục, độ khó hoặc từ khóa. |
| `/api/quizzes`                    | POST        | Tạo quiz mới               | `QuizDTO` trong body         | Tạo một bài quiz mới với tiêu đề, mô tả và các thông số cấu hình như thời gian làm bài hoặc độ khó. |
| `/api/quizzes/{id}`               | GET         | Lấy thông tin quiz theo ID | `id`                         | Truy xuất thông tin đầy đủ về một bài quiz, bao gồm tất cả câu hỏi và câu trả lời liên quan. |
| `/api/quizzes/{id}`               | PUT         | Cập nhật quiz              | `id`, `QuizDTO`              | Cập nhật thông tin và cài đặt cho bài quiz như tiêu đề, mô tả, hoặc thứ tự câu hỏi. |
| `/api/quizzes/{id}`               | DELETE      | Xóa quiz                   | `id`                         | Xóa bài quiz và tất cả dữ liệu liên quan khỏi hệ thống. |
| `/api/quizzes/user/{userId}`      | GET         | Lấy quiz theo người tạo    | `userId`                     | Truy xuất tất cả bài quiz được tạo bởi một người dùng cụ thể, hỗ trợ lọc theo trạng thái xuất bản. |
| `/api/quizzes/latest`             | GET         | Lấy quiz mới nhất          | `limit`                     | Truy xuất danh sách các bài quiz mới nhất được thêm vào hệ thống. |
| `/api/quizzes/popular`            | GET         | Lấy quiz phổ biến          | `limit`                     | Truy xuất danh sách các bài quiz phổ biến nhất dựa trên số lượt chơi, đánh giá hoặc lượt thích. |
| `/api/quiz-games`                 | POST        | Tạo phiên chơi mới         | `QuizGameDTO` trong body     | Tạo một phiên chơi mới cho một bài quiz cụ thể, bao gồm cài đặt như mã truy cập hoặc số người tham gia tối đa. |
| `/api/quiz-games/{id}`            | GET         | Lấy thông tin phiên chơi   | `id`                       | Truy xuất thông tin chi tiết về một phiên chơi, bao gồm trạng thái, người tham gia và tiến trình hiện tại. |
| `/api/quiz-collections`           | GET         | Lấy danh sách bộ sưu tập   | `page`, `size`               | Truy xuất danh sách các bộ sưu tập quiz có phân trang, với khả năng lọc theo danh mục hoặc người tạo. |
| `/api/quiz-collections`           | POST        | Tạo bộ sưu tập mới         | `CollectionDTO` trong body   | Tạo một bộ sưu tập quiz mới với tên, mô tả và các bài quiz được bao gồm. |
| `/api/quiz-collections/{id}`      | GET         | Lấy thông tin bộ sưu tập   | `id`                         | Truy xuất thông tin chi tiết về một bộ sưu tập, bao gồm danh sách tất cả các bài quiz được bao gồm. |
| `/api/quiz-collections/{id}/quiz` | PUT         | Thêm quiz vào bộ sưu tập   | `id`, `quizId`               | Thêm một bài quiz vào bộ sưu tập hiện có. |

#### 3. Question Service API

| Endpoint                    | Phương thức | Mô tả                   | Tham số chính                      | Chi tiết |
| --------------------------- | ----------- | ----------------------- | ---------------------------------- | -------- |
| `/api/questions`            | GET         | Lấy danh sách câu hỏi   | `page`, `size`, `quizId`, `typeId` | Truy xuất danh sách các câu hỏi có phân trang, với khả năng lọc theo bài quiz hoặc loại câu hỏi. |
| `/api/questions/{id}`       | GET         | Lấy chi tiết câu hỏi    | `id`                               | Truy xuất thông tin chi tiết về một câu hỏi cụ thể, bao gồm nội dung và các lựa chọn trả lời nếu có. |
| `/api/questions`            | POST        | Tạo câu hỏi chung       | `QuestionDTO` trong body           | Tạo một câu hỏi mới với định dạng cơ bản, bao gồm tiêu đề và nội dung. |
| `/api/questions/true-false` | POST        | Tạo câu hỏi đúng/sai    | `QuestionTrueFalseDTO`             | Tạo câu hỏi dạng đúng/sai với một phát biểu và đáp án đúng. |
| `/api/questions/true-false/{id}` | PUT    | Cập nhật câu hỏi đúng/sai | `id`, `QuestionTrueFalseDTO`     | Cập nhật thông tin cho câu hỏi đúng/sai hiện có. |
| `/api/questions/choice`     | POST        | Tạo câu hỏi trắc nghiệm | `QuestionChoiceDTO`                | Tạo câu hỏi dạng trắc nghiệm với nhiều lựa chọn và xác định đáp án đúng. |
| `/api/questions/choice/{id}` | PUT        | Cập nhật câu hỏi trắc nghiệm | `id`, `QuestionChoiceDTO`     | Cập nhật thông tin cho câu hỏi trắc nghiệm hiện có. |
| `/api/questions/slider`     | POST        | Tạo câu hỏi thanh trượt | `QuestionSliderDTO`                | Tạo câu hỏi sử dụng thanh trượt để chọn giá trị từ một phạm vi xác định. |
| `/api/questions/slider/{id}` | PUT        | Cập nhật câu hỏi thanh trượt | `id`, `QuestionSliderDTO`     | Cập nhật thông tin cho câu hỏi thanh trượt hiện có. |
| `/api/questions/puzzle`     | POST        | Tạo câu hỏi câu đố      | `QuestionPuzzleDTO`                | Tạo câu hỏi dạng câu đố yêu cầu sắp xếp các phần tử để tạo thành một đáp án hoàn chỉnh. |
| `/api/questions/puzzle/{id}` | PUT        | Cập nhật câu hỏi câu đố  | `id`, `QuestionPuzzleDTO`         | Cập nhật thông tin cho câu hỏi câu đố hiện có. |
| `/api/questions/text`       | POST        | Tạo câu hỏi văn bản     | `QuestionTypeTextDTO`              | Tạo câu hỏi yêu cầu người dùng nhập câu trả lời dạng văn bản. |
| `/api/questions/text/{id}`  | PUT         | Cập nhật câu hỏi văn bản | `id`, `QuestionTypeTextDTO`       | Cập nhật thông tin cho câu hỏi văn bản hiện có. |
| `/api/questions/{id}`       | DELETE      | Xóa câu hỏi             | `id`                               | Xóa một câu hỏi khỏi hệ thống. |
| `/api/questions/bulk-import` | POST        | Nhập nhiều câu hỏi      | `file`                             | Nhập nhiều câu hỏi từ file CSV hoặc Excel. |

#### 4. Gameplay Service API

| Endpoint                                   | Phương thức | Mô tả                           | Tham số chính         | Chi tiết |
| ------------------------------------------ | ----------- | ------------------------------- | --------------------- | -------- |
| `/api/quiz-game-tracking`                  | GET         | Lấy tất cả bản ghi theo dõi     | `page`, `size`        | Truy xuất danh sách tất cả các bản ghi theo dõi phiên chơi trò chơi câu đố từ cơ sở dữ liệu. |
| `/api/quiz-game-tracking`                  | POST        | Tạo theo dõi phiên chơi         | `QuizGameTrackingDTO` | Tạo một bản ghi mới để theo dõi hoạt động trong một phiên chơi quiz, bao gồm thông tin người chơi và quiz. |
| `/api/quiz-game-tracking/{id}`             | GET         | Lấy bản ghi theo ID             | `id`                  | Truy xuất thông tin chi tiết về một bản ghi theo dõi cụ thể dựa trên ID. |
| `/api/quiz-game-tracking/{id}`             | PUT         | Cập nhật bản ghi                | `id`, `QuizGameTrackingDTO` | Cập nhật thông tin cho bản ghi theo dõi hiện có, như điểm số hoặc thành tích. |
| `/api/quiz-game-tracking/{id}`             | DELETE      | Xóa bản ghi                     | `id`                  | Xóa một bản ghi theo dõi khỏi hệ thống. |
| `/api/quiz-game-tracking/quiz/{quizId}`    | GET         | Lấy bản ghi theo ID quiz        | `quizId`              | Truy xuất tất cả bản ghi theo dõi liên quan đến một quiz cụ thể, giúp phân tích hiệu suất quiz. |
| `/api/quiz-game-tracking/user/{userId}`    | GET         | Lấy lịch sử chơi của người dùng | `userId`              | Truy xuất lịch sử tất cả các phiên chơi của một người dùng cụ thể, bao gồm điểm số và thời gian. |
| `/api/question-tracking`                   | POST        | Ghi nhận câu trả lời            | `QuestionTrackingDTO` | Ghi lại thông tin về câu trả lời của một người dùng cho một câu hỏi cụ thể trong một phiên chơi. |
| `/api/question-tracking/{id}`              | GET         | Lấy chi tiết câu trả lời        | `id`                  | Truy xuất thông tin chi tiết về câu trả lời của người dùng, bao gồm tính chính xác và thời gian. |
| `/api/question-tracking/session/{sessionId}` | GET      | Lấy tất cả câu trả lời trong phiên | `sessionId`        | Truy xuất tất cả câu trả lời được ghi nhận trong một phiên chơi cụ thể. |
| `/api/participants`                        | POST        | Thêm người tham gia             | `ParticipantDTO`      | Đăng ký một người dùng làm người tham gia trong một phiên chơi quiz. |
| `/api/participants/{id}`                   | GET         | Lấy thông tin người tham gia    | `id`                  | Truy xuất thông tin chi tiết về một người tham gia cụ thể trong phiên chơi. |
| `/api/participants/{id}`                   | DELETE      | Xóa người tham gia              | `id`                  | Xóa một người tham gia khỏi phiên chơi (ví dụ: khi họ rời đi). |
| `/api/participants/quiz-game/{quizGameId}` | GET         | Lấy danh sách người chơi        | `quizGameId`          | Truy xuất danh sách tất cả những người tham gia trong một phiên chơi quiz cụ thể. |
| `/api/leaderboards/session/{sessionId}`    | GET         | Lấy bảng xếp hạng phiên         | `sessionId`           | Truy xuất bảng xếp hạng thời gian thực cho một phiên chơi quiz đang diễn ra. |
| `/api/statistics/quiz/{quizId}`            | GET         | Lấy thống kê quiz               | `quizId`              | Truy xuất dữ liệu thống kê tổng hợp cho một quiz dựa trên tất cả các lần chơi. |
| `/api/statistics/user/{userId}`            | GET         | Lấy thống kê người dùng         | `userId`              | Truy xuất dữ liệu thống kê về hiệu suất của một người dùng qua nhiều quiz. |

#### 5. Identity Service API

| Endpoint                   | Phương thức | Mô tả                       | Tham số chính                | Chi tiết |
| -------------------------- | ----------- | --------------------------- | ---------------------------- | -------- |
| `/api/auth/register`       | POST        | Đăng ký người dùng mới      | `RegisterRequest` trong body | Tạo tài khoản mới cho người dùng bằng cách cung cấp thông tin cá nhân và thông tin xác thực. |
| `/api/auth/login`          | POST        | Đăng nhập và nhận JWT token | `LoginRequest` trong body    | Xác thực người dùng và trả về JWT token để sử dụng cho các yêu cầu API tiếp theo. |
| `/api/auth/refresh-token`  | POST        | Làm mới token JWT           | `RefreshTokenRequest` trong body | Cung cấp token mới mà không cần đăng nhập lại khi token hiện tại sắp hết hạn. |
| `/api/auth/logout`         | POST        | Đăng xuất người dùng        | `tokenId` trong body        | Vô hiệu hóa token JWT hiện tại, đảm bảo nó không thể được sử dụng cho các yêu cầu tiếp theo. |
| `/api/auth/verify-email`   | GET         | Xác minh email người dùng   | `token` trong query param   | Xác minh địa chỉ email của người dùng bằng token được gửi qua email. |
| `/api/auth/reset-password` | POST        | Đặt lại mật khẩu            | `ResetPasswordRequest` trong body | Khởi tạo quá trình đặt lại mật khẩu cho người dùng quên mật khẩu. |
| `/api/auth/user-info`      | GET         | Lấy thông tin người dùng    | JWT token trong header      | Truy xuất thông tin người dùng hiện đang đăng nhập từ token JWT. |

---

## 👥 Thành Viên Nhóm và Đóng Góp

| Tên           | MSSV       | Đóng Góp                                              |
| ------------- | ---------- | ----------------------------------------------------- |
| Lê Trung Hiếu | B21DCCN356 | API gateway, identity service, user service, document |
| Đặng Minh Anh | B21DCCN140 | Gameplay service, app demo                            |
| Đặng Minh Đức | B21DCCN236 | quiz service, question service   |

### Chi tiết đóng góp:

#### Lê Trung Hiếu:

- **API Gateway**: Thiết kế và triển khai API Gateway để định tuyến yêu cầu từ client đến các dịch vụ thích hợp. Sử dụng Spring Cloud Gateway để xử lý các yêu cầu và cân bằng tải giữa các dịch vụ.

- **Identity Service**: Xây dựng dịch vụ xác thực và ủy quyền, sử dụng JWT tokens để bảo mật các yêu cầu API. Cung cấp các endpoint cho đăng ký, đăng nhập và quản lý token.

- **User Service**: Thiết kế và triển khai dịch vụ người dùng để quản lý thông tin người dùng, mối quan hệ và cài đặt cá nhân. Cung cấp các API cho việc đăng ký, cập nhật hồ sơ và tìm kiếm người dùng.

#### Đặng Minh Anh:

- **Gameplay Service**: Xây dựng dịch vụ theo dõi phiên chơi quiz, bao gồm việc lưu trữ câu trả lời của người chơi, tính điểm và thống kê. Cung cấp các API cho việc tạo và quản lý phiên chơi.

- **App Demo**: Tạo một ứng dụng demo để trình bày các tính năng chính của hệ thống.

#### Đặng Minh Đức:

- **Quiz Service**: Thiết kế và triển khai dịch vụ quản lý quiz, bao gồm việc tạo, lưu trữ và phân loại quiz. Cung cấp các API cho việc tìm kiếm và quản lý quiz.

- **Question Service**: Xây dựng dịch vụ quản lý các loại câu hỏi khác nhau, bao gồm việc tạo, cập nhật và truy xuất câu hỏi. Cung cấp các API cho việc nhập/xuất câu hỏi hàng loạt.

---

## 📚 Ghi Chú Phát Triển

### Kiến trúc và Thiết kế:

- Các dịch vụ đăng ký với Eureka để khám phá dịch vụ, cho phép chúng tự động tìm và giao tiếp với nhau
- Xác thực được xử lý thông qua JWT tokens, đảm bảo tính bảo mật và toàn vẹn của dữ liệu
- Mỗi dịch vụ có schema cơ sở dữ liệu riêng, tuân thủ nguyên tắc của kiến trúc microservices
- API Gateway định tuyến tất cả các yêu cầu từ client đến các dịch vụ thích hợp, cung cấp một điểm vào duy nhất
- Các dịch vụ giao tiếp với nhau sử dụng Feign clients, đơn giản hóa việc gọi REST API giữa các dịch vụ

### Các thực hành tốt nhất:

- Triển khai Circuit Breakers để ngăn lỗi dây chuyền giữa các dịch vụ
- Sử dụng centralized logging để theo dõi và gỡ lỗi trong môi trường phân tán
- Triển khai health checks cho mỗi dịch vụ để giám sát tình trạng hệ thống
- Áp dụng phân trang và giới hạn tốc độ để tối ưu hiệu suất API
- Sử dụng cache để giảm tải cơ sở dữ liệu và cải thiện thời gian phản hồi

### Hướng phát triển tương lai:

- Thêm Analytics Service để phân tích dữ liệu người dùng và cung cấp insights
- Triển khai Notification Service để gửi thông báo qua email, push notifications
- Tích hợp hệ thống thanh toán cho nội dung premium
- Thêm hỗ trợ cho các tính năng xã hội như chia sẻ quiz, bảng xếp hạng
- Mở rộng khả năng quốc tế hóa với hỗ trợ đa ngôn ngữ

### Sử Dụng OpenAPI Descriptions

#### 1. Sử Dụng API Client Generators

Các file YAML OpenAPI có thể được sử dụng để tạo API clients tự động. Dưới đây là một số công cụ phổ biến:

```bash
# Tạo client TypeScript cho frontend
openapi-generator-cli generate -i docs/api-specs/quiz-service.yaml -g typescript-fetch -o clients/quiz-service-ts

# Tạo client Java cho microservices khác
openapi-generator-cli generate -i docs/api-specs/gameplay-service.yaml -g java -o clients/gameplay-client-java
```

#### 2. Tích Hợp với Postman

Để import API specs vào Postman:

1. Mở Postman
2. Chọn **Import** > **File** > chọn file YAML từ `docs/api-specs/`
3. Postman sẽ tạo collection với tất cả endpoints và các ví dụ request

#### 3. Tích Hợp với Frontend

Trong frontend, OpenAPI specs có thể được sử dụng để tạo API clients và interfaces TypeScript:

```typescript
// Example generated client
import { QuizApi, Configuration } from './generated-api';

const config = new Configuration({
  basePath: 'http://localhost:8080',
  accessToken: () => localStorage.getItem('token') || ''
});

const quizApi = new QuizApi(config);
const quizzes = await quizApi.getAllQuizzes();
```

#### 4. Tạo API Mocks

Dựa trên OpenAPI specs, có thể tạo các API mocks để phát triển frontend mà không cần backend đầy đủ:

```bash
# Sử dụng Prism để chạy mock server
prism mock docs/api-specs/quiz-service.yaml
```

#### 5. Validate API Responses

Sử dụng công cụ validator để đảm bảo API tuân thủ theo specs:

```bash
# Kiểm tra response từ API dựa trên OpenAPI spec
openapi-validator --url http://localhost:8082/api/quizzes --spec docs/api-specs/quiz-service.yaml
```

### OpenAPI Extensions cho Swagger UI

Các service trong dự án này sử dụng một số extension của OpenAPI để cải thiện trải nghiệm người dùng với Swagger UI:

#### 1. Ví Dụ Request/Response

Các model đều được định nghĩa với ví dụ để giúp người dùng API hiểu định dạng dữ liệu:

```java
@Schema(example = """
    {
        "title": "Science Quiz",
        "description": "A quiz about general science topics",
        "userId": 42,
        "visible": true,
        "shuffle": true
    }
    """)
public class QuizDTO {
    // Properties
}
```

#### 2. Custom Schema Documentation

Schema phức tạp được mô tả chi tiết với annotation:

```java
@Schema(description = "Question Tracking Model", 
        requiredProperties = {"quizId", "userId", "questionId"})
public class QuestionTracking {
    // Properties
}
```

#### 3. API Version Control

API versions được mô tả và quản lý thông qua:

```java
@OpenAPIDefinition(
    info = @Info(
        title = "Quiz Service API",
        version = "2.0",
        description = "API for Quiz Management",
        contact = @Contact(name = "Support", email = "support@quizapp.com"),
        license = @License(name = "Apache 2.0", url = "https://www.apache.org/licenses/LICENSE-2.0")
    )
)
```
