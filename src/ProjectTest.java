import org.junit.jupiter.api.BeforeAll;
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

public class ProjectTest {
    private static HttpClient client;

    @BeforeAll
    public static void setup() {
        client = HttpClient.newHttpClient();
    }

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
    public void testPostProjects() throws IOException, InterruptedException {
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
    public void testGetProjectById() throws IOException, InterruptedException {
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
    public void testPutProjectById() throws IOException, InterruptedException {
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
    public void testGetProjectTasksByProjectId() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:4567/projects/1/tasks"))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, BodyHandlers.ofString());

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
    public void testDeleteProjectTaskThatDoesNotExist() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:4567/projects/10"))
                .header("Content-Type", "application/json")
                .DELETE()
                .build();
        client.send(request, BodyHandlers.ofString());
        HttpResponse<String> response = client.send(request, BodyHandlers.ofString());

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
}