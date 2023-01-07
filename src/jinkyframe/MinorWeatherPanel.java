package jinkyframe;

import java.awt.*;
import java.awt.image.BufferedImage;

import static jinkyframe.Colours.black;
import static jinkyframe.ImageUtils.*;

public final class MinorWeatherPanel {
    private MinorWeatherPanel() {
        // static methods
    }

    private static final int width = 53;

    private static final Font font = font("Minecraftia-Regular.ttf", 8.0f);

    public static BufferedImage generate(Info info, ImageUtils.Margins margins) {
        var currentWeather = info.weatherInfo().forecast().hourly().get(0);

        int clouds = currentWeather.clouds();
        double probabilityOfPerception = currentWeather.pop() * 100;
        int humidity = currentWeather.humidity();
        int pressure = currentWeather.pressure();

        return createImage(width, 112, g -> {
            g.setColor(black);
            g.setFont(font);

            string("Clouds", 18, g);
            string(String.format("%s %%", clouds), 30, g);
            string("Rain % 1h", 44, g);
            string(String.format("%s %%", probabilityOfPerception), 56, g);
            string("Humidity", 70, g);
            string(String.format("%s %%", humidity), 82, g);
            string("Pressure", 96, g);
            string(String.format("%shPa", pressure), 108, g);
        });
    }

    private static void string(String title, int y, Graphics2D g) {
        int titleWidth = g.getFontMetrics().stringWidth(title);
        g.drawString(title, diff(width, titleWidth), y);
    }
}
