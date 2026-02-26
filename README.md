# Global Finance API

High-performance backend engine for a financial platform, built with **Java 21** and **Spring Boot 3**.
This API handles multi-currency accounts, atomic transactions, and audit logs.

## 🚀 Key Features

* **Secure Authentication:** Robust user registration and login system powered by **JWT (JSON Web Tokens)** and Spring Security.
* **Multi-Currency Management:** Create accounts in different currencies (EUR, USD, etc.) with automated balance tracking.
* **Smart Transfers:** Atomic money transfers between accounts with **Real-Time Exchange Rates** integrated via external Financial APIs.
* **Financial Analytics:** Advanced spending statistics grouped by categories (Food, Rent, Leisure, etc.) using **JPQL Projections**.
* **Transaction History:** Paginated and filterable transaction logs with custom date ranges.
* **Data Integrity:** Strict validation layers (DTOs) and a centralized **Global Exception Handler** for clean API responses.

## Tech Stack

* **Core:** Java 21, Spring Boot 3.2+
* **Database:** PostgreSQL (Dockerized)
* **Security:** Spring Security, JWT
* **API Documentation:** **SpringDoc OpenAPI (Swagger UI)**
* **External Integrations:** **ExchangeRate-API** for real-time currency conversion
* **Tools:** Lombok, Maven, Docker Compose

## API Documentation

Once the application is running, you can explore and test the API endpoints through the **Swagger UI** interactive interface:

🔗 **[http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)**

The documentation includes detailed request/response schemas, security requirements, and example values for every endpoint.

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

5.  To enable real-time currency conversion, add your **ExchangeRate-API** key to the `src/main/resources/application.yml` file:
    ```yaml
    api:
      exchangerate:
        url: [https://v6.exchangerate-api.com/v6/](https://v6.exchangerate-api.com/v6/)
        key: ${EXCHANGE_RATE_KEY:your_api_key_here}
    ```
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
