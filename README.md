\# 🚀 Microservices Architecture with Spring Boot, Spring Cloud Gateway \& Redis Rate Limiter



This project demonstrates a \*\*microservices-based architecture\*\* built using \*\*Spring Boot\*\* and \*\*Spring Cloud Gateway\*\*, with \*\*inter-service communication via RestTemplate\*\* and \*\*API rate limiting via Redis\*\*.



It simulates a \*\*PayPal-like wallet system\*\* — one service handles user operations, and another manages wallet balances.



---



\## 🧩 Architecture Overview



\### Services:

1\. \*\*User Service\*\*

&nbsp;  - Handles user operations such as registration, login, and payments.

&nbsp;  - Fetches wallet balance by calling the Wallet Service using `RestTemplate`.



2\. \*\*Wallet Service\*\*

&nbsp;  - Manages user wallet data and provides APIs like `GET /get-balance` to retrieve balance by `userId`.



3\. \*\*API Gateway\*\*

&nbsp;  - Acts as a single entry point for all client requests.

&nbsp;  - Routes requests to respective services.

&nbsp;  - Implements \*\*Rate Limiting\*\* using Redis to prevent abuse and control request frequency.



---



\## ⚙️ Tech Stack



| Layer | Technology |

|-------|-------------|

| Backend | Spring Boot (v3.x) |

| API Gateway | Spring Cloud Gateway |

| Communication | RestTemplate |

| Rate Limiting | RedisRateLimiter |

| Token Security | JWT (io.jsonwebtoken) |

| Build Tool | Maven |

| Containerization | Docker |

| Database (optional) | PostgreSQL / MySQL |



---



\## 🧠 Features



✅ \*\*Microservice Communication\*\* – User Service calls Wallet Service using RestTemplate.  

✅ \*\*JWT Authentication\*\* – Secure APIs using tokens.  

✅ \*\*Rate Limiting at API Gateway\*\* – Protects from abuse (e.g., 5 requests/sec with burst up to 10).  

✅ \*\*Centralized Routing\*\* – Gateway handles all incoming traffic and routing rules.  

✅ \*\*Scalable Architecture\*\* – Easily add new services or adjust rate limits.  



---





