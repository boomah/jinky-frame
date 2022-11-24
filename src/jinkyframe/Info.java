package jinkyframe;

import java.time.*;

public record Info(DateInfo dateInfo, WeatherInfo weatherInfo, WifiInfo wifiInfo, SystemInfo systemInfo) {

    record DateInfo(LocalDate date) {
    }

    record WifiInfo(String networkName, String encryption, String password, String speed) {
    }

    record SystemInfo(LocalDateTime nextUpdate, LocalDateTime currentTime, String location, ZoneId zoneId,
                      String battery, String status) {
    }

    record WeatherInfo(WeatherForecast forecast) {
    }
}
