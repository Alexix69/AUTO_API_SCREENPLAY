Feature: Orders CRUD lifecycle

  @ordersCrud
  Scenario: Validate complete order lifecycle through API
    Given the api actor is authenticated
    When the actor creates a new order
    Then the order is created with a reusable order id
    When the actor retrieves the same order
    Then the retrieved order matches the created data
    When the actor updates the order table number
    Then the order reflects the updated table number
    When the actor deletes the order
    Then the delete operation is acknowledged
    And the actor requests the deleted order again
    Then the service reports order not found
