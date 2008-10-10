Story: Custom courses
  As a student
  I want to enter courses manually
  So that I don't have to depend on the incomplete course database
  
  Scenario: Entering a simple course
    Given a user entering a plan of study
    When the user clicks "Add Course"
    Then a window should appear with title "New Course"
