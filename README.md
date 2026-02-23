# Global Finance API

High-performance backend engine for a financial platform, built with **Java 21** and **Spring Boot 3**.
This API handles multi-currency accounts, atomic transactions, and audit logs.

## Tech Stack

* **Core:** Java 21, Spring Boot 3.2
* **Database:** PostgreSQL 15 (Dockerized)
* **Security:** Spring Security & JWT
* **DevOps:** Docker Compose
* **Architecture:** Hexagonal-inspired (Controller -> Service -> Repository)

## Setup & Installation

**Prerequisites:**
* Java 21 JDK
* Docker & Docker Compose

**How to run:**
1.  Clone the repository:
    ```bash
    git clone https://github.com/Pablo0-mb/global-finance-api.git
    ```
2.  Start the database infrastructure:
    ```bash
    docker compose up -d
    ```
3.  Run the application:
    ```bash
    mvnw spring-boot:run
    ```
4.  Access the API at 'http://localhost:8080'

## Project Structure

```text
src/main/java/com/github/pmbdev/global_finance_api
├── config/           # App configuration (Beans, PasswordEncoder)
├── controller/       # REST Endpoints (The entry point)
│   └── dto/          # Data Transfer Objects (Requests/Responses)
├── exception/        # Global Error Handling logic
│   └── custom/       # Custom Error Handling logic
├── repository/       # Data Access Layer (JPA Interfaces)
│   └── entity/       # Database Tables (The core data models)
│       └── enums/    # Enumerate Tables
├── security/         # JWT Filters & Security Config
└── service/          # Business Logic Interfaces
    └── impl/         # Business Logic Implementation (The brain)
