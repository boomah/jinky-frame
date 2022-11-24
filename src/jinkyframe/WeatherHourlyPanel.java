package jinkyframe;

import jinkyframe.WeatherForecast.HourWeather;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.time.Instant;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

import static jinkyframe.Colours.black;
import static jinkyframe.ImageUtils.*;
import static jinkyframe.WeatherForecast.ICON_MAP;

public final class WeatherHourlyPanel {
    private WeatherHourlyPanel() {
        // static methods
    }

    private static final Font iconFont = font("weathericons-regular-webfont.ttf", 20.0f);
    private static final Font hourFont = font("Lato-Black.ttf", 24.0f);
    private static final Font temperatureFont = font("Lato-Black.ttf", 24.0f);

    private static final int width = 350;
    private static final int height = 125;

    public static BufferedImage generate(Info info, ImageUtils.Margins margins) {
        try {
            var zoneId = info.systemInfo().zoneId();
            var forecast = info.weatherInfo().forecast();
            var hourly = hourlyWeather(forecast, zoneId);

            var iconPanel = generateHourlyIcons(hourly, zoneId);

            return createImage(width, height, g -> g.drawImage(iconPanel, 0, 0, null));
        } catch (Exception e) {
            e.printStackTrace();
            return createImage(width, height, g -> {
                g.setColor(black);
                g.drawString(e.getMessage(), margins.left(), 50);
            });
        }
    }

    private static List<HourWeather> hourlyWeather(WeatherForecast forecast, ZoneId zoneId) {
        var hourly = forecast.hourly();
        var firstHour = hour(hourly.get(0), zoneId);

        int extraHour;
        if (firstHour == 4 || firstHour == 14) {
            extraHour = -1;
        } else if (firstHour > 14 || firstHour < 4) {
            extraHour = 8;
        } else {
            extraHour = 18;
        }

        if (extraHour == -1) {
            return hourly.stream().limit(5).toList();
        } else {
            var hourlyWeather = hourly.stream().limit(4).collect(Collectors.toList());
            var extraWeather = hourly.stream().filter(hourWeather -> hour(hourWeather, zoneId) == extraHour).findFirst();
            extraWeather.ifPresent(hourlyWeather::add);
            return hourlyWeather;
        }
    }

    private static int hour(HourWeather hourWeather, ZoneId zoneId) {
        return Instant.ofEpochSecond(hourWeather.dt()).atZone(zoneId).getHour();
    }

    private static BufferedImage generateHourlyIcons(List<HourWeather> hourWeatherList, ZoneId zoneId) {
        var colour = black;
        return createImage(width, height, ColourModels.get(colour), g -> {
            var x = 15;
            var y = 80;
            var previousHour = -1;

            for (HourWeather hourWeather : hourWeatherList) {
                var hour = hour(hourWeather, zoneId);
                if ((previousHour != -1) && (hour != (previousHour + 1))) {
                    g.setPaint(Textures.dotFill(black));
                    int y1 = 35;
                    int y2 = 95;
                    g.drawLine(x, y1, x, y2);
                    x += 1;
                    g.drawLine(x, y1, x, y2);
                    x += 1;
                    g.drawLine(x, y1, x, y2);
                    x += 13;
                } else {
                    previousHour = hour;
                }
                g.setColor(colour);

                var weather = hourWeather.weather().get(0);
                var iconDetails = ICON_MAP.get(weather.icon());
                var font = iconFont.deriveFont(iconDetails.size() / 4.0f);
                g.setFont(font);
                var iconWidth = g.getFontMetrics().stringWidth(iconDetails.text());
                g.drawString(iconDetails.text(), x, y);

                g.setFont(hourFont);
                var formattedHour = String.format("%01d", hour);

                var hourTextWidth = g.getFontMetrics().stringWidth(formattedHour);
                var hourDiff = diff(iconWidth, hourTextWidth);

                g.drawString(formattedHour, x + hourDiff, y - 50);

                g.setFont(temperatureFont);

                var feelsLikeForWidth = Long.toString(Math.abs(Math.round(hourWeather.feelsLike())));
                var feelsLikeWidth = g.getFontMetrics().stringWidth(feelsLikeForWidth);
                var feelsLikeDiff = diff(iconWidth, feelsLikeWidth);

                var feelsLike = Math.round(hourWeather.feelsLike()) + "\u00B0";

                g.drawString(feelsLike, x + feelsLikeDiff, y + 35);

                x += 65;
            }
        });
    }
}
