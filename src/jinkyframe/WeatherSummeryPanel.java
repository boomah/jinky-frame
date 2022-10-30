package jinkyframe;

import java.awt.*;
import java.awt.image.BufferedImage;

import static jinkyframe.Colours.black;
import static jinkyframe.ImageUtils.*;
import static jinkyframe.WeatherForecast.ICON_MAP;

public final class WeatherSummeryPanel {
    private WeatherSummeryPanel() {
        // static methods
    }

    private static final Font iconFont = font("weathericons-regular-webfont.ttf", 100.0f);

    public static BufferedImage generate(Info info, Margins margins) {
        int width = 250;
        int height = 250;

        try {
            var today = info.dateInfo().date();
            var zoneId = info.systemInfo().zoneId();
            var forecast = info.weatherInfo().forecast();
            var currentWeather = forecast.current().weather().get(0);
            var dayWeather = forecast.daily().stream()
                    .filter(weather -> today.equals(Utils.unixDtToLocalDate(weather.dt(), zoneId)))
                    .findFirst()
                    .orElseThrow();

            var weather = dayWeather.weather().get(0);
            var iconText = ICON_MAP.get(weather.icon());
//            var iconText = ICON_MAP.get("01d");
//            var iconText = ICON_MAP.get("09d");

            var icon = drawString(iconText, iconFont, black);

            return createImage(width, height, g -> {
                g.drawImage(icon, margins.left(), margins.top(), null);
            });
        } catch (Exception e) {
            return createImage(width, height, g -> {
                g.setColor(black);
                g.drawString(e.getMessage(), margins.left(), margins.top());
            });
        }
    }
}
