package jinkyframe;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static jinkyframe.Colours.black;
import static jinkyframe.Colours.red;
import static jinkyframe.ImageUtils.*;

public final class DatePanel {
    private DatePanel() {
        // static methods
    }

    private static final DateTimeFormatter dayNumberFormatter = DateTimeFormatter.ofPattern("dd");
    private static final DateTimeFormatter monthFormatter = DateTimeFormatter.ofPattern("MMM");
    private static final DateTimeFormatter dayFormatter = DateTimeFormatter.ofPattern("EEE");

    private static final Font dayNumberFont = font("Lato-Black.ttf", 152.0f)/*.deriveFont(Font.BOLD)*/;
    private static final Font monthDayFont = font("Lato-Bold.ttf", 60.0f);

    public static BufferedImage generate(Info info, Margins margins) {
        var date = info.systemInfo().currentTime().toLocalDate();
        var dayNumber = generateDayNumber(date);
        var monthDay = generateMonthDay(date, dayNumber.getHeight(), margins);

        int width = 350;
        int height = 125;

        int monthDayX = dayNumber.getWidth() + ((width - dayNumber.getWidth() - monthDay.getWidth()) / 2) + 2;

        return createImage(width, height, g -> {
            g.drawImage(dayNumber, margins.left(), margins.top(), null);
            g.drawImage(monthDay, monthDayX, margins.top(), null);

            g.setPaint(Textures.dotFill(black));
            g.drawLine(margins.left(), height - 2, width - 2, height - 2);
            g.drawLine(margins.left() - 1, height - 1, width - 2, height - 1);
            g.drawLine(width - 1, margins.top() - 1, width - 1, height - 2);
            g.drawLine(width - 2, margins.top(), width - 2, height - 2);
        });
    }

    private static BufferedImage generateDayNumber(LocalDate date) {
        var dayNumber = dayNumberFormatter.format(date);
        return drawString(dayNumber, dayNumberFont, red);
    }

    private static BufferedImage generateMonthDay(LocalDate date, int height, Margins margins) {
        var month = monthFormatter.format(date).toUpperCase();
        var day = dayFormatter.format(date).toUpperCase();

        var monthImage = drawString(month, monthDayFont, black);
        var dayImage = drawString(day, monthDayFont, black);

        int width = 160;
        int monthX = (width - monthImage.getWidth()) / 2;
        int dayX = (width - dayImage.getWidth()) / 2;

        return createImage(width, height, g -> {
            g.drawImage(monthImage, monthX, margins.top(), null);
            g.drawImage(dayImage, dayX, height - margins.bottom() - dayImage.getHeight(), null);
            g.setPaint(Textures.dotFill(black));
            int mid = height / 2;
            for (int y = mid - 1; y <= mid + 1; y++) {
                g.drawLine(margins.left() * 2, y, width - margins.right() * 2, y);
            }
        });
    }
}
