import java.io.*;
import java.net.URL;
import java.net.http.HttpResponse;
import java.util.Map;
import java.util.regex.Pattern;

public class App {

    private static final String IMDB_API_KEY;
    private static final String NASA_API_KEY;
    // TODO: Procurar por mais API's
    private static final String IMDB_API = "https://imdb-api.com/en/API";
    private static final String NASA_API = "https://api.nasa.gov/planetary";

    static {
        // TODO: checar a biblioteca `dotenv-java` (https://github.com/cdimascio/dotenv-java)
        String iMDBTmp = null;
        String nasaTmp = null;
        try {
            var filename = "./src/.env";
            var reader = new BufferedReader(new FileReader(filename));
            iMDBTmp = reader.readLine();
            nasaTmp = reader.readLine();
            reader.close();
        } catch (IOException ioe) {
            if (iMDBTmp == null) {
                iMDBTmp = "";
            }
            if (nasaTmp == null) {
                nasaTmp = "";
            }
        }
        IMDB_API_KEY = iMDBTmp;
        NASA_API_KEY = nasaTmp;
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        // TODO: Permitir que o usuário avalie o filme de alguma forma

        var stickerMaker = new StickerMaker();

        var imdbRequester = new APIRequester("{0}/{2}/{1}", IMDB_API, IMDB_API_KEY);
        var nasaRequester = new APIRequester("{0}/{2}?api_key={1}", NASA_API, NASA_API_KEY);

        var imdbResponse = getResponse(imdbRequester, "Top250Movies");
        var nasaResponse = getResponse(nasaRequester, "apod");

        var imdbExtractor = new ContentExtractor() {
            @Override
            public String keyMapper(ContentKey from) {
                return switch (from) {
                    case TITLE -> "title";
                    case URL_IMAGE -> "image";
                };
            }
        };

        var nasaExtractor = new ContentExtractor() {
            @Override
            public String keyMapper(ContentKey from) {
                return switch (from) {
                    case TITLE -> "title";
                    case URL_IMAGE -> "url";
                };
            }
        };

        var pattern = Pattern.compile("\\W+");
        imdbExtractor.extractContent(imdbResponse.body())
                .limit(5)
                .forEach(content -> {
                    System.out.println(content.title());

                    var url = content.urlImage();
                    var title = pattern.matcher(content.title())
                            .replaceAll("");

                    try {
                        var stream = new URL(url).openStream();
                        // TODO: Texto deve ser personalizável
                        stickerMaker.make(stream, "TOPZERA", "./image/" + title + ".png");
                    } catch (IOException ioe) {
                        // TODO: Criar uma Exception para embrulhar essa Exception
                        throw new RuntimeException(ioe);
                    }
                });

        nasaExtractor.extractContent(nasaResponse.body())
                .limit(5)
                .forEach(content -> {
                    System.out.println(content.title());

                    var url = content.urlImage();
                    var title = pattern.matcher(content.title())
                            .replaceAll("");

                    try {
                        var stream = new URL(url).openStream();
                        // TODO: Texto deve ser personalizável
                        stickerMaker.make(stream, "TOPZERA", "./image/" + title + ".png");
                    } catch (IOException ioe) {
                        // TODO: Criar uma Exception para embrulhar essa Exception
                        throw new RuntimeException(ioe);
                    }
                });
    }

    private static HttpResponse<String> getResponse(APIRequester requester, String endpoint) throws IOException, InterruptedException {
        System.out.println("Trying API");
        var response = requester.get(endpoint);
        System.out.println("Status Code: " + response.statusCode());
        System.out.println();
        return response;
    }

    // TODO: Melhorar a saída (deixar mais bonitinha)
    private static void printMovie(Map<String, String> movie) {
        System.out.println("   " + movie.get("title"));
        System.out.println("   " + movie.get("image"));
        System.out.println("   " + movie.get("imDbRating"));
        System.out.println();
    }
}
