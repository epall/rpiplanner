Story: Creating a plan of study
  As a student
  I want to create a plan of study
  So that I know what courses I'm taking between now and graduation
  
  Scenario: Using a template
    Given a course template for EE
    And courses for that template
    When the user starts the application
    And enters his major
    And selects "Use Template"
    Then the application should show the template
