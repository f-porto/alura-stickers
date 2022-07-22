import java.net.http.HttpClient;

public class IMDBRequester extends APIRequester {

    private IMDBRequester(HttpClient client, String api, String apiKey) {
        super(client, api, apiKey);
    }

    @Override
    protected String createUrl(String endpoint) {
        return api + "/" + endpoint + "/" + apiKey;
    }
}
