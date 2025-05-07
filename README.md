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
├── init-db.sql                     # Script khởi tạo cơ sở dữ liệu
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
└── service-with-ci-cd/             # Ví dụ về dịch vụ với pipeline CI/CD
    ├── Dockerfile                  # File cấu hình Docker
    └── src/                        # Mã nguồn dịch vụ
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
     - Giao diện đăng ký và giám sát dịch vụ
     - Xem các dịch vụ đã đăng ký và trạng thái của chúng
   
   - **API Gateway:** http://localhost:8080
     - Điểm vào chính cho tất cả các yêu cầu từ client
     - Tất cả các yêu cầu API nên được định tuyến thông qua endpoint này
   
   - **Identity Service:** http://localhost:8085
     - Xử lý đăng nhập, đăng ký và quản lý token
     - Cung cấp các endpoint xác thực và ủy quyền

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
- **IDE đề xuất:** IntelliJ IDEA hoặc Eclipse với hỗ trợ Spring Boot
- **Postman hoặc Insomnia:** Để kiểm thử API

### Các yêu cầu bổ sung:
- **Git:** Để quản lý phiên bản mã nguồn
- **MySQL Workbench:** Để quản lý và theo dõi cơ sở dữ liệu (tùy chọn)
- **Ít nhất 4GB RAM:** Để chạy tất cả các dịch vụ cùng lúc

---

## 📊 Sơ Đồ Dịch Vụ

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

Sơ đồ trên minh họa cách các dịch vụ trong hệ thống tương tác với nhau:

1. **Clients** gửi yêu cầu đến **API Gateway**
2. **API Gateway** định tuyến yêu cầu đến dịch vụ thích hợp, được xác định thông qua **Eureka Server**
3. Các dịch vụ giao tiếp với nhau sử dụng Feign Clients và gọi REST API
4. **Identity Service** xác thực và ủy quyền các yêu cầu trước khi các dịch vụ khác xử lý chúng
5. Mỗi dịch vụ có thể tương tác với dịch vụ khác khi cần, tạo thành một hệ thống phân tán hoàn chỉnh

---

## 👥 Thành Viên Nhóm và Đóng Góp

| Tên | MSSV | Đóng Góp |
|------|------------|--------------|
| [Thành viên 1] | [MSSV] | Identity Service, API Gateway, Tài liệu hệ thống |
| [Thành viên 2] | [MSSV] | User Service, Question Service, Thiết kế cơ sở dữ liệu |
| [Thành viên 3] | [MSSV] | Quiz Service, Gameplay Service, Tích hợp hệ thống |
| [Thành viên 4] | [MSSV] | Eureka Server, Cấu hình Docker, Kiểm thử |

### Chi tiết đóng góp:

#### [Thành viên 1]:
- Thiết kế và triển khai Identity Service
- Cấu hình API Gateway với Spring Cloud Gateway
- Xây dựng hệ thống xác thực JWT
- Viết tài liệu kiến trúc hệ thống và hướng dẫn triển khai

#### [Thành viên 2]:
- Phát triển User Service với đầy đủ CRUD operations
- Thiết kế và triển khai Question Service với hỗ trợ nhiều loại câu hỏi
- Thiết kế schema cơ sở dữ liệu cho toàn bộ hệ thống
- Tối ưu hóa truy vấn và hiệu suất cơ sở dữ liệu

#### [Thành viên 3]:
- Xây dựng Quiz Service với chức năng tạo và quản lý quiz
- Triển khai Gameplay Service với xử lý phiên chơi thời gian thực
- Tích hợp các dịch vụ sử dụng Feign Clients
- Phát triển các API endpoints và DTO patterns

#### [Thành viên 4]:
- Cấu hình và triển khai Eureka Server
- Xây dựng các Dockerfiles và docker-compose.yml
- Thiết lập CI/CD pipeline với GitHub Actions
- Phát triển các test cases và đảm bảo chất lượng mã

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

---

## 📝 Giấy Phép

[Tên Giấy Phép Của Bạn] © [Năm] [Tên Nhóm/Tổ Chức]

Phát triển và duy trì bởi [Tên Nhóm], [Trường/Tổ Chức]. Tất cả các quyền được bảo lưu.

Sử dụng mã nguồn này phải tuân theo các điều khoản của giấy phép và bao gồm thông báo bản quyền này.

