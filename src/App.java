import java.io.*;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;

public class App {

    private static final String apiKey;
    private static final HttpClient client = HttpClient.newHttpClient();
    private static final Writer out = new PrintWriter(System.out);

    static {
        String tmp;
        try {
            var filename = "./src/.env";
            var reader = new BufferedReader(new FileReader(filename));
            tmp = reader.readLine();
            reader.close();
        } catch (IOException ioe) {
            tmp = "";
        }
        apiKey = tmp;
    }

    private static HttpResponse<String> get(String endpoint, String secondUrl) throws IOException, InterruptedException {
        var url = "https://imdb-api.com/en/API/" + endpoint + "/" + apiKey;
        return new ResponseGetter(client, out)
                .getFrom(url)
                .ifNot(200)
                .getFrom(secondUrl)
                .getResponse();
    }

    private static HttpResponse<String> getTop250Movies() throws IOException, InterruptedException {
        return get("Top250Movies", "https://alura-filmes.herokuapp.com/conteudos");
    }

    private static HttpResponse<String> getMostPopularMovies() throws IOException, InterruptedException {
        return get("MostPopularMovies", "https://alura-filmes.herokuapp.com/conteudos");
    }

    private static HttpResponse<String> getTop250TVs() throws IOException, InterruptedException {
        return get("Top250TVs", "https://alura-filmes.herokuapp.com/conteudos");
    }

    private static HttpResponse<String> getMostPopularTVs() throws IOException, InterruptedException {
        return get("MostPopularTVs", "https://alura-filmes.herokuapp.com/conteudos");
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        var parser = new JsonParser();

        var response = getTop250Movies();
        var movies = parser.parse(response.body());

        System.out.println("\nTop 250 Movies:");
        for (var movie : movies) {
            System.out.println("   " + movie.get("title"));
            System.out.println("   " + movie.get("image"));
            System.out.println("   " + movie.get("imDbRating"));
            System.out.println();
        }
    }
}
