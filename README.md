<p align="center">
  <img src="src/main/resources/static/images/logo.png" alt="Global Finance API Logo" width="200">
</p>

<h1 align="center">Global Finance API</h1>

High-performance backend engine for a financial platform, built with **Java 21** and **Spring Boot 3**.
This API handles multi-currency accounts, atomic transactions, audit logs, and high-speed data caching.

🚀 **[Try the API live (Swagger UI)](https://global-finance-api-production.up.railway.app)**

## Key Features

* **Secure Authentication:** Robust user registration and login system powered by **JWT (JSON Web Tokens)** and Spring Security.
* **Multi-Currency Management:** Create accounts in different currencies (EUR, USD, etc.) with automated balance tracking.
* **Smart Transfers:** Atomic money transfers between accounts with **Real-Time Exchange Rates** integrated via external Financial APIs.
* **High-Performance Caching:** Integrated **Redis** to cache heavy database queries (like financial analytics), reducing response times to milliseconds and preventing database overload.
* **Financial Analytics:** Advanced spending statistics grouped by categories (Food, Rent, Leisure, etc.) using **JPQL Projections**.
* **Transaction History:** Paginated and filterable transaction logs with custom date ranges.
* **Data Integrity & Resilience:** Strict validation layers (DTOs) and a centralized **Global Exception Handler** for clean, standardized API responses.

## Tech Stack

* **Core:** Java 21, Spring Boot 3.2+
* **Databases:** PostgreSQL (Relational) & Redis (In-Memory Cache)
* **Security:** Spring Security, JWT
* **Deployment & CI/CD:** Railway (Cloud Hosting), Docker
* **API Documentation:** SpringDoc OpenAPI (Swagger UI)
* **External Integrations:** ExchangeRate-API for real-time currency conversion
* **Testing:** JUnit 5, Mockito
* **Tools:** Lombok, Maven, Docker Compose

## ☁️ Cloud Deployment (Production)

This project is fully deployed in the cloud using a **Continuous Deployment (CI/CD)** pipeline via **Railway**. Every update pushed to the repository is automatically built and deployed.

The production infrastructure consists of:
* A containerized **Spring Boot** application. 
* A managed **PostgreSQL** database. 
* A managed **Redis** instance for caching. 
* Secure routing with HTTPS and proxy headers properly configured for production environments.

🔗 **[Access the Production Environment](https://global-finance-api-production.up.railway.app)**

## Setup & Installation (Local Environment)

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

## Monitoring & Documentation
Once running locally, you can access the following tools:
* **Swagger UI:** http://localhost:8080/swagger-ui.html
* **Prometheus:** http://localhost:9090
* **Grafana:** http://localhost:3000 (User: `admin` / Pass: `admin1234`)

## Project Structure

```text
src/main/java/com/github/pmbdev/global_finance_api
├── config/           # App configuration (Beans, OpenAPI, CORS)
├── controller/       # REST Endpoints (The entry point)
│   └── dto/          # Data Transfer Objects (Requests/Responses)
├── exception/        # Global Error Handling (@ControllerAdvice)
│   └── custom/       # Custom Exception definitions
├── repository/       # Data Access Layer (JPA Interfaces)
│   └── entity/       # Database Tables (The core data models)
│       └── enums/    # Enumerate Tables
├── security/         # JWT Filters & Security Config
│   └── filter/       # Authentication and Authorization filters
└── service/          # Business Logic Interfaces
    └── impl/         # Business Logic Implementation (The brain)
