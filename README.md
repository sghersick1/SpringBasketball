# SpringBasketball
Spring Boot backend connected to MySQL database for managing basketball league data

## Tech Stack

- **Language:** Java 21
- **Framework:** Spring Boot 3.5.7
- **Build Tool:** Maven
- **Database:** MySQL 8+ (local)
- **Security:** Spring Security (JDBC-based users/authorities)
- **DB Access:** Spring JDBC (JdbcTemplate), no JPA

## Prerequisites

- Java JDK 21
- Maven 3.9+
- MySQL Server 8+ running locally
- An IDE (IntelliJ IDEA, Eclipse, etc.)

## Database Schema

1. Configure [`src/main/resources/application.properties`](src/main/resources/application.properties) with valid details
2. Create `src/main/resources/secrets.properties` file following [`secrets.properties example`](src/main/resources/secrets.example.properties)
3. To create all required tables, run the SQL DDL script located here:
[`src/main/resources/db/Schema.sql`](src/main/resources/db/Schema.sql)
