# User Service

## Tổng quan

User Service là service chịu trách nhiệm quản lý thông tin người dùng, profile, và các mối quan hệ giữa người dùng trong hệ thống. Service này cung cấp các API để tạo, cập nhật và truy vấn thông tin người dùng.

## Chức năng chính

- Quản lý thông tin cá nhân và profile người dùng
- Quản lý các mối quan hệ giữa người dùng (theo dõi, bạn bè)
- Lưu trữ tùy chọn và cấu hình cá nhân của người dùng
- Quản lý hiệu ứng âm nhạc cho người dùng
- Cung cấp API để tìm kiếm và lọc người dùng

## Cấu trúc API

| Endpoint | Phương thức | Mô tả |
|----------|------------|-------|
| `/api/users` | GET | Lấy danh sách người dùng |
| `/api/users` | POST | Tạo người dùng mới |
| `/api/users/{id}` | GET | Lấy thông tin người dùng theo ID |
| `/api/users/{id}` | PUT | Cập nhật thông tin người dùng |
| `/api/users/{id}` | DELETE | Xóa người dùng |
| `/api/user-follows` | GET | Lấy danh sách theo dõi |
| `/api/user-follows` | POST | Tạo quan hệ theo dõi mới |
| `/api/user-music-effects` | GET | Lấy danh sách hiệu ứng âm nhạc |

## Mô hình dữ liệu

Service sử dụng cơ sở dữ liệu MySQL với các bảng chính:

- `users`: Lưu thông tin chi tiết của người dùng
- `user_profiles`: Lưu thông tin profile mở rộng
- `user_follows`: Lưu quan hệ theo dõi giữa các người dùng
- `user_settings`: Lưu cấu hình và tùy chọn của người dùng
- `user_music_effects`: Lưu thông tin hiệu ứng âm nhạc

## Kết nối với các Service khác

User Service kết nối với:

- **Identity Service**: Để xác thực và lấy thông tin người dùng
- **Quiz Service**: Cung cấp thông tin người dùng cho quiz
- **Gameplay Service**: Cung cấp thông tin người dùng cho các phiên chơi

## Cấu hình

Service chạy trên port 8081 với các cấu hình cơ bản:

```properties
server.port=8081
spring.application.name=user-service
eureka.client.service-url.defaultZone=http://localhost:8761/eureka/
```

## Cách chạy

### Yêu cầu

- Java 17+
- Maven
- MySQL

### Cấu hình cơ sở dữ liệu

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/user_db
spring.datasource.username=root
spring.datasource.password=root
```

### Lệnh chạy

```bash
# Đi đến thư mục User Service
cd user-service

# Build project
./mvnw clean package

# Chạy service
java -jar target/user-service-0.0.1-SNAPSHOT.jar
```

### Sử dụng Docker

```bash
# Build Docker image
docker build -t user-service .

# Chạy container
docker run -p 8081:8081 user-service
```