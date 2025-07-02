Feature: store and get books
  Scenario: the user creates two entries and retrieves both
    Given the user creates the book "Pasta Cooking" from "James"
    And the user creates the book "Hobbit" from "Tolkien"
    When the user retrieves all books
    Then the list of books should contain
      | title           | author   |
      | Pasta Cooking   | James    |
      | Hobbit          | Tolkien  |