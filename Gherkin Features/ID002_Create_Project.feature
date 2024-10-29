Feature: Create Project

As a REST API To-Do List Manager user
I want to create a new project
So that I can organize my tasks into a new project


Scenario Outline: Create a new project with title (Normal Flow)

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
When user requests to create a new project with title <title>
Then a new project with title <title> should be created in the system
And the response status code should be 201


Scenario Outline: Create a new project without title (Alternate Flow)

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
When user requests to create a new project
Then a new project with an empty value for title should be created in the system
And the response status code should be 201


Scenario Outline: Create a new project with invalid data format (Error Flow)

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
When user Coco requests to create project with request body INVALID_REQUEST_BODY
Then an error message indicating the format error is issued
And the response status code should be 400