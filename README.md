# HTTP Basic Auth Practice API

> A Spring Boot + Spring Security REST API focused on HTTP Basic authentication and role-based access control. This project is an iterative approach to solidifying security best practices in software architecture and implementation - each iteration starts from scratch and refines the previous.

---

## Objective

Build a REST API using **Spring Boot** and **Spring Security** with HTTP Basic authentication and authorization, emphasizing shift-left security through early and automated test coverage.

---

## Scope

**In Scope**
- HTTP Basic authentication
- Role-based access control (RBAC)
- Secure endpoint protection
- Proper HTTP status codes and response headers
- Security behavior testing (shift-left approach)

**Out of Scope**
- User registration
- JWT / token-based auth
- Refresh tokens
- Account lockout
- Rate limiting
- Audit logging
- Access revocation
- STRIDE analysis

---

## API Contract

| Method | Endpoint | Description | Auth Required | Role Required |
|--------|----------|-------------|---------------|---------------|
| `GET` | `/api/me` | Returns the authenticated user's non-sensitive profile details and granted roles | ✔️ | Any |
| `GET` | `/api/admin/users` | Returns a list of all users | ✔️ | `ADMIN` |

---

## Functional Requirements

| ID | Requirement |
|----|-------------|
| FR-01 | The client authenticates by sending a valid HTTP Basic `Authorization` header |
| FR-02 | `GET /api/me` returns only the currently authenticated user's details |
| FR-03 | `GET /api/admin/users` is accessible only to users with the `ADMIN` role |
| FR-04 | All protected endpoints require authentication by default unless explicitly configured otherwise |
| FR-05 | API responses use appropriate HTTP status codes, headers, and JSON response bodies |

---

## Acceptance Criteria

| ID | Criteria |
|----|----------|
| AC-01 | Requests to protected endpoints without valid credentials return `401 Unauthorized` |
| AC-02 | `401` responses include a `WWW-Authenticate` header for Basic authentication |
| AC-03 | Authenticated users without the `ADMIN` role receive `403 Forbidden` on `/api/admin/**` |
| AC-04 | Authenticated users with the `ADMIN` role receive `200 OK` on `/api/admin/**` |
| AC-05 | `GET /api/me` returns only the authenticated user's non-sensitive details and roles |
| AC-06 | Passwords are **never** returned in API responses |
| AC-07 | Automated tests cover controller, service, security configuration, and all auth scenarios (unauthorized / forbidden / success) |
