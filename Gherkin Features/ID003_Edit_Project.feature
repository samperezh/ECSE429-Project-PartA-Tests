Feature: Edit Project

As a REST API To-Do List Manager user
I want to edit an existing project
So that I can update the details of a project


Scenario Outline: Edit the title of an existing project (Normal Flow)

Given the following projects exist in the system:
| project_id | title    | completed | active | description   |
| 1          | project1 | false     | true   | "my comment"  | 
| 2          | project2 | true      | false  | ""            | 
| 3          | project3 | false     | true   | "i like this" | 
| 4          | project4 | false     | false  | "cool project"| 
And the following tasks exist for each project:
| project_id | task_id |
| 1          | 1       |
| 1          | 2       |
| 2          | 3       |
| 3          | 4       |
When user requests to edit project <project_id> with new title <new_title>
Then the title of project <project_id> should be updated to <new_title>
And the response status code should be 200


Scenario Outline: Edit an the description of an existing project (Alternate Flow)

Given the following projects exist in the system:
| project_id | title    | completed | active | description   |
| 1          | project1 | false     | true   | "my comment"  | 
| 2          | project2 | true      | false  | ""            | 
| 3          | project3 | false     | true   | "i like this" | 
| 4          | project4 | false     | false  | "cool project"| 
And the following tasks exist for each project:
| project_id | task_id |
| 1          | 1       |
| 1          | 2       |
| 2          | 3       |
| 3          | 4       |
When user requests to edit project <project_id> with new description <new_description>
Then the description of project <project_id> should be updated to <new_description>
And the response status code should be 200


Scenario Outline: Edit an the complete status of an existing project (Alternate Flow)

Given the following projects exist in the system:
| project_id | title    | completed | active | description   |
| 1          | project1 | false     | true   | "my comment"  | 
| 2          | project2 | true      | false  | ""            | 
| 3          | project3 | false     | true   | "i like this" | 
| 4          | project4 | false     | false  | "cool project"| 
And the following tasks exist for each project:
| project_id | task_id |
| 1          | 1       |
| 1          | 2       |
| 2          | 3       |
| 3          | 4       |
When user requests to edit project <project_id> with new completed status <new_completed>
Then the completed status of project <project_id> should be updated to <new_completed>
And the response status code should be 200


Scenario Outline: Edit an the active status of an existing project (Alternate Flow)

Given the following projects exist in the system:
| project_id | title    | completed | active | description   |
| 1          | project1 | false     | true   | "my comment"  | 
| 2          | project2 | true      | false  | ""            | 
| 3          | project3 | false     | true   | "i like this" | 
| 4          | project4 | false     | false  | "cool project"| 
And the following tasks exist for each project:
| project_id | task_id |
| 1          | 1       |
| 1          | 2       |
| 2          | 3       |
| 3          | 4       |
When user requests to edit project <project_id> with new active status <new_active>
Then the active status of project <project_id> should be updated to <new_active>
And the response status code should be 200


Scenario Outline: Edit an nonexistent project (Error Flow)

Given the following projects exist in the system:
| project_id | title    | completed | active | description   |
| 1          | project1 | false     | true   | "my comment"  | 
| 2          | project2 | true      | false  | ""            | 
| 3          | project3 | false     | true   | "i like this" | 
| 4          | project4 | false     | false  | "cool project"| 
And the following tasks exist for each project:
| project_id | task_id |
| 1          | 1       |
| 1          | 2       |
| 2          | 3       |
| 3          | 4       |
When user Lucrecia requests to edit project with project_id NONEXISTENT_PROJECT_ID
Then an error message indicating the project does not exist is issued
And the response status code should be 400


Scenario Outline: Edit an existing project with invalid data format (Error Flow)

Given the following projects exist in the system:
| project_id | title    | completed | active | description   |
| 1          | project1 | false     | true   | "my comment"  | 
| 2          | project2 | true      | false  | ""            | 
| 3          | project3 | false     | true   | "i like this" | 
| 4          | project4 | false     | false  | "cool project"| 
And the following tasks exist for each project:
| project_id | task_id |
| 1          | 1       |
| 1          | 2       |
| 2          | 3       |
| 3          | 4       |
When user Nova requests to edit project with request body INVALID_REQUEST_BODY
Then an error message indicating the format error is issued
And the response status code should be 400