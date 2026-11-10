# Microservices Architecture with Spring Boot, Spring Cloud Gateway & Redis Rate Limiting

A **microservices-based application** built using Spring Boot and Spring Cloud Gateway, demonstrating inter-service communication with `RestTemplate`, JWT-based authentication, and API rate limiting using Redis.

The project simulates a **PayPal-like digital wallet system**, where independent services handle user management and wallet operations, with an API Gateway serving as the single entry point for client requests.

---

## 🏗️ Architecture Overview

The application consists of three main components:

### 1. User Service
- Handles user-related operations, including registration, login, and payment requests.
- Communicates with the Wallet Service using `RestTemplate`.
- Retrieves wallet balances using the user's ID.
- Handles JWT-based authentication.

### 2. Wallet Service
- Manages user wallet information and balances.
- Provides REST APIs for wallet-related operations.
- Exposes endpoints such as `GET /get-balance` to retrieve a user's wallet balance.
- Maintains wallet-related business logic independently of the User Service.

### 3. API Gateway
- Acts as the **single entry point** for all incoming client requests.
- Routes requests to the appropriate microservices using Spring Cloud Gateway.
- Implements Redis-based rate limiting to control request frequency.
- Helps protect backend services from excessive traffic and API abuse.

---

## 🛠️ Tech Stack

| Layer | Technology |
|---|---|
| Backend Framework | Spring Boot 3.x |
| API Gateway | Spring Cloud Gateway |
| Inter-Service Communication | RestTemplate |
| Rate Limiting | Redis & RedisRateLimiter |
| Authentication | JWT (io.jsonwebtoken) |
| Build Tool | Apache Maven |
| Containerization | Docker |
| Database | PostgreSQL / MySQL (optional) |

---

## ✨ Key Features

- **Microservices Architecture:** Separates user management and wallet operations into independently manageable services.
- **Inter-Service Communication:** Uses `RestTemplate` for synchronous REST-based communication between microservices.
- **JWT Authentication:** Secures API endpoints using JSON Web Tokens.
- **Redis Rate Limiting:** Controls incoming API traffic using Spring Cloud Gateway's `RedisRateLimiter`.
- **Centralized API Gateway:** Provides a unified entry point and manages request routing.
- **Scalable Design:** Allows additional microservices to be integrated without significantly changing the existing architecture.
- **Traffic Protection:** Supports configurable request limits, including an example configuration of 5 requests per second with a burst capacity of 10.

---

## 🔄 Request Flow

1. The client sends an HTTP request to the API Gateway.
2. The Gateway applies configured Redis-based rate limiting.
3. The request is routed to the appropriate microservice.
4. The User Service communicates with the Wallet Service through `RestTemplate` when wallet information is required.
5. The Wallet Service processes the request and returns the relevant data.
6. The response is returned to the client through the API Gateway.

---

## 📌 Project Objective

The primary objective of this project is to demonstrate practical implementation of distributed application concepts using the Spring ecosystem, including:

- Microservices communication.
- API Gateway and centralized routing.
- JWT-based authentication.
- Redis-based distributed rate limiting.
- Service separation and scalability.

---

## 👨‍💻 Author

**Shivanshu Aggarwal**

