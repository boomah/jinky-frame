package jinkyframe;

public record Info(WifiInfo wifiInfo) {
    record WifiInfo(String networkName, String encryption, String password, String speed) {

    }
}
