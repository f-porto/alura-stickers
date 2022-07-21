import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class StickerMaker {

    public void make(InputStream stream, String text, String pathoutput) throws IOException {
        var originalImage = ImageIO.read(stream);

        var originalWidth = originalImage.getWidth();
        var originalHeight = originalImage.getHeight();

        var pixels = (int) (originalHeight * 0.2f);
        var newHeight = originalHeight + pixels;

        var newImage = new BufferedImage(originalWidth, newHeight, BufferedImage.TRANSLUCENT);

        var graphics = newImage.createGraphics();
        graphics.drawImage(originalImage, 0, 0, null);

        // TODO: Centralizar o texto
        // TODO: Usar outras fontes
        // TODO: Fazer outline no texto
        graphics.setFont(new Font(Font.SANS_SERIF, Font.BOLD, pixels));
        graphics.setColor(Color.YELLOW);
        graphics.drawString(text, 0, newHeight);

        var output = new File(pathoutput);
        output.mkdirs();
        ImageIO.write(newImage, "png", output);
    }
}
