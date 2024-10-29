Feature: Delete Project

As a REST API To-Do List Manager user
I want to delete an existing project
So that I can remove a project from the system when no longer needed


Background:
Given the REST API To-Do List Manager application is running


Scenario Outline: Delete an existing project by id (Normal Flow)

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
When user requests to delete project <project_id>
Then project <project_id> should be deleted from the system
And the response status code should be 200


Scenario Outline: Delete an existing project with no title (Alternate Flow)

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
When user requests to delete project <project_id>
Then project <project_id> should be deleted from the system
And the response status code should be 200


Scenario Outline: Delete an nonexistent project (Error Flow)

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
When user Jasper requests to delete project with project_id NONEXISTENT_PROJECT_ID
Then an error message indicating the project does not exist is issued
And the response status code should be 400