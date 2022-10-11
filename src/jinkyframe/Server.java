package jinkyframe;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.time.Instant;

public final class Server {
    public static void main(String[] args) throws IOException {
        var info = ImageGenerator.loadInfo();
        var server = HttpServer.create(new InetSocketAddress(8080), 0);
        server.createContext("/jinky", exchange -> {
            try {
                System.out.println(Instant.now() + ": generate image");
                var bytes = generateImageBytes(info);
                System.out.println(Instant.now() + ": image generated, " + bytes.length + " bytes");
                exchange.sendResponseHeaders(200, bytes.length);
                var body = exchange.getResponseBody();
                body.write(bytes);
                body.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        server.start();
    }

    private static byte[] generateImageBytes(Info info) {
        try {
            return ImageGenerator.generateImageBytes(info);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
