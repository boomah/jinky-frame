package jinkyframe;

import java.awt.*;
import java.awt.image.BufferedImage;

import static jinkyframe.Colours.black;
import static jinkyframe.ImageUtils.*;

public final class HumidityPanel {
    private HumidityPanel() {
        // static methods
    }

    private static final Font font = font("Minecraftia-Regular.ttf", 8.0f);

    public static BufferedImage generate(Info info, Margins margins) {

        var currentWeather = info.weatherInfo().forecast().hourly().get(0);
        int humidity = currentWeather.humidity();

        var width = 53;
        return createImage(width, 42, g -> {
            g.setColor(black);
            g.setFont(font);

            var title = "Humidity";
            int titleWidth = g.getFontMetrics().stringWidth(title);
            g.drawString(title, diff(width, titleWidth), 22);

            var humidityPercentage = String.format("%s%%", humidity);
            int humidityWidth = g.getFontMetrics().stringWidth(humidityPercentage);
            g.drawString(humidityPercentage, diff(width, humidityWidth), 38);
        });
    }
}
