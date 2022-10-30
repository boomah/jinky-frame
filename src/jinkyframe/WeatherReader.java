package jinkyframe;

import com.google.gson.Gson;

import java.io.IOException;
import java.net.URI;
import java.net.http.*;

public final class WeatherReader {

    private WeatherReader() {
        // static methods
    }

    private static final Gson gson = new Gson();

    public static WeatherForecast readWeather(String lat, String lon, String id) throws IOException, InterruptedException {
        String address = String.format(
                "https://api.openweathermap.org/data/2.5/onecall?lat=%s&lon=%s&units=metric&exclude=&appid=%s",
                lat, lon, id);
        var client = HttpClient.newHttpClient();
        var request = HttpRequest.newBuilder(URI.create(address)).build();
        var body = client.send(request, HttpResponse.BodyHandlers.ofString())
                .body()
                .replace("timezone_offset", "timezoneOffset")
                .replace("feels_like", "feelsLike")
                .replace("dew_point", "dewPoint")
                .replace("wind_speed", "windSpeed")
                .replace("wind_deg", "windDeg");

        return gson.fromJson(body, WeatherForecast.class);
    }
}
