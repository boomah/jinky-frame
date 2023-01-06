package jinkyframe;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public final class BirthdayReader {
    private BirthdayReader() {
        // static methods
    }

    private static final Gson gson = new Gson();
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    public static List<Birthday> readBirthdays() throws IOException {
        var birthdaysJson = Files.readString(Path.of("secrets/birthdays.json"));
        var nameType = TypeToken.getParameterized(List.class, String.class).getType();
        var mapType = TypeToken.getParameterized(Map.class, String.class, nameType).getType();
        Map<String, List<String>> birthdayMap = gson.fromJson(birthdaysJson, mapType);

        return birthdayMap.entrySet().stream()
                .map(e -> new Birthday(LocalDate.parse(e.getKey(), formatter), e.getValue()))
                .sorted(Comparator.comparingInt(birthday -> birthday.day().getDayOfYear()))
                .toList();
    }
}
