package jinkyframe;

import jinkyframe.WeatherForecast.DayWeather;

import java.awt.*;
import java.awt.image.BufferedImage;

import static jinkyframe.Colours.black;
import static jinkyframe.ImageUtils.*;
import static jinkyframe.WeatherForecast.ICON_MAP;

public final class WeatherSummeryPanel {
    private WeatherSummeryPanel() {
        // static methods
    }

    private static final Font iconFont = font("weathericons-regular-webfont.ttf", 20.0f);
    private static final Font textFont = font("Lato-Black.ttf", 40.0f);

    private static final int width = 250;
    private static final int height = 250;

    private static final int maxIconHeight = 204;

    public static BufferedImage generate(Info info, Margins margins) {
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
            var iconDetails = ICON_MAP.get(weather.icon());
//            var iconDetails = ICON_MAP.get("01d");
//            var iconDetails = ICON_MAP.get("09n");

            var icon = drawString(iconDetails.text(), iconFont.deriveFont(iconDetails.size()), black);

/*            if (ImageGenerator.DEBUG) {
                ICON_MAP.values().stream().sorted(Comparator.comparing(WeatherForecast.IconDetails::text)).forEach(details -> {
                    var iconX = drawString(details.text(), iconFont.deriveFont(details.size()), black);
                    System.out.println(details.text() + " : " + iconX.getWidth() + ", " + iconX.getHeight());
                });
            }*/

            var temperaturePanel = generateTemperaturePanel(dayWeather, margins);

            return createImage(width, height, g -> {
                g.drawImage(icon, diff(width, icon.getWidth()), margins.top(), null);
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
        var maxTemp = drawString(maxTempText, textFont, black);
        var minTemp = drawString(minTempText, textFont, black);

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
}
