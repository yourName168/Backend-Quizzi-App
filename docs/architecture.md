# Kiến Trúc Hệ Thống Microservices Quiz Application

## Tổng Quan Kiến Trúc

Hệ thống Quiz Application được xây dựng dựa trên kiến trúc microservices, cho phép các thành phần riêng biệt hoạt động độc lập và giao tiếp với nhau thông qua API. Kiến trúc này mang lại nhiều lợi ích như khả năng mở rộng cao, bảo trì dễ dàng, và phát triển linh hoạt.

```
┌─────────────┐                        ┌─────────────────┐
│             │                        │                 │
│   Client    │◄───────────────────────┤  API Gateway    │
│             │     JWT token          │  (Xác thực JWT) │
│             │                        │                 │
└──────┬──────┘                        └─────────┬───────┘
       │                                         │
       │ 1. Đăng nhập/                           │
       │    Đăng ký                              │
       │                                         │
       ▼                                         │
┌─────────────┐                                  │
│             │                                  │
│  Identity   │                                  │
│  Service    │                                  │
│             │                                  │
└─────────────┘                                  │
       │                                         │
       │ 2. Trả về                               │ 3. Kiểm tra
       │    JWT token                            │    token
       │                                         │
       │    JWT token                            │
       │                                         │
       ▼                                         ▼
┌──────────────────────────────────────────────────────────┐
│                                                          │
│                   Service Discovery                      │
│                   (Eureka Server)                        │
│                                                          │
└──────────────────────────────────────────────────────────┘
       ▲                 ▲                 ▲
       │                 │                 │
       │                 │                 │
┌──────┴──────┐   ┌──────┴──────┐   ┌──────┴──────┐
│             │   │             │   │             │
│   User      │   │   Quiz      │   │  Question   │
│  Service    │   │  Service    │   │  Service    │
│             │   │             │   │             │
└─────────────┘   └─────────────┘   └─────────────┘
                                           │
                                           │
                                           ▼
                                    ┌─────────────┐
                                    │             │
                                    │  Gameplay   │
                                    │  Service    │
                                    │             │
                                    └─────────────┘
```

## Các Thành Phần Chính

### 1. API Gateway (Port: 8080)

API Gateway là điểm vào duy nhất của hệ thống, chịu trách nhiệm:
- Định tuyến request đến các service tương ứng
- Xử lý xác thực JWT token
- Cân bằng tải giữa các instance của mỗi service
- Xử lý CORS và bảo mật chung

Công nghệ: Spring Cloud Gateway

### 2. Eureka Server (Port: 8761)

Eureka Server đóng vai trò là service registry, giúp:
- Đăng ký và khám phá các service 
- Theo dõi trạng thái của các service
- Hỗ trợ cân bằng tải giữa các instance
- Tự động loại bỏ các service không hoạt động

Công nghệ: Spring Cloud Netflix Eureka

### 3. Identity Service (Port: 8085)

Identity Service chịu trách nhiệm về xác thực và phân quyền:
- Đăng ký người dùng mới (thông qua User Service)
- Xác thực đăng nhập và tạo JWT token
- Làm mới token
- Quản lý đăng xuất và vô hiệu hóa token

Công nghệ: Spring Boot, Spring Security, JWT

**Cải tiến mới**: Identity Service giờ đây hoạt động hoàn toàn stateless, không lưu trữ dữ liệu người dùng mà sử dụng User Service để xác thực và đăng ký.

### 4. User Service (Port: 8081)

User Service quản lý thông tin người dùng:
- CRUD thông tin người dùng
- Quản lý profile và cài đặt cá nhân
- Quản lý mối quan hệ theo dõi giữa người dùng
- Lưu trữ tùy chọn và hiệu ứng âm nhạc

Cơ sở dữ liệu: MySQL (user_db)
Công nghệ: Spring Boot, Spring Data JPA, Hibernate

### 5. Quiz Service (Port: 8082)

Quiz Service quản lý bài trắc nghiệm:
- CRUD bài trắc nghiệm
- Quản lý bộ sưu tập trắc nghiệm
- Quản lý thẻ và phân loại trắc nghiệm
- Liên kết với Question Service để lấy câu hỏi

Cơ sở dữ liệu: MySQL (quiz_db)
Công nghệ: Spring Boot, Spring Data JPA

### 6. Question Service (Port: 8083)

Question Service quản lý câu hỏi:
- CRUD câu hỏi và đáp án
- Quản lý loại câu hỏi
- Tìm kiếm và lọc câu hỏi
- Cung cấp API cho Quiz Service và Gameplay Service

Cơ sở dữ liệu: MySQL (question_db)
Công nghệ: Spring Boot, Spring Data JPA

### 7. Gameplay Service (Port: 8084)

Gameplay Service theo dõi hoạt động chơi game:
- Theo dõi tiến trình trò chơi
- Ghi nhận câu trả lời
- Quản lý người tham gia
- Phân tích kết quả và thống kê

Cơ sở dữ liệu: MySQL (gameplay_db)
Công nghệ: Spring Boot, Spring Data JPA

## Luồng Hoạt Động

### Đăng Ký và Đăng Nhập
1. Client gửi request đến `/api/auth/login` hoặc `/api/auth/register`
2. API Gateway xác định đây là endpoint công khai, chuyển tiếp request đến Identity Service
3. Identity Service xác thực người dùng qua User Service và tạo JWT token
4. Token được trả về cho Client

### Xác Thực Request
1. Client gửi request đến API Gateway kèm theo JWT token trong header `Authorization`
2. API Gateway xác thực token:
   - Kiểm tra tính hợp lệ của token
   - Kiểm tra thời hạn của token
   - Trích xuất thông tin người dùng từ token
3. API Gateway thêm header `X-Auth-User` chứa thông tin người dùng
4. API Gateway chuyển request đến service tương ứng

### Giao Tiếp Giữa Các Services
- Các service sử dụng Feign Client để gọi API của service khác
- Service discovery thông qua Eureka Server để tìm kiếm service cần gọi
- Giao tiếp đồng bộ thông qua REST API
- Xác thực giữa các service thông qua API Gateway

## Khả Năng Mở Rộng và Triển Khai

### Containerization
- Mỗi service được đóng gói trong Docker container riêng
- Docker Compose được sử dụng để điều phối các container trong môi trường phát triển
- Kubernetes có thể được sử dụng cho môi trường production

### Horizontal Scaling
- Mỗi service có thể được mở rộng độc lập bằng cách tăng số lượng instance
- Eureka Server và API Gateway tự động cân bằng tải giữa các instance
- Stateless design cho phép mở rộng dễ dàng

### Fault Tolerance
- Circuit breaker pattern để ngăn chặn lỗi cascade
- Fallback mechanisms để xử lý khi service không khả dụng
- Health checks và auto-recovery

## Bảo Mật

### Xác Thực
- JWT token cho xác thực người dùng
- Tokens chứa thông tin về user ID, roles, và thời hạn
- Access token và refresh token để tăng cường bảo mật

### Phân Quyền
- Role-based access control (RBAC)
- Resource-level permissions
- Method-level security với Spring Security

### Bảo Vệ Dữ Liệu
- Mã hóa mật khẩu với BCrypt
- HTTPS cho tất cả các kết nối
- Input validation để ngăn chặn injection attacks

## Kết Luận

Kiến trúc microservices của Quiz Application cung cấp một hệ thống mạnh mẽ, linh hoạt và có khả năng mở rộng cao. Mỗi service có trách nhiệm rõ ràng và hoạt động độc lập, đồng thời vẫn liên kết chặt chẽ với nhau thông qua API gateway và service discovery. Thiết kế này cho phép phát triển, triển khai và mở rộng từng thành phần một cách riêng biệt, đồng thời duy trì tính nhất quán và bảo mật cho toàn bộ hệ thống.