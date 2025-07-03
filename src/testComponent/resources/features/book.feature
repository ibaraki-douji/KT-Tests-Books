Feature: Books
  Scenario: the user creates two books and retrieves both
    Given the user creates the book "Pasta Cooking" from "James"
    And the user creates the book "Hobbit" from "Tolkien"
    When the user retrieves all books
    Then the list of books should contain
      | title           | author   |
      | Pasta Cooking   | James    |
      | Hobbit          | Tolkien  |

  Scenario: the user creates a book and reserves it
    Given the user creates the book "Pasta Cooking" from "James"
    When the user reserves the book "Pasta Cooking"
    Then the book "Pasta Cooking" should be reserved