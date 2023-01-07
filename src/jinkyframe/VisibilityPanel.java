package jinkyframe;

import java.awt.*;
import java.awt.image.BufferedImage;

import static jinkyframe.Colours.black;
import static jinkyframe.ImageUtils.*;

public final class VisibilityPanel {
    private VisibilityPanel() {
        // static methods
    }

    private static final Font font = font("Minecraftia-Regular.ttf", 8.0f);

    public static BufferedImage generate(Info info, Margins margins) {

        var currentWeather = info.weatherInfo().forecast().hourly().get(0);
        int visibility = currentWeather.visibility();

        var width = 53;
        return createImage(width, 42, g -> {
            g.setColor(black);
            g.setFont(font);

            var title = "Visibility";
            int titleWidth = g.getFontMetrics().stringWidth(title);
            g.drawString(title, diff(width, titleWidth), 22);

            var visibilityString = visibility == 10_000 ?
                    String.format(">%sm", visibility) :
                    String.format("%sm", visibility);
            int visibilityWidth = g.getFontMetrics().stringWidth(visibilityString);
            g.drawString(visibilityString, diff(width, visibilityWidth), 38);
        });
    }
}
