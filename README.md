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
src/main/java/com/github/[user]/globalfinance
├── controller/   # REST Endpoints
├── service/      # Business Logic
├── repository/   # Data Access (JPA)
└── entity/       # Database Models