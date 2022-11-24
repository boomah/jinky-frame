package jinkyframe;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.time.format.DateTimeFormatter;

import static java.lang.String.*;
import static jinkyframe.Colours.black;
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

        var status = string(systemInfo.status());

        var topY = height - margins.bottom() - h * 4 - 12;
        var statusBoxWidth = width - margins.left() - margins.right();
        var statusBoxHeight = topY - margins.top() - margins.bottom();

        var statusX = margins.left() + diff(statusBoxWidth, status.getWidth());
        var statusY = margins.top() + diff(statusBoxHeight, status.getHeight());

        System.out.println("! " + statusBoxHeight + " " + status.getHeight() + " " + statusY);

        var x = margins.left();

        return createImage(width, height, g -> {
            g.setColor(black);

            g.drawRect(margins.left(), margins.top(), statusBoxWidth, statusBoxHeight);
            g.drawImage(status, statusX, statusY, null);

            g.drawImage(battery, x, topY, null);
            g.drawImage(timeZone, x, height - margins.bottom() - h * 3 - 8, null);
            g.drawImage(location, x, height - margins.bottom() - h * 2 - 4, null);
            g.drawImage(update, x, height - margins.bottom() - h, null);
        });
    }

    private static BufferedImage string(String string) {
        return drawString(string, font, black);
    }
}
