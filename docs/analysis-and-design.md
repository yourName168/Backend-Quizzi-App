# 📊 Quiz Application Microservices - Analysis and Design

This document outlines the **analysis** and **design** process for the Quiz Application microservices-based system. It explains the architecture decisions and system components.

---

## 1. 🎯 Problem Statement

The Quiz Application is an interactive platform designed to create, manage, and participate in quiz games.

- **Who are the users?**
  - Students seeking to test their knowledge
  - Teachers/educators creating quiz content
  - Administrators managing the platform
  - Casual users playing quiz games for entertainment

- **What are the main goals?**
  - Allow users to create and customize quizzes with various question types
  - Provide an interactive real-time quiz gameplay experience
  - Track user performance and progress
  - Support different quiz formats and question types
  - Ensure scalability and reliability of the platform

- **What kind of data is processed?**
  - User account information and profiles
  - Quiz content including questions and answers
  - Gameplay statistics and user performance data
  - User relationships (follows, shares)

---

## 2. 🧩 Identified Microservices

| Service Name      | Responsibility                                              | Tech Stack             |
|-------------------|------------------------------------------------------------|-----------------------|
| User Service      | Manages user profiles, relationships, and preferences       | Spring Boot, MySQL    |
| Quiz Service      | Handles quiz creation, collections, and metadata            | Spring Boot, MySQL    |
| Question Service  | Manages various question types and their content            | Spring Boot, MySQL    |
| Gameplay Service  | Tracks quiz game sessions, participants, and results        | Spring Boot, MySQL    |
| Identity Service  | Handles authentication, authorization, and security         | Spring Boot, JWT      |
| Eureka Server     | Service discovery and registration                          | Spring Cloud Netflix  |
| API Gateway       | Routes requests and acts as entry point to the system       | Spring Cloud Gateway  |

---

## 3. 🔄 Service Communication

Services communicate primarily through REST APIs with the following patterns:

- **API Gateway ⇄ All Services**: Gateway routes external requests to appropriate services (REST)
- **Identity Service ⇄ All Services**: For authentication and user verification (JWT token)
- **Service-to-Service Communication**: Direct REST calls through Feign Clients:
  - Quiz Service ⇄ User Service: To validate quiz creators
  - Question Service ⇄ Quiz Service: To verify quiz existence when creating questions
  - Gameplay Service ⇄ Quiz Service: To retrieve quiz data for gameplay sessions
  - Gameplay Service ⇄ User Service: To verify participants
  - Gameplay Service ⇄ Question Service: To retrieve questions during gameplay

All services register with Eureka Server for service discovery, allowing dynamic scaling and failover.

---

## 4. 🗂️ Data Design

Each service maintains its own database to ensure loose coupling:

- **User Service Database**:
  - `users`: Core user information
  - `user_profiles`: Extended profile information
  - `user_follows`: User relationship data
  - `user_settings`: User preferences and settings
  - `user_music_effects`: User audio preferences

- **Quiz Service Database**:
  - `quizzes`: Core quiz metadata
  - `quiz_collections`: Groups of related quizzes
  - `quiz_games`: Live game session metadata
  - `quiz_tags`: Categorization tags
  - `quiz_categories`: Main quiz categories

- **Question Service Database**:
  - `questions`: Base question entity
  - `question_types`: Different formats (TRUE_FALSE, CHOICE, SLIDER, etc.)
  - Type-specific tables:
    - `question_true_false`: Boolean questions
    - `question_choice`: Multiple choice questions
    - `question_slider`: Numeric range questions
    - `question_puzzle`: Puzzle-based questions
    - `question_text`: Free text response questions

- **Gameplay Service Database**:
  - `quiz_game_tracking`: Session progress and statistics
  - `participants`: Users participating in game sessions
  - `question_tracking`: Records of user responses to questions
  - `game_results`: Final outcome data
  - `game_analytics`: Performance metrics

---

## 5. 🔐 Security Considerations

- **JWT Authentication**: Secure token-based authentication system
  - Tokens issued by Identity Service upon login
  - Tokens validated by API Gateway before forwarding requests
  - Service-to-service calls include authentication headers

- **Role-Based Access Control**:
  - User roles (USER, ADMIN) determine permitted actions
  - Admin-only endpoints protected with `@PreAuthorize` annotations

- **Input Validation**:
  - Request validation on both client and server sides
  - DTO pattern used to filter and validate incoming data

- **Secure Communication**:
  - HTTPS for all external API communications
  - Internal service calls secured within Docker network

- **Error Handling**:
  - Custom exception handlers prevent leaking sensitive information
  - Standardized error responses across services

---

## 6. 📦 Deployment Plan

- **Containerization with Docker**:
  - Each service has its own Dockerfile
  - MySQL database containerized with initialization scripts
  - Environment variables for service configuration

- **Container Orchestration**:
  - Docker Compose for local development environment
  - Services configured to start in proper order with health checks
  - Scalable for production using Kubernetes (future)

- **Configuration Management**:
  - External configuration via application.properties
  - Environment-specific settings through Docker environment variables
  - Consistent port assignments across environments

- **CI/CD Pipeline**:
  - Automated build and test processes
  - Integration tests for service interactions
  - Service versioning for compatibility

---

## 7. 🎨 Architecture Diagram

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
                                        ^                           ^
                                        |                           |
                                        v                           v
                                  +-----------------------------+
                                  |                             |
                                  |      MySQL Databases        |
                                  |                             |
                                  +-----------------------------+
```

---

## ✅ Summary

The microservices architecture for the Quiz Application provides several advantages:

1. **Independent Development & Deployment**: Each service can be developed, tested, and deployed independently, allowing for faster feature delivery and team autonomy.

2. **Scalability**: Services can be scaled individually based on demand (e.g., Gameplay Service during peak usage, Question Service for content-heavy operations).

3. **Technology Flexibility**: While currently standardized on Spring Boot, each service could potentially use different technologies if needed for specific requirements.

4. **Resilience**: Failure in one service does not bring down the entire system, and services can be designed with fallback mechanisms.

5. **Maintainability**: Smaller, focused codebases are easier to understand and maintain compared to a monolithic application.

6. **Performance Optimization**: Resources can be allocated based on specific service needs rather than scaling the entire application.

The architecture supports the requirements of an interactive quiz platform while providing a foundation for future growth and feature expansion.

## Author

- Team 3


Implementation adapted for Quiz Application Microservices.
