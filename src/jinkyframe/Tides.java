package jinkyframe;

import java.util.List;

public record Tides(List<TidalHeightOccurrence> tidalHeightOccurrenceList) {

    public record TidalHeightOccurrence(String dateTime, double height) {
    }
}
