Feature: Member Service Management
  As an administrator
  I want to manage member information
  So that I can maintain accurate member records

  Scenario: Get member by valid ID
    Given a member exists with ID 1
    When I request member information with ID 1
    Then the member details should be returned
    And the response status should be OK

  Scenario: Get member by invalid ID
    Given no member exists with ID 999
    When I request member information with ID 999
    Then the member should not be found
    And the response status should be NOT_FOUND

  Scenario: Search members by name
    Given the following members exist in the system:
      | memberId | memberName | email               | phoneNumber  | status |
      | 1        | John Doe   | john.doe@email.com  | 123-456-7890 | ACTIVE |
      | 2        | Jane Smith | jane.smith@email.com| 234-567-8901 | ACTIVE |
      | 3        | John Smith | john.smith@email.com| 345-678-9012 | INACTIVE |
    When I search for members with name "John"
    Then the search result should contain 2 members
    And the search result should include members with IDs 1 and 3
    
  Scenario: Create a new member
    Given I have valid member information
    When I submit a request to create a new member
    Then a new member should be created successfully
    And the response should include the new member ID
    
  Scenario: Update member information
    Given a member exists with ID 1
    When I update the member's email to "updated.email@example.com"
    Then the member's email should be updated successfully
    And the response status should be OK