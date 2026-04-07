@capability:API
@feature:Authorization
Feature: Role-Based API Endpoint Protection

  @apiAuthCocineroOrders
  Scenario: COCINERO is denied access to the orders creation endpoint
    Given a role-restricted actor is authenticated and ready
    When the actor calls POST "/api/orders"
    Then the API responds with HTTP 403

  @apiAuthMeseroBarTasks
  Scenario: MESERO is denied access to the bar station tasks endpoint
    Given a role-restricted actor is authenticated and ready
    When the actor calls GET "/api/tasks/station/BAR"
    Then the API responds with HTTP 403

  @apiAuthMeseroTasks
  Scenario: MESERO is denied access to the tasks station endpoint
    Given a role-restricted actor is authenticated and ready
    When the actor calls GET "/api/tasks/station/BAR"
    Then the API responds with HTTP 403

  @apiCrossRoleStartForbidden
  Scenario: COCINERO calling a BAR task start endpoint receives HTTP 403
    Given a COCINERO actor is authenticated with a valid token
    When the actor calls PATCH on a BARTENDER task start endpoint
    Then the API responds with HTTP 403

  @apiStartAlreadyInPreparationConflict
  Scenario: Calling start on a task already in preparation returns HTTP 409
    Given a COCINERO actor is authenticated with a valid token
    When the actor calls PATCH on a task already in preparation
    Then the API responds with HTTP 409
