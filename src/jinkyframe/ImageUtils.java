package jinkyframe;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.IndexColorModel;
import java.io.InputStream;
import java.util.function.Consumer;

import static java.awt.image.BufferedImage.TYPE_BYTE_INDEXED;
import static java.lang.Math.max;
import static java.lang.Math.min;
import static jinkyframe.Colours.debugColour;
import static jinkyframe.Colours.white;

public final class ImageUtils {
    private ImageUtils() {
        // static methods
    }

    public static BufferedImage createImage(int width, int height, Color background) {
        return updateImage(new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB), g -> {
            g.setColor(background);
            g.fillRect(0, 0, width, height);
        });
    }

    public static BufferedImage createImage(int width, int height) {
        return updateImage(new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB), g -> {
            g.setColor(white);
            g.fillRect(0, 0, width, height);
        });
    }

    public static BufferedImage createImage(int width, int height, IndexColorModel model) {
        return updateImage(new BufferedImage(width, height, TYPE_BYTE_INDEXED, model), g -> {
            g.setColor(white);
            g.fillRect(0, 0, width, height);
        });
    }

    public static BufferedImage updateImage(BufferedImage image, Consumer<Graphics2D> g2d) {
        var g = image.createGraphics();
        try {
//            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
//            g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
            g2d.accept(g);
            if (ImageGenerator.DEBUG) {
                var width = image.getWidth();
                var height = image.getHeight();
                g.setColor(debugColour);
                g.drawLine(0, 0, width, 0);
                g.drawLine(width - 1, 0, width - 1, height);
                g.drawLine(width - 1, height - 1, 0, height - 1);
                g.drawLine(0, height - 1, 0, 0);
            }
        } finally {
            g.dispose();
        }
        return image;
    }

    public static BufferedImage createImage(int width, int height, Consumer<Graphics2D> g2d) {
        return updateImage(createImage(width, height), g2d);
    }

    public static BufferedImage createImage(int width, int height, IndexColorModel model, Consumer<Graphics2D> g2d) {
        return updateImage(createImage(width, height, model), g2d);
    }

    public static BufferedImage drawString(String string, Font font, Color colour) {
        var currentDebug = ImageGenerator.DEBUG;
        ImageGenerator.DEBUG = false;

        var image = createImage(300, 150, ColourModels.get(colour), g -> {
            g.setColor(colour);
            g.setFont(font);
            g.drawString(string, 20, 130);
        });

        var croppedImage = cropImage(image);
        ImageGenerator.DEBUG = currentDebug;
        return croppedImage;
    }

    public static BufferedImage cropImage(BufferedImage image) {
        return copyImage(subImage(image, findBounds(image)));
    }

    public static BufferedImage copyImage(BufferedImage image) {
        return createImage(image.getWidth(), image.getHeight(),
                g -> g.drawImage(image, 0, 0, null)
        );
    }

    public static BufferedImage subImage(BufferedImage image, Bounds bounds) {
        return image.getSubimage(bounds.x, bounds.y, bounds.w, bounds.h);
    }

    public static Bounds findBounds(BufferedImage image) {
        var minX = Integer.MAX_VALUE;
        var maxX = Integer.MIN_VALUE;
        var minY = Integer.MAX_VALUE;
        var maxY = Integer.MIN_VALUE;

        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                var rgb = image.getRGB(x, y);
                if (rgb != -1) {
                    minX = min(x, minX);
                    maxX = max(x, maxX);
                    minY = min(y, minY);
                    maxY = max(y, maxY);
                }
            }
        }

        if (minX == Integer.MAX_VALUE) {
            return new Bounds(0, 0, image.getWidth(), image.getHeight());
        } else {
            return new Bounds(minX, minY, maxX - minX + 1, maxY - minY + 1);
        }
    }

    public static Font font(String name, float size) {
        return Utils.unsafe(() -> Font.createFont(Font.TRUETYPE_FONT, fontResource(name)).deriveFont(size));
    }

    public static InputStream resource(String name) {
        return ImageUtils.class.getClassLoader().getResourceAsStream(name);
    }

    public static InputStream fontResource(String name) {
        return resource("fonts/" + name);
    }

    record Margins(int top, int right, int bottom, int left) {
        Margins(int margin) {
            this(margin, margin, margin, margin);
        }
    }

    record Bounds(int x, int y, int w, int h) {
    }

}
