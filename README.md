# Quiz Application Microservices

## Tổng quan dự án

Đây là một ứng dụng trắc nghiệm được xây dựng theo kiến trúc microservice, sử dụng các công nghệ Spring Boot, Spring Cloud và Docker. Hệ thống bao gồm nhiều service độc lập, mỗi service có nhiệm vụ riêng biệt và có thể triển khai, mở rộng độc lập với nhau.

## Kiến trúc hệ thống

Hệ thống bao gồm các microservice chính sau:

- **Eureka Server**: Service discovery server, đăng ký và quản lý các service
- **API Gateway**: Điểm vào duy nhất của hệ thống, định tuyến request tới các service tương ứng
- **User Service**: Quản lý thông tin người dùng và profile
- **Quiz Service**: Quản lý các bài trắc nghiệm và bộ sưu tập trắc nghiệm
- **Question Service**: Quản lý các câu hỏi và loại câu hỏi
- **Gameplay Service**: Theo dõi quá trình chơi và tương tác của người dùng
- **Identity Service**: Xử lý xác thực, đăng nhập, đăng xuất và phân quyền người dùng

## Công nghệ sử dụng

- **Java 11+**
- **Spring Boot**: Framework để xây dựng các ứng dụng Java
- **Spring Cloud**: Cung cấp các công cụ để xây dựng hệ thống phân tán
- **Netflix Eureka**: Service discovery
- **Spring Cloud Gateway**: API Gateway
- **Spring Security**: Bảo mật và xác thực
- **MySQL**: Cơ sở dữ liệu quan hệ
- **Docker & Docker Compose**: Container hóa và điều phối các service
- **Maven**: Công cụ quản lý dependency và build project
- **JWT**: JSON Web Tokens để xác thực và phân quyền

## Cấu trúc dự án

```
project-root/
├── api-gateway/               # API Gateway service
├── eureka-server/             # Service discovery server
├── user-service/              # Quản lý người dùng
├── quiz-service/              # Quản lý bài trắc nghiệm
├── question-service/          # Quản lý câu hỏi
├── gameplay-service/          # Quản lý quá trình chơi
├── identity-service/          # Quản lý xác thực và phân quyền
├── docker-compose.yml         # Cấu hình Docker Compose
└── init-db.sql                # Script khởi tạo cơ sở dữ liệu
```

## Cài đặt và chạy

### Yêu cầu

- JDK 11 trở lên
- Docker và Docker Compose
- Maven

### Các bước cài đặt

1. Clone dự án:
```bash
git clone <repository-url>
cd <project-folder>
```

2. Build tất cả các service:
```bash
# Chạy từng service
cd eureka-server && ./mvnw clean package
cd ../api-gateway && ./mvnw clean package
cd ../user-service && ./mvnw clean package
cd ../quiz-service && ./mvnw clean package
cd ../question-service && ./mvnw clean package
cd ../gameplay-service && ./mvnw clean package
cd ../identity-service && ./mvnw clean package
```

3. Khởi động hệ thống với Docker Compose:
```bash
docker-compose up -d
```

4. Kiểm tra dịch vụ:
   - Eureka Server: http://localhost:8761
   - API Gateway: http://localhost:8080

## Cấu hình port

- Eureka Server: 8761
- API Gateway: 8080
- User Service: 8081
- Quiz Service: 8082
- Question Service: 8083
- Gameplay Service: 8084
- Identity Service: 8085

## Các API chính

### User API
- `GET /api/users`: Lấy danh sách người dùng
- `POST /api/users`: Đăng ký người dùng mới
- `GET /api/users/{id}`: Lấy thông tin người dùng

### Quiz API
- `GET /api/quizzes`: Lấy danh sách bài trắc nghiệm
- `POST /api/quizzes`: Tạo bài trắc nghiệm mới
- `GET /api/quiz-games`: Lấy danh sách trò chơi trắc nghiệm

### Question API
- `GET /api/questions`: Lấy danh sách câu hỏi
- `POST /api/questions`: Tạo câu hỏi mới
- `GET /api/question-types`: Lấy các loại câu hỏi

### Gameplay API
- `GET /api/quiz-game-tracking`: Theo dõi tiến trình trò chơi
- `POST /api/participants`: Quản lý người tham gia

### Identity API
- `POST /api/auth/login`: Đăng nhập
- `POST /api/auth/register`: Đăng ký tài khoản mới

## Bảo mật

Hệ thống sử dụng JWT (JSON Web Token) để xác thực người dùng. API Gateway xác thực token cho tất cả các request ngoại trừ endpoints đăng nhập và đăng ký. Quy trình xác thực:

1. Người dùng đăng nhập/đăng ký qua Identity Service
2. Identity Service tạo JWT token và trả về cho client
3. Client sử dụng token này trong header Authorization cho các request tiếp theo
4. API Gateway xác thực token và chuyển tiếp request đến các service tương ứng

## Phát triển

Để phát triển service mới:

1. Tạo project Spring Boot mới
2. Cấu hình Eureka Client
3. Thêm route vào API Gateway
4. Thêm service vào docker-compose.yml

## Liên hệ

Nếu có thắc mắc hoặc gặp vấn đề, vui lòng tạo issue mới hoặc liên hệ qua email: <email@example.com>