package jinkyframe;

import com.google.zxing.WriterException;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.*;
import java.util.*;

import static jinkyframe.Colours.*;
import static jinkyframe.ImageUtils.*;
import static jinkyframe.ImageUtils.createImage;
import static jinkyframe.ImageUtils.updateImage;

public final class ImageGenerator {
    public static boolean DEBUG = false;

    private static final int width = 600;
    private static final int height = 448;

    public static void main(String[] args) throws Exception {
        var info = loadInfo();
        var image = generateImage(info);
        saveImage(Paths.get("jinky-frame.png"), image);
        DEBUG = true;
        var debugImage = generateImage(info);
        saveImage(Paths.get("debug-jinky-frame.png"), debugImage);
    }

    public static Info loadInfo() throws IOException {
        var props = new Properties();
        props.load(new FileInputStream("secrets/jinky.properties"));
        var wifiInfo = new Info.WifiInfo(
                props.getProperty("guestNetworkName"),
                props.getProperty("guestNetworkEncryption"),
                props.getProperty("guestNetworkPassword"),
                props.getProperty("guestNetworkSpeed")
        );
        return new Info(wifiInfo);
    }

    public static byte[] generateImageBytes(Info info) throws Exception {
        var bufferedImage = generateImage(info);
        return imageTo3BitBytes(bufferedImage);
    }

    private static BufferedImage generateImage(Info info) throws Exception {
        var image = createImage(600, 448, Color.WHITE);

/*        updateImage(image, g -> {
            g.setColor(black);
            g.fillRect(0, 0, 75, 448);
            g.setColor(white);
            g.fillRect(75, 0, 75, 448);
            g.setColor(green);
            g.fillRect(150, 0, 75, 448);
            g.setColor(blue);
            g.fillRect(225, 0, 75, 448);
            g.setColor(red);
            g.fillRect(300, 0, 75, 448);
            g.setColor(yellow);
            g.fillRect(375, 0, 75, 448);
            g.setColor(orange);
            g.fillRect(450, 0, 75, 448);
            g.setColor(taupe);
            g.fillRect(525, 0, 75, 448);
        });*/

        var wifiPanel = WifiPanel.generate(info, new Margins(5));


        return updateImage(image, g -> {
            g.drawImage(wifiPanel, width - wifiPanel.getWidth(), height - wifiPanel.getHeight(), null);
        });
    }

    private static byte[] imageTo3BitBytes(BufferedImage image) {
        var colourMap = Map.of(
                -16777216, new boolean[]{false, false, false}, // black
                -1, new boolean[]{false, false, true},             // white
                -16711936, new boolean[]{false, true, false},      // green
                -16776961, new boolean[]{false, true, true},       // blue
                -65536, new boolean[]{true, false, false},        // red
                -256, new boolean[]{true, false, true},          // yellow
                -14336, new boolean[]{true, true, false},        // orange
                -4805218, new boolean[]{true, true, true}       // taupe
        );
        var bits = new BitSet(image.getWidth() * image.getHeight() * 3);

        var index = 0;
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                var pixelBits = colourMap.get(image.getRGB(x, y));
                for (boolean bit : pixelBits) {
                    if (bit) {
                        bits.set(index);
                    }
                    index++;
                }
            }
        }
        return bits.toByteArray();
    }

    private static void saveImage(Path path, BufferedImage image) throws IOException {
        ImageIO.write(image, "png", path.toFile());
    }

    private static void saveBytes(Path path, byte[] bytes) throws IOException {
        Files.write(path, bytes);
    }
}
