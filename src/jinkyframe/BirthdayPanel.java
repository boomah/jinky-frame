package jinkyframe;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.time.temporal.ChronoUnit.DAYS;
import static jinkyframe.Colours.black;
import static jinkyframe.Colours.orange;
import static jinkyframe.ImageUtils.*;

public final class BirthdayPanel {
    private BirthdayPanel() {
        // static methods
    }

    private static final Font font = font("Minecraftia-Regular.ttf", 8.0f);
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MMM");

    public static BufferedImage generate(Info info, Margins margins) {
        var birthdays = info.birthdayInfo().birthdays();
        var currentDate = info.systemInfo().currentTime().toLocalDate();
        int currentDayOfYear = currentDate.getDayOfYear();
        int startIndex = IntStream.range(0, birthdays.size())
                .filter(i -> birthdays.get(i).day().getDayOfYear() > currentDayOfYear)
                .findFirst()
                .orElse(0);
        int number = 5;

        int width = 120;

        var title = string("Upcoming Birthdays");

        var birthdayStrings = IntStream.range(startIndex, startIndex + number)
                .mapToObj(i -> birthdays.get(i % birthdays.size()))
                .map(birthday -> {
                    var day = birthday.day();
                    var dayString = formatter.format(day);
                    int age = currentDate.getYear() - day.getYear();
                    var names = birthday.names().stream()
                            .map(name -> String.format("%s (%s)", name, age))
                            .collect(Collectors.joining(", "));
                    var extraSpace = dayString.contains("t") ? " " : "";
                    return String.format("%s %s:  %s", dayString, extraSpace, names);
                })
                .toList();
        var lines = birthdayStrings.stream().map(BirthdayPanel::string).collect(Collectors.toList());
        var nextBirthday = birthdays.get(startIndex);
        long daysUntilNext = daysUntilNext(currentDate, nextBirthday.day().withYear(currentDate.getYear()));
        boolean soon = daysUntilNext < 5;
        var nextBirthdayImage = border(lines.remove(0), 4, soon);

        return createImage(width, 86, g -> {
            g.drawImage(title, margins.left(), margins.top(), null);

            g.drawImage(nextBirthdayImage, margins.left(), title.getHeight() + 10, null);

            int y = title.getHeight() + 30;
            for (BufferedImage line : lines) {
                g.drawImage(line, margins.left() + 4, y, null);
                y += 12;
            }
        });
    }

    private static BufferedImage string(String string) {
        return drawString(string, font, black);
    }

    private static BufferedImage border(BufferedImage image, int size, boolean soon) {
        int width = image.getWidth() + size * 2;
        int height = image.getHeight() + size * 2;
        return createImage(width, height, g -> {
            if (soon) {
                g.setColor(orange);
                g.drawRect(1, 1, width - 3, height - 3);
                g.drawRect(2, 2, width - 5, height - 5);
                g.setColor(black);
                g.drawRect(0, 0, width - 1, height - 1);
            }
            g.drawImage(image, size, size, null);
        });
    }

    private static long daysUntilNext(LocalDate currentDate, LocalDate birthday) {
        long between = DAYS.between(currentDate, birthday);
        return between >= 0 ? between : daysUntilNext(currentDate, birthday.withYear(currentDate.getYear() + 1));
    }
}
