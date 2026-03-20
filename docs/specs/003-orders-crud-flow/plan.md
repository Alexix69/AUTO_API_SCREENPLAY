# Implementation Plan: Orders CRUD API Lifecycle Validation

**Branch**: `003-orders-crud-flow` | **Date**: 2026-03-20 | **Spec**: [spec.md](./spec.md)
**Input**: Feature specification from `/specs/003-orders-crud-flow/spec.md`

## Summary

Design a single Screenplay + Serenity Rest scenario in `AUTO_API_SCREENPLAY` that validates an authenticated end-to-end lifecycle for `orders`: authenticate, create, retrieve, update, delete, and verify non-existence. The design enforces deterministic data generation, dynamic token handling, strict ID propagation, and assertions over both status and response content to satisfy workshop constraints and Constitution §8.

## Technical Context

**Language/Version**: Java 21 (OpenJDK)  
**Build Tool**: Gradle Wrapper (`./gradlew`)  
**Project Type**: API automation test project (Serenity BDD)  
**Primary Dependencies**: Serenity BDD, Serenity Screenplay, Serenity Rest, Cucumber, JUnit Platform  
**Storage**: N/A (test suite does not own storage; uses external API state)  
**Framework**: Serenity BDD + Cucumber  
**Pattern**: Screenplay (Actor, Task/Interaction, Question separation)  
**API Client**: Serenity Rest (REST Assured integration)  
**Reporting**: Serenity HTML reports (`aggregate`)  
**Target Project**: `AUTO_API_SCREENPLAY`  
**External System Under Test**: `FoodTech-Kitchen-Services` REST API (`/api/auth`, `/api/orders`)  
**Constraints**: Single scenario, independent execution, no shared mutable state, dynamic auth token, deterministic assertions, orderId reuse across lifecycle

## Constitution Check (Pre-Design Gate)

*GATE: Must pass before Phase 0 research. Re-check after Phase 1 design.*

| # | Gate | §Ref | Status |
|---|---|---|---|
| G1 | Scenarios validate **system behavior**, not UI mechanics | §2 | ✅ |
| G2 | All scenarios are **independently executable** — no shared state | §3 | ✅ |
| G3 | Gherkin uses Given/When/Then with **no implementation details** | §4 | ✅ |
| G4 | Serenity BDD components present: feature files, step defs, runner, config | §5 | ✅ |
| G5 | **POM only** (AUTO_FRONT_POM_FACTORY): Page Objects hold locators + behavior, no assertions | §6 | N/A |
| G6 | **Screenplay only** (AUTO_FRONT/API_SCREENPLAY): Actor/Task/Action/Question split enforced, SRP | §7 | ✅ |
| G7 | **API project**: validates status code + response body + business rules | §8 | ✅ |
| G8 | Semantic class naming, no abbreviations | §9 | ✅ |
| G9 | No commented code, unused variables, or unclear methods | §10 | ✅ |
| G10 | Serenity report configured and generated on every test run | §11 | ✅ |
| G11 | No direct implementation — spec → plan → tasks → implement flow followed | §12 | ✅ |

## Project Structure

### Documentation (this feature)

```text
specs/003-orders-crud-flow/
├── plan.md
├── research.md
├── data-model.md
├── quickstart.md
├── contracts/
│   └── orders-crud-api.md
└── tasks.md   # generated later by /speckit.tasks
```

### Source Code (repository root)

```text
AUTO_API_SCREENPLAY/
├── features/
├── src/
│   └── test/
│       ├── java/
│       └── resources/
└── README.md
```

**Structure Decision**: Single API automation project under `AUTO_API_SCREENPLAY`, targeting the existing backend API exposed by `FoodTech-Kitchen-Services`.

## Phase 0 — Research & Decisions

Research output: [research.md](./research.md)

Resolved items:
1. Deterministic test-data strategy for order payload uniqueness
2. Dynamic authentication token acquisition and propagation strategy
3. API workflow contract for create→read→update→delete→verify-not-found
4. Assertion strategy for status + payload validation per lifecycle step
5. Failure handling model for stable CI diagnostics

## Phase 1 — Design & Contracts

Design outputs:
- [data-model.md](./data-model.md)
- [contracts/orders-crud-api.md](./contracts/orders-crud-api.md)
- [quickstart.md](./quickstart.md)

Design decisions:
1. Single scenario with shared per-scenario execution context (`token`, `orderId`, expected fields)
2. Minimal entity set: `AuthSession`, `OrderPayload`, `OrderSnapshot`, `CrudExecutionContext`
3. Endpoint contract explicitly defines required assertions and ID propagation expectations
4. Not-found verification after delete is mandatory scenario terminator

## Phase 1.5 — Agent Context Update

Command to execute:

```bash
.specify/scripts/bash/update-agent-context.sh copilot
```

Expected outcome:
- Agent instructions context updated with latest planning stack and conventions from this plan.

## Constitution Check (Post-Design Re-evaluation)

| # | Gate | §Ref | Status |
|---|---|---|---|
| G1 | Behavior-focused outcomes preserved in scenario design | §2 | ✅ |
| G2 | Data/context isolation defined at scenario level | §3 | ✅ |
| G3 | Acceptance language remains declarative and business-readable | §4 | ✅ |
| G4 | Serenity artifacts explicitly planned in quickstart/contracts | §5 | ✅ |
| G5 | POM constraints not applicable | §6 | N/A |
| G6 | Screenplay SRP boundaries captured in design model | §7 | ✅ |
| G7 | Contract requires status, body and business-rule validation | §8 | ✅ |
| G8 | Naming standards reflected in planned artifact names | §9 | ✅ |
| G9 | Clean-code constraints included in implementation guidance | §10 | ✅ |
| G10 | Report generation included in quickstart verification steps | §11 | ✅ |
| G11 | Workflow remains spec → plan → tasks → implement | §12 | ✅ |

## Phase 2 Preview (for /speckit.tasks)

Planned implementation slices:
1. Serenity + Screenplay + REST project bootstrap validation in `AUTO_API_SCREENPLAY`
2. Foundation layer: abilities, context holder, request builders, response questions
3. Single Gherkin CRUD scenario wiring and step definitions
4. Assertions for each lifecycle stage and final not-found verification
5. Reporting and CI execution command (`clean test aggregate`)

## Complexity Tracking

No constitution violations require exception handling at planning time.
