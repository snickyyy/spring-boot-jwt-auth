# Spring Boot JWT Auth

This repository provides an example implementation of authorization and authentication using Spring Boot and Spring Security.

---

## 🚀 About The Project

This project is a backend service that provides robust security based on JWT, including a **Refresh Token Rotation** strategy. It features user registration, login, and secured endpoints.

---

## 🛠️ Tech Stack

-   **Java 21+**
-   **Spring Boot 3.5.6**
-   **Spring Security 6**
-   **Spring Data JPA**
-   **PostgreSQL**
-   **Gradle**
-   **Docker & Docker Compose**
-   **Redis** *(planned for future implementation)*

---

## ⚙️ Getting Started

Follow these steps to get a local copy up and running.

### Prerequisites
-   Docker and Docker Compose

### Installation & Launch

1.  **Clone the repository:**
    ```bash
    git clone https://github.com/snickyyy/spring-boot-jwt-auth.git
    cd spring-boot-jwt-auth
    ```

2.  **Configure Environment Variables:**
    Create a `.env` file in the root of the project and add the necessary environment variables for the database connection.

    ```env
    POSTGRES_URL=jdbc:postgresql://localhost:5432/your_db
    POSTGRES_USERNAME=your_user
    POSTGRES_PASSWORD=your_password
    ```
    *Replace `your_db`, `your_user`, and `your_password` with your actual database credentials.*

3.  **Launch the application:**
    Run the following command to start the application and the required services using Docker Compose.
    ```bash
    make compose-up
    ```
---
