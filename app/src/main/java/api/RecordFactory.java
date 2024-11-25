package api;

/**
 * Factory class for creating Record objects.
 */
public class RecordFactory {
    public static Record createRecord(String year, String place, int amount) {
        validateYear(year);
        validateAmount(amount);
        return new Record(year, place, amount);
    }

    private static void validateYear(String year) {
        if (!year.matches("\\d+")) {
            throw new IllegalArgumentException("Year must be a valid number");
        }
    }

    private static void validateAmount(int amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Amount must be a positive number");
        }
    }
}
