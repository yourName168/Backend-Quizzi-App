# Gameplay Service

## Tổng quan

Gameplay Service là service quản lý quá trình chơi trắc nghiệm trong hệ thống. Service này theo dõi tiến trình của người dùng, ghi nhận câu trả lời, tính điểm và cung cấp phân tích về kết quả chơi.

## Chức năng chính

- Theo dõi tiến trình chơi trắc nghiệm của người dùng
- Ghi nhận câu trả lời và tính điểm
- Quản lý người tham gia trong phiên chơi
- Cung cấp thống kê và phân tích kết quả
- Hỗ trợ các chế độ chơi khác nhau (thời gian thực, không đồng bộ)

## Cấu trúc API

| Endpoint | Phương thức | Mô tả |
|----------|------------|-------|
| `/api/quiz-game-tracking` | GET | Lấy danh sách tracking game |
| `/api/quiz-game-tracking` | POST | Tạo tracking game mới |
| `/api/quiz-game-tracking/{id}` | GET | Lấy thông tin tracking game theo ID |
| `/api/question-tracking` | POST | Ghi nhận câu trả lời cho câu hỏi |
| `/api/question-tracking/{id}` | GET | Lấy thông tin câu trả lời |
| `/api/participants` | GET | Lấy danh sách người tham gia |
| `/api/participants` | POST | Thêm người tham gia mới |

## Mô hình dữ liệu

Service sử dụng cơ sở dữ liệu MySQL với các bảng chính:

- `quiz_game_tracking`: Lưu thông tin tiến trình chơi quiz
- `question_tracking`: Lưu thông tin câu trả lời cho từng câu hỏi
- `participants`: Lưu thông tin người tham gia
- `game_results`: Lưu thông tin kết quả trò chơi
- `game_analytics`: Lưu thông tin phân tích trò chơi

## Xử lý dữ liệu thời gian thực

Gameplay Service có thể xử lý dữ liệu theo thời gian thực để hỗ trợ các tính năng như:

- Bảng xếp hạng trực tiếp
- Hiển thị số người đang tham gia
- Thông báo kết quả ngay lập tức
- Theo dõi tiến độ của các người chơi khác

## Kết nối với các Service khác

Gameplay Service kết nối với:

- **User Service**: Lấy thông tin người chơi
- **Quiz Service**: Lấy thông tin bài trắc nghiệm
- **Question Service**: Lấy thông tin câu hỏi và đáp án

Service sử dụng Feign Client để gọi đến các service khác:

```java
@FeignClient(name = "quiz-service")
public interface QuizServiceClient {
    @GetMapping("/api/quizzes/{id}")
    QuizDto getQuiz(@PathVariable("id") Long id);
}
```

## Cấu hình

Service chạy trên port 8084 với các cấu hình cơ bản:

```properties
server.port=8084
spring.application.name=gameplay-service
eureka.client.service-url.defaultZone=http://localhost:8761/eureka/
```

## Cách chạy

### Yêu cầu

- Java 17+
- Maven
- MySQL

### Cấu hình cơ sở dữ liệu

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/gameplay_db
spring.datasource.username=root
spring.datasource.password=root
```

### Lệnh chạy

```bash
# Đi đến thư mục Gameplay Service
cd gameplay-service

# Build project
./mvnw clean package

# Chạy service
java -jar target/gameplay-service-0.0.1-SNAPSHOT.jar
```

### Sử dụng Docker

```bash
# Build Docker image
docker build -t gameplay-service .

# Chạy container
docker run -p 8084:8084 gameplay-service
```