import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import static org.junit.jupiter.api.Assertions.*;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.junit.jupiter.api.TestMethodOrder;

@TestMethodOrder(MethodOrderer.Random.class)
public class ProjectTest {

    private static HttpClient client;

    @BeforeAll
    public static void setup() {
        client = HttpClient.newHttpClient();
    }

    /******************************
     * TESTS for /projects ENDPOINTS *
     * ****************************/

    @Test
    public void testGetProjects() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:4567/projects"))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, BodyHandlers.ofString());

        assertEquals(200, response.statusCode());

        assertNotNull(response.body());
    }

    @Test
    public void testHeadProjects() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:4567/projects")).
                method("HEAD", HttpRequest.BodyPublishers.noBody())
                .build();

        HttpResponse<String> response = client.send(request, BodyHandlers.ofString());

        assertEquals(200, response.statusCode());

        assertNotNull(response.body());
    }

    @Test
    public void testPostProjectsXmlBody() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:4567/projects"))
                .header("Content-Type", "application/xml")
                .POST(HttpRequest.BodyPublishers.ofString("<project><title>test_project</title></project>"))
                .build();
        HttpResponse<String> response = client.send(request, BodyHandlers.ofString());

        assertEquals(201, response.statusCode());
        assertTrue(response.body().contains("test_project"));

        assertNotNull(response.body());

        // remove what was created
        JsonObject jsonObject = JsonParser.parseString(response.body()).getAsJsonObject();
        String id = jsonObject.get("id").getAsString();
        request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:4567/projects/" + id))
                .header("Content-Type", "application/json")
                .DELETE()
                .build();
        client.send(request, BodyHandlers.ofString());
    }

    @Test
    public void testPostProjectsJsonBody() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:4567/projects"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString("{\"title\":\"test_project\"}"))
                .build();
        HttpResponse<String> response = client.send(request, BodyHandlers.ofString());

        assertEquals(201, response.statusCode());
        assertTrue(response.body().contains("test_project"));

        assertNotNull(response.body());

        // remove what was created
        JsonObject jsonObject = JsonParser.parseString(response.body()).getAsJsonObject();
        String id = jsonObject.get("id").getAsString();
        request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:4567/projects/" + id))
                .header("Content-Type", "application/json")
                .DELETE()
                .build();
        client.send(request, BodyHandlers.ofString());
    }

    @Test
    public void testPostProjectsMalformedJsonBody() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:4567/projects"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString("{")) // malformed json
                .build();
        HttpResponse<String> response = client.send(request, BodyHandlers.ofString());

        assertEquals(400, response.statusCode());
        assertNotNull(response.body());
    }

    @Test
    public void testPostProjectsMalformedXmlBody() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:4567/projects"))
                .header("Content-Type", "application/xml")
                .POST(HttpRequest.BodyPublishers.ofString("<project><title>test_project</title>")) // malformed json
                .build();
        HttpResponse<String> response = client.send(request, BodyHandlers.ofString());

        assertEquals(400, response.statusCode());
        assertNotNull(response.body());
    }

    @Test
    public void testPostProjectsWithEmptyRequestBody() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:4567/projects"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString("{}"))
                .build();
        HttpResponse<String> response = client.send(request, BodyHandlers.ofString());

        assertEquals(201, response.statusCode());

        assertNotNull(response.body());

        // remove what was created
        JsonObject jsonObject = JsonParser.parseString(response.body()).getAsJsonObject();
        String id = jsonObject.get("id").getAsString();
        request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:4567/projects/" + id))
                .header("Content-Type", "application/json")
                .DELETE()
                .build();
        client.send(request, BodyHandlers.ofString());
    }

    @Test
    public void testGetProjectByIdJsonBody() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:4567/projects"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString("{\"title\":\"test_project\"}"))
                .build();
        HttpResponse<String> response = client.send(request, BodyHandlers.ofString());

        JsonObject jsonObject = JsonParser.parseString(response.body()).getAsJsonObject();
        String id = jsonObject.get("id").getAsString();

        request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:4567/projects/" + id))
                .GET()
                .build();
        client.send(request, BodyHandlers.ofString());
        response = client.send(request, BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        assertTrue(response.body().contains("test_project"));

        assertNotNull(response.body());
    }

    @Test
    public void testGetProjectByIdXmlBody() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:4567/projects"))
                .header("Content-Type", "application/xml")
                .POST(HttpRequest.BodyPublishers.ofString("<project><title>test_project</title></project>"))
                .build();
        HttpResponse<String> response = client.send(request, BodyHandlers.ofString());

        JsonObject jsonObject = JsonParser.parseString(response.body()).getAsJsonObject();
        String id = jsonObject.get("id").getAsString();

        request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:4567/projects/" + id))
                .GET()
                .build();
        client.send(request, BodyHandlers.ofString());
        response = client.send(request, BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        assertTrue(response.body().contains("test_project"));

        assertNotNull(response.body());
    }

    @Test
    public void testGetProjectByIdThatDoesNotExist() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:4567/projects/8"))
                .GET()
                .build();
        client.send(request, BodyHandlers.ofString());
        HttpResponse<String> response = client.send(request, BodyHandlers.ofString());

        assertEquals(404, response.statusCode());
        assertNotNull(response.body());
    }

    @Test
    public void testPutProjectByIdJsonBody() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:4567/projects"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString("{\"title\":\"test_project\"}"))
                .build();
        HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
        JsonObject jsonObject = JsonParser.parseString(response.body()).getAsJsonObject();
        String id = jsonObject.get("id").getAsString();

        request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:4567/projects/" + id))
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString("{\"title\":\"test_project_new_name\"}"))
                .build();
        response = client.send(request, BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        assertTrue(response.body().contains("test_project_new_name"));

        assertNotNull(response.body());

        // remove what was changed
        request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:4567/projects/" + id))
                .header("Content-Type", "application/json")
                .DELETE()
                .build();
        client.send(request, BodyHandlers.ofString());
    }

    @Test
    public void testPutProjectByIdXmlBody() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:4567/projects"))
                .header("Content-Type", "application/xml")
                .POST(HttpRequest.BodyPublishers.ofString("<project><title>test_project</title></project>"))
                .build();
        HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
        JsonObject jsonObject = JsonParser.parseString(response.body()).getAsJsonObject();
        String id = jsonObject.get("id").getAsString();

        request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:4567/projects/" + id))
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString("{\"title\":\"test_project_new_name\"}"))
                .build();
        response = client.send(request, BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        assertTrue(response.body().contains("test_project_new_name"));

        assertNotNull(response.body());

        // remove what was changed
        request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:4567/projects/" + id))
                .header("Content-Type", "application/json")
                .DELETE()
                .build();
        client.send(request, BodyHandlers.ofString());
    }

    @Test
    public void testPutProjectByIdThatDoesNotExist() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:4567/projects/10"))
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString("{\"title\":\"test_project_new_name\"}"))
                .build();
        HttpResponse<String> response = client.send(request, BodyHandlers.ofString());

        assertEquals(404, response.statusCode());
        assertNotNull(response.body());
    }

    @Test
    public void testDeleteProjectById() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:4567/projects"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString("{\"title\":\"test_project\"}"))
                .build();
        HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
        JsonObject jsonObject = JsonParser.parseString(response.body()).getAsJsonObject();
        String id = jsonObject.get("id").getAsString();

        request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:4567/projects/" + id))
                .GET()
                .build();
        response = client.send(request, BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:4567/projects/" + id))
                .header("Content-Type", "application/json")
                .DELETE()
                .build();
        response = client.send(request, BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        assertNotNull(response.body());

        request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:4567/projects/" + id))
                .GET()
                .build();
        response = client.send(request, BodyHandlers.ofString());
        assertEquals(404, response.statusCode());
    }

    @Test
    public void testDeleteProjectByIdThatDoesNotExist() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:4567/projects/10"))
                .header("Content-Type", "application/json")
                .DELETE()
                .build();
        HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
        assertEquals(404, response.statusCode());
        assertNotNull(response.body());
    }

    @Test
    public void testGetProjectTasksByProjectId() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:4567/projects/1/tasks"))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, BodyHandlers.ofString());

        assertEquals(200, response.statusCode());

        assertNotNull(response.body());
    }

    /*
    BUG
    Notice how this test is incorrect. Project with Id 10 does not exist, while a list of tasks
    with a relationship with nonexistent project is still displayed
     */
    @Test
    public void testGetProjectTasksByProjectIdThatDoesNotExist() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:4567/projects/10"))
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
        assertEquals(404, response.statusCode());

        request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:4567/projects/10/tasks"))
                .GET()
                .build();

        response = client.send(request, BodyHandlers.ofString());

        assertNotEquals(404, response.statusCode());
        assertEquals(200, response.statusCode());
        assertNotNull(response.body());
    }

    @Test
    public void testPostProjectTasksByProjectId() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:4567/projects/1/tasks"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString("{\"title\":\"test_task\"}"))
                .build();
        HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
        JsonObject jsonObject = JsonParser.parseString(response.body()).getAsJsonObject();
        String id = jsonObject.get("id").getAsString();

        assertEquals(201, response.statusCode());
        assertNotNull(response.body());

        HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:4567/projects/" + id))
                .header("Content-Type", "application/json")
                .DELETE()
                .build();
        client.send(request, BodyHandlers.ofString());
    }

    @Test
    public void testPostProjectTasksByProjectIdThatDoesNotExist() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:4567/projects/10"))
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
        assertEquals(404, response.statusCode());

        request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:4567/projects/10/tasks"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString("{\"title\":\"test_task\"}"))
                .build();
        response = client.send(request, BodyHandlers.ofString());

        assertEquals(404, response.statusCode());
        assertNotNull(response.body());
    }

    @Test
    public void testDeleteProjectTaskByTaskIdThatDoesNotExist() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:4567/projects/1/Tasks/10"))
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
        assertEquals(404, response.statusCode());

        request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:4567/projects/1/Tasks/10"))
                .header("Content-Type", "application/json")
                .DELETE()
                .build();
        client.send(request, BodyHandlers.ofString());
        response = client.send(request, BodyHandlers.ofString());

        assertEquals(404, response.statusCode());
        assertNotNull(response.body());
    }

    @Test
    public void testGetProjectCategoriesByProjectId() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:4567/projects/1/categories"))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, BodyHandlers.ofString());

        assertEquals(200, response.statusCode());

        assertNotNull(response.body());
    }

    /*
    BUG
    Notice how this test is incorrect. Project with Id 10 does not exist, while a list of categories
    with a relationship with nonexistent project is still displayed
     */
    @Test
    public void testGetProjectCategoriesByProjectIdThatDoesNotExist() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:4567/projects/10"))
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
        assertEquals(404, response.statusCode());

        request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:4567/projects/10/categories"))
                .GET()
                .build();

        response = client.send(request, BodyHandlers.ofString());

        assertNotEquals(404, response.statusCode());
        assertEquals(200, response.statusCode());
        assertNotNull(response.body());
    }

    @Test
    public void testPostProjectCategoriesByProjectId() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:4567/projects/1/categories"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString("{\"title\":\"test_category\"}"))
                .build();
        HttpResponse<String> response = client.send(request, BodyHandlers.ofString());

        assertEquals(201, response.statusCode());
        assertTrue(response.body().contains("test_category"));

        assertNotNull(response.body());

        // remove what was created
        JsonObject jsonObject = JsonParser.parseString(response.body()).getAsJsonObject();
        String id = jsonObject.get("id").getAsString();
        request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:4567/projects/" + id))
                .header("Content-Type", "application/json")
                .DELETE()
                .build();
        client.send(request, BodyHandlers.ofString());
    }

    @Test
    public void testPostProjectCategoriesByProjectIdThatDoesNotExist() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:4567/projects/10/categories"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString("{\"title\":\"test_category\"}"))
                .build();
        HttpResponse<String> response = client.send(request, BodyHandlers.ofString());

        assertEquals(404, response.statusCode());
        assertNotNull(response.body());
    }
}