import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import static org.junit.jupiter.api.Assertions.*;

public class TodosTest {

    private static HttpClient client;

    @BeforeAll
    public static void setup() {
        client = HttpClient.newHttpClient();
    }

    // Test for GET /todos
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
}