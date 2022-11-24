package jinkyframe;

import java.time.*;

public final class Utils {
    private Utils() {
        // static methods
    }

    public static <T> T unsafe(UnsafeSupplier<T> supplier) {
        try {
            return supplier.get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public interface UnsafeSupplier<T> {
        T get() throws Exception;
    }

    public static LocalDate unixDtToLocalDate(long unixDt, ZoneId zoneId) {
        return unixDtToLocalDateTime(unixDt, zoneId).toLocalDate();
    }

    public static LocalDateTime unixDtToLocalDateTime(long unixDt, ZoneId zoneId) {
        return ZonedDateTime.ofInstant(Instant.ofEpochSecond(unixDt), zoneId).toLocalDateTime();
    }
}
