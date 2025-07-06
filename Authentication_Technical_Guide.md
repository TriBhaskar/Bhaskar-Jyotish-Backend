# Bhaskar Jyotish Authentication Technical Guide

## Overview

This technical guide documents the authentication and user management endpoints available in the Bhaskar Jyotish application. It provides details on each endpoint's purpose, request/response format, and the flow of various authentication use cases.

## Base URL

All endpoints are prefixed with the API prefix defined in `ApiPaths.API_PREFIX`.

## Authentication Endpoints

### User Registration

**Endpoint:** `POST /api/register`

**Purpose:** Register a new user in the system

**Request Body:**
```json
{
  "email": "user@example.com",
  "password": "SecurePassword123",
  "firstName": "John",
  "lastName": "Doe",
  "mobileNumber": "+1234567890"
}
```

**Response:**
```json
{
  "userId": 123,
  "email": "user@example.com",
  "message": "User registered successfully. Please verify your email.",
  "status": "SUCCESS"
}
```

**Flow:**
1. User submits registration information
2. System validates the input data
3. System creates a new user record in the database
4. System generates an OTP and sends it to the user's email
5. User receives a success response with instructions to verify email

### Email Verification

**Endpoint:** `POST /api/verify-email`

**Purpose:** Verify user's email using the OTP sent during registration

**Request Body:**
```json
{
  "email": "user@example.com",
  "otp": "123456"
}
```

**Response:**
```json
{
  "message": "Email verified successfully",
  "status": "SUCCESS"
}
```

**Flow:**
1. User submits email and OTP
2. System validates the OTP against the stored value
3. System marks the user's email as verified
4. User receives a success response

### User Login

**Endpoint:** `POST /api/login`

**Purpose:** Authenticate a user and provide access tokens

**Request Body:**
```json
{
  "email": "user@example.com",
  "password": "SecurePassword123"
}
```

**Response:** 
*(Note: Currently returns a placeholder message as the implementation is pending)*

**Expected Implementation Flow:**
1. User submits credentials
2. System validates credentials
3. System generates an access token and refresh token
4. System stores the refresh token
5. User receives tokens with expiration information

### Token Refresh

**Endpoint:** `POST /api/refresh`

**Purpose:** Obtain a new access token using a valid refresh token

**Request Body:**
```json
{
  "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

**Response:**
```json
{
  "user": {
    "id": 123,
    "email": "user@example.com",
    "firstName": "John",
    "lastName": "Doe"
  },
  "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "expiresAt": "2025-07-06T14:30:00Z",
  "status": "SUCCESS"
}
```

**Flow:**
1. Client submits a refresh token
2. System validates the refresh token
3. System generates a new access token
4. Optionally, system may generate a new refresh token
5. Client receives new tokens

### Token Revocation (Logout)

**Endpoint:** `POST /api/revoke`

**Purpose:** Revoke a refresh token (logout)

**Request Body:**
```json
{
  "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

**Response:** HTTP 200 OK on success, HTTP 400 Bad Request if token is invalid

**Flow:**
1. Client submits a refresh token
2. System invalidates the refresh token
3. Client receives a success response

### Revoke All Tokens (Logout from All Devices)

**Endpoint:** `POST /api/revoke-all`

**Purpose:** Revoke all refresh tokens for a user (logout from all devices)

**Request Parameters:**
- `userId`: The ID of the user whose tokens should be revoked

**Response:** HTTP 200 OK

**Flow:**

***Initial Login Flow:***
1. User submits credentials
2. Server validates credentials
3. Server generates:
    - Access token (JWT, 2 hours)
    - Refresh token (secure random string, 30 days)
4. Server stores refresh token in database with session info
5. Client receives both tokens
6. Client stores refresh token securely (not in localStorage!)

***API Request Flow:***

1. Client sends request with: Authorization: Bearer <access_token>
2. Server validates access token
3. If valid → Process request
4. If expired → Return 401 Unauthorized
5. Client uses refresh token to get new access token

***Token Refresh Flow:***

1. Client detects access token is expired (401 response)
2. Client sends refresh token to /api/auth/refresh
3. Server validates refresh token:
    - Exists in database?
    - Is active?
    - Not expired?
4. Server generates new access token
5. Server returns new access token
6. Client retries original request with new token


### Other Authentication Endpoints (Not Yet Implemented)

The following endpoints are planned but not yet fully implemented:

- **Resend OTP:** `POST /api/resend-otp`
- **Forgot Password:** `POST /api/forgot-password`
- **Validate Token:** `POST /api/validate-token`
- **Reset Password:** `POST /api/reset-password`

## Security Considerations

1. **CORS Configuration:** The API allows cross-origin requests from any origin (`*`). For production, consider restricting this to specific trusted domains.
2. **Token Storage:** Refresh tokens should be stored securely, and access tokens should never be stored server-side.
3. **Token Expiration:** Access tokens should have a short lifespan (e.g., 15-30 minutes), while refresh tokens can have a longer lifespan.

## Error Handling

All endpoints use standard HTTP status codes:
- 200 OK: Request successful
- 400 Bad Request: Invalid input or request
- 401 Unauthorized: Authentication failure
- 403 Forbidden: Insufficient permissions
- 500 Internal Server Error: Server-side error

## Implementation Notes

- The application uses Spring Security for authentication
- JWT (JSON Web Tokens) are used for access and refresh tokens
- Redis is used for token storage and management
