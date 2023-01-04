package jinkyframe;

import java.time.*;

public record Info(DateInfo dateInfo, WeatherInfo weatherInfo, TideInfo tideInfo, WifiInfo wifiInfo,
                   SystemInfo systemInfo) {

    record DateInfo(LocalDate date) {
    }

    record WifiInfo(String networkName, String encryption, String password, String speed) {
    }

    record SystemInfo(LocalDateTime nextUpdate, LocalDateTime currentTime, String location, ZoneId zoneId,
                      String battery, String status) {

        public ZonedDateTime currentZonedDateTime() {
            return currentTime.atZone(zoneId);
        }
    }

    record WeatherInfo(WeatherForecast forecast) {
    }

    record TideInfo(Tides tides) {
    }
}
