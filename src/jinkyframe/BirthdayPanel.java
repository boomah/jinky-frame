package jinkyframe;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static jinkyframe.Colours.black;
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
        int currentDay = currentDate.getDayOfYear();
        int startIndex = IntStream.range(0, birthdays.size())
                .filter(i -> birthdays.get(i).day().getDayOfYear() > currentDay)
                .findFirst()
                .orElse(0);
        int number = 5;

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
        var lines = birthdayStrings.stream().map(BirthdayPanel::string).toList();

        return createImage(120, 86, g -> {
            g.drawImage(title, margins.left(), margins.top(), null);
            int y = title.getHeight() + margins.top() * 3;
            for (BufferedImage line : lines) {
                g.drawImage(line, margins.left() + 2, y, null);
                y += 12;
            }
        });
    }

    private static BufferedImage string(String string) {
        return drawString(string, font, black);
    }
}
