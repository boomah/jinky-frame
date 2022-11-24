package jinkyframe;

import com.google.gson.Gson;

import java.io.IOException;
import java.net.URI;
import java.net.http.*;

public final class TideReader {
    private TideReader() {
        // static methods
    }

    private static final Gson gson = new Gson();

    public static Tides readTide(String stationId) throws IOException, InterruptedException {
        String address = String.format("https://easytide.admiralty.co.uk/Home/GetPredictionData?stationId=%s",
                stationId);
        var client = HttpClient.newHttpClient();
        var request = HttpRequest.newBuilder(URI.create(address)).build();
        var body = client.send(request, HttpResponse.BodyHandlers.ofString())
                .body();

        return gson.fromJson(body, Tides.class);
    }
}
