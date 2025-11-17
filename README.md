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

To create all required tables, run the SQL DDL script located here:
[`src/main/java/loyola/basketball/resources/db/schema.sql`](src/main/java/loyola/basketball/resources/db/schema.sql)