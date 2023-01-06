package jinkyframe;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.math.BigDecimal;

import static jinkyframe.Colours.black;
import static jinkyframe.ImageUtils.*;

public final class MoonPanel {
    private MoonPanel() {
        // static methods
    }

    private static final Font iconFont = font("weathericons-regular-webfont.ttf", 65.0f);
    private static final Font font = font("Minecraftia-Regular.ttf", 8.0f);

    public static BufferedImage generate(Info info, Margins margins) {
        long currentEpochSecond = info.systemInfo().currentZonedDateTime().toEpochSecond();
        var moonCode = moonCode(currentEpochSecond);

        int width = 53 + margins.width();
        int height = 70 + margins.height();
        return createImage(width, height, g -> {
            var iconImage = drawString(moonCode, iconFont, black);
            int iconX = diff(width, iconImage.getWidth());
            int iconY = height - margins.bottom() - iconImage.getHeight();
            g.drawImage(iconImage, iconX, iconY, null);

            g.setColor(black);
            g.setFont(font);

            var title = "Moon";
            int titleWidth = g.getFontMetrics().stringWidth(title);
            g.drawString(title, diff(width, titleWidth), 18);
        });
    }

    private static String moonCode(long currentEpochSecond) {
        long newMoonDate = 1263539460L; // known new moon date (2010-01-15 07:11 UTC)
        var fullPhase = new BigDecimal(String.valueOf((currentEpochSecond - newMoonDate) / 2551442.777777664));
        double phase = fullPhase.subtract(new BigDecimal(fullPhase.intValue())).doubleValue();
        int iconNumber = ((int) Math.round(phase * 28)) % 28;
        char iconChar = (char) (0xF095 + iconNumber);
        return Character.toString(iconChar);
    }
}
