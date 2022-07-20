import java.io.*;
import java.net.http.HttpClient;

public class App {

    private static String apiKey;

    private static String getApiKey() throws IOException {
        if (apiKey == null) {
            var filename = "./src/.env";
            var reader = new BufferedReader(new FileReader(filename));
            apiKey = reader.readLine();
            reader.close();
        }
        return apiKey;
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        String apiKey;
        try {
            apiKey = getApiKey();
        } catch (IOException ioe) {
            apiKey = "";
        }

        var url = "https://imdb-api.com/en/API/Top250Movies/" + apiKey;
        var client = HttpClient.newHttpClient();
        var out = new BufferedWriter(new PrintWriter(System.out));
        var body = new ResponseGetter(client, out)
                .getFrom(url)
                .ifNot(200)
                .getFrom("https://api.mocki.io/v2/549a5d8b")
                .getResponse()
                .body();

        var parser = new JsonParser();
        var movies = parser.parse(body);

        for (var movie : movies) {
            System.out.println(movie);
        }
    }
}
