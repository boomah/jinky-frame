package jinkyframe;

import java.time.LocalDate;

public record Info(DateInfo dateInfo, WifiInfo wifiInfo) {

    record DateInfo(LocalDate date) {

    }

    record WifiInfo(String networkName, String encryption, String password, String speed) {

    }
}
