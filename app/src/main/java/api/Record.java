
package api;

/**
 * Represents a single record with year, place, and amount.
 */
public class Record {
    private final String year;
    private final String place;
    private final int amount;

    // Constructor is private; use the factory for creating records
    Record(String year, String place, int amount) {
        this.year = year;
        this.place = place;
        this.amount = amount;
    }

    public String getYear() {
        return year;
    }

    public String getPlace() {
        return place;
    }

    public int getAmount() {
        return amount;
    }

    @Override
    public String toString() {
        return "Record{" +
                "year=" + year +
                ", place='" + place +
                ", amount=" + amount +
                '}';
    }
}
    