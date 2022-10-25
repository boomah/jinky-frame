package jinkyframe;

import java.awt.*;
import java.awt.image.BufferedImage;

public final class Textures {
    private Textures() {
        // static methods
    }
        
    public static TexturePaint dotFill(Color colour) {
        int width = 2;
        int height = 2;
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = image.createGraphics();
        graphics.setColor(Color.WHITE);
        graphics.fillRect(0, 0, width, height);
        graphics.setColor(colour);
        graphics.fillRect(0, 0, 1, 1);
        graphics.fillRect(1, 1, 1, 1);
        graphics.dispose();
        return new TexturePaint(image, new Rectangle(width, height));
    }
}
