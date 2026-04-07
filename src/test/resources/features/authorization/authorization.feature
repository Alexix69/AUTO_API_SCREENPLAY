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
