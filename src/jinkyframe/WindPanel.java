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

            var windIcon = windIcons.get(windIndex);
            var iconImage = drawString(windIcon.code, iconFont, black);
            int iconX = diff(width, iconImage.getWidth());
            int iconY = (height - windY) - windIcon.heightOffset;
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

    private static final List<WindIcon> windIcons = Arrays.asList(
            new WindIcon("\uF058", 2),
            new WindIcon("\uF057", -2),
            new WindIcon("\uF04D", -2),
            new WindIcon("\uF088", -2),
            new WindIcon("\uF044", 2),
            new WindIcon("\uF043", -2),
            new WindIcon("\uF048", -2),
            new WindIcon("\uF087", -2)
    );

    private record WindIcon(String code, int heightOffset) {
    }
}
