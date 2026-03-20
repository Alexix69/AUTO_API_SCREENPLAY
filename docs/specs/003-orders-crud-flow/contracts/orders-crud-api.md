# API Contract — Orders CRUD Lifecycle (Automation Scope)

## Purpose
Define the external API contract consumed by the Screenplay automation scenario.

## Authentication

### Login
- Endpoint: `/api/auth/login`
- Request body:
```json
{
  "identifier": "<automation-user>",
  "password": "<automation-password>"
}
```
- Success:
  - Status: `200`
  - Body contains token field:
```json
{
  "token": "<jwt-token>"
}
```
- Contract rules:
  - token MUST be captured and reused in `Authorization: Bearer <token>`.

## Orders lifecycle

### Create order
- Endpoint: `/api/orders`
- Headers: `Authorization: Bearer <token>`
- Request body (example):
```json
{
  "tableNumber": "AUTO-TBL-<unique>",
  "products": [
    { "name": "Soup", "type": "HOT_KITCHEN" },
    { "name": "Juice", "type": "BAR" }
  ]
}
```
- Success:
  - Status: `201`
  - Body includes:
    - `orderId` (Long)
    - `tableNumber`
    - `tasksCreated`
    - `message`

### Read order by id
- Endpoint: `/api/orders/{orderId}`
- Headers: `Authorization: Bearer <token>`
- Success:
  - Status: `200`
  - Body includes:
    - `orderId` (same as created)
    - `tableNumber` (same as created)
    - `status`
    - `products`

### Update order
- Endpoint: `/api/orders/{orderId}`
- Headers: `Authorization: Bearer <token>`
- Request body:
```json
{
  "tableNumber": "AUTO-TBL-<updated>"
}
```
- Success:
  - Status: `200`
  - Body includes:
    - `orderId` (same id)
    - `tableNumber` (updated value)
    - `status`
    - `products`

### Delete order
- Endpoint: `/api/orders/{orderId}`
- Headers: `Authorization: Bearer <token>`
- Success:
  - Status: `204`
  - No response body required

### Verify non-existence after delete
- Endpoint: `/api/orders/{orderId}`
- Headers: `Authorization: Bearer <token>`
- Expected:
  - Status: `404`
  - Error body should indicate not found semantics

## Assertions matrix
- Login: status + token present
- Create: status + orderId present + tableNumber echoed
- Read: status + id and data consistency
- Update: status + updated field reflected
- Delete: status 204
- Post-delete read: status 404 + error semantics

## Out of scope
- Performance testing
- Authorization role matrix testing
- Bulk orders operations
- Non-order resources
