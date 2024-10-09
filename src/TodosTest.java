import org.apiguardian.api.API;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.google.gson.JsonParser;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import static org.junit.jupiter.api.Assertions.*;

public class TodosTest {
    // TODO: figure out: at least one unit test module for each undocumented API discovered during exploratory testing.
    // TODO: find a bug:
        // Identify bugs in the API implementation if the actual behavior is different from the documented behavior.
        // include two separate modules one showing the expected behavior failing and one showing the actual behavior working

    // TOODO: Confirm that each API can generate payloads in JSON or XML
    // TODO: Confirm that command line queries function correctly.
    // TODO: Confirm return codes are correctly generated.


    // TODO: make sure they run in any order
    // TODO: Restore the system to the initial state after each test
    // TODO: Ensure the system is ready to be tested
    // TODO: Save the system state
    // TODO: Set up the initial conditions for the test

    // Additional Unit Test Considerations
    // Ensure unit tests fail if service is not running.
    // Include at least one test to see what happens if a JSON payload is malformed.
    // Include at least one test to see what happens if an XML payload is malformed.
    // For each API identified in the exploratory testing include tests of invalid operations, 
        // for example, attempting to delete an object which has already been deleted.

    private static HttpClient client;

    @BeforeAll
    public static void setup() {
        client = HttpClient.newHttpClient();
    }

    /******************************
     * TESTS for /todos ENDPOINTS *
     * ****************************/

    // Test for GET /todos
    // Endpoint should return all the instances of todo
    @Test
    public void testGetTodos() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:4567/todos"))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, BodyHandlers.ofString());

        assertEquals(200, response.statusCode()); 
        
        assertNotNull(response.body());
    }

    // Test for HEAD /todos
    // Endpoint should headers for all the instances of todo
    @Test
    public void testHeadTodos() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:4567/todos"))
                .method("HEAD", BodyPublishers.noBody())
                .build();

        HttpResponse<String> response = client.send(request, BodyHandlers.ofString());

        assertEquals(200, response.statusCode()); 
        
        assertNotNull(response.headers());
    }

    // Test for POST /todos
    // With this endpoint, we should be able to create todo without a ID using the field values in the body of the message
    @Test
    public void testPostTodos() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:4567/todos"))
                .POST(BodyPublishers.ofString( "{ \"title\": \"New Todo\"}"))
                .build();

        HttpResponse<String> response = client.send(request, BodyHandlers.ofString());

        assertEquals(201, response.statusCode()); 
        
        assertNotNull(response.body().contains("New Todo"));

        // Restore the system to the initial state after each test (POST, PUT, DELETE)
        String id = JsonParser.parseString(response.body()).getAsJsonObject().get("id").getAsString();
        HttpRequest deleteRequest = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:4567/todos/" + id))
                .method("DELETE", BodyPublishers.noBody())
                .build();
        HttpResponse<String> deleteResponse = client.send(deleteRequest, BodyHandlers.ofString());
        assertEquals(200, deleteResponse.statusCode()); 
    }

    /**********************************
     * TESTS for /todos/:id ENDPOINTS *
     * ********************************/

    // Test for GET /todos/:id
    // Endpoint should return a specific instances of todo using a id
    @Test
    public void testGetTodosId() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:4567/todos/2"))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, BodyHandlers.ofString());

        assertEquals(200, response.statusCode()); 
        
        assertNotNull(response.body());

        assertTrue(response.body().contains("2"));
    }

    // Test for HEAD /todos/:id
    // Endpoint should headers for a specific instances of todo using a id
    @Test
    public void testHeadTodosId() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:4567/todos/2"))
                .method("HEAD", BodyPublishers.noBody())
                .build();

        HttpResponse<String> response = client.send(request, BodyHandlers.ofString());

        assertEquals(200, response.statusCode()); 
        
        assertEquals(200, response.statusCode()); 
        
        assertNotNull(response.headers());
    }

    // Test for POST /todos/:id
    // Endpoint should amend a specific instances of todo using a id with a body containing the fields to amend
    @Test
    public void testPostTodosId() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:4567/todos/2"))
                .POST(BodyPublishers.ofString( "{ \"title\": \"Update title\"}"))
                .build();

        HttpResponse<String> response = client.send(request, BodyHandlers.ofString());

        assertEquals(200, response.statusCode()); 
        
        assertNotNull(response.body().contains("Update title"));
    }

    // Test for PUT /todos/:id
    // Endpoint should amend a specific instances of todo using a id with a body containing the fields to amend
    @Test
    public void testPutTodosId() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:4567/todos/2"))
                .PUT(BodyPublishers.ofString( "{ \"title\": \"Put title\"}"))
                .build();

        HttpResponse<String> response = client.send(request, BodyHandlers.ofString());

        assertEquals(200, response.statusCode()); 
        
        assertNotNull(response.body().contains("Put title"));
    }

    // Test for DELETE /todos/:id
    // Endpoint should delete a specific instances of todo using a id
    @Test
    public void testDeleteTodosId() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:4567/todos/2"))
                .method("DELETE", BodyPublishers.noBody())
                .build();

        HttpResponse<String> response = client.send(request, BodyHandlers.ofString());

        assertEquals(200, response.statusCode()); 
    }



    /*********************************************
     * TESTS for /todos/:id/categories ENDPOINTS *
     * *******************************************/ 

    // API not identified in the exploratory testing.

    // Test for GET /todos/:id/categories
    // Endpoint should return all the category items related to todo, with given id, by the relationship named categories
    @Test
    public void testGetTodosIdCategories() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:4567/todos/1/categories"))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, BodyHandlers.ofString());

        assertEquals(200, response.statusCode()); 
        
        assertNotNull(response.body());
    }

    // Test for HEAD /todos/:id/categories
    // Endpoint should headers for the category items related to todo, with given id, by the relationship named categories
    @Test
    public void testHeadTodosIdCategories() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:4567/todos/1/categories"))
                .method("HEAD", BodyPublishers.noBody())
                .build();

        HttpResponse<String> response = client.send(request, BodyHandlers.ofString());

        assertEquals(200, response.statusCode()); 
        
        assertNotNull(response.body());
        assertNotNull(response.headers());
    }

    /*************************************************
     * TESTS for /todos/:id/categories/:id ENDPOINTS *
     * ***********************************************/ 

    // API not identified in the exploratory testing.

    /******************************************
     * TESTS for /todos/:id/tasksof ENDPOINTS *
     * ****************************************/ 

    // API not identified in the exploratory testing.

    /**********************************************
     * TESTS for /todos/:id/tasksof/:id ENDPOINTS *
     * ********************************************/
    
     // API not identified in the exploratory testing.
}