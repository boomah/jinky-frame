package jinkyframe;

import java.time.LocalDate;
import java.util.List;

public record Birthday(LocalDate day, List<String> names) {
}
