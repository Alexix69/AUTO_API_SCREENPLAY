# Phase 1 Data Model — Orders CRUD API Lifecycle Validation

## 1) AuthSession
Represents runtime authentication state for the actor.

Fields:
- token: String (non-empty bearer token)
- issuedAt: Instant (optional, if response exposes it)

Validation rules:
- token MUST be present before any protected order operation.

State transitions:
- NOT_AUTHENTICATED -> AUTHENTICATED (after successful login)
- AUTHENTICATED -> INVALID (on 401 during protected call)

## 2) OrderPayload
Represents request payload data used for create/update.

Fields:
- tableNumber: String (required, non-blank)
- products: List<ProductItem> (required for create)

Validation rules:
- tableNumber MUST be non-empty.
- products MUST be non-empty for creation.

State transitions:
- DRAFT -> CREATED_PAYLOAD (create request built)
- CREATED_PAYLOAD -> UPDATED_PAYLOAD (update request built)

## 3) ProductItem
Represents one item in order request/response.

Fields:
- name: String
- type: String

Validation rules:
- name non-empty
- type within allowed API enum values

## 4) OrderSnapshot
Represents observed order response at each lifecycle checkpoint.

Fields:
- orderId: Long
- tableNumber: String
- status: String
- products: List<ProductItem>

Validation rules:
- orderId MUST be non-null after creation.
- read/update responses MUST reference same orderId captured at creation.

State transitions:
- NON_EXISTENT -> CREATED -> RETRIEVED -> UPDATED -> DELETED -> NON_EXISTENT

## 5) CrudExecutionContext
Per-scenario context object to chain operations deterministically.

Fields:
- authSession: AuthSession
- createdOrderId: Long
- originalTableNumber: String
- updatedTableNumber: String
- lastStatusCode: int
- lastErrorBody: String (optional)

Validation rules:
- createdOrderId assigned exactly once after create.
- updatedTableNumber differs from originalTableNumber.
- context is cleared after scenario completion.

Relationships:
- AuthSession 1..1 CrudExecutionContext
- CrudExecutionContext 1..1 OrderSnapshot (current)
- OrderSnapshot 1..* ProductItem
