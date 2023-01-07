package jinkyframe;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.*;
import java.time.*;
import java.util.List;
import java.util.*;

import static jinkyframe.Colours.*;
import static jinkyframe.ImageUtils.*;
import static jinkyframe.Info.*;

public final class ImageGenerator {
    public static boolean DEBUG = false;

    private static final int width = 600;
    private static final int height = 448;

    public static void main(String[] args) throws Exception {
        var info = generateInfo();
        var image = generateImage(info);
        saveImage(Paths.get("jinky-frame-full.png"), image);
        var processedImage = bytesToImage(imageTo3BitBytes(image));
        saveImage(Paths.get("jinky-frame.png"), processedImage);
        DEBUG = true;
        var debugImage = generateImage(info);
        saveImage(Paths.get("debug-jinky-frame.png"), debugImage);
        System.out.println("Generated at " + Instant.now());
    }

    public static Info generateInfo() throws IOException, InterruptedException {
        var props = new Properties();
        props.load(new FileInputStream("secrets/jinky.properties"));

        Instant now = Instant.now();
        var zoneId = ZoneId.of(props.getProperty("zoneId"));
        var zonedDateTime = ZonedDateTime.ofInstant(now, zoneId);
        var localDate = zonedDateTime.toLocalDate();
        var dateInfo = new DateInfo(localDate);
        var localDateTime = zonedDateTime.toLocalDateTime();
        var lat = props.getProperty("lat");
        var lon = props.getProperty("lon");
        var location = props.getProperty("location") + String.format(" (%s / %s)", lat, lon);
        var weatherApiKey = props.getProperty("weatherApiKey");

        var forecast = WeatherReader.readWeather(lat, lon, weatherApiKey);
        var weatherInfo = new WeatherInfo(forecast);

        var stationId = props.getProperty("tideStationId");
        var tides = TideReader.readTide(stationId);
        var tideInfo = new TideInfo(tides);

        var birthdays = BirthdayReader.readBirthdays();
        var birthdayInfo = new BirthdayInfo(birthdays);

        var wifiInfo = new WifiInfo(
                props.getProperty("guestNetworkName"),
                props.getProperty("guestNetworkEncryption"),
                props.getProperty("guestNetworkPassword"),
                props.getProperty("guestNetworkSpeed")
        );

        var systemInfo = new SystemInfo(
                localDateTime.plusHours(1),
                localDateTime,
                location,
                zoneId,
                "Unknown",
                "All systems are go!"
        );

        return new Info(dateInfo, weatherInfo, tideInfo, birthdayInfo, wifiInfo, systemInfo);
    }

    public static byte[] generateImageBytes(Info info) throws Exception {
        var bufferedImage = generateImage(info);
        return imageTo3BitBytes(bufferedImage);
    }

    private static BufferedImage generateImage(Info info) throws Exception {
        var image = createImage(600, 448, Color.WHITE);

        /*updateImage(image, g -> {
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

        var margins = new Margins(5);
        var datePanel = DatePanel.generate(info, margins);
        var weatherSummaryPanel = WeatherSummeryPanel.generate(info, margins);
        var weatherHourlyPanel = WeatherHourlyPanel.generate(info, margins);
        var tidePanel = TidePanel.generate(info, margins);
        var windPanel = WindPanel.generate(info, margins);
        var visibilityPanel = VisibilityPanel.generate(info, margins);
        var minorWeatherPanel = MinorWeatherPanel.generate(info, margins);
        var birthdayPanel = BirthdayPanel.generate(info, margins);
        var wifiPanel = WifiPanel.generate(info, margins);
        var systemPanel = SystemPanel.generate(info, margins);

        var windX = tidePanel.getWidth();
        int wifiX = width - wifiPanel.getWidth();
        int systemX = wifiX - systemPanel.getWidth();
        int bottomY = height - systemPanel.getHeight();
        var row3Y = weatherSummaryPanel.getHeight();

        return updateImage(image, g -> {
            g.drawImage(datePanel, 0, 0, null);
            g.drawImage(weatherSummaryPanel, datePanel.getWidth(), 0, null);
            g.drawImage(weatherHourlyPanel, 0, datePanel.getHeight(), null);
            g.drawImage(tidePanel, 0, row3Y, null);
            g.drawImage(windPanel, windX, row3Y, null);
            g.drawImage(visibilityPanel, windX, row3Y + windPanel.getHeight(), null);
            g.drawImage(minorWeatherPanel, windX + windPanel.getWidth(), row3Y, null);
            g.drawImage(birthdayPanel, systemX - birthdayPanel.getWidth(), bottomY, null);
            g.drawImage(wifiPanel, wifiX, bottomY, null);
            g.drawImage(systemPanel, systemX, bottomY, null);
        });
    }

    private static byte[] imageTo3BitBytes(BufferedImage image) {
        var colourMap = Map.of(
                -16777216, new boolean[]{false, false, false}, // black
                -1, new boolean[]{false, false, true},             // white
                -16711936, new boolean[]{false, true, false},      // green
                -16776961, new boolean[]{false, true, true},       // blue
                -65536, new boolean[]{true, false, false},         // red
                -256, new boolean[]{true, false, true},            // yellow
                -14336, new boolean[]{true, true, false},          // orange
                -4805218, new boolean[]{true, true, true}          // taupe
        );
        var bits = new BitSet(image.getWidth() * image.getHeight() * 3);

        var index = 0;
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                var pixelBits = colourMap.getOrDefault(image.getRGB(x, y), colourMap.get(-1));
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

    private static BufferedImage bytesToImage(byte[] bytes) {
        var bits = BitSet.valueOf(bytes);
        var colourMap = Map.of(
                List.of(false, false, false), black,
                List.of(false, false, true), white,
                List.of(false, true, false), green,
                List.of(false, true, true), blue,
                List.of(true, false, false), red,
                List.of(true, false, true), yellow,
                List.of(true, true, false), orange,
                List.of(true, true, true), taupe
        );
        return createImage(width, height, g -> {
            var index = 0;
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    var key = List.of(bits.get(index++), bits.get(index++), bits.get(index++));
                    g.setColor(colourMap.get(key));
                    g.drawRect(x, y, 1, 1);
                }
            }
        });
    }

    private static void saveImage(Path path, BufferedImage image) throws IOException {
        ImageIO.write(image, "png", path.toFile());
    }

    private static void saveBytes(Path path, byte[] bytes) throws IOException {
        Files.write(path, bytes);
    }
}
