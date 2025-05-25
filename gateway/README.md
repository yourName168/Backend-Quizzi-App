# API Gateway Service

## Tổng quan

API Gateway là điểm vào duy nhất của hệ thống, chịu trách nhiệm định tuyến các request đến các service tương ứng và xử lý việc xác thực bảo mật. Service này sử dụng Spring Cloud Gateway và thực hiện xác thực bằng JWT token.

## Chức năng chính

- Định tuyến các request đến các microservice phù hợp
- Xác thực JWT token cho mọi request (trừ đăng nhập/đăng ký)
- Cung cấp một điểm vào duy nhất cho toàn bộ hệ thống
- Cân bằng tải (load balancing) giữa các instance của mỗi service
- Xử lý CORS để cho phép các request từ frontend

## Cấu trúc API

| Đường dẫn | Service đích | Mô tả |
|-----------|--------------|-------|
| `/api/auth/login`, `/api/auth/register` | identity-service | Không yêu cầu xác thực JWT |
| `/api/users/**` | user-service | Quản lý người dùng |
| `/api/quizzes/**` | quiz-service | Quản lý bài trắc nghiệm |
| `/api/questions/**` | question-service | Quản lý câu hỏi |
| `/api/quiz-game-tracking/**` | gameplay-service | Theo dõi quá trình chơi |

## Cấu hình

Service chạy trên port 8080 và có các cấu hình chính sau:

- Xác thực JWT với khóa bí mật từ tham số cấu hình
- Các route được định nghĩa trong file application.properties hoặc thông qua RouteLocator
- Đăng ký với Eureka Server để service discovery

## Bảo mật

API Gateway thực hiện xác thực JWT cho mọi yêu cầu trừ các endpoint công khai. Quy trình xác thực:

1. Kiểm tra tồn tại của header Authorization
2. Trích xuất JWT token từ header
3. Xác thực tính hợp lệ của token và thời hạn
4. Thêm thông tin người dùng vào request header trước khi chuyển tiếp

## Cách chạy

### Yêu cầu

- Java 17+
- Maven

### Lệnh chạy

```bash
# Đi đến thư mục API Gateway
cd api-gateway

# Build project
./mvnw clean package

# Chạy service
java -jar target/api-gateway-0.0.1-SNAPSHOT.jar
```

### Sử dụng Docker

```bash
# Build Docker image
docker build -t api-gateway .

# Chạy container
docker run -p 8080:8080 api-gateway
```