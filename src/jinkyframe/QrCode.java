package jinkyframe;

import com.google.zxing.*;
import com.google.zxing.qrcode.QRCodeWriter;

import java.awt.image.BufferedImage;
import java.util.Map;

import static jinkyframe.Colours.blue;
import static jinkyframe.ImageUtils.createImage;

public final class QrCode {
    private QrCode() {
        // static methods
    }

    public static BufferedImage generate(Info.WifiInfo info, int width, int height) throws WriterException {
        var content = String.format("WIFI:S:%s;T:%s;P:%s;;",
                info.networkName(), info.encryption(), info.password());
        var writer = new QRCodeWriter();
        var hints = Map.of(EncodeHintType.MARGIN, 0);
        var matrix = writer.encode(content, BarcodeFormat.QR_CODE, width, height, hints);

        return createImage(width, height, g -> {
            g.setColor(blue);
            for (int x = 0; x < matrix.getWidth(); x++) {
                for (int y = 0; y < matrix.getHeight(); y++) {
                    if (matrix.get(x, y)) {
                        g.fillRect(x, y, 1, 1);
                    }
                }
            }
        });
    }
}
