import java.util.stream.Stream;

public abstract class ContentExtractor {

    public enum ContentKey {
        TITLE,
        URL_IMAGE
    }

    public record Content(String title, String urlImage) {
    }

    public final Stream<Content> extractContent(String json) {
        // TODO: Substituir por uma biblioteca especializada para arquivos JSON
        return new JsonParser().parse(json)
                .stream()
                .map(item -> {
                    var title = item.get(keyMapper(ContentKey.TITLE));
                    var urlImage = item.get(keyMapper(ContentKey.URL_IMAGE));

                    return new Content(title, urlImage);
                });
    }

    public abstract String keyMapper(ContentKey from);
}
