import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Objects;

public abstract class APIRequester {

    private final HttpClient client;
    protected final String api;
    protected final String apiKey;

    public APIRequester(HttpClient client, String api, String apiKey) {
        this.client = client;
        this.api = api;
        this.apiKey = apiKey;
    }

    public static <T extends APIRequester> Builder<T> newBuilder(Class<T> klass) {
        Objects.requireNonNull(klass);
        return new Builder<>(klass);
    }

    public HttpResponse<String> get(String endpoint) throws IOException, InterruptedException {
        var url = createUrl(endpoint);
        var uri = URI.create(url);
        var request = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .build();
        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    protected abstract String createUrl(String endpoint);

    public static class Builder<T extends APIRequester> {
        private final Class<T> klass;

        private HttpClient client;
        private String api;
        private String apiKey;
        
        private Builder(Class<T> klass) {
            this.klass = klass;
        }
        
        public Builder<T> httpClient(HttpClient httpClient) {
            this.client = httpClient;
            return this;
        }

        public Builder<T> api(String api) {
            this.api = api;
            return this;
        }

        public Builder<T> apiKey(String apiKey) {
            this.apiKey = apiKey;
            return this;
        }
        
        public APIRequester build() {
            Objects.requireNonNull(api);
            if (client == null)
                client = HttpClient.newHttpClient();
            if (apiKey == null)
                apiKey = "";

            try {
                var constructor = klass.getConstructor(HttpClient.class, String.class, String.class);
                return constructor.newInstance(client, api, apiKey);
            } catch (InvocationTargetException | IllegalAccessException | InstantiationException | NoSuchMethodException e) {
                // TODO: Criar uma exceção para embrulhar essas exceções
                throw new RuntimeException(e);
            }
        }
    }
}
