package jinkyframe;

import java.time.*;
import java.util.List;

public record Tides(List<TidalHeightOccurrence> tidalHeightOccurrenceList) {

    public record TidalHeightOccurrence(String dateTime, double height) {
        public LocalDateTime localDateTime(ZoneId zoneId) {
            var instant = Instant.parse(dateTime);
            return instant.atZone(zoneId).toLocalDateTime();
        }
    }
}
