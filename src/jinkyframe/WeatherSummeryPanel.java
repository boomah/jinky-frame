package jinkyframe;

import jinkyframe.WeatherForecast.DayWeather;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;

import static jinkyframe.Colours.black;
import static jinkyframe.ImageUtils.*;
import static jinkyframe.Utils.unixDtToLocalDate;
import static jinkyframe.Utils.unixDtToLocalDateTime;
import static jinkyframe.WeatherForecast.ICON_MAP;

public final class WeatherSummeryPanel {
    private WeatherSummeryPanel() {
        // static methods
    }

    private static final Font iconFont = font("weathericons-regular-webfont.ttf", 20.0f);
    private static final Font sunEventFont = iconFont.deriveFont(20.0f);
    private static final Font temperatureFont = font("Lato-Black.ttf", 40.0f);
    private static final Font timeFont = temperatureFont.deriveFont(14.0f);

    private static final int width = 250;
    private static final int height = 250;

    private static final int maxIconHeight = 204;

    private static final String sunriseIcon = "\uF051";
    private static final String sunsetIcon = "\uF052";

    public static BufferedImage generate(Info info, Margins margins) {
        try {
            var today = info.dateInfo().date();
            var zoneId = info.systemInfo().zoneId();
            var forecast = info.weatherInfo().forecast();
            var currentWeather = forecast.current().weather().get(0);
            var dayWeather = forecast.daily().stream()
                    .filter(weather -> today.equals(unixDtToLocalDate(weather.dt(), zoneId)))
                    .findFirst()
                    .orElseThrow();
            var tomorrowDayWeather = forecast.daily().stream()
                    .filter(weather -> today.plusDays(1).equals(unixDtToLocalDate(weather.dt(), zoneId)))
                    .findFirst()
                    .orElseThrow();

            var updateTime = info.systemInfo().currentTime();
            var sunriseToday = unixDtToLocalDateTime(dayWeather.sunrise(), zoneId);
            var sunsetToday = unixDtToLocalDateTime(dayWeather.sunset(), zoneId);
            var sunriseTomorrow = unixDtToLocalDateTime(tomorrowDayWeather.sunrise(), zoneId);
            var sunsetTomorrow = unixDtToLocalDateTime(tomorrowDayWeather.sunset(), zoneId);
            SunEvent sunEvent1, sunEvent2;
            if (updateTime.isAfter(sunriseToday) && updateTime.isAfter(sunsetToday)) {
                sunEvent1 = new SunEvent(sunriseIcon, sunriseTomorrow);
                sunEvent2 = new SunEvent(sunsetIcon, sunsetTomorrow);
            } else if (updateTime.isAfter(sunriseToday)) {
                sunEvent1 = new SunEvent(sunsetIcon, sunsetToday);
                sunEvent2 = new SunEvent(sunriseIcon, sunriseTomorrow);
            } else {
                sunEvent1 = new SunEvent(sunriseIcon, sunriseToday);
                sunEvent2 = new SunEvent(sunsetIcon, sunsetToday);
            }

            var sunEventPanel1 = drawString(sunEvent1.icon, sunEventFont, black);
            var sunEventTimePanel1 = drawString(sunEvent1.timeString(), timeFont, black);
            var sunEventPanel2 = drawString(sunEvent2.icon, sunEventFont, black);
            var sunEventTimePanel2 = drawString(sunEvent2.timeString(), timeFont, black);
            var sunEventY = maxIconHeight - Math.min(sunEventPanel1.getHeight(), sunEventPanel2.getHeight());

            var weather = dayWeather.weather().get(0);
            var iconDetails = ICON_MAP.get(weather.icon());
//            var iconDetails = ICON_MAP.get("01n");
//            var iconDetails = ICON_MAP.get("13n");
//            var iconDetails = ICON_MAP.get("09n");

            var icon = drawString(iconDetails.text(), iconFont.deriveFont(iconDetails.size()), black);

            if (ImageGenerator.DEBUG) {
                ICON_MAP.values().stream().sorted(Comparator.comparing(WeatherForecast.IconDetails::text)).forEach(details -> {
                    var iconX = drawString(details.text(), iconFont.deriveFont(details.size() / 4.0f), black);
                    System.out.println(details.text() + " : " + iconX.getWidth() + ", " + iconX.getHeight());
                });
            }

            var temperaturePanel = generateTemperaturePanel(dayWeather, margins);

            return createImage(width, height, g -> {
                g.drawImage(icon, diff(width, icon.getWidth()), margins.top(), null);
                g.drawImage(sunEventPanel1, margins.left(), sunEventY, null);
                var time1X = margins.left() + sunEventPanel1.getWidth() + 5;
                g.drawImage(sunEventTimePanel1, time1X, sunEventY + 6, null);
                var time2X = width - margins.right() - sunEventPanel2.getWidth() - 5 - sunEventTimePanel2.getWidth();
                g.drawImage(sunEventTimePanel2, time2X, sunEventY + 6, null);
                g.drawImage(sunEventPanel2, time2X + 5 + sunEventTimePanel2.getWidth(), sunEventY, null);
                g.drawImage(temperaturePanel, 0, margins.top() + maxIconHeight, null);
            });
        } catch (Exception e) {
            e.printStackTrace();
            return createImage(width, height, g -> {
                g.setColor(black);
                g.drawString(e.getMessage(), margins.left(), 50);
            });
        }
    }

    private static BufferedImage generateTemperaturePanel(DayWeather dayWeather, Margins margins) {
        var maxTempText = "H:" + Math.round(dayWeather.temp().max()) + "\u00B0";
        var minTempText = "L:" + Math.round(dayWeather.temp().min()) + "\u00B0";
        var maxTemp = drawString(maxTempText, temperatureFont, black);
        var minTemp = drawString(minTempText, temperatureFont, black);

        var temperatureImage = cropImage(createImage(300, 300, g -> {
            g.drawImage(maxTemp, 0, 0, null);
            g.drawImage(minTemp, maxTemp.getWidth() + 15, 0, null);
        }));

        var temperatureHeight = height - maxIconHeight - margins.top();

        var x = diff(width, temperatureImage.getWidth());
        var y = diff(temperatureHeight, temperatureImage.getHeight());

        return createImage(width, temperatureHeight, g -> {
            g.drawImage(temperatureImage, x, y, null);
        });
    }

    record SunEvent(String icon, LocalDateTime time) {
        String timeString() {
            return time.toLocalTime().truncatedTo(ChronoUnit.MINUTES).toString();
        }
    }
}
