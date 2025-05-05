# Kiến Trúc Hệ Thống

## Tổng Quan
Ứng Dụng Quiz là nền tảng dựa trên microservices được thiết kế để tạo, quản lý và tham gia vào các trò chơi quiz tương tác. Hệ thống cho phép người dùng tạo các quiz với nhiều loại câu hỏi khác nhau, tham gia vào các phiên quiz trực tiếp, theo dõi hiệu suất và quản lý tương tác người dùng.

## Thành Phần Hệ Thống

- **User Service**: Quản lý hồ sơ người dùng, mối quan hệ, tùy chọn và cài đặt. Lưu trữ thông tin cá nhân và dữ liệu liên quan đến người dùng.

- **Quiz Service**: Xử lý việc tạo và quản lý các quiz, bộ sưu tập quiz và metadata quiz. Chịu trách nhiệm tổ chức nội dung và làm cho nó có thể được khám phá.

- **Question Service**: Quản lý các loại câu hỏi khác nhau (đúng/sai, trắc nghiệm, thanh trượt, câu đố, phản hồi văn bản) và nội dung của chúng. Cung cấp câu hỏi cho Quiz Service.

- **Gameplay Service**: Theo dõi các phiên quiz, phản hồi của người tham gia, điểm số và thống kê. Xử lý tương tác trò chơi theo thời gian thực và kết quả.

- **Identity Service**: Xử lý xác thực, ủy quyền và bảo mật. Cấp token JWT và xác minh danh tính người dùng.

- **API Gateway**: Định tuyến yêu cầu từ bên ngoài đến các microservice thích hợp. Đóng vai trò là điểm vào cho tất cả các yêu cầu của client.

- **Eureka Server**: Cung cấp khám phá và đăng ký dịch vụ. Giúp các dịch vụ định vị và giao tiếp với nhau.

## Giao Tiếp
Các dịch vụ giao tiếp chủ yếu thông qua REST API sử dụng các mẫu sau:

- **Giao Tiếp Bên Ngoài**: Tất cả các yêu cầu từ client đi qua API Gateway, định tuyến chúng đến dịch vụ thích hợp.

- **Giao Tiếp Nội Bộ**: Các dịch vụ giao tiếp với nhau bằng Feign Clients, cung cấp cách khai báo để gọi các endpoint REST của dịch vụ khác.

- **Khám Phá Dịch Vụ**: Tất cả các dịch vụ đăng ký với Eureka Server, cho phép chúng khám phá và giao tiếp với nhau mà không cần URL cứng.

- **Luồng Xác Thực**: Identity Service cấp token JWT được bao gồm trong các yêu cầu và được xác thực bởi các dịch vụ.

## Luồng Dữ Liệu

1. **Luồng Xác Thực Người Dùng**:
   - Client gửi yêu cầu đăng nhập đến API Gateway
   - Gateway định tuyến đến Identity Service
   - Identity Service xác thực thông tin đăng nhập và cấp token JWT
   - Token được trả về client và sử dụng trong các yêu cầu tiếp theo

2. **Luồng Tạo Quiz**:
   - Quiz Service nhận yêu cầu tạo quiz
   - Quiz Service xác thực người dùng thông qua User Service
   - Dữ liệu quiz được lưu trữ trong cơ sở dữ liệu Quiz Service
   - Câu hỏi được tạo thông qua Question Service

3. **Luồng Gameplay**:
   - Gameplay Service tạo phiên quiz
   - Người dùng tham gia thông qua các endpoint Participant
   - Dữ liệu câu hỏi được lấy từ Question Service
   - Phản hồi của người dùng được theo dõi và tính điểm
   - Kết quả được lưu trữ và thống kê được cập nhật

## Sơ Đồ

```
+-------------+                 +----------------+
|             |                 |                |
|   Clients   |---------------->|  API Gateway   |
| (Web/Mobile)|                 |   (Port 8080)  |
|             |<----------------|                |
+-------------+                 +----------------+
                                        |
                                        v
                               +-----------------+
                               |                 |
                               | Eureka Server   |
                               |  (Port 8761)    |
                               |                 |
                               +-----------------+
                                        |
            +---------------------------|---------------------------+
            |                           |                           |
            v                           v                           v
   +----------------+          +----------------+          +----------------+
   |                |          |                |          |                |
   | Identity Service|<-------->| User Service   |<-------->| Quiz Service   |
   |  (Port 8085)   |          |  (Port 8081)   |          |  (Port 8082)   |
   |                |          |                |          |                |
   +----------------+          +----------------+          +----------------+
            ^                           ^                           ^
            |                           |                           |
            |                           v                           v
            |                  +----------------+          +----------------+
            |                  |                |          |                |
            +----------------->| Question Service|<-------->| Gameplay Service|
                               |  (Port 8083)   |          |  (Port 8084)   |
                               |                |          |                |
                               +----------------+          +----------------+
```

## Khả Năng Mở Rộng & Khả Năng Chịu Lỗi

- **Khả Năng Mở Rộng Theo Chiều Ngang**: Mỗi dịch vụ có thể được mở rộng độc lập dựa trên nhu cầu. Ví dụ, trong hoạt động quiz cao điểm, Gameplay Service có thể được mở rộng mà không ảnh hưởng đến các dịch vụ khác.

- **Đăng Ký Dịch Vụ**: Eureka Server cung cấp khám phá dịch vụ, cho phép đăng ký và khám phá động các phiên bản dịch vụ.

- **Cô Lập Lỗi**: Nếu một dịch vụ gặp sự cố, nó không làm sập toàn bộ hệ thống. Ví dụ, nếu Question Service gặp vấn đề, các trò chơi hiện tại có thể tiếp tục với các câu hỏi được cache.

- **Cô Lập Cơ Sở Dữ Liệu**: Mỗi dịch vụ có cơ sở dữ liệu riêng, ngăn chặn lỗi dây chuyền từ vấn đề cơ sở dữ liệu.

- **Giám Sát Sức Khỏe**: Các dịch vụ triển khai các endpoint sức khỏe để giám sát và tự động khởi động lại khi cần thiết.

- **Circuit Breaking**: Triển khai trong tương lai sẽ bao gồm các circuit breakers để ngăn chặn lỗi dây chuyền khi giao tiếp giữa các dịch vụ thất bại.