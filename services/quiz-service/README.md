# Quiz Service

## Tổng quan

Quiz Service là service quản lý các bài trắc nghiệm và bộ sưu tập trắc nghiệm trong hệ thống. Service này cho phép người dùng tạo, chỉnh sửa, chia sẻ và tìm kiếm các bài trắc nghiệm.

## Chức năng chính

- Tạo và quản lý bài trắc nghiệm
- Quản lý bộ sưu tập trắc nghiệm
- Cung cấp API để tìm kiếm và lọc trắc nghiệm
- Quản lý thông tin trò chơi trắc nghiệm
- Phân loại và gắn thẻ cho các bài trắc nghiệm

## Cấu trúc API

| Endpoint | Phương thức | Mô tả |
|----------|------------|-------|
| `/api/quizzes` | GET | Lấy danh sách bài trắc nghiệm |
| `/api/quizzes` | POST | Tạo bài trắc nghiệm mới |
| `/api/quizzes/{id}` | GET | Lấy thông tin bài trắc nghiệm theo ID |
| `/api/quizzes/{id}` | PUT | Cập nhật bài trắc nghiệm |
| `/api/quizzes/{id}` | DELETE | Xóa bài trắc nghiệm |
| `/api/quiz-collections` | GET | Lấy danh sách bộ sưu tập trắc nghiệm |
| `/api/quiz-games` | GET | Lấy danh sách trò chơi trắc nghiệm |
| `/api/quiz-games` | POST | Tạo trò chơi trắc nghiệm mới |

## Mô hình dữ liệu

Service sử dụng cơ sở dữ liệu MySQL với các bảng chính:

- `quizzes`: Lưu thông tin chính của bài trắc nghiệm
- `quiz_collections`: Lưu thông tin bộ sưu tập trắc nghiệm
- `quiz_games`: Lưu thông tin trò chơi trắc nghiệm
- `quiz_tags`: Lưu thông tin thẻ của bài trắc nghiệm
- `quiz_categories`: Lưu thông tin phân loại trắc nghiệm

## Kết nối với các Service khác

Quiz Service kết nối với:

- **User Service**: Lấy thông tin người tạo quiz
- **Question Service**: Lấy thông tin câu hỏi để tạo quiz
- **Gameplay Service**: Cung cấp thông tin quiz cho phiên chơi

## Sử dụng Feign Client

Service sử dụng Feign Client để gọi đến các service khác:

```java
@FeignClient(name = "question-service")
public interface QuestionServiceClient {
    @GetMapping("/api/questions/{id}")
    QuestionDto getQuestion(@PathVariable("id") Long id);
}
```

## Cấu hình

Service chạy trên port 8082 với các cấu hình cơ bản:

```properties
server.port=8082
spring.application.name=quiz-service
eureka.client.service-url.defaultZone=http://localhost:8761/eureka/
```

## Cách chạy

### Yêu cầu

- Java 17+
- Maven
- MySQL

### Cấu hình cơ sở dữ liệu

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/quiz_db
spring.datasource.username=root
spring.datasource.password=root
```

### Lệnh chạy

```bash
# Đi đến thư mục Quiz Service
cd quiz-service

# Build project
./mvnw clean package

# Chạy service
java -jar target/quiz-service-0.0.1-SNAPSHOT.jar
```

### Sử dụng Docker

```bash
# Build Docker image
docker build -t quiz-service .

# Chạy container
docker run -p 8082:8082 quiz-service
```