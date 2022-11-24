package jinkyframe;

import java.util.*;

public record WeatherForecast(
        double lat,
        double lon,
        String timezone,
        int timezoneOffset,
        CurrentWeather current,
        List<MinuteWeather> minutely,
        List<HourWeather> hourly,
        List<DayWeather> daily
) {

    public record CurrentWeather(
            long dt,
            long sunrise,
            long sunset,
            double temp,
            double feelsLike,
            int pressure,
            int humidity,
            double dewPoint,
            double uvi,
            int clouds,
            int visibility,
            double windSpeed,
            int windDeg,
            List<Weather> weather
    ) {
    }

    public record Weather(int id, String main, String description, String icon) {
    }

    public record MinuteWeather(long dt, double precipitation) {
    }

    public record HourWeather(
            long dt,
            double temp,
            double feelsLike,
            int pressure,
            int humidity,
            double dewPoint,
            double uvi,
            int clouds,
            int visibility,
            double windSpeed,
            int windDeg,
            List<Weather> weather,
            double pop,
            Map<String, Double> rain
    ) {
    }

    public record DayWeather(
            long dt,
            long sunrise,
            long sunset,
            Temperature temp,
            FeelsLike feelsLike,
            int pressure,
            int humidity,
            double dewPoint,
            double windSpeed,
            double windDeg,
            List<Weather> weather,
            int clouds,
            double pop,
            double rain,
            double uvi
    ) {
    }

    public record Temperature(
            double day,
            double min,
            double max,
            double night,
            double eve,
            double morn
    ) {
    }

    public record FeelsLike(double day, double night, double eve, double more) {
    }

    public static final Map<String, IconDetails> ICON_MAP = Map.ofEntries(
            // day
            entry("01d", new IconDetails("\uF00D", 181.0f)), // clear sky                
            entry("02d", new IconDetails("\uF002", 143.0f)), // few clouds               
            entry("03d", new IconDetails("\uF041", 185.0f)), // scattered clouds         
            entry("04d", new IconDetails("\uF013", 173.0f)), // broken / overcast clouds 
            entry("09d", new IconDetails("\uF009", 143.0f)), // drizzle / shower         
            entry("10d", new IconDetails("\uF019", 185.0f)), // rain                     
            entry("11d", new IconDetails("\uF01E", 185.0f)), // thunderstorm             
            entry("13d", new IconDetails("\uF076", 255.0f)), // snow                     
            entry("50d", new IconDetails("\uF014", 155.0f)), // mist                     

            // night
            entry("01n", new IconDetails("\uF02E", 271.0f)), // clear sky                
            entry("02n", new IconDetails("\uF086", 177.0f)), // few clouds               
            entry("03n", new IconDetails("\uF041", 185.0f)), // scattered clouds         
            entry("04n", new IconDetails("\uF013", 173.0f)), // broken / overcast clouds 
            entry("09n", new IconDetails("\uF029", 176.0f)), // drizzle / shower         
            entry("10n", new IconDetails("\uF019", 185.0f)), // rain                     
            entry("11n", new IconDetails("\uF01E", 185.0f)), // thunderstorm             
            entry("13n", new IconDetails("\uF076", 255.0f)), // snow                     
            entry("50n", new IconDetails("\uF014", 155.0f))  // mist                     
    );
    
    private static Map.Entry<String, IconDetails> entry(String key, IconDetails value) {
        return new AbstractMap.SimpleEntry<>(key, value);
    }

    public record IconDetails(String text, float size) {
    }    
}
