import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import com.google.gson.JsonParser;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.Random.class)
public class TodosTest {
    // TODO: figure out: at least one unit test module for each undocumented API discovered during exploratory testing.
    // TODO: find a bug:
        // Identify bugs in the API implementation if the actual behavior is different from the documented behavior.
        // include two separate modules one showing the expected behavior failing and one showing the actual behavior working

    // TODO: Confirm that each API can generate payloads in JSON or XML
    // TODO: Confirm that command line queries function correctly.

    // TODO: Ensure the system is ready to be tested

    // Additional Unit Test Considerations
    // TODO: Include at least one test to see what happens if a JSON payload is malformed.
    // TODO: Include at least one test to see what happens if an XML payload is malformed.
    // TODO: For each API identified in the exploratory testing include tests of invalid operations, 
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
        // MAIN TEST
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:4567/todos"))
                .POST(BodyPublishers.ofString( "{ \"title\": \"New Todo\"}"))
                .build();

        HttpResponse<String> response = client.send(request, BodyHandlers.ofString());

        assertEquals(201, response.statusCode()); 
        
        assertNotNull(response.body().contains("New Todo"));

        // RESTORE THE SYSTEM TO INITIAL STATE
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
        
        assertNotNull(response.headers());
    }

    // Test for POST /todos/:id
    // Endpoint should amend a specific instances of todo using a id with a body containing the fields to amend
    @Test
    public void testPostTodosId() throws IOException, InterruptedException {
        // TEST PREP
        // Create a new instance to test the post 
        // we will modify it and delete it in order for the system to be restored to the initial state by the end of this test
        HttpRequest postRequest = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:4567/todos"))
                .POST(BodyPublishers.ofString( "{ \"title\": \"New Todo\"}"))
                .build();

        HttpResponse<String> postResponse = client.send(postRequest, BodyHandlers.ofString());

        assertEquals(201, postResponse.statusCode()); 
        
        String id = JsonParser.parseString(postResponse.body()).getAsJsonObject().get("id").getAsString();
        
        // MAIN TEST
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:4567/todos/" + id))
                .POST(BodyPublishers.ofString( "{ \"title\": \"Update title\"}"))
                .build();

        HttpResponse<String> response = client.send(request, BodyHandlers.ofString());

        assertEquals(200, response.statusCode()); 
        
        assertNotNull(response.body().contains("Update title"));

        // RESTORE THE SYSTEM TO INITIAL STATE
        HttpRequest deleteRequest = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:4567/todos/" + id))
                .method("DELETE", BodyPublishers.noBody())
                .build();
        HttpResponse<String> deleteResponse = client.send(deleteRequest, BodyHandlers.ofString());
        assertEquals(200, deleteResponse.statusCode());  
    }

    // Test for PUT /todos/:id
    // Endpoint should amend a specific instances of todo using a id with a body containing the fields to amend
    @Test
    public void testPutTodosId() throws IOException, InterruptedException {
        // TEST PREP
        // Create a new instance to test the post 
        // we will modify it and delete it in order for the system to be restored to the initial state by the end of this test
        HttpRequest postRequest = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:4567/todos"))
                .POST(BodyPublishers.ofString( "{ \"title\": \"New Todo\"}"))
                .build();

        HttpResponse<String> postResponse = client.send(postRequest, BodyHandlers.ofString());

        assertEquals(201, postResponse.statusCode()); 
        
        String id = JsonParser.parseString(postResponse.body()).getAsJsonObject().get("id").getAsString();

        // MAIN TEST
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:4567/todos/" + id))
                .PUT(BodyPublishers.ofString( "{ \"title\": \"Put title\"}"))
                .build();

        HttpResponse<String> response = client.send(request, BodyHandlers.ofString());

        assertEquals(200, response.statusCode()); 
        
        assertNotNull(response.body().contains("Put title"));

         // RESTORE THE SYSTEM TO INITIAL STATE
        HttpRequest deleteRequest = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:4567/todos/" + id))
                .method("DELETE", BodyPublishers.noBody())
                .build();
        HttpResponse<String> deleteResponse = client.send(deleteRequest, BodyHandlers.ofString());
        assertEquals(200, deleteResponse.statusCode());
    }

    // Test for DELETE /todos/:id
    // Endpoint should delete a specific instances of todo using a id
    @Test
    public void testDeleteTodosId() throws IOException, InterruptedException {
        // TEST PREP
        //Create a new instance to test the delete - this way the system will be restored to the initial state
        HttpRequest postRequest = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:4567/todos"))
                .POST(BodyPublishers.ofString( "{ \"title\": \"Delete this\"}"))
                .build();
        HttpResponse<String> postResponse = client.send(postRequest, BodyHandlers.ofString());
        assertEquals(201, postResponse.statusCode()); 

        String id = JsonParser.parseString(postResponse.body()).getAsJsonObject().get("id").getAsString();

        // MAIN TEST

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:4567/todos/" + id))
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