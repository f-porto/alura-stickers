import java.io.IOException;
import java.io.Writer;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ResponseGetter {
    private final HttpClient client;
    private final Writer writer;

    private boolean successful;
    private HttpResponse<String> response;

    public ResponseGetter(HttpClient client, Writer writer) {
        this.writer = writer;
        this.client = client;
        this.successful = false;
    }

    public ResponseGetter getFrom(String url) throws IOException, InterruptedException {
        if (successful) {
            writer.write("Skipping: " + url + "\n");
            writer.flush();
            return this;
        }

        writer.write("Trying: " + url + "\n");
        writer.flush();

        var uri = URI.create(url);
        var request = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return this;
    }

    public ResponseGetter ifNot(int statusCode) throws IOException {
        if (!successful) {
            successful = response.statusCode() == statusCode;
            if (successful)
                writer.write("Got: " + response.statusCode() + "\n");
            else
                writer.write("Failed with: " + response.statusCode() + "\n");
        } else {
            writer.write("Already had success before\n");
        }
        writer.flush();
        return this;
    }

    public HttpResponse<String> getResponse() {
        return response;
    }
}
