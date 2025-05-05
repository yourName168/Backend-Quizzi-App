# Identity Service

## Tổng quan

Identity Service là service chịu trách nhiệm về xác thực và phân quyền cho toàn bộ hệ thống. Service này quản lý đăng nhập, đăng ký, cũng như cung cấp JWT token cho người dùng để sử dụng trong các request đến các service khác.

## Chức năng chính

- Đăng ký tài khoản người dùng mới (thông qua User Service)
- Xác thực người dùng và tạo JWT token
- Quản lý thông tin xác thực người dùng
- Cung cấp token xác thực cho API Gateway

## Cấu trúc API

| Endpoint | Phương thức | Mô tả |
|----------|------------|-------|
| `/api/auth/login` | POST | Đăng nhập và nhận JWT token |
| `/api/auth/register` | POST | Đăng ký tài khoản mới (tạo user trong User Service) |

## Quy trình đăng ký

1. Client gửi request đăng ký đến `/api/auth/register`
2. Identity Service kiểm tra username và email chưa tồn tại
3. Identity Service gọi API của User Service để tạo người dùng mới
4. Nếu User Service tạo thành công, Identity Service tạo một bản ghi xác thực
5. Identity Service tạo JWT token và trả về cho client

## Cấu hình JWT

Identity Service tạo và quản lý JWT token với các cấu hình sau:

- Secret key: Được lưu trong cấu hình ứng dụng
- Thời hạn token: Mặc định 24 giờ
- Thông tin được mã hóa trong token: userId, username

## Cơ sở dữ liệu

Service sử dụng cơ sở dữ liệu MySQL với bảng chính:

- `users`: Lưu thông tin xác thực người dùng (username, email, password)

## Kết nối với các service khác

Identity Service kết nối với:

1. **API Gateway**: Cung cấp xác thực và JWT token
2. **User Service**: Tạo và quản lý thông tin người dùng
3. **Eureka Server**: Đăng ký service và khám phá các service khác

Quy trình hoạt động:

1. API Gateway chuyển các yêu cầu đăng nhập/đăng ký đến Identity Service
2. Identity Service xác thực và tạo JWT token, trả về cho client
3. API Gateway xác thực token trong các request tiếp theo
4. API Gateway thêm thông tin người dùng vào header trước khi chuyển tiếp đến các service khác

## Cách chạy

### Yêu cầu

- Java 17+
- Maven
- MySQL

### Cấu hình cơ sở dữ liệu

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/identity_db
spring.datasource.username=root
spring.datasource.password=root
```

### Lệnh chạy

```bash
# Đi đến thư mục Identity Service
cd identity-service

# Build project
./mvnw clean package

# Chạy service
java -jar target/identity-service-0.0.1-SNAPSHOT.jar
```

### Sử dụng Docker

```bash
# Build Docker image
docker build -t identity-service .

# Chạy container
docker run -p 8085:8085 identity-service
```

### Sử dụng docker-compose (trong hệ thống microservices)

```bash
# Chạy cùng với toàn bộ hệ thống
docker-compose up -d
```

## Đăng ký và đăng nhập

### Đăng ký tài khoản mới

**Request:**
```
POST /api/auth/register
```

**Body:**
```json
{
  "username": "user123",
  "email": "user123@example.com",
  "password": "password123"
}
```

**Response:**
```json
{
  "accessToken": "eyJhbGciOiJIUzI1...",
  "tokenType": "Bearer",
  "userId": 1,
  "username": "user123",
  "email": "user123@example.com"
}
```

### Đăng nhập

**Request:**
```
POST /api/auth/login
```

**Body:**
```json
{
  "usernameOrEmail": "user123",
  "password": "password123"
}
```

**Response:**
```json
{
  "accessToken": "eyJhbGciOiJIUzI1...",
  "tokenType": "Bearer",
  "userId": 1,
  "username": "user123",
  "email": "user123@example.com"
}
```