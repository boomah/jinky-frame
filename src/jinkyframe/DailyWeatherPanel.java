package jinkyframe;

import jinkyframe.WeatherForecast.DayWeather;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import static jinkyframe.Colours.black;
import static jinkyframe.ImageUtils.*;
import static jinkyframe.Utils.unixDtToLocalDate;
import static jinkyframe.WeatherForecast.ICON_MAP;

public final class DailyWeatherPanel {
    private DailyWeatherPanel() {
        // static methods
    }

    private static final DateTimeFormatter dayFormatter = DateTimeFormatter.ofPattern("EEE");

    private static final Font dayFont = font("Lato-Black.ttf", 24.0f);
    private static final Font iconFont = font("weathericons-regular-webfont.ttf", 20.0f);
    private static final Font temperatureFont = dayFont.deriveFont(12.0f);

    public static BufferedImage generate(Info info, Margins margins) {
        var zoneId = info.systemInfo().zoneId();

        int width = 180;
        int height = 112;

        return createImage(width, height, g -> {
            int x = 3;
            int panelWidth = (width - x) / 3;
            for (int i = 1; i < 4; i++) {
                var dayWeather = info.weatherInfo().forecast().daily().get(i);
                var dayImage = generateDailyIcon(dayWeather, panelWidth, height, zoneId);
                g.drawImage(dayImage, x, 0, null);
                x += dayImage.getWidth();
            }

            g.setPaint(Textures.dotFill(black));
            g.drawLine(0, 3, 0, height - 4);

            g.drawLine(0, 2, 174, 2);
            g.drawLine(0, 3, 174, 3);

            g.drawLine(0, height - 4, 174, height - 4);
            g.drawLine(0, height - 3, 174, height - 3);
        });
    }

    private static BufferedImage generateDailyIcon(DayWeather dayWeather, int width, int height, ZoneId zoneId) {
        var weather = dayWeather.weather().get(0);

        var day = dayFormatter.format(unixDtToLocalDate(dayWeather.dt(), zoneId)).toUpperCase();
        var dayImage = drawString(day, dayFont, black);
        int dayX = diff(width, dayImage.getWidth()) - 1;

        var iconDetails = ICON_MAP.get(weather.icon());
        var font = iconFont.deriveFont(iconDetails.size() / 4.0f);
        var iconImage = drawIcon(iconDetails.text(), font);
        int iconX = diff(width, iconImage.getWidth()) - 1;
        int iconY = diff(height, iconImage.getHeight());

        var high = drawString(Math.round(dayWeather.temp().max()) + "°", temperatureFont, black);
        var low = drawString(Math.round(dayWeather.temp().min()) + "°", temperatureFont, black);

        var h = drawString("H", temperatureFont, black);
        var l = drawString("L", temperatureFont, black);

        int hX = 16;

        var colon = drawString(" : ", temperatureFont, black);
        int colonX = diff(width, colon.getWidth()) - 3;

        int lowY = 84;
        int highY = 95;

        return createImage(width, height, g -> {
            g.drawImage(dayImage, dayX, 10, null);
            g.drawImage(iconImage, iconX, iconY, null);

            g.drawImage(l, hX + 1, lowY, null);
            g.drawImage(h, hX, highY, null);
            g.drawImage(colon, colonX, lowY + 2, null);
            g.drawImage(colon, colonX, highY + 2, null);
            g.drawImage(low, colonX + 4, lowY, null);
            g.drawImage(high, colonX + 4, highY, null);
        });
    }
}
