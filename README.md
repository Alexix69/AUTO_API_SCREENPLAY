# AUTO_API_SCREENPLAY

REST API automation for the **FoodTech Kitchen Services** backend using the **Screenplay** pattern with **Serenity BDD + REST Assured**.

---

## Workshop Context

This project is the third of three automation deliverables for **Semana 5 — Maestría en automatización: del objeto al actor**. It covers the API CRUD validation requirement using the Screenplay pattern with Serenity REST, exercising all four HTTP verbs (POST, GET, PUT, DELETE) in a single lifecycle scenario against the FoodTech Kitchen Services API.

---

## API Under Test

**FoodTech Kitchen Services** — Spring Boot 3.2.1 REST API running at `http://localhost:8080` (configurable).

| Endpoint | Method | Purpose |
|---|---|---|
| `/api/auth/login` | POST | Authenticate and obtain JWT token |
| `/api/orders` | POST | Create a new order |
| `/api/orders/{id}` | GET | Retrieve an order by ID |
| `/api/orders/{id}` | PUT | Update an order's table number |
| `/api/orders/{id}` | DELETE | Delete an order |

---

## Scenarios Covered

| # | Tag | Feature | Description |
|---|---|---|---|
| 1 | `@ordersCrud` | Orders CRUD lifecycle | Full lifecycle: POST → GET → PUT → DELETE → GET (404 verification) |
| 2 | `@baseline` | Serenity baseline | Sanity check confirming the automation configuration is functional |

**Lifecycle scenario steps:**

```gherkin
Given the api actor is authenticated
When the actor creates a new order                    ← POST /api/orders
Then the order is created with a reusable order id
When the actor retrieves the same order               ← GET /api/orders/{id}
Then the retrieved order matches the created data
When the actor updates the order table number         ← PUT /api/orders/{id}
Then the order reflects the updated table number
When the actor deletes the order                      ← DELETE /api/orders/{id}
Then the delete operation is acknowledged
And the actor requests the deleted order again        ← GET /api/orders/{id}
Then the service reports order not found              ← HTTP 404
```

---

## Tech Stack

| Tool | Version |
|---|---|
| Java | 21 |
| Serenity BDD | 5.3.2 |
| Serenity Screenplay REST | 5.3.2 |
| REST Assured (via Serenity) | managed by Serenity |
| Cucumber | 7.34.2 (JUnit Platform Engine) |
| Gradle | Wrapper (included) |
| JUnit Platform | 1.13.0 |

---

## Architecture

```
src/test/java/com/foodtech/automation/api/screenplay/
├── tasks/
│   ├── auth/
│   │   └── AuthenticateUser.java       ← Task: POST /api/auth/login, stores JWT (SRP: auth only)
│   └── orders/
│       ├── CreateOrder.java            ← Task: POST /api/orders, stores orderId in context (SRP: create only)
│       ├── GetOrderById.java           ← Task: GET /api/orders/{id}, uses stored orderId (SRP: read only)
│       ├── UpdateOrderById.java        ← Task: PUT /api/orders/{id}, new tableNumber (SRP: update only)
│       └── DeleteOrderById.java        ← Task: DELETE /api/orders/{id} (SRP: delete only)
├── questions/
│   ├── LastResponseStatus.java         ← Question: returns HTTP status of the last response
│   └── LastResponseBodyField.java      ← Question: extracts a named JSON field from the last response body
├── stepdefinitions/
│   ├── orders/
│   │   ├── OrdersCrudStepDefinitions.java   ← Glue: Gherkin → Task delegation
│   │   ├── OrdersCrudAssertions.java        ← Assertions: HTTP status + body field verification
│   │   └── OrdersDeterminismStepDefinitions.java
│   ├── hooks/
│   │   └── OrdersCrudHooks.java        ← @Before: fresh actor; @After: cleanup guard
│   └── BaselineStepDefinitions.java    ← Baseline scenario glue
├── runners/
│   ├── OrdersCrudTestSuite.java        ← @Suite (JUnit Platform), tag filter @ordersCrud
│   └── BaselineApiTestSuite.java       ← @Suite (JUnit Platform), tag filter @baseline
└── support/
    ├── actors/ApiActors.java           ← Actor factory with CallAnApi ability
    ├── api/ApiRoutes.java              ← Endpoint URL constants
    ├── config/TestEnvironment.java     ← Base URL resolution (env var → default)
    ├── context/CrudExecutionContext.java ← ThreadLocal: orderId + tableNumber + auth token
    ├── data/OrderTestDataFactory.java  ← Unique tableNumber generation per run
    └── model/                          ← AuthSession, OrderPayload, OrderProduct, OrderSnapshot

src/test/resources/features/
├── orders/orders_crud.feature    ← CRUD lifecycle scenario (@ordersCrud)
└── baseline/serenity_baseline.feature  ← Baseline sanity scenario (@baseline)
serenity.properties                     ← API base URL and report configuration
```

**Screenplay SRP enforcement**:
- Each `Task` calls exactly one API endpoint — no Task combines multiple HTTP calls.
- `Questions` are stateless — they read from `SerenityRest.lastResponse()` without modifying any context.
- `CrudExecutionContext` (ThreadLocal) carries the `orderId` captured at create-time through the entire lifecycle without coupling Tasks to each other directly.
- Step Definitions are pure glue — one delegation per Gherkin step, no assertions, no HTTP calls.

---

## Prerequisites

- Java 21 (`java -version`)
- FoodTech Kitchen Services API running and healthy

**Start the backend with Docker Compose** (from the `FoodTech-Kitchen-Services` project):
```bash
docker compose up -d --build
```
Verify: `curl -s http://localhost:8080/actuator/health | grep UP`

Default test credentials: `username=admin` / `password=admin123` (configured in backend seed data).

---

## Configuration

API base URL resolution order:

1. Environment variable: `FOODTECH_API_BASE_URL`
2. Default: `http://localhost:8080`

Configured in `serenity.properties`:
```properties
serenity.project.name=AUTO API SCREENPLAY Baseline
serenity.outputDirectory=target/site/serenity
api.base.url=http://localhost:8080
serenity.take.screenshots=FOR_FAILURES
```

---

## How to Execute

**Run all scenarios:**
```bash
./gradlew clean test aggregate
```

**Run only the CRUD lifecycle scenario:**
```bash
./gradlew clean test aggregate -Dcucumber.filter.tags="@ordersCrud"
```

**Run only the baseline scenario:**
```bash
./gradlew clean test aggregate -Dcucumber.filter.tags="@baseline"
```

**Override the API base URL:**
```bash
FOODTECH_API_BASE_URL=http://my-host:8080 ./gradlew clean test aggregate
```

---

## Reports

After execution:

| Report | Location |
|---|---|
| Serenity HTML report | `target/site/serenity/index.html` |
| Single-page report | `target/site/serenity/serenity-summary.html` |
| Capabilities view | `target/site/serenity/capabilities-view.html` |
| Features view | `target/site/serenity/features.html` |

Open `target/site/serenity/index.html`. The report navigation includes **Overall Test Results**, **Requirements**, **Capabilities**, and **Features** tabs. The Capabilities and Features tabs are generated by the `decorateSerenityNavigation` Gradle task that runs automatically after `aggregate`.

---

## Spec-Driven Development with SpecKit

This project was built using **Spec-Driven Development (SDD)** with the SpecKit workflow:

```
constitution → specify → plan → tasks → implement
```

All specification artifacts produced during development are preserved in [`docs/specs/003-orders-crud-flow/`](docs/specs/003-orders-crud-flow/):

| Artifact | Purpose |
|---|---|
| [`spec.md`](docs/specs/003-orders-crud-flow/spec.md) | Feature specification: user stories, acceptance criteria, edge cases |
| [`plan.md`](docs/specs/003-orders-crud-flow/plan.md) | Implementation plan: Screenplay REST architecture, constitution compliance gate |
| [`tasks.md`](docs/specs/003-orders-crud-flow/tasks.md) | Atomic task breakdown with 31 tasks, all completed |
| [`research.md`](docs/specs/003-orders-crud-flow/research.md) | API contract investigation: endpoint discovery, response shapes |
| [`data-model.md`](docs/specs/003-orders-crud-flow/data-model.md) | Request/response payload contracts |
| [`quickstart.md`](docs/specs/003-orders-crud-flow/quickstart.md) | Backend setup and smoke test guide |
| [`contracts/`](docs/specs/003-orders-crud-flow/contracts/) | Orders CRUD API contracts used to design Tasks |
| [`checklists/`](docs/specs/003-orders-crud-flow/checklists/) | Pre-implementation readiness gates |

The spec was produced by AI-assisted analysis of the FoodTech Kitchen Services API — contracts defined before any Task was written.

---

## Notes for Evaluators

- The lifecycle scenario covers all four required HTTP verbs in order: POST (create) → GET (read) → PUT (update) → DELETE (remove) → GET (verify 404).
- `CrudExecutionContext` (ThreadLocal) carries the `orderId` from the create step through all subsequent steps — no hardcoded IDs, no test data coupling.
- `OrderTestDataFactory` generates a unique table number on each run using `Instant.now().toEpochMilli()`, ensuring deterministic and collision-free test data.
- `@Before` hook creates a fresh actor with `CallAnApi` ability for each scenario; `@After` attempts cleanup if the scenario fails mid-run.
- The JUnit Platform `@Suite` runner with `SerenityReporterParallel` plugin enables the extended Serenity requirements hierarchy in the report.
