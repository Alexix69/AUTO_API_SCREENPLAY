# Phase 0 Research — Orders CRUD API Lifecycle Validation

## Decision 1: Authentication handling is runtime and per-scenario
- Decision: Obtain a fresh auth token at scenario start via login and store it in scenario execution context.
- Rationale: Prevents brittle dependence on static tokens and ensures independent execution.
- Alternatives considered:
  - Static token in config: rejected due to expiration and coupling risk.
  - Shared token cache across scenarios: rejected due to non-independence and parallel execution risks.

## Decision 2: Order identity propagation must come only from create response
- Decision: Capture `orderId` from create-order response and reuse it for read, update, delete, and final not-found check.
- Rationale: Guarantees strict data lineage and verifiable lifecycle consistency.
- Alternatives considered:
  - Query list endpoint to discover latest order: rejected due to race/collision risk.
  - Infer id from unrelated payload fields: rejected as non-deterministic.

## Decision 3: Deterministic data strategy uses unique tableNumber per run
- Decision: Generate a unique, run-scoped table identifier and validate returned values against expected context values.
- Rationale: Minimizes collisions while preserving deterministic assertions on known expected values.
- Alternatives considered:
  - Fixed tableNumber across runs: rejected due to collision and non-isolation risk.
  - Fully random assertions without expected-value checks: rejected because it weakens verification quality.

## Decision 4: Assertions are dual-layer (transport + business data)
- Decision: Validate both status code and response body content at every lifecycle step that returns content.
- Rationale: Aligns with Constitution §8 and workshop requirement for robust API verification.
- Alternatives considered:
  - Status-only assertions: rejected as insufficient for business-rule validation.
  - Body-only assertions: rejected because protocol-level behavior would remain unverified.

## Decision 5: Final delete verification uses direct read-by-id not-found
- Decision: After successful deletion, invoke read by same `orderId` and assert not-found response.
- Rationale: Provides the strongest observable proof that resource lifecycle completed.
- Alternatives considered:
  - Trust delete status only: rejected as incomplete end-state validation.
  - Verify absence through list filtering: rejected due to potential pagination/filter ambiguity.

## Decision 6: Scope remains one end-to-end scenario
- Decision: Keep a single CRUD scenario for workshop fit; avoid multiple feature slices at this stage.
- Rationale: Matches explicit workshop requirement and reduces orchestration complexity.
- Alternatives considered:
  - Separate scenarios per operation: rejected because it breaks the required end-to-end flow.
  - Multi-resource scenario: rejected as out-of-scope.

## Resolved Clarifications
- No unresolved `NEEDS CLARIFICATION` items remain in technical context.
- API under test supports required order CRUD flow including post-delete not-found check as per current backend contract assumptions.
