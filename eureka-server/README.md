# Eureka Server

## Tổng quan

Eureka Server là trung tâm đăng ký và khám phá dịch vụ (service registry and discovery) cho hệ thống microservice. Nó cho phép các service đăng ký chính mình và tìm kiếm các service khác mà không cần biết chính xác vị trí của chúng.

## Chức năng chính

- Đăng ký và theo dõi trạng thái của các service trong hệ thống
- Cho phép các service tìm kiếm và giao tiếp với nhau một cách linh hoạt
- Cung cấp giao diện quản trị để theo dõi các service đã đăng ký
- Hỗ trợ cân bằng tải giữa các instance của cùng một service
- Tự động loại bỏ các service không còn hoạt động (health check)

## Giao diện quản trị

Eureka Server cung cấp một giao diện web để theo dõi các service đã đăng ký. Giao diện này hiển thị:

- Danh sách tất cả các service đang hoạt động
- Trạng thái của mỗi service và các instance của chúng
- Thông tin về máy chủ, port và các metadata khác
- Thời gian hoạt động và trạng thái sức khỏe

## Cấu hình

Service chạy trên port 8761 và có các cấu hình chính sau:

- Tự vô hiệu hóa đăng ký bản thân làm client (`eureka.client.register-with-eureka=false`)
- Không truy xuất registry từ Eureka khác (`eureka.client.fetch-registry=false`)
- Thời gian chờ để xác định một service không khả dụng

## Kết nối với Eureka Server

Các service khác cần thêm Spring Cloud Eureka Client và cấu hình sau trong file `application.properties` hoặc `application.yml`:

```properties
eureka.client.service-url.defaultZone=http://localhost:8761/eureka/
eureka.instance.prefer-ip-address=true
```

## Cách chạy

### Yêu cầu

- Java 17+
- Maven

### Lệnh chạy

```bash
# Đi đến thư mục Eureka Server
cd eureka-server

# Build project
./mvnw clean package

# Chạy service
java -jar target/eureka-server-0.0.1-SNAPSHOT.jar
```

### Sử dụng Docker

```bash
# Build Docker image
docker build -t eureka-server .

# Chạy container
docker run -p 8761:8761 eureka-server
```

## Truy cập Dashboard

Giao diện quản trị Eureka có thể được truy cập tại:
http://localhost:8761