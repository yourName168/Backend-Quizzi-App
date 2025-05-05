# System Architecture

## Overview
The Quiz Application is a microservices-based platform designed to create, manage, and participate in interactive quiz games. The system allows users to create quizzes with various question types, participate in live quiz sessions, track performance, and manage user interactions.

## System Components

- **User Service**: Manages user profiles, relationships, preferences, and settings. It stores personal information and user-related data.

- **Quiz Service**: Handles the creation and management of quizzes, quiz collections, and quiz metadata. It's responsible for organizing content and making it discoverable.

- **Question Service**: Manages different types of questions (true/false, multiple choice, sliders, puzzles, text responses) and their content. It provides questions to the Quiz Service.

- **Gameplay Service**: Tracks quiz game sessions, participants' responses, scores, and statistics. It handles real-time game interactions and results.

- **Identity Service**: Handles authentication, authorization, and security. It issues JWT tokens and verifies user identities.

- **API Gateway**: Routes external requests to the appropriate microservices. It serves as the entry point for all client requests.

- **Eureka Server**: Provides service discovery and registration. It helps services locate and communicate with each other.

## Communication
Services communicate primarily through REST APIs using the following patterns:

- **External Communication**: All client requests go through the API Gateway, which routes them to the appropriate service.

- **Internal Communication**: Services communicate with each other using Feign Clients, which provide a declarative way to call other services' REST endpoints.

- **Service Discovery**: All services register with Eureka Server, allowing them to discover and communicate with each other without hardcoded URLs.

- **Authentication Flow**: The Identity Service issues JWT tokens that are included in requests and validated by services.

## Data Flow

1. **User Authentication Flow**:
   - Client sends login request to API Gateway
   - Gateway routes to Identity Service
   - Identity Service validates credentials and issues JWT token
   - Token is returned to client and used in subsequent requests

2. **Quiz Creation Flow**:
   - Quiz Service receives quiz creation request
   - Quiz Service validates user via User Service
   - Quiz data is stored in Quiz Service database
   - Questions are created via Question Service

3. **Gameplay Flow**:
   - Gameplay Service creates a quiz session
   - Users join via Participant endpoints
   - Question data is retrieved from Question Service
   - User responses are tracked and scored
   - Results are stored and statistics updated

## Diagram

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

## Scalability & Fault Tolerance

- **Horizontal Scalability**: Each service can be independently scaled based on demand. For example, during high quiz activity, the Gameplay Service can be scaled without affecting other services.

- **Service Registry**: Eureka Server provides service discovery, allowing for dynamic registration and discovery of service instances.

- **Fault Isolation**: If one service fails, it doesn't bring down the entire system. For example, if the Question Service experiences issues, existing games can continue with cached questions.

- **Database Isolation**: Each service has its own database, preventing cascading failures from database issues.

- **Health Monitoring**: Services implement health endpoints for monitoring and automatic restarts when necessary.

- **Circuit Breaking**: Future implementation will include circuit breakers to prevent cascading failures when inter-service communication fails.