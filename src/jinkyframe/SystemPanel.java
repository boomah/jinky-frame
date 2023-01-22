package jinkyframe;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.time.format.DateTimeFormatter;

import static java.lang.String.format;
import static jinkyframe.ColourModels.whiteGreenColourModel;
import static jinkyframe.Colours.*;
import static jinkyframe.ImageUtils.*;

public final class SystemPanel {
    private SystemPanel() {
        // static methods
    }

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd MMM HH:mm");
    private static final DateTimeFormatter zoneIdFormatter = DateTimeFormatter.ofPattern("OOOO");

    private static final Font font = font("Minecraftia-Regular.ttf", 8.0f);

    public static BufferedImage generate(Info info, Margins margins) {
        var width = 210;
        int height = 86;
        var systemInfo = info.systemInfo();

        var battery = string("Battery        : " + systemInfo.battery());
        var zoneId = systemInfo.zoneId();
        var zoneIdText = zoneIdFormatter.format(zoneId.getRules().getOffset(systemInfo.currentTime()));
        var timeZone = string("Time Zone    : " + format("%s (%s)", zoneId, zoneIdText));
        var location = string("Location      : " + systemInfo.location());
        var nextUpdate = dateTimeFormatter.format(systemInfo.nextUpdate());
        var previousUpdate = dateTimeFormatter.format(systemInfo.currentTime());
        var update = string("Next/Prev : " + format("%s / %s", nextUpdate, previousUpdate));

        var h = update.getHeight();

        var topY = height - margins.bottom() - h * 4 - 12;
        var statusBoxWidth = width - margins.left() - margins.right();
        var statusBoxHeight = topY - margins.top() - margins.bottom();

        var status = drawStatus(systemInfo.status(), statusBoxWidth, statusBoxHeight);

        var x = margins.left();

        return createImage(width, height, g -> {
            g.drawImage(status, margins.left(), margins.top(), null);
            g.setColor(black);
            g.drawRect(margins.left(), margins.top(), statusBoxWidth, statusBoxHeight);

            g.drawImage(battery, x, topY, null);
            g.drawImage(timeZone, x, height - margins.bottom() - h * 3 - 8, null);
            g.drawImage(location, x, height - margins.bottom() - h * 2 - 4, null);
            g.drawImage(update, x, height - margins.bottom() - h, null);
        });
    }

    private static BufferedImage drawStatus(String status, int width, int height) {
        return createImage(width, height, whiteGreenColourModel, g -> {
            g.setColor(green);
            g.fillRect(0, 0, width, height);
            g.setColor(white);
            int stringWidth = g.getFontMetrics().stringWidth(status);
            var x = diff(width, stringWidth);
            g.drawString(status, x, 20);
        });
    }

    private static BufferedImage string(String string) {
        return drawString(string, font, black);
    }
}
