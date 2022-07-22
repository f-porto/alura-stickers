import java.net.http.HttpClient;

public class MockRequester extends APIRequester {

    private MockRequester(HttpClient client, String api, String apiKey) {
        super(client, api, apiKey);
    }

    @Override
    protected String createUrl(String endpoint) {
        return api + "/" + endpoint;
    }
}
