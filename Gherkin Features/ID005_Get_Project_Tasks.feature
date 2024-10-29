Feature: Get Project Tasks

As a REST API To-Do List Manager user
I want to retrieve tasks for a project
So that I can view details of tasks for a project


Scenario Outline: Retrieve all tasks associated to a specific existing project (Normal Flow)

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
When user requests to retrieve all tasks for project with id <project_id>
Then a list of all tasks for project with id <project_id> should be displayed
And the response status code should be 200


Scenario Outline: Retrieve all tasks for an existing project that does not have any tasks (Alternate Flow)

Given the following projects exist in the system:
| project_id | title    | completed | active | description   |
| 1          | project1 | false     | true   | "my comment"  | 
| 2          | project2 | true      | false  | ""            | 
| 3          | project3 | false     | true   | "i like this" | 
| 4          | project4 | false     | false  | "cool project"| 
And no tasks exist for project <project_id>
When user requests to retrieve all tasks for project with id <project_id>
Then an empty list of tasks for project with id <project_id> should be displayed
And the response status code should be 200


Scenario Outline: Retrieve all tasks for an nonexistent project (Error Flow)

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
When user Taylor requests to retrieve tasks for project with id NONEXISTENT_PROJECT_ID
Then an "Could not find an instance with projects/NONEXISTENT_PROJECT_ID/tasks" message is issued
And the response status code should be 404