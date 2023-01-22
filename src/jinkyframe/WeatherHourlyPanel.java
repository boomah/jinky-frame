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

            var iconPanel = generateHourlyIcons(hourly, zoneId, margins);

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

    private static boolean checkHourSkip(HourWeather firstHour, ZoneId zoneId) {
        var hour = hour(firstHour, zoneId);
        return (hour != 4) && (hour != 14);
    }

    private static BufferedImage generateHourlyIcons(List<HourWeather> hourWeatherList, ZoneId zoneId, Margins margins) {
        return createImage(width, height, g -> {
            var x = 15;
            var counter = 0;

            for (HourWeather hourWeather : hourWeatherList) {
                var hour = hour(hourWeather, zoneId);
                if (counter == 4 && checkHourSkip(hourWeatherList.get(0), zoneId)) {
                    g.setPaint(Textures.dotFill(black));
                    int y1 = 30;
                    int y2 = 96;
                    g.drawLine(x, y1, x, y2);
                    x += 1;
                    g.drawLine(x, y1, x, y2);
                    x += 1;
                    g.drawLine(x, y1, x, y2);
                    x += 13;
                }
                g.setColor(black);

                var weather = hourWeather.weather().get(0);
                var iconDetails = ICON_MAP.get(weather.icon());
//                var iconDetails = ICON_MAP.get("01d");
//                var iconDetails = ICON_MAP.get("09d");
//                var iconDetails = ICON_MAP.get("11n");

                var font = iconFont.deriveFont(iconDetails.size() / 4.0f);
                var iconImage = drawIcon(iconDetails.text(), font);
                var iconWidth = iconImage.getWidth();

                g.setFont(hourFont);
                var formattedHour = String.format("%01d", hour);
                var hourImage = drawString(formattedHour, hourFont, black);
                var hourDiff = diff(iconWidth, hourImage.getWidth());
                g.drawImage(hourImage, x + hourDiff, 10, null);

                var iconY = diff(height, iconImage.getHeight());
                g.drawImage(iconImage, x, iconY, null);

                g.setFont(temperatureFont);
                var feelsLikeForWidth = Long.toString(Math.round(hourWeather.feelsLike()));
                var feelsLikeForWidthAbs = Long.toString(Math.abs(Math.round(hourWeather.feelsLike())));
                var feelsLikeWidth = drawString(feelsLikeForWidth, temperatureFont, black).getWidth();
                var feelsLikeWidthAbs = drawString(feelsLikeForWidthAbs, temperatureFont, black).getWidth();
                var temperatureX = diff(iconWidth, feelsLikeWidth) - ((feelsLikeWidth - feelsLikeWidthAbs) / 2);
                var feelsLike = Math.round(hourWeather.feelsLike()) + "°";
                var temperatureImage = drawString(feelsLike, temperatureFont, black);
                g.drawImage(temperatureImage, x + temperatureX, height - 26, null);

                x += 65;
                counter++;
            }
        });
    }
}
