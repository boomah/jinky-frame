package jinkyframe;

import java.time.*;
import java.util.List;

public record Info(DateInfo dateInfo, WeatherInfo weatherInfo, TideInfo tideInfo, BirthdayInfo birthdayInfo,
                   WifiInfo wifiInfo, SystemInfo systemInfo) {

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

    record BirthdayInfo(List<Birthday> birthdays) {
    }
}
