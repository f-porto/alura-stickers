import java.io.*;
import java.net.URL;
import java.net.http.HttpClient;
import java.util.Map;

public class App {

    private static final String API_KEY;
    private static final String IMDB_API = "https://imdb-api.com/en/API";
    private static final String MOCK_API = "https://api.mocki.io/v2/549a5d8b";

    static {
        // TODO: checar a biblioteca `dotenv-java` (https://github.com/cdimascio/dotenv-java)
        String tmp;
        try {
            var filename = "./src/.env";
            var reader = new BufferedReader(new FileReader(filename));
            tmp = reader.readLine();
            reader.close();
        } catch (IOException ioe) {
            tmp = "";
        }
        API_KEY = tmp;
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        // TODO: Permitir que o usuário avalie o filme de alguma forma

        // TODO: Substituir por uma biblioteca especializada para arquivos JSON
        var parser = new JsonParser();
        var stickerMaker = new StickerMaker();

        // TODO: Talvez usar Factories ou Anounymous Classes
        var iMDBRequester = APIRequester.newBuilder(IMDBRequester.class)
                .api(IMDB_API)
                .apiKey(API_KEY)
                .build();
        var mockRequester = APIRequester.newBuilder(MockRequester.class)
                .api(MOCK_API)
                .build();

        System.out.println("Trying iMDB API");
        var response = iMDBRequester.get("Top250Movies");
        System.out.println("Status Code: " + response.statusCode());

        if (response.statusCode() != 200) {
            System.out.println("Trying Mock API");
            response = mockRequester.get("Top250Movies");
            System.out.println("Status Code: " + response.statusCode());
        }
        System.out.println();

        // TODO: checar o pacote `streamex` (https://github.com/amaembo/streamex)
        parser.parse(response.body())
                .stream()
                .limit(5)
                .forEach(movie -> {
                    // TODO: Tentar pegar uma imagem maior (trocar as dimensões pela url ou pegar os poster pela API do IMDB
                    var url = movie.get("image");
                    var id = movie.get("id");
                    var title = movie.get("title");

                    System.out.println(title);

                    try {
                        var stream = new URL(url).openStream();
                        // TODO: Texto deve ser personalizável
                        stickerMaker.make(stream, "TOPZERA", "./image/" + id + ".png");
                    } catch (IOException ioe) {
                        // TODO: Criar uma Exception para embrulhar essa Exception
                        throw new RuntimeException(ioe);
                    }
                });
    }

    // TODO: Melhorar a saída (deixar mais bonitinha)
    private static void printMovie(Map<String, String> movie) {
        System.out.println("   " + movie.get("title"));
        System.out.println("   " + movie.get("image"));
        System.out.println("   " + movie.get("imDbRating"));
        System.out.println();
    }
}
