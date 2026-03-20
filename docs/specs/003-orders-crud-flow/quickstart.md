# Quickstart — Orders CRUD API Automation (Screenplay + Serenity Rest)

## Objective
Run one deterministic end-to-end scenario that validates:
1. Login
2. Create order
3. Read order
4. Update order
5. Delete order
6. Verify not found after delete

## Preconditions
- Backend API running and reachable (`FoodTech-Kitchen-Services`)
- Automation credentials available for login
- `AUTO_API_SCREENPLAY` project dependencies configured (Serenity + Cucumber + Screenplay + Serenity Rest)

## Suggested environment variables
- `FOODTECH_API_BASE_URL`
- `FOODTECH_AUTOMATION_USER`
- `FOODTECH_AUTOMATION_PASSWORD`

## Execution outline
1. Navigate to `AUTO_API_SCREENPLAY`
2. Run isolated scenario by tag (to be defined in tasks/implementation phase)
3. Confirm all six lifecycle assertions pass
4. Generate Serenity aggregate report

## Verification checklist
- Auth token acquired dynamically at runtime
- `orderId` captured from create response
- Same `orderId` reused across read/update/delete/final-read
- Status and response payload validated for every relevant call
- Final read returns not found after delete
- Scenario leaves no shared state dependencies

## Reporting
- Run Serenity report generation after tests
- Confirm report includes full lifecycle step evidence and assertions

## Failure triage hints
- `401` on order endpoints: verify token propagation
- `404` before delete: verify create response id capture and reuse
- Data mismatch after update: verify expected context values
- Flaky collisions: verify unique tableNumber generation
