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

    public static final Map<String, String> ICON_MAP = Map.ofEntries(
            // day
            entry("01d", "\uF00D"), // clear sky                
            entry("02d", "\uF002"), // few clouds               
            entry("03d", "\uF041"), // scattered clouds         
            entry("04d", "\uF013"), // broken / overcast clouds 
            entry("09d", "\uF009"), // drizzle / shower         
            entry("10d", "\uF019"), // rain                     
            entry("11d", "\uF01E"), // thunderstorm             
            entry("13d", "\uF076"), // snow                     
            entry("50d", "\uF014"), // mist                     

            // night
            entry("01n", "\uF02E"), // clear sky                
            entry("02n", "\uF086"), // few clouds               
            entry("03n", "\uF041"), // scattered clouds         
            entry("04n", "\uF013"), // broken / overcast clouds 
            entry("09n", "\uF029"), // drizzle / shower         
            entry("10n", "\uF019"), // rain                     
            entry("11n", "\uF01E"), // thunderstorm             
            entry("13n", "\uF076"), // snow                     
            entry("50n", "\uF014")  // mist                     
    );
    
    private static Map.Entry<String, String> entry(String key, String value) {
        return new AbstractMap.SimpleEntry<>(key, value);
    }
}
