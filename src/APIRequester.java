import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.text.MessageFormat;

public final class APIRequester {

    private final HttpClient client;
    private final MessageFormat template;
    private final Object[] objs = new Object[3];

    public APIRequester(HttpClient client, String template, String api, String apiKey) {
        this.client = client;
        this.template = new MessageFormat(template);

        objs[0] = api;
        objs[1] = apiKey;
    }

    public APIRequester(String template, String api, String apiKey) {
        this(HttpClient.newHttpClient(), template, api, apiKey);
    }

    public HttpResponse<String> get(String endpoint) throws IOException, InterruptedException {
        objs[2] = endpoint;
        var url = template.format(objs);
        var uri = URI.create(url);
        var request = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .build();
        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

}
