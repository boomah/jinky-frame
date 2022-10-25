package jinkyframe;

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
}
