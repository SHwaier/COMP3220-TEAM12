package api;

/**
 * Used to store the data of a single record
 * Data includes the year, place and the amount of power
 * generated/exported/imported
 * 
 * @see RecordsCentral
 */
public class Record {
    private String year;
    private String place;
    private int amount;

    /**
     * Constructor for the Record class
     * 
     * @param year   the year of the record
     * @param place  to which part of the world the record belongs (could be a
     *               country or a region)
     * @param amount the amount of power generated/exported/imported
     */
    public Record(String year, String place, int amount) {
        this.setYear(year);
        this.setPlace(place);
        this.setAmount(amount);
    }

    /**
     * Constructor for the Record class
     * 
     * @param place  the place of the record
     * @param amount the amount of power generated/exported
     */
    public Record(String place, int amount) {
        this.setPlace(place);
        this.setAmount(amount);
        this.year = "";
    }

    /**
     * Get the place of the record
     * 
     * @return the place of the record
     */
    public String getPlace() {
        return place;
    }

    /**
     * Set the place of the record
     * 
     * @param place the place of the record
     */
    public void setPlace(String place) {
        this.place = place;
    }

    /**
     * Get the year of the record
     * 
     * @return the year of the record
     */
    public String getYear() {
        return year;
    }

    /**
     * Set the year of the record
     * 
     * @param year must be a valid year number in string format
     */
    public void setYear(String year) {
        if (!year.matches("[0-9]+")) {
            throw new IllegalArgumentException("Year must be a number");
        }
        this.year = year;
    }

    /**
     * Get the amount
     * 
     * @return the amount
     */
    public int getAmount() {
        return amount;
    }

    /**
     * Set the amount of power generated/exported
     * 
     * @param amount
     */
    public void setAmount(int amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Amount must be a positive number");
        }
        this.amount = amount;
    }

    /**
     * Override the toString method to provide a custom string representation of the
     * record
     */
    @Override
    public String toString() {
        return "Record{" +
                "year=" + year +
                ", place='" + place + '\'' +
                ", amount=" + amount +
                '}';
    }
}
