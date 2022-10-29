package jinkyframe;

import java.time.*;

public record Info(DateInfo dateInfo, WifiInfo wifiInfo, SystemInfo systemInfo) {

    record DateInfo(LocalDate date) {

    }

    record WifiInfo(String networkName, String encryption, String password, String speed) {

    }

    record SystemInfo(LocalDateTime nextUpdate, LocalDateTime previousUpdate, String location, ZoneId zoneId,
                      String battery, String status) {

    }
}
