# Question Service

## Tổng quan

Question Service là service quản lý các câu hỏi và loại câu hỏi trong hệ thống trắc nghiệm. Service này cho phép tạo, chỉnh sửa và tổ chức các câu hỏi được sử dụng trong các bài trắc nghiệm.

## Chức năng chính

- Tạo và quản lý câu hỏi
- Hỗ trợ nhiều loại câu hỏi khác nhau
- Cung cấp API để tìm kiếm và lọc câu hỏi
- Quản lý đáp án và điểm số cho mỗi câu hỏi
- Phân loại và gắn thẻ cho câu hỏi

## Cấu trúc API

| Endpoint | Phương thức | Mô tả |
|----------|------------|-------|
| `/api/questions` | GET | Lấy danh sách câu hỏi |
| `/api/questions` | POST | Tạo câu hỏi mới |
| `/api/questions/{id}` | GET | Lấy thông tin câu hỏi theo ID |
| `/api/questions/{id}` | PUT | Cập nhật câu hỏi |
| `/api/questions/{id}` | DELETE | Xóa câu hỏi |
| `/api/question-types` | GET | Lấy danh sách loại câu hỏi |
| `/api/questions/search` | GET | Tìm kiếm câu hỏi theo tiêu chí |

## Loại câu hỏi hỗ trợ

Service hỗ trợ nhiều loại câu hỏi khác nhau:

- Trắc nghiệm một lựa chọn
- Trắc nghiệm nhiều lựa chọn
- Đúng / Sai
- Điền vào chỗ trống
- Ghép đôi
- Câu hỏi tự luận ngắn

## Mô hình dữ liệu

Service sử dụng cơ sở dữ liệu MySQL với các bảng chính:

- `questions`: Lưu thông tin chính của câu hỏi
- `question_types`: Lưu thông tin loại câu hỏi
- `options`: Lưu thông tin các lựa chọn cho câu hỏi
- `correct_answers`: Lưu thông tin đáp án đúng
- `question_tags`: Lưu thông tin thẻ của câu hỏi

## Kết nối với các Service khác

Question Service kết nối với:

- **Quiz Service**: Cung cấp câu hỏi cho bài trắc nghiệm
- **Gameplay Service**: Cung cấp thông tin câu hỏi cho phiên chơi

## Cấu hình

Service chạy trên port 8083 với các cấu hình cơ bản:

```properties
server.port=8083
spring.application.name=question-service
eureka.client.service-url.defaultZone=http://localhost:8761/eureka/
```

## Cách chạy

### Yêu cầu

- Java 17+
- Maven
- MySQL

### Cấu hình cơ sở dữ liệu

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/question_db
spring.datasource.username=root
spring.datasource.password=root
```

### Lệnh chạy

```bash
# Đi đến thư mục Question Service
cd question-service

# Build project
./mvnw clean package

# Chạy service
java -jar target/question-service-0.0.1-SNAPSHOT.jar
```

### Sử dụng Docker

```bash
# Build Docker image
docker build -t question-service .

# Chạy container
docker run -p 8083:8083 question-service
```