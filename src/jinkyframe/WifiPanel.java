package jinkyframe;

import java.awt.*;
import java.awt.image.BufferedImage;

import static java.lang.Math.max;
import static jinkyframe.Colours.black;
import static jinkyframe.ImageUtils.*;

public final class WifiPanel {
    private WifiPanel() {
        // static methods
    }

    private static final Font lineFont = font("line_pixel-7.ttf", 14.0f);

    public static BufferedImage generate(Info info, Margins margins) throws Exception {
        var wifiInfo = info.wifiInfo();

        var wifi = drawString("WIFI", lineFont, black);
        var wifiSpeed = drawString(wifiInfo.speed(), lineFont, black);

        var qrSize = 66;
        var qrCode = QrCode.generate(wifiInfo, qrSize, qrSize);

        var padding = 3;
        var textHeight = max(wifi.getHeight(), wifiSpeed.getHeight());
        var width = qrCode.getWidth() + margins.left() + margins.right();
        var height = qrCode.getHeight() + margins.top() + margins.bottom() + textHeight + padding;

        return createImage(width, height, g -> {
            g.drawImage(wifi, margins.left(), margins.top(), null);
            g.drawImage(wifiSpeed, width - wifiSpeed.getWidth() - margins.right(), margins.top(), null);
            g.drawImage(qrCode, margins.left(), margins.top() + textHeight + padding, null);
        });
    }
}
