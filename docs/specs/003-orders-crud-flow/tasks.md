# Tasks: Orders CRUD API Lifecycle Validation

**Input**: Design documents from `/specs/003-orders-crud-flow/`
**Prerequisites**: plan.md, spec.md, research.md, data-model.md, contracts/orders-crud-api.md, quickstart.md
**Target Project**: `AUTO_API_SCREENPLAY`
**Pattern**: Screenplay + Serenity Rest

## Format: `[ID] [P?] [Story?] Description`

---

## Phase 1: Setup (Shared Infrastructure)

**Purpose**: Bootstrap `AUTO_API_SCREENPLAY` with Serenity Starter baseline and minimal required tooling.

- [X] T001 Adopt Serenity Starter Repo baseline in AUTO_API_SCREENPLAY/README.md documenting source and importing only required files (build config, wrapper, serenity config, runner layout)
- [X] T002 [P] Create AUTO_API_SCREENPLAY/build.gradle using Serenity Starter minimal dependencies only (serenity-core, serenity-screenplay, serenity-rest-assured, serenity-cucumber, junit-platform)
- [X] T003 [P] Add Gradle wrapper files in AUTO_API_SCREENPLAY/gradlew, AUTO_API_SCREENPLAY/gradlew.bat, AUTO_API_SCREENPLAY/gradle/wrapper/gradle-wrapper.properties, AUTO_API_SCREENPLAY/gradle/wrapper/gradle-wrapper.jar
- [X] T004 [P] Add Serenity config in AUTO_API_SCREENPLAY/serenity.properties with API base URL and report settings
- [X] T005 Create Cucumber Serenity runner in AUTO_API_SCREENPLAY/src/test/java/com/foodtech/automation/api/screenplay/runners/OrdersCrudTestSuite.java
- [X] T006 Validate setup by running AUTO_API_SCREENPLAY/./gradlew clean test aggregate and confirm report generation in AUTO_API_SCREENPLAY/target/site/serenity/index.html

---

## Phase 2: Foundational (Blocking Prerequisites)

**Purpose**: Build reusable Screenplay API foundation required by all stories.

**⚠️ CRITICAL**: No user story implementation starts before this phase is complete.

- [X] T007 [P] Create environment config helper in AUTO_API_SCREENPLAY/src/test/java/com/foodtech/automation/api/screenplay/support/config/TestEnvironment.java
- [X] T008 [P] Create actor factory with CallAnApi ability in AUTO_API_SCREENPLAY/src/test/java/com/foodtech/automation/api/screenplay/support/actors/ApiActors.java
- [X] T009 [P] Create per-scenario execution context in AUTO_API_SCREENPLAY/src/test/java/com/foodtech/automation/api/screenplay/support/context/CrudExecutionContext.java
- [X] T010 [P] Create deterministic data factory for unique tableNumber in AUTO_API_SCREENPLAY/src/test/java/com/foodtech/automation/api/screenplay/support/data/OrderTestDataFactory.java
- [X] T011 [P] Create request/response models in AUTO_API_SCREENPLAY/src/test/java/com/foodtech/automation/api/screenplay/support/model/AuthSession.java, AUTO_API_SCREENPLAY/src/test/java/com/foodtech/automation/api/screenplay/support/model/OrderPayload.java, AUTO_API_SCREENPLAY/src/test/java/com/foodtech/automation/api/screenplay/support/model/OrderSnapshot.java
- [X] T012 [P] Create endpoint registry constants in AUTO_API_SCREENPLAY/src/test/java/com/foodtech/automation/api/screenplay/support/api/ApiRoutes.java
- [X] T013 Create authentication task in AUTO_API_SCREENPLAY/src/test/java/com/foodtech/automation/api/screenplay/tasks/auth/AuthenticateUser.java
- [X] T014 Create order create task capturing orderId in AUTO_API_SCREENPLAY/src/test/java/com/foodtech/automation/api/screenplay/tasks/orders/CreateOrder.java
- [X] T015 [P] Create order read task in AUTO_API_SCREENPLAY/src/test/java/com/foodtech/automation/api/screenplay/tasks/orders/GetOrderById.java
- [X] T016 [P] Create order update task in AUTO_API_SCREENPLAY/src/test/java/com/foodtech/automation/api/screenplay/tasks/orders/UpdateOrderById.java
- [X] T017 [P] Create order delete task in AUTO_API_SCREENPLAY/src/test/java/com/foodtech/automation/api/screenplay/tasks/orders/DeleteOrderById.java
- [X] T018 Create reusable response questions/assertions in AUTO_API_SCREENPLAY/src/test/java/com/foodtech/automation/api/screenplay/questions/LastResponseStatus.java and AUTO_API_SCREENPLAY/src/test/java/com/foodtech/automation/api/screenplay/questions/LastResponseBodyField.java

---

## Phase 3: User Story 1 - Complete Order Lifecycle Validation (Priority: P1) 🎯 MVP

**Goal**: Validate one full authenticated CRUD lifecycle over a single order and verify non-existence after delete.

**Independent Test**: Run only the CRUD scenario tag and confirm full flow passes end-to-end.

- [X] T019 [US1] Create feature file with single E2E CRUD scenario in AUTO_API_SCREENPLAY/features/orders/orders_crud.feature
- [X] T020 [US1] Create lifecycle hooks for stage/context setup and cleanup in AUTO_API_SCREENPLAY/src/test/java/com/foodtech/automation/api/screenplay/stepdefinitions/hooks/OrdersCrudHooks.java
- [X] T021 [US1] Implement Given/When/Then step definitions wiring CRUD flow in AUTO_API_SCREENPLAY/src/test/java/com/foodtech/automation/api/screenplay/stepdefinitions/orders/OrdersCrudStepDefinitions.java
- [X] T022 [US1] Add assertions for status and payload consistency (create/read/update/delete/post-delete-read) in AUTO_API_SCREENPLAY/src/test/java/com/foodtech/automation/api/screenplay/stepdefinitions/orders/OrdersCrudAssertions.java
- [X] T023 [US1] Execute isolated CRUD scenario with tag filter using AUTO_API_SCREENPLAY/./gradlew clean test -Dcucumber.filter.tags="@ordersCrud"

---

## Phase 4: User Story 2 - Deterministic and Independent Execution (Priority: P2)

**Goal**: Ensure repeated isolated executions are deterministic and collision-free.

**Independent Test**: Run deterministic tag scenario repeatedly and verify no cross-run state leakage.

- [X] T024 [US2] Add deterministic/repeatability scenario in AUTO_API_SCREENPLAY/features/orders/orders_crud.feature using unique order data per execution
- [X] T025 [US2] Implement step definitions for repeated independent executions in AUTO_API_SCREENPLAY/src/test/java/com/foodtech/automation/api/screenplay/stepdefinitions/orders/OrdersDeterminismStepDefinitions.java
- [X] T026 [US2] Add context-reset and orderId isolation checks in AUTO_API_SCREENPLAY/src/test/java/com/foodtech/automation/api/screenplay/stepdefinitions/hooks/OrdersCrudHooks.java
- [X] T027 [US2] Execute deterministic scenario multiple times with AUTO_API_SCREENPLAY/./gradlew test -Dcucumber.filter.tags="@ordersDeterministic"

---

## Phase 5: Polish & Cross-Cutting Concerns

**Purpose**: Final hardening, documentation alignment, and reporting verification.

- [X] T028 Update execution guide in AUTO_API_SCREENPLAY/README.md with required env vars, tags, and commands from specs/003-orders-crud-flow/quickstart.md
- [X] T029 [P] Verify Serenity report includes both CRUD and determinism scenarios in AUTO_API_SCREENPLAY/target/site/serenity/index.html
- [X] T030 [P] Review AUTO_API_SCREENPLAY/build.gradle to keep only necessary Serenity Starter components for this implementation (remove non-required starter extras)
- [X] T031 Run final validation command AUTO_API_SCREENPLAY/./gradlew clean test aggregate

---

## Dependencies & Execution Order

### Phase Dependencies

- Phase 1 → required first
- Phase 2 → depends on Phase 1 and blocks all user stories
- Phase 3 (US1) → depends on Phase 2
- Phase 4 (US2) → depends on Phase 3 baseline flow
- Phase 5 → depends on US1 + US2 completion

### User Story Dependencies

- US1 (P1): No dependency on US2
- US2 (P2): Depends on US1 reusable flow and context wiring

### Within Each User Story

- Feature file before step definitions
- Hooks/context setup before assertions
- Scenario execution after implementation tasks

### Parallel Opportunities

- Setup: T002, T003, T004 in parallel after T001
- Foundation: T007–T012 in parallel, then T013–T018
- US1: T020 and T021 can progress in parallel after T019
- US2: T025 and T026 can progress in parallel after T024
- Polish: T029 and T030 in parallel after T028

---

## Parallel Example: User Story 1

- Run `T020` and `T021` in parallel because they touch different files:
	- `AUTO_API_SCREENPLAY/src/test/java/com/foodtech/automation/api/screenplay/stepdefinitions/hooks/OrdersCrudHooks.java`
	- `AUTO_API_SCREENPLAY/src/test/java/com/foodtech/automation/api/screenplay/stepdefinitions/orders/OrdersCrudStepDefinitions.java`

---

## Implementation Strategy

### MVP First (US1)

1. Complete Phase 1 + Phase 2
2. Deliver Phase 3 (single end-to-end CRUD scenario)
3. Validate with isolated execution tag

### Incremental Delivery

1. Add US1 complete lifecycle
2. Add US2 determinism hardening
3. Finalize reporting and documentation
