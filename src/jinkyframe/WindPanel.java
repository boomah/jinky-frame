package jinkyframe;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.List;

import static jinkyframe.Colours.black;
import static jinkyframe.ImageUtils.*;

public final class WindPanel {
    private WindPanel() {
        // static methods
    }

    private static final Font iconFont = font("weathericons-regular-webfont.ttf", 65.0f);
    private static final Font font = font("Minecraftia-Regular.ttf", 8.0f);

    public static BufferedImage generate(Info info, Margins margins) {

        var currentWeather = info.weatherInfo().forecast().hourly().get(0);
        int windDeg = currentWeather.windDeg();
        int windIndex = Math.round(((windDeg + 180.0f) % 360) / 45);
        long windSpeedMph = Math.round(currentWeather.windSpeed() * 2.23694);

        int width = 53;
        int height = 70;
        return createImage(width, height, g -> {
            int windY = 33;

            var iconImage = drawString(windIcons.get(windIndex), iconFont, black);
            int iconX = diff(width, iconImage.getWidth());
            int iconY = (height - windY) - 2;
            g.drawImage(iconImage, iconX, iconY, null);

            g.setColor(black);
            g.setFont(font);

            var title = "Wind";
            int titleWidth = g.getFontMetrics().stringWidth(title);
            g.drawString(title, diff(width, titleWidth), 18);

            var windSpeed = String.format("%s mph", windSpeedMph);
            int windWidth = g.getFontMetrics().stringWidth(windSpeed);
            g.drawString(windSpeed, diff(width, windWidth), windY);
        });
    }

    private static final List<String> windIcons = Arrays.asList(
            "\uF058",
            "\uF057",
            "\uF04D",
            "\uF088",
            "\uF044",
            "\uF043",
            "\uF048",
            "\uF087"
    );
}
