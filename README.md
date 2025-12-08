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

## API Documentation

### SecurityController Endpoints

#### Register User

**POST** `/auth/register`

Register a new user account. New users are automatically assigned the `USER` role.

**Request Body:**

```json
{
  "username": "string",
  "password": "string"
}
```

**Success Response (201 Created):**

```json
{
  "accessToken": "string",
  "refreshToken": "string"
}
```

**Error Response (409 Conflict):**

```json
{
  "error": "USERNAME_TAKEN",
  "message": "The username 'username' is already in use.",
  "username": "string"
}
```

---

#### Login

**POST** `/auth/login`

Authenticate an existing user and receive JWT tokens.

**Request Body:**

```json
{
  "username": "string",
  "password": "string"
}
```

**Success Response (200 OK):**

```json
{
  "accessToken": "string",
  "refreshToken": "string"
}
```

**Error Response (401 Unauthorized):**

```json
{
  "error": "INVALID_CREDENTIALS",
  "message": "Invalid username or password."
}
```

---

#### Refresh Access Token

**POST** `/auth/refresh`

Obtain a new access token using a valid refresh token.

**Request Body:**

```json
{
  "refreshToken": "string"
}
```

**Success Response (200 OK):**

```json
{
  "accessToken": "string"
}
```

**Error Response (403 Forbidden):**

```
Invalid refresh token
```

---

### Authentication Flow

1. **Registration/Login**:

   - Call `/auth/register` to create a new account, or `/auth/login` to authenticate
   - Both endpoints return an `accessToken` and `refreshToken`

2. **Using Protected Endpoints**:

   - Include the `accessToken` in the `Authorization` header as: `Authorization: Bearer <accessToken>`
   - Access tokens are used to authenticate requests to protected endpoints:
     - `/team/**` - Requires authentication (any authenticated user)
     - `/game/**` - Requires authentication (any authenticated user)
     - `/player/**` - Requires `ADMIN` role

3. **Token Refresh**:

   - When the access token expires, use `/auth/refresh` with your `refreshToken` to obtain a new `accessToken`
   - Refresh tokens have a longer expiration time than access tokens

4. **Token Format**:
   - Tokens are JWT (JSON Web Tokens) and should be included in the `Authorization` header
   - Format: `Authorization: Bearer <token>`
