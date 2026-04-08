@capability:API
@feature:TaskCompleteOrderLogic
Feature: Task Complete Endpoint — Order Status Logic and State Validation
  As a system owner
  I want the complete-task endpoint to update order status correctly and reject invalid state transitions
  So that order completion is accurate and the task lifecycle is enforced at the API level

  @apiOrderNotCompletedWhenTasksRemain
  Scenario: Completing a task when other tasks remain pending — order stays unchanged
    Given an order contains an IN_PREPARATION task and at least one other non-completed task
    When the complete-task endpoint is called for the IN_PREPARATION task
    Then the task status is COMPLETED
    And the order status is not COMPLETED

  @apiOrderCompletedWhenLastTaskDone
  Scenario: Completing the last remaining task — order transitions to COMPLETED
    Given an order contains exactly one IN_PREPARATION task and all other tasks are already COMPLETED
    When the complete-task endpoint is called for the IN_PREPARATION task
    Then the task status is COMPLETED
    And the order status is COMPLETED

  @apiCompleteTaskInPendingReturns409
  Scenario: Calling complete on a PENDING task returns HTTP 409
    Given a task is in PENDING state and the complete call is made on it
    When the complete-task endpoint is called for the PENDING task
    Then the API responds with HTTP 409

  @concurrentStartRaceCondition
  Scenario: Two simultaneous start calls on the same PENDING task — exactly one succeeds
    Given two COCINERO operators are authenticated and a PENDING task exists
    When both operators simultaneously call the start-preparation endpoint for that task
    Then exactly one response is HTTP 200 and the other is HTTP 409
