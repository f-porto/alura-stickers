import java.io.*;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.util.Map;

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
        // TODO: Permitir que o usuário avalie o filme de alguma forma

        // TODO: Substituir por uma biblioteca especializada para arquivos JSON
        var parser = new JsonParser();

        var response = getTop250Movies();
        var movies = parser.parse(response.body());

        var stickerMaker = new StickerMaker();

        var limit = 5;
        for (var i = 0; i < limit && i < movies.size(); i++) {
            var movie = movies.get(i);

            // TODO: Tentar pegar uma imagem maior (trocar as dimensões pela url ou pegar os poster pela API do IMDB)
            var url = movie.get("image");
            var title = movie.get("title");
            var stream = new URL(url).openStream();

            // TODO: Texto deve ser personalizável
            stickerMaker.make(stream, "TOPZERA", "./image/" + movie.get("id") + ".png");

            System.out.println(title);
        }
    }

    // TODO: Melhorar a saída (deixar mais bonitinha)
    private static void printMovie(Map<String, String> movie) {
        System.out.println("   " + movie.get("title"));
        System.out.println("   " + movie.get("image"));
        System.out.println("   " + movie.get("imDbRating"));
        System.out.println();
    }
}
